package com.talanlabs.component.mapper;

import org.apache.commons.lang3.NotImplementedException;

public interface IComponentMapperTypeAdapter<T, U> extends com.talanlabs.typeadapters.ITypeAdapter<T, U> {

    /**
     * Convert src to new type U
     *
     * @param src source
     * @return instance of U
     */
    U convert(T src);

    @Override
    default T toSrc(U u) {
        throw new NotImplementedException("toSrc is not implemented");
    }

    @Override
    default U toDst(T t) {
        return convert(t);
    }
}
