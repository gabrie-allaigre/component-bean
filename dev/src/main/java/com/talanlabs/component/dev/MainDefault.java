package com.talanlabs.component.dev;

import com.talanlabs.component.dev.samples.ITest3;
import com.talanlabs.component.dev.samples.Test3Builder;
import com.talanlabs.component.factory.ComponentDescriptor;
import com.talanlabs.component.factory.ComponentFactory;

public class MainDefault {

    public static void main(String[] args) throws Exception {
        ITest3 test = Test3Builder.newBuilder().build();
        test.setName("Gabriel");
        System.out.println(test.getName());
        test.rien();

        System.out.println(test.straightGetPropertyNames());
        ComponentDescriptor<ITest3> cd = ComponentFactory.getInstance().getDescriptor(test);
        System.out.println(cd.getPropertyDescriptors());
    }
}
