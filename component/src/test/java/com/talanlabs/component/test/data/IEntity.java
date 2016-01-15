package com.talanlabs.component.test.data;

import com.talanlabs.component.IComponent;

public interface IEntity extends IComponent {

    @EqualsKey
    Long getId();

    void setId(Long id);

    Integer getVersion();

    void setVersion(Integer version);

}
