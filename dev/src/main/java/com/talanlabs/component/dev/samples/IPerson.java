package com.talanlabs.component.dev.samples;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.annotation.ComponentBean;
import com.talanlabs.component.annotation.GenerateDto;

@ComponentBean
@GenerateDto(includeFields = { "name", "zip", "fonction->ITotoDto", "toto" }, includeExtends = { "com.talanlabs.component.dev.samples.IFonction<java.lang.String>->ITotoDto" }, classToClasss = {
        "com.talanlabs.component.dev.samples.IFonction<java.lang.String>->ITitiDto" })
public interface IPerson extends IComponent, IFonction<String> {

    String getName();

    void setName(String name);

    String getSurname();

    void setSurname(String surname);

    String getZip();

    void setZip(String zip);

    String getCity();

    void setCity(String city);

    String getCountry();

    void setCountry(String country);

    IFonction<String> getFonction();

    void setFonction(IFonction<String> fonction);

    default String getFullName() {
        return getName() + " " + getSurname();
    }

    <E extends IFonction<String> & IAddress> E getToto();

    <E extends IFonction<String> & IAddress> void setToto(E toto);

}
