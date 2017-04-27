package com.talanlabs.component.annotation.test.data;

@com.talanlabs.component.annotation.GeneratedFrom(com.talanlabs.component.annotation.test.data.IUser.class)
@javax.annotation.Generated("com.talanlabs.component.annotation.processor.ComponentBeanProcessor")

public final class UserBuilder {

	private final java.util.Map<String,Object> builder;

	private UserBuilder() {
		this.builder = new java.util.HashMap<String,Object>();
	}

	public static UserBuilder newBuilder() {
		return new UserBuilder();
	}

	public UserBuilder _copy(com.talanlabs.component.annotation.test.data.IUser component) {
		if (component != null) {
			builder.putAll(component.straightGetProperties());
		}
		return this;
	}

	public  UserBuilder login(java.lang.String login) {
		builder.put("login",login);
		return this;
	}

	public  UserBuilder nikename(java.lang.String nikename) {
		builder.put("nikename",nikename);
		return this;
	}

	public  UserBuilder email(java.lang.String email) {
		builder.put("email",email);
		return this;
	}

	public  UserBuilder groups(java.util.List<java.lang.String> groups) {
		builder.put("groups",groups);
		return this;
	}

	public  UserBuilder address(com.talanlabs.component.annotation.test.data.IAddress address) {
		builder.put("address",address);
		return this;
	}

	public  UserBuilder addresses(java.util.List<com.talanlabs.component.annotation.test.data.IAddress> addresses) {
		builder.put("addresses",addresses);
		return this;
	}

	public  UserBuilder addresses2(com.talanlabs.component.annotation.test.data.IAddress[] addresses2) {
		builder.put("addresses2",addresses2);
		return this;
	}

	public com.talanlabs.component.annotation.test.data.IUser build() {
		com.talanlabs.component.annotation.test.data.IUser component = com.talanlabs.component.factory.ComponentFactory.getInstance().createInstance(com.talanlabs.component.annotation.test.data.IUser.class);
		component.straightSetProperties(builder);
		return component;
	}
}