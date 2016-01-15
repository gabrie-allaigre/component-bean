package com.talanlabs.component.configuration.factory.tostring.compare;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.factory.ComponentDescriptor;

import java.util.Comparator;

public class EqualsKeyPropertyComparator implements IPropertyComparator {

    public EqualsKeyPropertyComparator() {
        super();
    }

    @Override
    public <E extends IComponent> Comparator<String> createComparator(ComponentDescriptor<E> componentDescriptor, E component) {
        return new FirstPropertyComparator(componentDescriptor.getEqualsKeyPropertyNames()).createComparator(componentDescriptor, component);
    }
}