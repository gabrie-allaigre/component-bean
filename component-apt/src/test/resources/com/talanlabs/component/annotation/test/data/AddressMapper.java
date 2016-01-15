package com.talanlabs.component.annotation.test.data;

@com.talanlabs.component.annotation.GeneratedFrom(com.talanlabs.component.annotation.test.data.IAddress.class)

public final class AddressMapper {

	private AddressMapper() { super(); }

	public static com.talanlabs.component.annotation.test.data.IAddress toAddress(com.talanlabs.component.annotation.test.data.IAddressDto addressDto) {
		return com.talanlabs.component.mapper.ComponentMapper.getInstance().toComponent(addressDto, com.talanlabs.component.annotation.test.data.IAddress.class);
	}

	public static com.talanlabs.component.annotation.test.data.IAddressDto toAddressDto(com.talanlabs.component.annotation.test.data.IAddress address) {
		return com.talanlabs.component.mapper.ComponentMapper.getInstance().toComponent(address, com.talanlabs.component.annotation.test.data.IAddressDto.class);
	}
}