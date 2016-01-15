package com.talanlabs.component.test.unit;

import com.talanlabs.component.configuration.factory.IToStringFactory;
import com.talanlabs.component.configuration.factory.tostring.ClassToStringFactory;
import com.talanlabs.component.configuration.factory.tostring.CompleteToStringFactory;
import com.talanlabs.component.configuration.factory.tostring.compare.IPropertyComparator;
import com.talanlabs.component.factory.ComponentDescriptor;
import com.talanlabs.component.factory.ComponentFactory;
import com.talanlabs.component.test.data.IOffer;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ToStringFactoryTest {

    @Test
    public void testSimpleToStringFactory() {
        ComponentDescriptor<IOffer> cd = ComponentFactory.getInstance().getDescriptor(IOffer.class);

        IOffer offer = ComponentFactory.getInstance().createInstance(IOffer.class);
        offer.setName("fill");
        offer.setCreatedBy("fill");
        offer.setUpdatedBy("fill");
        offer.setVersion(10);

        Assertions.assertThat(IToStringFactory.simple().buildToString(cd, offer)).startsWith("com.talanlabs.component.test.data.IOffer@").doesNotEndWith("}");
    }

    @Test
    public void testUniqueLineCompleteToStringFactory() {
        ComponentDescriptor<IOffer> cd = ComponentFactory.getInstance().getDescriptor(IOffer.class);

        IOffer offer = ComponentFactory.getInstance().createInstance(IOffer.class);
        offer.setName("fill");
        offer.setCreatedBy("fill");
        offer.setUpdatedBy("fill");
        offer.setVersion(10);

        Assertions.assertThat(new CompleteToStringFactory(false, false, false, IPropertyComparator.natural()).buildToString(cd, offer)).startsWith("com.talanlabs.component.test.data.IOffer@")
                .endsWith("{\"createdBy\" : \"fill\",\"id\" : null,\"name\" : \"fill\",\"updatedBy\" : \"fill\",\"version\" : 10}");
    }

    @Test
    public void testMultiLineCompleteToStringFactory() {
        ComponentDescriptor<IOffer> cd = ComponentFactory.getInstance().getDescriptor(IOffer.class);

        IOffer offer = ComponentFactory.getInstance().createInstance(IOffer.class);
        offer.setName("fill");
        offer.setCreatedBy("fill");
        offer.setUpdatedBy("fill");
        offer.setVersion(10);

        Assertions.assertThat(new CompleteToStringFactory(false, true, false, IPropertyComparator.natural()).buildToString(cd, offer)).startsWith("com.talanlabs.component.test.data.IOffer@")
                .endsWith("{\n" + "\t\"createdBy\" : \"fill\",\n" + "\t\"id\" : null,\n" + "\t\"name\" : \"fill\",\n" + "\t\"updatedBy\" : \"fill\",\n" + "\t\"version\" : 10\n" + "}");
    }

    @Test
    public void testNotHeaderCompleteToStringFactory() {
        ComponentDescriptor<IOffer> cd = ComponentFactory.getInstance().getDescriptor(IOffer.class);

        IOffer offer = ComponentFactory.getInstance().createInstance(IOffer.class);
        offer.setName("fill");
        offer.setCreatedBy("fill");
        offer.setUpdatedBy("fill");
        offer.setVersion(10);

        Assertions.assertThat(new CompleteToStringFactory(true, true, false, IPropertyComparator.natural()).buildToString(cd, offer))
                .isEqualTo("{\n" + "\t\"createdBy\" : \"fill\",\n" + "\t\"id\" : null,\n" + "\t\"name\" : \"fill\",\n" + "\t\"updatedBy\" : \"fill\",\n" + "\t\"version\" : 10\n" + "}");
    }

    @Test
    public void testHideNullValueCompleteToStringFactory() {
        ComponentDescriptor<IOffer> cd = ComponentFactory.getInstance().getDescriptor(IOffer.class);

        IOffer offer = ComponentFactory.getInstance().createInstance(IOffer.class);
        offer.setId(null);
        offer.setName("fill");
        offer.setCreatedBy(null);
        offer.setUpdatedBy(null);
        offer.setVersion(10);

        Assertions.assertThat(new CompleteToStringFactory(true, true, true, IPropertyComparator.natural()).buildToString(cd, offer))
                .isEqualTo("{\n" + "\t\"name\" : \"fill\",\n" + "\t\"version\" : 10\n" + "}");
    }

    @Test
    public void testDefaultClassCompleteToStringFactory() {
        ComponentDescriptor<IOffer> cd = ComponentFactory.getInstance().getDescriptor(IOffer.class);

        IOffer offer = ComponentFactory.getInstance().createInstance(IOffer.class);
        offer.setName("fill");
        offer.setCreatedBy("fill");
        offer.setUpdatedBy("fill");
        offer.setVersion(10);

        ClassToStringFactory classToStringFactory = new ClassToStringFactory(IToStringFactory.simple());
        Assertions.assertThat(classToStringFactory.buildToString(cd, offer)).startsWith("com.talanlabs.component.test.data.IOffer@").doesNotEndWith("}");
    }

    @Test
    public void testClassCompleteToStringFactory() {
        ComponentDescriptor<IOffer> cd = ComponentFactory.getInstance().getDescriptor(IOffer.class);

        IOffer offer = ComponentFactory.getInstance().createInstance(IOffer.class);
        offer.setName("fill");
        offer.setCreatedBy("fill");
        offer.setUpdatedBy("fill");
        offer.setVersion(10);

        ClassToStringFactory classToStringFactory = new ClassToStringFactory(IToStringFactory.simple());
        classToStringFactory.put(IOffer.class, IToStringFactory.complete());
        Assertions.assertThat(classToStringFactory.buildToString(cd, offer)).startsWith("com.talanlabs.component.test.data.IOffer@").endsWith("}");
    }
}
