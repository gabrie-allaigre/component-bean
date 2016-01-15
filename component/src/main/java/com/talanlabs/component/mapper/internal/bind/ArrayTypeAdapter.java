package com.talanlabs.component.mapper.internal.bind;

import com.google.common.reflect.TypeToken;
import com.talanlabs.component.mapper.ComponentMapper;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapter;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapterFactory;

import java.lang.reflect.Array;

public class ArrayTypeAdapter<T, U> implements IComponentMapperTypeAdapter<Object, Object> {

    public static final IComponentMapperTypeAdapterFactory FACTORY = new IComponentMapperTypeAdapterFactory() {

        @Override
        @SuppressWarnings("unchecked")
        public <T2, U2> IComponentMapperTypeAdapter<T2, U2> create(ComponentMapper mapper, TypeToken<T2> srcTypeToken, TypeToken<U2> dstTypeToken) {
            if (!srcTypeToken.isArray() || !dstTypeToken.isArray() || dstTypeToken.getComponentType() == null) {
                return null;
            }

            IComponentMapperTypeAdapter<?, ?> componentTypeAdapter = mapper.getTypeAdapter(srcTypeToken.getComponentType(), dstTypeToken.getComponentType());
            return new ArrayTypeAdapter(mapper, componentTypeAdapter, srcTypeToken.getComponentType(), dstTypeToken.getComponentType());
        }
    };

    private final TypeToken<U> dstComponentTypeToken;
    private final IComponentMapperTypeAdapter<T, U> componentTypeAdapter;

    private ArrayTypeAdapter(ComponentMapper mapper, IComponentMapperTypeAdapter<T, U> componentTypeAdapter, TypeToken<T> srcComponentTypeToken, TypeToken<U> dstComponentTypeToken) {
        super();

        this.componentTypeAdapter = new TypeAdapterRuntimeTypeWrapper<>(mapper, componentTypeAdapter, srcComponentTypeToken, dstComponentTypeToken);
        this.dstComponentTypeToken = dstComponentTypeToken;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object convert(Object src) {
        if (src == null) {
            return null;
        }

        int length = Array.getLength(src);
        Object array = Array.newInstance(dstComponentTypeToken.getRawType(), length);
        for (int i = 0; i < length; i++) {
            Array.set(array, i, componentTypeAdapter.convert((T) Array.get(src, i)));
        }
        return array;
    }
}
