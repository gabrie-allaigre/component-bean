package com.talanlabs.component.configuration;

import com.talanlabs.component.configuration.factory.IComputedFactory;
import com.talanlabs.component.configuration.factory.IToStringFactory;

public interface IComponentFactoryConfiguration {

    /**
     * @return a computed factory
     */
    IComputedFactory getComputedFactory();

    /**
     * @return a To String factory
     */
    IToStringFactory getToStringFactory();

}
