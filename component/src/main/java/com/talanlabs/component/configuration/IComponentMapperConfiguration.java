package com.talanlabs.component.configuration;

import com.talanlabs.component.mapper.IComponentMapperTypeAdapterFactory;
import com.talanlabs.component.mapper.InstanceFactory;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public interface IComponentMapperConfiguration {

    /**
     * @return
     */
    Map<Type, InstanceFactory<?>> getInstanceFactoryMap();

    /**
     * @return
     */
    List<IComponentMapperTypeAdapterFactory> getTypeAdapterFactories();

}
