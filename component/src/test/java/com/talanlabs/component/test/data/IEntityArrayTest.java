package com.talanlabs.component.test.data;

import com.talanlabs.component.IComponent;

public interface IEntityArrayTest extends IComponent {

    @EqualsKey
    IEntity[] getIds();

    void setIds(IEntity[] ids);

}
