package com.jada.ie;

public class ItemSimpleCsvMapping {
	private String ieProfileGroupName;
	private Integer ieProfileGroupIndex;
	private String ieProfileFieldName;
	private Class<?> ieProfileFieldType;
	private Integer ieProfilePosition;
	private String ieProfileFieldValue;
	
	public ItemSimpleCsvMapping() {
		
	}
	public ItemSimpleCsvMapping(String ieProfileGroupName,
								Integer ieProfileGroupIndex,
								String ieProfileFieldName, 
								Class<?> ieProfileFieldType,
								Integer ieProfilePosition,
								String ieProfileFieldValue) {
		this.ieProfileGroupName = ieProfileGroupName;
		this.ieProfileGroupIndex = ieProfileGroupIndex;
		this.ieProfileFieldName = ieProfileFieldName;
		this.ieProfileFieldType = ieProfileFieldType;
		this.ieProfilePosition = ieProfilePosition;
		this.ieProfileFieldValue = ieProfileFieldValue;
	}
	public String getIeProfileFieldName() {
		return ieProfileFieldName;
	}
	public void setIeProfileFieldName(String ieProfileFieldName) {
		this.ieProfileFieldName = ieProfileFieldName;
	}
	public Integer getIeProfilePosition() {
		return ieProfilePosition;
	}
	public void setIeProfilePosition(Integer ieProfilePosition) {
		this.ieProfilePosition = ieProfilePosition;
	}
	public String getIeProfileFieldValue() {
		return ieProfileFieldValue;
	}
	public void setIeProfileFieldValue(String ieProfileFieldValue) {
		this.ieProfileFieldValue = ieProfileFieldValue;
	}
	public String getIeProfileGroupName() {
		return ieProfileGroupName;
	}
	public void setIeProfileGroupName(String ieProfileGroupName) {
		this.ieProfileGroupName = ieProfileGroupName;
	}
	public Integer getIeProfileGroupIndex() {
		return ieProfileGroupIndex;
	}
	public void setIeProfileGroupIndex(Integer ieProfileGroupIndex) {
		this.ieProfileGroupIndex = ieProfileGroupIndex;
	}
	public Class<?> getIeProfileFieldType() {
		return ieProfileFieldType;
	}
	public void setIeProfileFieldType(Class<?> ieProfileFieldType) {
		this.ieProfileFieldType = ieProfileFieldType;
	}
}
