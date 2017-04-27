package com.talanlabs.component.annotation.test.data;

@com.talanlabs.component.annotation.ComponentBean(createBuilder = true, createFields = true)
@com.talanlabs.component.annotation.GeneratedFrom(com.talanlabs.component.annotation.test.data.IAddress.class)
@javax.annotation.Generated("com.talanlabs.component.annotation.processor.ComponentBeanProcessor")

public interface IAddressDto extends com.talanlabs.component.IComponent {

    java.lang.String getCity();

    void setCity(java.lang.String city);

    int getPostalZip();

    void setPostalZip(int postalZip);

    com.talanlabs.component.annotation.test.data.ICountry getCountry();

    void setCountry(com.talanlabs.component.annotation.test.data.ICountry country);

    int[] getInts();

    void setInts(int[] ints);

}