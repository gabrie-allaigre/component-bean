package com.talanlabs.component.dev;

import com.talanlabs.component.dev.samples.ITest2;
import com.talanlabs.component.dev.samples.Test2Builder;
import com.talanlabs.component.factory.ComponentDescriptor;
import com.talanlabs.component.factory.ComponentFactory;

public class MainComputed {

    public static void main(String[] args) throws Exception {
        ITest2 test = Test2Builder.newBuilder().build();
        test.setName("Gabriel");
        System.out.println(test.getName());
        test.rien();

        System.out.println(test.straightGetPropertyNames());
        ComponentDescriptor<ITest2> cd = ComponentFactory.getInstance().getDescriptor(test);
        System.out.println(cd.getPropertyDescriptors());

        System.out.println(test.straightGetProperty("name"));

        test.straightSetProperty("name", "Sandra");
        System.out.println(test.getName());
    }
}
