package com.talanlabs.component.dev;

import com.talanlabs.component.dev.samples.AgenceBuilder;
import com.talanlabs.component.dev.samples.IAddress;
import com.talanlabs.component.dev.samples.IAgence;
import com.talanlabs.component.dev.samples.IZip;
import com.talanlabs.component.dev.samples.ZipBuilder;
import com.talanlabs.component.mapper.ComponentMapper;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainMapper2 {

    public static void main(String[] args) {
        Map<String, IZip> map3 = new HashMap<>();
        map3.put("ex1", ZipBuilder.newBuilder().country("Map1").build());
        map3.put("ex2", ZipBuilder.newBuilder().country("Map2").build());

        Map<IZip, IAgence> map4 = new HashMap<>();
        map4.put(ZipBuilder.newBuilder().country("Map1").build(), AgenceBuilder.newBuilder().name("Agence1").build());
        map4.put(ZipBuilder.newBuilder().country("Map2").build(), AgenceBuilder.newBuilder().name("Agence2").build());

        Address address = new Address();
        address.country = "France";
        address.city = "Valence";
        address.postalZip = 26000;
        address.ints = new int[] { 1, 2, 3 };
        address.zips = new IZip[] { ZipBuilder.newBuilder().country("Rien").build() };
        address.zips2 = Arrays.asList(ZipBuilder.newBuilder().country("Toto1").build(), ZipBuilder.newBuilder().country("Toto2").build());
        address.zips3 = map3;
        address.zips4 = map4;
        address.createdBy = "Gabriel Allaigre";

        System.out.println(address);

        IAddress address1 = ComponentMapper.getInstance().toComponent(address, IAddress.class);
        System.out.println(address1);

        Address address2 = ComponentMapper.getInstance().fromComponent(address1, Address.class);
        System.out.println(address2);
    }

    public static class Address extends Tracable {

        public String city;

        private int postalZip;

        private String country;

        private int[] ints;

        private IZip[] zips;

        private List<IZip> zips2;

        private Map<String, IZip> zips3;

        private Map<IZip, IAgence> zips4;

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

    public static class Tracable {

        public String createdBy;

    }
}
