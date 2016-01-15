package com.talanlabs.component.annotation.test.data;

@com.talanlabs.component.annotation.ComponentBean(createBuilder = true, createFields = true)
@com.talanlabs.component.annotation.GeneratedFrom(com.talanlabs.component.annotation.test.data.IUser.class)

public interface IUserDto extends com.talanlabs.component.IComponent {

	@com.talanlabs.component.IComponent.EqualsKey
	java.lang.String getLogin();

	void setLogin(java.lang.String login);

	java.lang.String getNikename();

	void setNikename(java.lang.String nikename);

	java.lang.String getEmail();

	void setEmail(java.lang.String email);

	java.util.List<java.lang.String> getGroups();

	void setGroups(java.util.List<java.lang.String> groups);

	com.talanlabs.component.annotation.test.data.IAddressDto getAddress();

	void setAddress(com.talanlabs.component.annotation.test.data.IAddressDto address);

	java.util.List<com.talanlabs.component.annotation.test.data.IAddressDto> getAddresses();

	void setAddresses(java.util.List<com.talanlabs.component.annotation.test.data.IAddressDto> addresses);

	com.talanlabs.component.annotation.test.data.IAddressDto[] getAddresses2();

	void setAddresses2(com.talanlabs.component.annotation.test.data.IAddressDto[] addresses2);

}