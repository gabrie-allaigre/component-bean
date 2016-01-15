package com.talanlabs.component.configuration.factory.tostring.compare;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.factory.ComponentDescriptor;

import java.util.Comparator;

public class NullValuePropertyComparator implements IPropertyComparator {

    private final int bottom;

    public NullValuePropertyComparator() {
        this(true);
    }

    public NullValuePropertyComparator(boolean bottom) {
        super();

        this.bottom = bottom ? -1 : 1;
    }

    @Override
    public <E extends IComponent> Comparator<String> createComparator(ComponentDescriptor<E> componentDescriptor, E component) {
        return (o1, o2) -> {
            Object v1 = component.straightGetProperty(o1);
            Object v2 = component.straightGetProperty(o2);
            if ((v1 != null && v2 != null) || (v1 == null && v2 == null)) {
                return 0;
            } else if (v1 != null) {
                return bottom;
            }
            return -bottom;
        };
    }
}
