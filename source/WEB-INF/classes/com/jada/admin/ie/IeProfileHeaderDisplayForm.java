package com.jada.admin.ie;

public class IeProfileHeaderDisplayForm {
	boolean selected;
	String ieProfileHeaderId;
	String ieProfileHeaderName;
	String ieProfileType;
	String ieProfileTypeValue;
	String systemRecord;
	boolean modify;
	public String getIeProfileHeaderId() {
		return ieProfileHeaderId;
	}
	public void setIeProfileHeaderId(String ieProfileHeaderId) {
		this.ieProfileHeaderId = ieProfileHeaderId;
	}
	public String getIeProfileHeaderName() {
		return ieProfileHeaderName;
	}
	public void setIeProfileHeaderName(String ieProfileHeaderName) {
		this.ieProfileHeaderName = ieProfileHeaderName;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getSystemRecord() {
		return systemRecord;
	}
	public void setSystemRecord(String systemRecord) {
		this.systemRecord = systemRecord;
	}
	public boolean isModify() {
		return modify;
	}
	public void setModify(boolean modify) {
		this.modify = modify;
	}
	public String getIeProfileType() {
		return ieProfileType;
	}
	public void setIeProfileType(String ieProfileType) {
		this.ieProfileType = ieProfileType;
	}
	public String getIeProfileTypeValue() {
		return ieProfileTypeValue;
	}
	public void setIeProfileTypeValue(String ieProfileTypeValue) {
		this.ieProfileTypeValue = ieProfileTypeValue;
	}
}
