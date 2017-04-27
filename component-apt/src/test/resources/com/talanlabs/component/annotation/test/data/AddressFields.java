package com.talanlabs.component.annotation.test.data;

import com.talanlabs.component.field.*;
import com.talanlabs.component.helper.*;
@com.talanlabs.component.annotation.GeneratedFrom(com.talanlabs.component.annotation.test.data.IAddress.class)
@javax.annotation.Generated("com.talanlabs.component.annotation.processor.ComponentBeanProcessor")

public final class AddressFields {

	private AddressFields() { super(); }

	public static final String CLASS_NAME = "com.talanlabs.component.annotation.test.data.IAddress";

	public static final String city = "city";
	private static IField city_ = null;
	public static synchronized IField city() { if (city_ == null) { city_ = new DefaultField(city); } return city_; }

	public static final String postalZip = "postalZip";
	private static IField postalZip_ = null;
	public static synchronized IField postalZip() { if (postalZip_ == null) { postalZip_ = new DefaultField(postalZip); } return postalZip_; }

	public static final String country = "country";
	private static IField country_ = null;
	public static synchronized IField country() { if (country_ == null) { country_ = new DefaultField(country); } return country_; }

	public static final String ints = "ints";
	private static IField ints_ = null;
	public static synchronized IField ints() { if (ints_ == null) { ints_ = new DefaultField(ints); } return ints_; }

	public static final String dataXml = "dataXml";
	private static IField dataXml_ = null;
	public static synchronized IField dataXml() { if (dataXml_ == null) { dataXml_ = new DefaultField(dataXml); } return dataXml_; }

	public static final class SubFields {

		private String parentName;

		public SubFields(String parentName) { super(); this.parentName = parentName; }

		private IField city_ = null;
		public synchronized IField city() { if (city_ == null) { city_ = new DefaultField(ComponentHelper.PropertyDotBuilder.build(parentName,city)); } return city_; }

		private IField postalZip_ = null;
		public synchronized IField postalZip() { if (postalZip_ == null) { postalZip_ = new DefaultField(ComponentHelper.PropertyDotBuilder.build(parentName,postalZip)); } return postalZip_; }

		private IField country_ = null;
		public synchronized IField country() { if (country_ == null) { country_ = new DefaultField(ComponentHelper.PropertyDotBuilder.build(parentName,country)); } return country_; }

		private IField ints_ = null;
		public synchronized IField ints() { if (ints_ == null) { ints_ = new DefaultField(ComponentHelper.PropertyDotBuilder.build(parentName,ints)); } return ints_; }

		private IField dataXml_ = null;
		public synchronized IField dataXml() { if (dataXml_ == null) { dataXml_ = new DefaultField(ComponentHelper.PropertyDotBuilder.build(parentName,dataXml)); } return dataXml_; }

	}
}