package com.jada.content.frontend;

import java.util.Hashtable;

import org.apache.struts.action.ActionForm;

public class FrontendBaseForm extends ActionForm {
	private static final long serialVersionUID = 1L;
	Hashtable<String, String> table = new Hashtable<String, String>();
	boolean useTemplate = false;
	boolean printTemplate = false;
	
	public void addMessage(String key, String value) {
		table.put(key, value);
	}
	
	public String getMessage(String key) {
		String value = table.get(key);
		if (value == null) {
			return "";
		}
		return value;
	}
	
	public boolean hasMessage() {
		return !table.isEmpty();
	}

	public boolean isUseTemplate() {
		return useTemplate;
	}

	public void setUseTemplate(boolean useTemplate) {
		this.useTemplate = useTemplate;
	}

	public boolean isPrintTemplate() {
		return printTemplate;
	}

	public void setPrintTemplate(boolean printTemplate) {
		this.printTemplate = printTemplate;
	}
}
