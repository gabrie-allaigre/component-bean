package com.talanlabs.component.test.data;

import com.talanlabs.component.IComponent;

public interface IAddress extends IComponent {

    String getCity();

    void setCity(String city);

    String getPostalZip();

    void setPostalZip(String postalZip);

    String getCountry();

    void setCountry(String country);

    @Computed(AddressComputed.class)
    String getFullname();

    @Computed(value = AddressComputed.class)
    String getDataXml();

    @Computed(AddressComputed.class)
    void setDataXml(String dataXml);

    @Computed(AddressComputed.class)
    void transform();

    @Computed(value = AddressComputed.class)
    @NoProperty
    String getDataXml2();
}
