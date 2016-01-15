package com.talanlabs.component.dev.samples;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.annotation.ComponentBean;

@ComponentBean
public interface IAgence extends IComponent {

    String getName();

    void setName(String name);

}
