package com.talanlabs.component.dev;

import com.talanlabs.component.configuration.ComponentFactoryConfigurationBuilder;
import com.talanlabs.component.configuration.factory.IToStringFactory;
import com.talanlabs.component.configuration.factory.tostring.ClassToStringFactory;
import com.talanlabs.component.configuration.factory.tostring.CompleteToStringFactory;
import com.talanlabs.component.configuration.factory.tostring.compare.IPropertyComparator;
import com.talanlabs.component.configuration.factory.tostring.compare.NullValuePropertyComparator;
import com.talanlabs.component.dev.samples.AddressBuilder;
import com.talanlabs.component.dev.samples.AddressFields;
import com.talanlabs.component.dev.samples.EntityFields;
import com.talanlabs.component.dev.samples.IAddress;
import com.talanlabs.component.dev.samples.ITracable;
import com.talanlabs.component.dev.samples.IZip;
import com.talanlabs.component.dev.samples.OfferBuilder;
import com.talanlabs.component.dev.samples.UserBuilder;
import com.talanlabs.component.dev.samples.ZipBuilder;
import com.talanlabs.component.factory.ComponentFactory;

import java.util.Arrays;

public class MainToString {

    public static void main(String[] args) throws Exception {
        IPropertyComparator defaultPropertyComparator = IPropertyComparator
                .compose(IPropertyComparator.equalsKey(), IPropertyComparator.first(EntityFields.version), IPropertyComparator.last(ITracable.class), new NullValuePropertyComparator(),
                        IPropertyComparator.natural());

        ClassToStringFactory classToStringFactory = new ClassToStringFactory(new CompleteToStringFactory(false, true, false, defaultPropertyComparator));
        classToStringFactory.put(IZip.class, IToStringFactory.simple());
        classToStringFactory.put(IAddress.class, new CompleteToStringFactory(false, true, false, defaultPropertyComparator, false, AddressFields.ints, AddressFields.zips));

        ComponentFactory.setInstance(new ComponentFactory(ComponentFactoryConfigurationBuilder.newBuilder().toStringFactory(classToStringFactory).build()));

        System.out.println(OfferBuilder.newBuilder().id(10L).version(0).name("gaby").createdBy("Moi").build());

        System.out.println(UserBuilder.newBuilder().login("login").nikename("ici")
                .address(AddressBuilder.newBuilder().city("Valence").zips2(Arrays.asList(ZipBuilder.newBuilder().country("France").build())).build()).build());
    }
}
