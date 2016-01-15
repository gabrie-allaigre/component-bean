package com.talanlabs.component.mapper.internal.bind;

import com.google.common.reflect.TypeToken;
import com.talanlabs.component.mapper.ComponentMapper;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapter;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapterFactory;
import com.talanlabs.component.mapper.internal.ConstructorConstructor;
import com.talanlabs.component.mapper.internal.ObjectConstructor;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

public class MapTypeAdapterFactory implements IComponentMapperTypeAdapterFactory {

    private final ConstructorConstructor constructorConstructor;

    public MapTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
        super();

        this.constructorConstructor = constructorConstructor;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, U> IComponentMapperTypeAdapter<T, U> create(ComponentMapper mapper, TypeToken<T> srcTypeToken, TypeToken<U> dstTypeToken) {
        if (!Map.class.isAssignableFrom(srcTypeToken.getRawType()) || !Map.class.isAssignableFrom(dstTypeToken.getRawType())) {
            return null;
        }

        ParameterizedType srcType = (ParameterizedType) srcTypeToken.getSupertype((Class<? super T>) Map.class).getType();
        TypeToken<?> srcKeyTypeToken = TypeToken.of(srcType.getActualTypeArguments()[0]);
        TypeToken<?> srcValueTypeToken = TypeToken.of(srcType.getActualTypeArguments()[1]);
        ParameterizedType dstType = (ParameterizedType) dstTypeToken.getSupertype((Class<? super U>) Map.class).getType();
        TypeToken<?> dstKeyTypeToken = TypeToken.of(dstType.getActualTypeArguments()[0]);
        TypeToken<?> dstValueTypeToken = TypeToken.of(dstType.getActualTypeArguments()[1]);
        IComponentMapperTypeAdapter<?, ?> keyTypeAdapter = mapper.getTypeAdapter(srcKeyTypeToken, dstKeyTypeToken);
        IComponentMapperTypeAdapter<?, ?> valueTypeAdapter = mapper.getTypeAdapter(srcValueTypeToken, dstValueTypeToken);
        ObjectConstructor<U> constructor = constructorConstructor.get(dstTypeToken);
        return (IComponentMapperTypeAdapter<T, U>) new Adapter(mapper, keyTypeAdapter, valueTypeAdapter, constructor, srcKeyTypeToken, srcValueTypeToken, dstKeyTypeToken, dstValueTypeToken);
    }

    private static class Adapter<T, U, K, V> implements IComponentMapperTypeAdapter<Map<T, U>, Map<K, V>> {

        private final IComponentMapperTypeAdapter<T, K> keyTypeAdapter;
        private final IComponentMapperTypeAdapter<U, V> valueTypeAdapter;
        private final ObjectConstructor<? extends Map<K, V>> constructor;

        public Adapter(ComponentMapper mapper, IComponentMapperTypeAdapter<T, K> keyTypeAdapter, IComponentMapperTypeAdapter<U, V> valueTypeAdapter, ObjectConstructor<? extends Map<K, V>> constructor,
                TypeToken<T> srcKeyTypeToken, TypeToken<U> srcValueTypeToken, TypeToken<K> dstKeyTypeToken, TypeToken<V> dstValueTypeToken) {
            super();

            this.keyTypeAdapter = new TypeAdapterRuntimeTypeWrapper<>(mapper, keyTypeAdapter, srcKeyTypeToken, dstKeyTypeToken);
            this.valueTypeAdapter = new TypeAdapterRuntimeTypeWrapper<>(mapper, valueTypeAdapter, srcValueTypeToken, dstValueTypeToken);
            this.constructor = constructor;
        }

        @Override
        public Map<K, V> convert(Map<T, U> src) {
            if (src == null) {
                return null;
            }

            Map<K, V> map = constructor.construct();
            for (Map.Entry<T, U> entry : src.entrySet()) {
                K key = keyTypeAdapter.convert(entry.getKey());
                V value = valueTypeAdapter.convert(entry.getValue());
                map.put(key, value);
            }
            return map;
        }
    }
}
