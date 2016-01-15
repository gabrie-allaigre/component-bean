package com.talanlabs.component.configuration.factory.tostring.compare;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.factory.ComponentDescriptor;

import java.util.Comparator;
import java.util.Set;

public class FirstPropertyComparator implements IPropertyComparator {

    private final Comparator<String> comparator;

    public FirstPropertyComparator(Set<String> propertyNames) {
        super();

        this.comparator = (o1, o2) -> {
            if (propertyNames == null) {
                return 0;
            }
            boolean b1 = propertyNames.contains(o1);
            boolean b2 = propertyNames.contains(o2);
            return b1 == b2 ? 0 : (b1 ? -1 : 1);
        };
    }

    @Override
    public <E extends IComponent> Comparator<String> createComparator(ComponentDescriptor<E> componentDescriptor, E component) {
        return comparator;
    }
}