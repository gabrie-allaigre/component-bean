package com.talanlabs.component.mapper.internal.bind;

import com.google.common.reflect.TypeToken;
import com.talanlabs.component.IComponent;
import com.talanlabs.component.factory.ComponentDescriptor;
import com.talanlabs.component.factory.ComponentFactory;
import com.talanlabs.component.mapper.ComponentMapper;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapter;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapterFactory;
import com.talanlabs.component.mapper.internal.ConstructorConstructor;
import com.talanlabs.component.mapper.internal.ObjectConstructor;

import java.util.Map;

public class ComponentToBeanTypeAdapterFactory implements IComponentMapperTypeAdapterFactory {

    private final ConstructorConstructor constructorConstructor;

    public ComponentToBeanTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
        super();

        this.constructorConstructor = constructorConstructor;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, U> IComponentMapperTypeAdapter<T, U> create(ComponentMapper mapper, TypeToken<T> srcTypeToken, TypeToken<U> dstTypeToken) {
        if (!ComponentFactory.getInstance().isComponentType(srcTypeToken.getType()) || !Object.class.isAssignableFrom(dstTypeToken.getRawType())) {
            return null;
        }

        ObjectConstructor<U> constructor = constructorConstructor.get(dstTypeToken);
        return (IComponentMapperTypeAdapter<T, U>) new Adapter(mapper, constructor, BeanHelper.getBoundFields(mapper, dstTypeToken));
    }

    private static class Adapter<U> implements IComponentMapperTypeAdapter<IComponent, U> {

        private final ComponentMapper mapper;
        private final ObjectConstructor<? extends U> constructor;
        private final Map<String, BeanHelper.BoundField> boundFields;

        public Adapter(ComponentMapper mapper, ObjectConstructor<? extends U> constructor, Map<String, BeanHelper.BoundField> boundFields) {
            super();

            this.mapper = mapper;
            this.constructor = constructor;
            this.boundFields = boundFields;
        }

        @Override
        public U convert(IComponent src) {
            if (src == null) {
                return null;
            }
            U res = constructor.construct();

            ComponentDescriptor<IComponent> srcCd = ComponentFactory.getInstance().getDescriptor(src);
            try {
                for (ComponentDescriptor.PropertyDescriptor propertyDescriptor : srcCd.getPropertyDescriptors()) {
                    String propertyName = propertyDescriptor.getPropertyName();

                    if (boundFields.containsKey(propertyName)) {
                        TypeToken<?> fieldTypeToken = TypeToken.of(propertyDescriptor.getPropertyType());
                        Object value = src.straightGetProperty(propertyName);

                        boundFields.get(propertyName).set(res, value, fieldTypeToken);
                    }
                }
                return res;
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            }
        }
    }
}
