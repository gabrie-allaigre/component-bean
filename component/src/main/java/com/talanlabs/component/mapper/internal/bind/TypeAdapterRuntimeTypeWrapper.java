package com.talanlabs.component.mapper.internal.bind;

import com.google.common.reflect.TypeToken;
import com.talanlabs.component.mapper.ComponentMapper;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapter;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class TypeAdapterRuntimeTypeWrapper<T, U> implements IComponentMapperTypeAdapter<T, U> {

    private final ComponentMapper mapper;
    private final IComponentMapperTypeAdapter<T, U> delegate;
    private final TypeToken srcTypeToken;
    private final TypeToken dstTypeToken;

    public TypeAdapterRuntimeTypeWrapper(ComponentMapper mapper, IComponentMapperTypeAdapter<T, U> delegate, TypeToken srcTypeToken, TypeToken dstTypeToken) {
        super();

        this.mapper = mapper;
        this.delegate = delegate;
        this.srcTypeToken = srcTypeToken;
        this.dstTypeToken = dstTypeToken;
    }

    @SuppressWarnings("unchecked")
    @Override
    public U convert(T src) {
        IComponentMapperTypeAdapter chosen = delegate;
        Type runtimeType = getRuntimeTypeIfMoreSpecific(srcTypeToken.getType(), src);
        if (runtimeType != srcTypeToken.getType()) {
            chosen = mapper.getTypeAdapter(TypeToken.of(runtimeType), dstTypeToken);
        }
        return (U) chosen.convert(src);
    }

    /**
     * Finds a compatible runtime type if it is more specific
     */
    private Type getRuntimeTypeIfMoreSpecific(Type type, Object value) {
        if (value != null && (type == Object.class || type instanceof TypeVariable<?> || type instanceof Class<?>)) {
            type = value.getClass();
        }
        return type;
    }
}
