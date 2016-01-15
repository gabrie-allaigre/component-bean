package com.talanlabs.component.configuration.factory.tostring;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.configuration.factory.IToStringFactory;
import com.talanlabs.component.factory.ComponentDescriptor;

public class SimpleToStringFactory implements IToStringFactory {

    @Override
    public <E extends IComponent> String buildToString(ComponentDescriptor<E> componentDescriptor, E component) {
        return componentDescriptor.getName() + "@" + component.hashCode();
    }
}
