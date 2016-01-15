package com.talanlabs.component.test.it;

import com.talanlabs.component.factory.ComponentFactory;
import com.talanlabs.component.mapper.ComponentMapper;
import com.talanlabs.component.test.data.IAddress;
import com.talanlabs.component.test.data.IUser;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ComponentMapperIT {

    @Test
    public void test0() {
        IAddress address = ComponentFactory.getInstance().createInstance(IAddress.class);
        address.setCity("Valence");
        address.setCountry("France");
        address.setPostalZip("26000");

        Map<String, IAddress> map = new HashMap<>();
        map.put("Valence", address);

        IUser user = ComponentFactory.getInstance().createInstance(IUser.class);
        user.setLogin("gaby");
        user.setNikename("Gabriel");
        user.setGroups(Arrays.asList("admin", "system"));
        user.setInts(new int[] { 1, 2, 3, 4 });
        user.setAddress(address);
        user.setAddresses(Collections.singletonList(address));
        user.setAddresses2(new IAddress[] { address });
        user.setAddresseMap(map);

        IUser newUser = ComponentMapper.getInstance().toComponent(user, IUser.class);

        Assertions.assertThat(newUser).isNotSameAs(user);
        Assertions.assertThat(newUser.getLogin()).isEqualTo(user.getLogin());
        Assertions.assertThat(newUser.getNikename()).isEqualTo(user.getNikename());
        Assertions.assertThat(newUser.getGroups()).isNotSameAs(user.getGroups());
        Assertions.assertThat(newUser.getGroups()).hasSize(2);
        Assertions.assertThat(newUser.getGroups()).containsExactlyElementsOf(user.getGroups());
        Assertions.assertThat(newUser.getInts()).isNotSameAs(user.getInts());
        Assertions.assertThat(newUser.getInts()).containsExactly(user.getInts());
        Assertions.assertThat(newUser.getAddress()).isNotSameAs(user.getAddress());
        Assertions.assertThat(newUser.getAddress().getCity()).isEqualTo(user.getAddress().getCity());
        Assertions.assertThat(newUser.getAddress().getCountry()).isEqualTo(user.getAddress().getCountry());
        Assertions.assertThat(newUser.getAddress().getPostalZip()).isEqualTo(user.getAddress().getPostalZip());
        Assertions.assertThat(newUser.getAddresses()).isNotSameAs(user.getAddresses());
        Assertions.assertThat(newUser.getAddresses()).hasSize(user.getAddresses().size());
        Assertions.assertThat(newUser.getAddresses().get(0)).isNotSameAs(user.getAddresses().get(0));
        Assertions.assertThat(newUser.getAddresses().get(0).getFullname()).isEqualTo(user.getAddresses().get(0).getFullname());
        Assertions.assertThat(newUser.getAddresses2()).isNotSameAs(user.getAddresses2());
        Assertions.assertThat(newUser.getAddresses2()).hasSize(user.getAddresses2().length);
        Assertions.assertThat(newUser.getAddresses2()[0].getFullname()).isEqualTo(user.getAddresses2()[0].getFullname());
        Assertions.assertThat(newUser.getAddresseMap()).isNotSameAs(user.getAddresseMap());
        Assertions.assertThat(newUser.getAddresseMap()).hasSize(user.getAddresseMap().size());
        Assertions.assertThat(newUser.getAddresseMap()).containsKey("Valence");
        Assertions.assertThat(newUser.getAddresseMap().get("Valence")).isNotSameAs(user.getAddresseMap().get("Valence"));
        Assertions.assertThat(newUser.getAddresseMap().get("Valence").getFullname()).isEqualTo(user.getAddresseMap().get("Valence").getFullname());
    }

}
