package com.talanlabs.component.test.data;

import com.talanlabs.component.IComponent;

public interface ITracable extends IComponent {

    String getCreatedBy();

    void setCreatedBy(String createdBy);

    String getUpdatedBy();

    void setUpdatedBy(String updatedBy);

}
