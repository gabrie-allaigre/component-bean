package com.talanlabs.component.dev.samples;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.annotation.ComponentBean;
import com.talanlabs.component.annotation.GenerateDto;
import com.talanlabs.component.annotation.GenerateDtos;

@ComponentBean
@GenerateDtos({ @GenerateDto(), @GenerateDto(name = "ITitiDto"), @GenerateDto(name = "ITotoDto") })
public interface IFonction<E> extends IComponent {

    E getType();

    void setType(E type);

}
