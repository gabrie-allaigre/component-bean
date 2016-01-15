package com.talanlabs.component.test.unit;

import com.google.common.collect.Lists;
import com.talanlabs.component.configuration.factory.tostring.compare.IPropertyComparator;
import com.talanlabs.component.factory.ComponentDescriptor;
import com.talanlabs.component.factory.ComponentFactory;
import com.talanlabs.component.test.data.IOffer;
import com.talanlabs.component.test.data.ITracable;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PropertyComparatorTest {

    @Test
    public void testNaturalPropertyComparator() {
        ComponentDescriptor<IOffer> cd = ComponentFactory.getInstance().getDescriptor(IOffer.class);
        IOffer offer = ComponentFactory.getInstance().createInstance(IOffer.class);

        Comparator<String> comparator = IPropertyComparator.natural().createComparator(cd, offer);

        List<String> propertyNames = Lists.newArrayList(cd.getPropertyNames());
        Collections.sort(propertyNames, comparator);
        Assertions.assertThat(propertyNames).containsExactly("createdBy", "id", "name", "updatedBy", "version");
    }

    @Test
    public void testFirstPropertyComparator() {
        ComponentDescriptor<IOffer> cd = ComponentFactory.getInstance().getDescriptor(IOffer.class);
        IOffer offer = ComponentFactory.getInstance().createInstance(IOffer.class);

        Comparator<String> comparator = IPropertyComparator.first("id", "version").createComparator(cd, offer);

        List<String> propertyNames = Lists.newArrayList(cd.getPropertyNames());
        Collections.sort(propertyNames, comparator);
        Assertions.assertThat(propertyNames.stream().limit(2).collect(Collectors.toList())).containsOnly("id", "version");
    }

    @Test
    public void testFirstClassPropertyComparator() {
        ComponentDescriptor<IOffer> cd = ComponentFactory.getInstance().getDescriptor(IOffer.class);
        IOffer offer = ComponentFactory.getInstance().createInstance(IOffer.class);

        Comparator<String> comparator = IPropertyComparator.first(ITracable.class).createComparator(cd, offer);

        List<String> propertyNames = Lists.newArrayList(cd.getPropertyNames());
        Collections.sort(propertyNames, comparator);
        Assertions.assertThat(propertyNames.stream().limit(2).collect(Collectors.toList())).containsOnly("createdBy", "updatedBy");
    }

    @Test
    public void testLastPropertyComparator() {
        ComponentDescriptor<IOffer> cd = ComponentFactory.getInstance().getDescriptor(IOffer.class);
        IOffer offer = ComponentFactory.getInstance().createInstance(IOffer.class);

        Comparator<String> comparator = IPropertyComparator.last("id", "version").createComparator(cd, offer);

        List<String> propertyNames = Lists.newArrayList(cd.getPropertyNames());
        Collections.sort(propertyNames, comparator);
        Assertions.assertThat(propertyNames.stream().skip(3).collect(Collectors.toList())).containsOnly("id", "version");
    }

    @Test
    public void testClassLastPropertyComparator() {
        ComponentDescriptor<IOffer> cd = ComponentFactory.getInstance().getDescriptor(IOffer.class);
        IOffer offer = ComponentFactory.getInstance().createInstance(IOffer.class);

        Comparator<String> comparator = IPropertyComparator.last(ITracable.class).createComparator(cd, offer);

        List<String> propertyNames = Lists.newArrayList(cd.getPropertyNames());
        Collections.sort(propertyNames, comparator);
        Assertions.assertThat(propertyNames.stream().skip(3).collect(Collectors.toList())).containsOnly("createdBy", "updatedBy");
    }

    @Test
    public void testEqualsKeyPropertyComparator() {
        ComponentDescriptor<IOffer> cd = ComponentFactory.getInstance().getDescriptor(IOffer.class);
        IOffer offer = ComponentFactory.getInstance().createInstance(IOffer.class);

        Comparator<String> comparator = IPropertyComparator.equalsKey().createComparator(cd, offer);

        List<String> propertyNames = Lists.newArrayList(cd.getPropertyNames());
        Collections.sort(propertyNames, comparator);
        Assertions.assertThat(propertyNames).startsWith("id");
    }

    @Test
    public void testBottomNullValuePropertyComparator() {
        ComponentDescriptor<IOffer> cd = ComponentFactory.getInstance().getDescriptor(IOffer.class);
        IOffer offer = ComponentFactory.getInstance().createInstance(IOffer.class);
        offer.setName("fill");
        offer.setCreatedBy("fill");
        offer.setUpdatedBy("fill");
        offer.setVersion(10);
        Comparator<String> comparator = IPropertyComparator.nullsLast().createComparator(cd, offer);

        List<String> propertyNames = Lists.newArrayList(cd.getPropertyNames());
        Collections.sort(propertyNames, comparator);
        Assertions.assertThat(propertyNames).endsWith("id");
    }

    @Test
    public void testUpNullValuePropertyComparator() {
        ComponentDescriptor<IOffer> cd = ComponentFactory.getInstance().getDescriptor(IOffer.class);
        IOffer offer = ComponentFactory.getInstance().createInstance(IOffer.class);
        offer.setName("fill");
        offer.setCreatedBy("fill");
        offer.setUpdatedBy("fill");
        offer.setVersion(10);
        Comparator<String> comparator = IPropertyComparator.nullsFirst().createComparator(cd, offer);

        List<String> propertyNames = Lists.newArrayList(cd.getPropertyNames());
        Collections.sort(propertyNames, comparator);
        Assertions.assertThat(propertyNames).startsWith("id");
    }

}
