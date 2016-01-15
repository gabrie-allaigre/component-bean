package com.talanlabs.component.annotation.test.data;

public class AddressComputed {

    private String dataXml;

    public String getFullname(IAddress address) {
        return address.getPostalZip() + " " + address.getCity() + " " + address.getCountry();
    }

    public String getDataXml(IAddress address) {
        return dataXml;
    }

    public void setDataXml(IAddress address, String dataXml) {
        this.dataXml = dataXml;
    }

    public void transform(IAddress address) {
    }

    public String getDataXml2(IAddress address) {
        return dataXml;
    }

    public void setDataXml2(IAddress address, String dataXml) {
        this.dataXml = dataXml;
    }
}
