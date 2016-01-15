package com.talanlabs.component.mapper.internal.bind;

import com.google.common.reflect.TypeToken;
import com.talanlabs.component.mapper.ComponentMapper;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapter;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapterFactory;

public class ObjectToTypeAdapter<U> implements IComponentMapperTypeAdapter<Object, U> {

    public static final IComponentMapperTypeAdapterFactory FACTORY = new IComponentMapperTypeAdapterFactory() {

        @Override
        @SuppressWarnings("unchecked")
        public <T2, U2> IComponentMapperTypeAdapter<T2, U2> create(ComponentMapper mapper, TypeToken<T2> srcTypeToken, TypeToken<U2> dstTypeToken) {
            if (srcTypeToken.getRawType() != Object.class) {
                return null;
            }
            return (IComponentMapperTypeAdapter<T2, U2>) new ObjectToTypeAdapter<>(mapper, dstTypeToken);
        }
    };

    private final ComponentMapper mapper;
    private final TypeToken<U> dstTypeToken;

    private ObjectToTypeAdapter(ComponentMapper mapper, TypeToken<U> dstTypeToken) {
        super();

        this.mapper = mapper;
        this.dstTypeToken = dstTypeToken;
    }

    @Override
    @SuppressWarnings("unchecked")
    public U convert(Object src) {
        if (src == null) {
            return null;
        }
        IComponentMapperTypeAdapter<Object, U> typeAdapter = (IComponentMapperTypeAdapter<Object, U>) mapper.getTypeAdapter(TypeToken.of(src.getClass()), dstTypeToken);
        if (typeAdapter instanceof ObjectToTypeAdapter) {
            return null;
        }
        return typeAdapter.convert(src);
    }
}
