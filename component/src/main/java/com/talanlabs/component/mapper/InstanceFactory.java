package com.talanlabs.component.mapper;

import java.lang.reflect.Type;

public interface InstanceFactory<T> {

    /**
     * Create instance for type
     *
     * @param type
     * @return
     */
    T createInstance(Type type);

}
