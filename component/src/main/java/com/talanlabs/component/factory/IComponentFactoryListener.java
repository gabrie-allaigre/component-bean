package com.talanlabs.component.factory;

import com.talanlabs.component.IComponent;

public interface IComponentFactoryListener {

    /**
     * Call after create instance of interfaceClass
     *
     * @param interfaceClass
     * @param instance
     */
    <G extends IComponent> void afterCreated(Class<G> interfaceClass, G instance);

}
