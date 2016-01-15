package com.talanlabs.component.annotation.test.data;

@com.talanlabs.component.annotation.GeneratedFrom(com.talanlabs.component.annotation.test.data.IUserDto.class)

public final class UserDtoBuilder {

	private final java.util.Map<String,Object> builder;

	private UserDtoBuilder() {
		this.builder = new java.util.HashMap<String,Object>();
	}

	public static UserDtoBuilder newBuilder() {
		return new UserDtoBuilder();
	}

	public UserDtoBuilder _copy(com.talanlabs.component.annotation.test.data.IUserDto component) {
		if (component != null) {
			builder.putAll(component.straightGetProperties());
		}
		return this;
	}

	public  UserDtoBuilder login(java.lang.String login) {
		builder.put("login",login);
		return this;
	}

	public  UserDtoBuilder nikename(java.lang.String nikename) {
		builder.put("nikename",nikename);
		return this;
	}

	public  UserDtoBuilder email(java.lang.String email) {
		builder.put("email",email);
		return this;
	}

	public  UserDtoBuilder groups(java.util.List<java.lang.String> groups) {
		builder.put("groups",groups);
		return this;
	}

	public  UserDtoBuilder address(com.talanlabs.component.annotation.test.data.IAddressDto address) {
		builder.put("address",address);
		return this;
	}

	public  UserDtoBuilder addresses(java.util.List<com.talanlabs.component.annotation.test.data.IAddressDto> addresses) {
		builder.put("addresses",addresses);
		return this;
	}

	public  UserDtoBuilder addresses2(com.talanlabs.component.annotation.test.data.IAddressDto[] addresses2) {
		builder.put("addresses2",addresses2);
		return this;
	}

	public com.talanlabs.component.annotation.test.data.IUserDto build() {
		com.talanlabs.component.annotation.test.data.IUserDto component = com.talanlabs.component.factory.ComponentFactory.getInstance().createInstance(com.talanlabs.component.annotation.test.data.IUserDto.class);
		component.straightSetProperties(builder);
		return component;
	}
}