package com.talanlabs.component.annotation.test.data;

@com.talanlabs.component.annotation.GeneratedFrom(com.talanlabs.component.annotation.test.data.IAddressDto.class)

public final class AddressDtoBuilder {

	private final java.util.Map<String,Object> builder;

	private AddressDtoBuilder() {
		this.builder = new java.util.HashMap<String,Object>();
	}

	public static AddressDtoBuilder newBuilder() {
		return new AddressDtoBuilder();
	}

	public AddressDtoBuilder _copy(com.talanlabs.component.annotation.test.data.IAddressDto component) {
		if (component != null) {
			builder.putAll(component.straightGetProperties());
		}
		return this;
	}

	public  AddressDtoBuilder city(java.lang.String city) {
		builder.put("city",city);
		return this;
	}

	public  AddressDtoBuilder postalZip(int postalZip) {
		builder.put("postalZip",postalZip);
		return this;
	}

	public  AddressDtoBuilder country(com.talanlabs.component.annotation.test.data.ICountry country) {
		builder.put("country",country);
		return this;
	}

	public  AddressDtoBuilder ints(int[] ints) {
		builder.put("ints",ints);
		return this;
	}

	public com.talanlabs.component.annotation.test.data.IAddressDto build() {
		com.talanlabs.component.annotation.test.data.IAddressDto component = com.talanlabs.component.factory.ComponentFactory.getInstance().createInstance(com.talanlabs.component.annotation.test.data.IAddressDto.class);
		component.straightSetProperties(builder);
		return component;
	}
}