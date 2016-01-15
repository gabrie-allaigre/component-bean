package com.talanlabs.component.annotation.test.data;

import com.talanlabs.component.IComponent;
import com.talanlabs.component.annotation.ComponentBean;
import com.talanlabs.component.annotation.GenerateDto;

import java.util.List;

@ComponentBean
@GenerateDto
public interface IUser extends IComponent {

    @EqualsKey
    String getLogin();

    void setLogin(String login);

    String getNikename();

    void setNikename(String nikename);

    String getEmail();

    void setEmail(String email);

    List<String> getGroups();

    void setGroups(List<String> groups);

    IAddress getAddress();

    void setAddress(IAddress address);

    List<IAddress> getAddresses();

    void setAddresses(List<IAddress> addresses);

    IAddress[] getAddresses2();

    void setAddresses2(IAddress[] addresses2);

    default String getFullname() {
        return getLogin() + " " + getNikename();
    }

    @NoProperty
    default String getFullname2() {
        return getLogin() + " " + getNikename();
    }
}
