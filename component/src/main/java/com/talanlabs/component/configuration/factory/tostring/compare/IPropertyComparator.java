package com.talanlabs.component.configuration.factory.tostring.compare;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.factory.ComponentDescriptor;
import com.talanlabs.component.factory.ComponentFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public interface IPropertyComparator {

    static IPropertyComparator comparator(final Comparator<String> comparator) {
        return new IPropertyComparator() {
            @Override
            public <E extends IComponent> Comparator<String> createComparator(ComponentDescriptor<E> componentDescriptor, E component) {
                return comparator;
            }
        };
    }

    /**
     * Natural order
     */
    static IPropertyComparator natural() {
        return comparator(Comparator.naturalOrder());
    }

    /**
     * Equals key is first
     */
    static IPropertyComparator equalsKey() {
        return new EqualsKeyPropertyComparator();
    }

    /**
     * Properties first
     */
    static IPropertyComparator first(String firstPropertyName, String... propertyNames) {
        Set<String> ps = new HashSet<>();
        ps.add(firstPropertyName);
        if (propertyNames != null) {
            ps.addAll(Arrays.asList(propertyNames));
        }
        return new FirstPropertyComparator(ps);
    }

    static IPropertyComparator first(Class<? extends IComponent> componentClass) {
        return new FirstPropertyComparator(ComponentFactory.getInstance().getDescriptor(componentClass).getPropertyNames());
    }

    /**
     * Properties last
     */
    static IPropertyComparator last(String firstPropertyName, String... propertyNames) {
        Set<String> ps = new HashSet<>();
        ps.add(firstPropertyName);
        if (propertyNames != null) {
            ps.addAll(Arrays.asList(propertyNames));
        }
        return new LastPropertyComparator(ps);
    }

    static IPropertyComparator last(Class<? extends IComponent> componentClass) {
        return new LastPropertyComparator(ComponentFactory.getInstance().getDescriptor(componentClass).getPropertyNames());
    }

    /**
     * Properties, null is last
     */
    static IPropertyComparator nullsLast() {
        return new NullValuePropertyComparator(true);
    }

    /**
     * Properties, null is first
     */
    static IPropertyComparator nullsFirst() {
        return new NullValuePropertyComparator(false);
    }

    /**
     * Compose comparator
     */
    static IPropertyComparator compose(IPropertyComparator... propertyComparators) {
        return new IPropertyComparator() {
            @Override
            public <E extends IComponent> Comparator<String> createComparator(ComponentDescriptor<E> componentDescriptor, E component) {
                IPropertyComparator firstPropertyComparator = propertyComparators[0];
                Comparator<String> res = firstPropertyComparator.createComparator(componentDescriptor, component);
                for (int i = 1; i < propertyComparators.length; i++) {
                    IPropertyComparator propertyComparator = propertyComparators[i];
                    res = res.thenComparing(propertyComparator.createComparator(componentDescriptor, component));
                }
                return res;
            }
        };
    }

    /**
     * Create a comparator
     *
     * @param componentDescriptor
     * @param component
     * @param <E>
     * @return
     */
    <E extends IComponent> Comparator<String> createComparator(ComponentDescriptor<E> componentDescriptor, E component);
}
