package com.talanlabs.component.test.data;

import com.talanlabs.component.IComponent;

public interface IIntArrayTest extends IComponent {

    @EqualsKey
    int[] getIds();

    void setIds(int[] ids);

}
