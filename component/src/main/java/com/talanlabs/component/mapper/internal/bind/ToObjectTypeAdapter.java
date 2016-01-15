package com.talanlabs.component.mapper.internal.bind;

import com.google.common.reflect.TypeToken;
import com.talanlabs.component.mapper.ComponentMapper;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapter;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapterFactory;

public class ToObjectTypeAdapter<T> implements IComponentMapperTypeAdapter<T, Object> {

    private static final IComponentMapperTypeAdapter<Object, Object> TP_OBJECT_TYPE_ADAPTER = new ToObjectTypeAdapter();

    public static final IComponentMapperTypeAdapterFactory FACTORY = new IComponentMapperTypeAdapterFactory() {

        @Override
        @SuppressWarnings("unchecked")
        public <T2, U2> IComponentMapperTypeAdapter<T2, U2> create(ComponentMapper mapper, TypeToken<T2> srcTypeToken, TypeToken<U2> dstTypeToken) {
            if (dstTypeToken.getRawType() != Object.class) {
                return null;
            }
            return (IComponentMapperTypeAdapter<T2, U2>) TP_OBJECT_TYPE_ADAPTER;
        }
    };

    @Override
    public Object convert(T src) {
        return src;
    }
}
