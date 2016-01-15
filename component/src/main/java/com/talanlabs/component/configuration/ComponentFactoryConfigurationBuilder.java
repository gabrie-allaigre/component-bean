package com.talanlabs.component.configuration;

import com.talanlabs.component.configuration.factory.DefaultComputedFactory;
import com.talanlabs.component.configuration.factory.IComputedFactory;
import com.talanlabs.component.configuration.factory.IToStringFactory;
import com.talanlabs.component.configuration.factory.tostring.CompleteToStringFactory;

public class ComponentFactoryConfigurationBuilder {

    private ComponentFactoryConfigurationImpl componentFactoryConfiguration;

    private ComponentFactoryConfigurationBuilder() {
        super();

        this.componentFactoryConfiguration = new ComponentFactoryConfigurationImpl();
    }

    public static ComponentFactoryConfigurationBuilder newBuilder() {
        return new ComponentFactoryConfigurationBuilder();
    }

    public ComponentFactoryConfigurationBuilder comptedFactory(IComputedFactory computedFactory) {
        componentFactoryConfiguration.computedFactory = computedFactory;
        return this;
    }

    public ComponentFactoryConfigurationBuilder toStringFactory(IToStringFactory toStringFactory) {
        componentFactoryConfiguration.toStringFactory = toStringFactory;
        return this;
    }

    public IComponentFactoryConfiguration build() {
        return componentFactoryConfiguration;
    }

    private static class ComponentFactoryConfigurationImpl implements IComponentFactoryConfiguration {

        private IComputedFactory computedFactory;

        private IToStringFactory toStringFactory;

        public ComponentFactoryConfigurationImpl() {
            super();

            this.computedFactory = new DefaultComputedFactory();
            this.toStringFactory = new CompleteToStringFactory();
        }

        @Override
        public IComputedFactory getComputedFactory() {
            return computedFactory;
        }

        @Override
        public IToStringFactory getToStringFactory() {
            return toStringFactory;
        }
    }
}