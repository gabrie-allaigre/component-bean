package com.talanlabs.component.mapper.internal.bind;

import com.google.common.reflect.TypeToken;
import com.talanlabs.component.mapper.ComponentMapper;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapter;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapterFactory;
import com.talanlabs.component.mapper.internal.ConstructorConstructor;
import com.talanlabs.component.mapper.internal.ObjectConstructor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.Iterator;

public class CollectionTypeAdapterFactory implements IComponentMapperTypeAdapterFactory {

    private final ConstructorConstructor constructorConstructor;

    public CollectionTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
        super();

        this.constructorConstructor = constructorConstructor;
    }

    @SuppressWarnings("unchecked")
    private static <T> Type getCollectionElementType(TypeToken<T> typeToken) {
        Type collectionType = typeToken.getSupertype((Class<? super T>) Collection.class).getType();

        if (collectionType instanceof WildcardType) {
            collectionType = ((WildcardType) collectionType).getUpperBounds()[0];
        }
        if (collectionType instanceof ParameterizedType) {
            return ((ParameterizedType) collectionType).getActualTypeArguments()[0];
        }
        return Object.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, U> IComponentMapperTypeAdapter<T, U> create(ComponentMapper mapper, TypeToken<T> srcTypeToken, TypeToken<U> dstTypeToken) {
        if (!Collection.class.isAssignableFrom(srcTypeToken.getRawType()) || !Collection.class.isAssignableFrom(dstTypeToken.getRawType())) {
            return null;
        }

        TypeToken srcSubTypeToken = TypeToken.of(getCollectionElementType(srcTypeToken));
        TypeToken dstSubTypeToken = TypeToken.of(getCollectionElementType(dstTypeToken));
        IComponentMapperTypeAdapter<?, ?> elementTypeAdapter = mapper.getTypeAdapter(srcSubTypeToken, dstSubTypeToken);
        ObjectConstructor<U> constructor = constructorConstructor.get(dstTypeToken);
        return (IComponentMapperTypeAdapter<T, U>) new Adapter(mapper, elementTypeAdapter, constructor, srcSubTypeToken, dstSubTypeToken);
    }

    private static class Adapter<T, U> implements IComponentMapperTypeAdapter<Collection<T>, Collection<U>> {

        private final IComponentMapperTypeAdapter<T, U> elementTypeAdapter;
        private final ObjectConstructor<? extends Collection<U>> constructor;

        public Adapter(ComponentMapper mapper, IComponentMapperTypeAdapter<T, U> elementTypeAdapter, ObjectConstructor<? extends Collection<U>> constructor, TypeToken srcSubTypeToken,
                TypeToken dstSubTypeToken) {
            super();

            this.elementTypeAdapter = new TypeAdapterRuntimeTypeWrapper<>(mapper, elementTypeAdapter, srcSubTypeToken, dstSubTypeToken);
            this.constructor = constructor;
        }

        @Override
        public Collection<U> convert(Collection<T> src) {
            if (src == null) {
                return null;
            }

            Iterator<T> it = src.iterator();
            Collection<U> collection = constructor.construct();
            while (it.hasNext()) {
                U instance = elementTypeAdapter.convert(it.next());
                collection.add(instance);
            }
            return collection;
        }
    }
}
