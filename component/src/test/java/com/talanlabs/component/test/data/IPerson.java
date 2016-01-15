package com.talanlabs.component.test.data;

import com.talanlabs.component.IComponent;

import java.time.LocalDate;

public interface IPerson extends IComponent {

    @EqualsKey
    String getFirstName();

    void setFirstName(String firstName);

    @EqualsKey
    String getLastName();

    void setLastName(String lastName);

    LocalDate getBirthday();

    void setBirthday(LocalDate birthday);

}
