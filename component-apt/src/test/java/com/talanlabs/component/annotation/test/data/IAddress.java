package com.talanlabs.component.annotation.test.data;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.annotation.ComponentBean;
import com.talanlabs.component.annotation.GenerateDto;

@ComponentBean
@GenerateDto
public interface IAddress extends IComponent {

    String getCity();

    void setCity(String city);

    int getPostalZip();

    void setPostalZip(int postalZip);

    ICountry getCountry();

    void setCountry(ICountry country);

    int[] getInts();

    void setInts(int[] ints);

    @Computed(value = AddressComputed.class)
    String getDataXml();

    @Computed(AddressComputed.class)
    void setDataXml(String dataXml);

    @Computed(value = AddressComputed.class)
    @NoProperty
    String getDataXml2();

    @Computed(value = AddressComputed.class)
    void setDataXml2(String dataXml);
}
