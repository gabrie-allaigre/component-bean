package com.talanlabs.component.test.data;

import com.talanlabs.component.IComponent;

public interface IStringArrayTest extends IComponent {

    @EqualsKey
    String[] getIds();

    void setIds(String[] ids);

}
