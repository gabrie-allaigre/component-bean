package com.talanlabs.component.dev.samples;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.annotation.ComponentBean;

@ComponentBean
public interface ITest2 extends IComponent {

    @Computed(Test2Computed.class)
    String getName();

    @Computed(Test2Computed.class)
    void setName(String name);

    @Computed(Test2Computed.class)
    void rien();
}
