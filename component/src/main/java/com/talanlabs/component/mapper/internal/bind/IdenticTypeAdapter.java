package com.talanlabs.component.mapper.internal.bind;

import com.google.common.reflect.TypeToken;
import com.talanlabs.component.mapper.ComponentMapper;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapter;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapterFactory;

public class IdenticTypeAdapter implements IComponentMapperTypeAdapter<Object, Object> {

    private static final IComponentMapperTypeAdapter<Object, Object> IDENTIC_TYPE_ADAPTER = new IdenticTypeAdapter();
    @SuppressWarnings("unchecked")
    public static final IComponentMapperTypeAdapterFactory FACTORY = new IComponentMapperTypeAdapterFactory() {
        public <T2, U2> IComponentMapperTypeAdapter<T2, U2> create(ComponentMapper mapper, TypeToken<T2> srcTypeToken, TypeToken<U2> dstTypeToken) {
            if (!srcTypeToken.isSubtypeOf(dstTypeToken)) {
                return null;
            }
            return (IComponentMapperTypeAdapter<T2, U2>) IDENTIC_TYPE_ADAPTER;
        }
    };

    private IdenticTypeAdapter() {
        super();
    }

    @Override
    public Object convert(Object src) {
        return src;
    }
}
