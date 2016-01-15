package com.talanlabs.component.dev.samples;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.annotation.ComponentBean;

@ComponentBean
public interface IEntity extends IComponent {

    @EqualsKey
    Long getId();

    void setId(Long id);

    Integer getVersion();

    void setVersion(Integer version);

}
