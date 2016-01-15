package com.talanlabs.component.dev.samples;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.annotation.ComponentBean;
import com.talanlabs.component.annotation.GenerateDto;

@ComponentBean
@GenerateDto
public interface IGeneric<E extends Enum<E>> extends IComponent {

    @EqualsKey
    E getName();

    void setName(E name);

}
