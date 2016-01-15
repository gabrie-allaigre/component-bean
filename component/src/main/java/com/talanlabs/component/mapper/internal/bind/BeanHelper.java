package com.talanlabs.component.mapper.internal.bind;

import com.google.common.reflect.TypeToken;
import com.talanlabs.component.mapper.ComponentMapper;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class BeanHelper {

    private BeanHelper() {
        super();
    }

    private static boolean excludeField(Field f, boolean serialize) {
        return true;
    }

    private static List<String> getFieldNames(Field f) {
        return Collections.singletonList(f.getName());
    }

    public static Map<String, BoundField> getBoundFields(ComponentMapper mapper, TypeToken<?> typeToken) {
        Map<String, BoundField> result = new LinkedHashMap<>();
        if (typeToken.getRawType().isInterface()) {
            return result;
        }

        Type declaredType = typeToken.getType();
        Class<?> raw = typeToken.getRawType();
        while (raw != Object.class) {
            Field[] fields = raw.getDeclaredFields();
            for (Field field : fields) {
                boolean serialize = excludeField(field, true);
                boolean deserialize = excludeField(field, false);
                if (!serialize && !deserialize) {
                    continue;
                }
                field.setAccessible(true);

                TypeToken<?> fieldTypeToken = typeToken.resolveType(field.getGenericType());
                List<String> fieldNames = getFieldNames(field);
                BoundField previous = null;
                for (int i = 0; i < fieldNames.size(); ++i) {
                    String name = fieldNames.get(i);
                    if (i != 0)
                        serialize = false; // only serialize the default name
                    BoundField boundField = createBoundField(mapper, field, name, fieldTypeToken, serialize, deserialize);
                    BoundField replaced = result.put(name, boundField);
                    if (previous == null)
                        previous = replaced;
                }
                if (previous != null) {
                    throw new IllegalArgumentException(declaredType + " declares multiple JSON fields named " + previous.name);
                }
            }
            typeToken = typeToken.resolveType(raw.getGenericSuperclass());
            raw = typeToken.getRawType();
        }
        return result;
    }

    private static BoundField createBoundField(final ComponentMapper mapper, final Field field, final String name, final TypeToken<?> fieldType, boolean serialize, boolean deserialize) {
        final boolean isPrimitive = fieldType.isPrimitive();
        return new BoundField(name, serialize, deserialize) {
            @SuppressWarnings("unchecked")
            @Override
            public Object get(Object obj, TypeToken<?> dstTypeToken) throws IllegalAccessException {
                Object value = field.get(obj);
                IComponentMapperTypeAdapter<Object, Object> typeAdapter = (IComponentMapperTypeAdapter<Object, Object>) new TypeAdapterRuntimeTypeWrapper(mapper,
                        mapper.getTypeAdapter(fieldType, dstTypeToken), fieldType, dstTypeToken);
                return typeAdapter.convert(value);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void set(Object obj, Object value, TypeToken<?> srcTypeToken) throws IllegalAccessException {
                IComponentMapperTypeAdapter<Object, Object> typeAdapter = (IComponentMapperTypeAdapter<Object, Object>) new TypeAdapterRuntimeTypeWrapper(mapper,
                        mapper.getTypeAdapter(srcTypeToken, fieldType), srcTypeToken, fieldType);
                value = typeAdapter.convert(value);
                if (value != null || !isPrimitive) {
                    field.set(obj, value);
                }
            }
        };
    }

    public static abstract class BoundField {
        final String name;
        final boolean serialized;
        final boolean deserialized;

        protected BoundField(String name, boolean serialized, boolean deserialized) {
            this.name = name;
            this.serialized = serialized;
            this.deserialized = deserialized;
        }

        public abstract Object get(Object obj, TypeToken<?> dstTypeToken) throws IllegalAccessException;

        public abstract void set(Object obj, Object value, TypeToken<?> srcTypeToken) throws IllegalAccessException;

    }
}
