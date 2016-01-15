package com.talanlabs.component.annotation.test.data;

import com.talanlabs.component.field.*;
import com.talanlabs.component.helper.*;
@com.talanlabs.component.annotation.GeneratedFrom(com.talanlabs.component.annotation.test.data.IUserDto.class)

public final class UserDtoFields {

	private UserDtoFields() { super(); }

	public static final String CLASS_NAME = "com.talanlabs.component.annotation.test.data.IUserDto";

	public static final String login = "login";
	private static IField login_ = null;
	public static synchronized IField login() { if (login_ == null) { login_ = new DefaultField(login); } return login_; }

	public static final String nikename = "nikename";
	private static IField nikename_ = null;
	public static synchronized IField nikename() { if (nikename_ == null) { nikename_ = new DefaultField(nikename); } return nikename_; }

	public static final String email = "email";
	private static IField email_ = null;
	public static synchronized IField email() { if (email_ == null) { email_ = new DefaultField(email); } return email_; }

	public static final String groups = "groups";
	private static IField groups_ = null;
	public static synchronized IField groups() { if (groups_ == null) { groups_ = new DefaultField(groups); } return groups_; }

	public static final String address = "address";
	private static IDotField<com.talanlabs.component.annotation.test.data.AddressDtoFields.SubFields> address_ = null;
	public static synchronized IDotField<com.talanlabs.component.annotation.test.data.AddressDtoFields.SubFields> address() { if (address_ == null) { address_ = new DefaultDotField<com.talanlabs.component.annotation.test.data.AddressDtoFields.SubFields>(new com.talanlabs.component.annotation.test.data.AddressDtoFields.SubFields(address),address); } return address_; }

	public static final String addresses = "addresses";
	private static IField addresses_ = null;
	public static synchronized IField addresses() { if (addresses_ == null) { addresses_ = new DefaultField(addresses); } return addresses_; }

	public static final String addresses2 = "addresses2";
	private static IField addresses2_ = null;
	public static synchronized IField addresses2() { if (addresses2_ == null) { addresses2_ = new DefaultField(addresses2); } return addresses2_; }

	public static final class SubFields {

		private String parentName;

		public SubFields(String parentName) { super(); this.parentName = parentName; }

		private IField login_ = null;
		public synchronized IField login() { if (login_ == null) { login_ = new DefaultField(ComponentHelper.PropertyDotBuilder.build(parentName,login)); } return login_; }

		private IField nikename_ = null;
		public synchronized IField nikename() { if (nikename_ == null) { nikename_ = new DefaultField(ComponentHelper.PropertyDotBuilder.build(parentName,nikename)); } return nikename_; }

		private IField email_ = null;
		public synchronized IField email() { if (email_ == null) { email_ = new DefaultField(ComponentHelper.PropertyDotBuilder.build(parentName,email)); } return email_; }

		private IField groups_ = null;
		public synchronized IField groups() { if (groups_ == null) { groups_ = new DefaultField(ComponentHelper.PropertyDotBuilder.build(parentName,groups)); } return groups_; }

		private IDotField<com.talanlabs.component.annotation.test.data.AddressDtoFields.SubFields> address_ = null;
		public synchronized IDotField<com.talanlabs.component.annotation.test.data.AddressDtoFields.SubFields> address() { if (address_ == null) { address_ = new DefaultDotField<com.talanlabs.component.annotation.test.data.AddressDtoFields.SubFields>(new com.talanlabs.component.annotation.test.data.AddressDtoFields.SubFields(ComponentHelper.PropertyDotBuilder.build(parentName,address)),ComponentHelper.PropertyDotBuilder.build(parentName,address)); } return address_; }

		private IField addresses_ = null;
		public synchronized IField addresses() { if (addresses_ == null) { addresses_ = new DefaultField(ComponentHelper.PropertyDotBuilder.build(parentName,addresses)); } return addresses_; }

		private IField addresses2_ = null;
		public synchronized IField addresses2() { if (addresses2_ == null) { addresses2_ = new DefaultField(ComponentHelper.PropertyDotBuilder.build(parentName,addresses2)); } return addresses2_; }

	}
}