package com.talanlabs.component.dev.samples;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.annotation.ComponentBean;
import com.talanlabs.component.annotation.GenerateDto;

@ComponentBean
@GenerateDto
public interface IZip extends IComponent {

    String getCity();

    void setCity(String city);

    int getPostalZip();

    void setPostalZip(int postalZip);

    String getCountry();

    void setCountry(String city);

}
