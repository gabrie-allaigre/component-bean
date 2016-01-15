package com.talanlabs.component.test.data;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.annotation.ComponentBean;

@ComponentBean
public interface ITest3 extends IComponent {

    default String getName() {
        return "coucou";
    }

    default void setName(String name) {
        System.out.println("Set name");
    }

    default void rien() {
        System.out.println("rien");
    }

    @NoProperty
    default String getName2() {
        return "coucou";
    }

    default String getName3() {
        return "coucou";
    }
}
