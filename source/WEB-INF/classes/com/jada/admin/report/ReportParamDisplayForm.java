package com.jada.admin.report;

import org.apache.struts.util.LabelValueBean;

public class ReportParamDisplayForm {
	String displayName;
	String name;
	String value;
	String type;
	String required;
	String message;
	LabelValueBean values[];
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRequired() {
		return required;
	}
	public void setRequired(String required) {
		this.required = required;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public LabelValueBean[] getValues() {
		return values;
	}
	public void setValues(LabelValueBean[] values) {
		this.values = values;
	}
}
