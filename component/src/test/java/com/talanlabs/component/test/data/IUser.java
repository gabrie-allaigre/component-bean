package com.talanlabs.component.test.data;

import com.talanlabs.component.IComponent;

import java.util.List;
import java.util.Map;

public interface IUser extends IComponent {

    @EqualsKey
    String getLogin();

    void setLogin(String login);

    String getNikename();

    void setNikename(String nikename);

    String getEmail();

    void setEmail(String email);

    int[] getInts();

    void setInts(int[] ints);

    List<String> getGroups();

    void setGroups(List<String> groups);

    IAddress getAddress();

    void setAddress(IAddress address);

    List<IAddress> getAddresses();

    void setAddresses(List<IAddress> addresses);

    Map<String, IAddress> getAddresseMap();

    void setAddresseMap(Map<String, IAddress> addresseMap);

    IAddress[] getAddresses2();

    void setAddresses2(IAddress[] addresses2);

    default String getFullname() {
        return getLogin() + " " + getNikename();
    }
}
