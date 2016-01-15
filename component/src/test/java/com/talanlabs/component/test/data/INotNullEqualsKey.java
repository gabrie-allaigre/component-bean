package com.talanlabs.component.test.data;

import com.talanlabs.component.IComponent;

public interface INotNullEqualsKey extends IComponent {

    @EqualsKey(nullEquals = false)
    String getFirstName();

    void setFirstName(String firstName);

}
