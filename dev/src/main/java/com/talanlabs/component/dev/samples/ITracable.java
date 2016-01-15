package com.talanlabs.component.dev.samples;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.annotation.ComponentBean;

@ComponentBean
public interface ITracable extends IComponent {

    String getCreatedBy();

    void setCreatedBy(String createdBy);

    String getUpdatedBy();

    void setUpdatedBy(String updatedBy);

}
