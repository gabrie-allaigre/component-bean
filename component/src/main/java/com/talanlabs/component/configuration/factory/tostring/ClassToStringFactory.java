package com.talanlabs.component.configuration.factory.tostring;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.configuration.factory.IToStringFactory;
import com.talanlabs.component.factory.ComponentDescriptor;

import java.util.HashMap;
import java.util.Map;

public class ClassToStringFactory implements IToStringFactory {

    private final IToStringFactory defaultToStringFactory;

    private final Map<Class<? extends IComponent>, IToStringFactory> map = new HashMap<>();

    public ClassToStringFactory(IToStringFactory defaultToStringFactory) {
        super();

        this.defaultToStringFactory = defaultToStringFactory;
    }

    /**
     * Add Component Class String factory
     *
     * @param componentClass  component class
     * @param toStringFactory to String factory
     */
    public void put(Class<? extends IComponent> componentClass, IToStringFactory toStringFactory) {
        putMulti(toStringFactory, componentClass);
    }

    /**
     * Add multi component class for a string factory
     *
     * @param toStringFactory to string factory
     * @param componentClasss multi component class
     */
    public void putMulti(IToStringFactory toStringFactory, Class<? extends IComponent>... componentClasss) {
        for (Class<? extends IComponent> componentClass : componentClasss) {
            map.put(componentClass, toStringFactory);
        }
    }

    /**
     * Remove to String component for component class
     *
     * @param componentClass component class
     */
    public void remove(Class<? extends IComponent> componentClass) {
        map.remove(componentClass);
    }

    @Override
    public <E extends IComponent> String buildToString(ComponentDescriptor<E> componentDescriptor, E component) {
        IToStringFactory toStringFactory = map.get(componentDescriptor.getComponentClass());
        return toStringFactory != null ? toStringFactory.buildToString(componentDescriptor, component) : defaultToStringFactory.buildToString(componentDescriptor, component);
    }
}
