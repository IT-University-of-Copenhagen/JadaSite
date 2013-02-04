package com.jada.admin.ie;

public class IeProfileDetailDisplayForm {
	boolean selected;
	String ieProfileDetailId;
	String ieProfileGroupName;
	String ieProfileGroupIndex;
	String ieProfileFieldName;
	String ieProfileFieldValue;
	String ieProfilePosition;
	String ieProfilePositionError;
	public String getIeProfileDetailId() {
		return ieProfileDetailId;
	}
	public void setIeProfileDetailId(String ieProfileDetailId) {
		this.ieProfileDetailId = ieProfileDetailId;
	}
	public String getIeProfileFieldName() {
		return ieProfileFieldName;
	}
	public void setIeProfileFieldName(String ieProfileDetailFieldName) {
		this.ieProfileFieldName = ieProfileDetailFieldName;
	}
	public String getIeProfileFieldValue() {
		return ieProfileFieldValue;
	}
	public void setIeProfileFieldValue(String ieProfileDetailFieldValue) {
		this.ieProfileFieldValue = ieProfileDetailFieldValue;
	}
	public String getIeProfilePosition() {
		return ieProfilePosition;
	}
	public void setIeProfilePosition(String ieProfilePosition) {
		this.ieProfilePosition = ieProfilePosition;
	}
	public String getIeProfilePositionError() {
		return ieProfilePositionError;
	}
	public void setIeProfilePositionError(String ieProfilePositionError) {
		this.ieProfilePositionError = ieProfilePositionError;
	}
	public String getIeProfileGroupName() {
		return ieProfileGroupName;
	}
	public void setIeProfileGroupName(String ieProfileGroupName) {
		this.ieProfileGroupName = ieProfileGroupName;
	}
	public String getIeProfileGroupIndex() {
		return ieProfileGroupIndex;
	}
	public void setIeProfileGroupIndex(String ieProfileGroupIndex) {
		this.ieProfileGroupIndex = ieProfileGroupIndex;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
