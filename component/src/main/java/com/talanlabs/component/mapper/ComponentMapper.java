package com.talanlabs.component.mapper;

import com.google.common.reflect.TypeToken;
import com.talanlabs.component.IComponent;
import com.talanlabs.component.configuration.ComponentMapperConfigurationBuilder;
import com.talanlabs.component.configuration.IComponentMapperConfiguration;
import com.talanlabs.component.factory.ComponentFactory;
import com.talanlabs.component.mapper.internal.ConstructorConstructor;
import com.talanlabs.component.mapper.internal.bind.ArrayTypeAdapter;
import com.talanlabs.component.mapper.internal.bind.BeanToComponentTypeAdapter;
import com.talanlabs.component.mapper.internal.bind.CollectionTypeAdapterFactory;
import com.talanlabs.component.mapper.internal.bind.ComponentToBeanTypeAdapterFactory;
import com.talanlabs.component.mapper.internal.bind.ComponentToComponentTypeAdapter;
import com.talanlabs.component.mapper.internal.bind.ComponentToMapTypeAdapter;
import com.talanlabs.component.mapper.internal.bind.IdenticTypeAdapter;
import com.talanlabs.component.mapper.internal.bind.MapToComponentTypeAdapter;
import com.talanlabs.component.mapper.internal.bind.MapTypeAdapterFactory;
import com.talanlabs.component.mapper.internal.bind.ObjectToTypeAdapter;
import com.talanlabs.component.mapper.internal.bind.PrimitivesTypeAdapter;
import com.talanlabs.component.mapper.internal.bind.ToObjectTypeAdapter;
import com.talanlabs.typeadapters.TypeAdaptersDelegate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ComponentMapper {

    private static Logger LOG = LogManager.getLogger(ComponentMapper.class);

    private static ComponentMapper instance;
    private final IComponentMapperConfiguration configuration;
    private final ConstructorConstructor constructorConstructor;
    private final TypeAdaptersDelegate<ComponentMapper> typeAdaptersDelegate;

    public ComponentMapper(IComponentMapperConfiguration configuration) {
        super();

        this.configuration = configuration;

        this.constructorConstructor = new ConstructorConstructor(configuration.getInstanceFactoryMap());

        List<IComponentMapperTypeAdapterFactory> factories = new ArrayList<>();

        factories.add(ObjectToTypeAdapter.FACTORY);
        factories.add(ToObjectTypeAdapter.FACTORY);

        factories.addAll(configuration.getTypeAdapterFactories());

        factories.add(PrimitivesTypeAdapter.FACTORY);
        factories.add(ArrayTypeAdapter.FACTORY);
        factories.add(new CollectionTypeAdapterFactory(constructorConstructor));
        factories.add(new MapTypeAdapterFactory(constructorConstructor));
        factories.add(MapToComponentTypeAdapter.FACTORY);
        factories.add(ComponentToMapTypeAdapter.FACTORY);
        factories.add(ComponentToComponentTypeAdapter.FACTORY);
        factories.add(BeanToComponentTypeAdapter.FACTORY);
        factories.add(new ComponentToBeanTypeAdapterFactory(constructorConstructor));
        factories.add(IdenticTypeAdapter.FACTORY);

        typeAdaptersDelegate = new TypeAdaptersDelegate<>(this, factories);
    }

    public static synchronized ComponentMapper getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new ComponentMapper(ComponentMapperConfigurationBuilder.newBuilder().build());
        return instance;
    }

    public static synchronized void setInstance(ComponentMapper instance) {
        ComponentMapper.instance = instance;
    }

    /**
     * Get current configuration
     *
     * @return configuration
     */
    public IComponentMapperConfiguration getComponentMapperConfiguration() {
        return this.configuration;
    }

    /**
     * Get constructor constructor
     *
     * @return
     */
    public ConstructorConstructor getConstructorConstructor() {
        return this.constructorConstructor;
    }

    /**
     * Convert src in dstComponent
     *
     * @param src               source
     * @param dstComponentClass destination class, class is not generic class
     * @return dstComponentClass instance
     */
    public <U extends IComponent> U toComponent(Object src, Class<U> dstComponentClass) {
        return toComponent(src, (Type) dstComponentClass);
    }

    /**
     * Convert src in dstComponent
     *
     * @param src              source
     * @param dstComponentType destination type, type is supertype of IComponent and not use TypeVariable
     * @return dstComponentType instance
     */
    @SuppressWarnings("unchecked")
    public <U extends IComponent> U toComponent(Object src, Type dstComponentType) {
        if (src == null) {
            return null;
        }
        return toComponent(src, src.getClass(), dstComponentType);
    }

    @SuppressWarnings("unchecked")
    public <U extends IComponent> U toComponent(Object src, Type srcType, Type dstComponentType) {
        if (!ComponentFactory.getInstance().isComponentType(dstComponentType)) {
            throw new IllegalArgumentException("dstComponentDtoType is not IComponent"); //FIXME
        }

        TypeToken<Object> srcTypeToken = (TypeToken<Object>) TypeToken.of(srcType);
        TypeToken<U> dstTypeToken = (TypeToken<U>) TypeToken.of(dstComponentType);

        IComponentMapperTypeAdapter<?, U> typeAdapter = getTypeAdapter(srcTypeToken, dstTypeToken);
        return ((IComponentMapperTypeAdapter<Object, U>) typeAdapter).convert(src);
    }

    public <U extends IComponent, V> V fromComponent(U srcComponent, Class<V> dstClass) {
        return fromComponent(srcComponent, (Type) dstClass);
    }

    public <U extends IComponent, V> V fromComponent(U srcComponent, Type dstType) {
        if (srcComponent == null) {
            return null;
        }
        return fromComponent(srcComponent, ComponentFactory.getInstance().getComponentType(srcComponent), dstType);
    }

    @SuppressWarnings("unchecked")
    private <U extends IComponent, V> V fromComponent(U src, Type srcType, Type dstType) {
        TypeToken<U> srcTypeToken = (TypeToken<U>) TypeToken.of(srcType);
        TypeToken<V> dstTypeToken = (TypeToken<V>) TypeToken.of(dstType);

        IComponentMapperTypeAdapter<U, V> typeAdapter = getTypeAdapter(srcTypeToken, dstTypeToken);
        return typeAdapter.convert(src);
    }

    /**
     * Find type adater for source and destination class
     *
     * @param srcClass source class
     * @param dstClass destination class
     * @return a type adapter, if not found throw exception
     */
    public <T, U> IComponentMapperTypeAdapter<T, U> getTypeAdapter(Class<T> srcClass, Class<U> dstClass) {
        return getTypeAdapter(TypeToken.of(srcClass), TypeToken.of(dstClass));
    }

    /**
     * Find type adater for source and destination type token
     *
     * @param srcTypeToken source
     * @param dstTypeToken destination
     * @return a type adapter, if not found throw exception
     */
    @SuppressWarnings("unchecked")
    public <E, U> IComponentMapperTypeAdapter<E, U> getTypeAdapter(TypeToken<E> srcTypeToken, TypeToken<U> dstTypeToken) {
        return (IComponentMapperTypeAdapter<E, U>) typeAdaptersDelegate.<E, U>getTypeAdapter(srcTypeToken, dstTypeToken);
    }
}
