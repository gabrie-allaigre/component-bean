package com.talanlabs.component.configuration.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DefaultComputedFactory extends AbstractComputedFactory {

    private static Logger LOG = LogManager.getLogger(DefaultComputedFactory.class);

    @Override
    public <E> E createInstance(Class<E> clazz) {
        E res = null;
        try {
            res = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOG.error(e, e);
        }
        return res;
    }
}
