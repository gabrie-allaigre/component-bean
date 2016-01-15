package com.talanlabs.component.dev.samples;

import com.talanlabs.component.annotation.ComponentBean;

@ComponentBean
public interface IOffer extends IEntity, ITracable {

    String getName();

    void setName(String name);

}
