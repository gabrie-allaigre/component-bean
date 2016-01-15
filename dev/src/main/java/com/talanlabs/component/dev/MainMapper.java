package com.talanlabs.component.dev;

import com.google.common.reflect.TypeToken;
import com.talanlabs.component.dev.samples.AddressBuilder;
import com.talanlabs.component.dev.samples.AddressMapper;
import com.talanlabs.component.dev.samples.AgenceBuilder;
import com.talanlabs.component.dev.samples.IAddress;
import com.talanlabs.component.dev.samples.IAddressDto;
import com.talanlabs.component.dev.samples.IAgence;
import com.talanlabs.component.dev.samples.IZip;
import com.talanlabs.component.dev.samples.ZipBuilder;
import com.talanlabs.component.mapper.ComponentMapper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainMapper {

    public static void main(String[] args) {
        Map<String, IZip> map3 = new HashMap<>();
        map3.put("ex1", ZipBuilder.newBuilder().country("Map1").build());
        map3.put("ex2", ZipBuilder.newBuilder().country("Map2").build());

        Map<IZip, IAgence> map4 = new HashMap<>();
        map4.put(ZipBuilder.newBuilder().country("Map1").build(), AgenceBuilder.newBuilder().name("Agence1").build());
        map4.put(ZipBuilder.newBuilder().country("Map2").build(), AgenceBuilder.newBuilder().name("Agence2").build());

        IAddress address = AddressBuilder.newBuilder().city("Valence").country("FRANCE").postalZip(26000).ints(new int[] { 1, 2, 3 })
                .zips(new IZip[] { ZipBuilder.newBuilder().country("Rien").build() })
                .zips2(Arrays.asList(ZipBuilder.newBuilder().country("Toto1").build(), ZipBuilder.newBuilder().country("Toto2").build())).zips3(map3).zips4(map4).createdBy("Gabriel Allaigre").build();
        IAddressDto addressDto = AddressMapper.toAddressDto(address);
        System.out.println(addressDto);
        System.out.println(AddressMapper.toAddress(addressDto));

        IAddress address1 = ComponentMapper.getInstance().toComponent(address.straightGetProperties(), IAddress.class);
        System.out.println(address1);

        Map<String, Object> map = ComponentMapper.getInstance().fromComponent(address, new TypeToken<Map<String, Object>>() {
        }.getType());
        System.out.println(map);
    }
}
