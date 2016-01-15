package com.talanlabs.component.dev.samples;

import com.talanlabs.component.annotation.ComponentBean;
import com.talanlabs.component.annotation.GenerateDto;

import java.util.List;
import java.util.Map;

@ComponentBean
@GenerateDto
public interface IAddress extends ITracable {

    @Deprecated
    String getCity();

    void setCity(String city);

    int getPostalZip();

    void setPostalZip(int postalZip);

    String getCountry();

    void setCountry(String country);

    int[] getInts();

    void setInts(int[] ints);

    IZip[] getZips();

    void setZips(IZip[] zips);

    List<IZip> getZips2();

    void setZips2(List<IZip> zips2);

    Map<String, IZip> getZips3();

    void setZips3(Map<String, IZip> zips3);

    Map<IZip, IAgence> getZips4();

    void setZips4(Map<IZip, IAgence> zips4);

}
