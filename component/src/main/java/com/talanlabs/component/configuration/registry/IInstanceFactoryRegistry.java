package com.talanlabs.component.configuration.registry;

import com.talanlabs.component.mapper.InstanceFactory;

import java.lang.reflect.Type;

public interface IInstanceFactoryRegistry {

    /**
     * @param type
     * @return
     */
    InstanceFactory<?> getInstanceFactory(Type type);
}
