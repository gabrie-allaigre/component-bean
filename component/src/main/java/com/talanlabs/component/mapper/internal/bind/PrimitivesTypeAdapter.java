package com.talanlabs.component.mapper.internal.bind;

import com.google.common.primitives.Primitives;
import com.google.common.reflect.TypeToken;
import com.talanlabs.component.mapper.ComponentMapper;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapter;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapterFactory;

import java.util.Set;

public class PrimitivesTypeAdapter<T> implements IComponentMapperTypeAdapter<T, T> {

    public static final IComponentMapperTypeAdapterFactory FACTORY = new IComponentMapperTypeAdapterFactory() {

        @Override
        @SuppressWarnings("unchecked")
        public <T2, U2> IComponentMapperTypeAdapter<T2, U2> create(ComponentMapper mapper, TypeToken<T2> srcTypeToken, TypeToken<U2> dstTypeToken) {
            srcTypeToken = srcTypeToken.isPrimitive() ? srcTypeToken.wrap() : srcTypeToken;
            dstTypeToken = dstTypeToken.isPrimitive() ? dstTypeToken.wrap() : dstTypeToken;

            Set<Class<?>> primitives = Primitives.allWrapperTypes();
            if (!primitives.contains(srcTypeToken.getRawType()) || !primitives.contains(dstTypeToken.getRawType()) || !srcTypeToken.equals(dstTypeToken)) {
                return null;
            }

            return (IComponentMapperTypeAdapter<T2, U2>) new PrimitivesTypeAdapter();
        }
    };

    private PrimitivesTypeAdapter() {
        super();
    }

    @Override
    public T convert(T src) {
        if (src == null) {
            return null;
        }
        return src;
    }
}
