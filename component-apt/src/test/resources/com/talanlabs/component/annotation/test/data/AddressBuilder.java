package com.talanlabs.component.annotation.test.data;

@com.talanlabs.component.annotation.GeneratedFrom(com.talanlabs.component.annotation.test.data.IAddress.class)

public final class AddressBuilder {

	private final java.util.Map<String,Object> builder;

	private AddressBuilder() {
		this.builder = new java.util.HashMap<String,Object>();
	}

	public static AddressBuilder newBuilder() {
		return new AddressBuilder();
	}

	public AddressBuilder _copy(com.talanlabs.component.annotation.test.data.IAddress component) {
		if (component != null) {
			builder.putAll(component.straightGetProperties());
		}
		return this;
	}

	public  AddressBuilder city(java.lang.String city) {
		builder.put("city",city);
		return this;
	}

	public  AddressBuilder postalZip(int postalZip) {
		builder.put("postalZip",postalZip);
		return this;
	}

	public  AddressBuilder country(com.talanlabs.component.annotation.test.data.ICountry country) {
		builder.put("country",country);
		return this;
	}

	public  AddressBuilder ints(int[] ints) {
		builder.put("ints",ints);
		return this;
	}

	public com.talanlabs.component.annotation.test.data.IAddress build() {
		com.talanlabs.component.annotation.test.data.IAddress component = com.talanlabs.component.factory.ComponentFactory.getInstance().createInstance(com.talanlabs.component.annotation.test.data.IAddress.class);
		component.straightSetProperties(builder);
		return component;
	}
}