package com.talanlabs.component.annotation.test.data;

@com.talanlabs.component.annotation.GeneratedFrom(com.talanlabs.component.annotation.test.data.IUser.class)
@javax.annotation.Generated("com.talanlabs.component.annotation.processor.ComponentBeanProcessor")

public final class UserMapper {

	private UserMapper() { super(); }

	public static com.talanlabs.component.annotation.test.data.IUser toUser(com.talanlabs.component.annotation.test.data.IUserDto userDto) {
		return com.talanlabs.component.mapper.ComponentMapper.getInstance().toComponent(userDto, com.talanlabs.component.annotation.test.data.IUser.class);
	}

	public static com.talanlabs.component.annotation.test.data.IUserDto toUserDto(com.talanlabs.component.annotation.test.data.IUser user) {
		return com.talanlabs.component.mapper.ComponentMapper.getInstance().toComponent(user, com.talanlabs.component.annotation.test.data.IUserDto.class);
	}
}