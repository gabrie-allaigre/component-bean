package com.talanlabs.component.dev.samples;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.annotation.ComponentBean;
import com.talanlabs.component.annotation.GenerateDto;

import java.util.List;

@ComponentBean
@GenerateDto
public interface IParent extends IComponent {

    List<IFils> getFils();

    void setFils(List<IFils> fils);

    @ComponentBean
    interface IFils extends IComponent {

        String getName();

        void setName(String name);

    }

}
