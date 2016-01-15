package com.talanlabs.component.configuration;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeToken;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapter;
import com.talanlabs.component.mapper.IComponentMapperTypeAdapterFactory;
import com.talanlabs.component.mapper.InstanceFactory;
import com.talanlabs.typeadapters.TypeAdaptersHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentMapperConfigurationBuilder {

    private ComponentMapperConfigurationImpl componentMapperConfiguration;

    private ComponentMapperConfigurationBuilder() {
        super();

        this.componentMapperConfiguration = new ComponentMapperConfigurationImpl();
    }

    public static ComponentMapperConfigurationBuilder newBuilder() {
        return new ComponentMapperConfigurationBuilder();
    }

    public ComponentMapperConfigurationBuilder registerInstanceFactory(Type type, InstanceFactory<?> instanceFactory) {
        componentMapperConfiguration.instanceFactories.put(type, instanceFactory);
        return this;
    }

    @SuppressWarnings("unchecked")
    public ComponentMapperConfigurationBuilder registerTypeAdapter(Type srcType, Type dstType, IComponentMapperTypeAdapter<?, ?> typeAdapter) {
        componentMapperConfiguration.typeAdapterFactories
                .add((IComponentMapperTypeAdapterFactory) TypeAdaptersHelper.newTypeFactory(TypeToken.of(srcType), TypeToken.of(dstType), (IComponentMapperTypeAdapter) typeAdapter));
        return this;
    }

    @SuppressWarnings("unchecked")
    public ComponentMapperConfigurationBuilder registerTypeHierarchyAdapter(Class<?> srcType, Class<?> dstType, IComponentMapperTypeAdapter<?, ?> typeAdapter) {
        componentMapperConfiguration.typeAdapterFactories
                .add((IComponentMapperTypeAdapterFactory) TypeAdaptersHelper.newTypeHierarchyFactory(srcType, dstType, (IComponentMapperTypeAdapter) typeAdapter));
        return this;
    }

    public ComponentMapperConfigurationBuilder registerTypeAdapterFactory(IComponentMapperTypeAdapterFactory... typeAdapterFactories) {
        componentMapperConfiguration.typeAdapterFactories.addAll(Arrays.asList(typeAdapterFactories));
        return this;
    }

    public IComponentMapperConfiguration build() {
        return componentMapperConfiguration;
    }

    private static class ComponentMapperConfigurationImpl implements IComponentMapperConfiguration {

        private final Map<Type, InstanceFactory<?>> instanceFactories = new HashMap<>();
        private final List<IComponentMapperTypeAdapterFactory> typeAdapterFactories = new ArrayList<>();

        public ComponentMapperConfigurationImpl() {
            super();
        }

        @Override
        public Map<Type, InstanceFactory<?>> getInstanceFactoryMap() {
            return ImmutableMap.copyOf(instanceFactories);
        }

        @Override
        public List<IComponentMapperTypeAdapterFactory> getTypeAdapterFactories() {
            return ImmutableList.copyOf(typeAdapterFactories);
        }
    }
}