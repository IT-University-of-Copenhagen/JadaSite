package com.jada.xml.ie;

import java.util.Date;
import java.util.Vector;

public class ItemAttributeDetail {
	private Long customAttribId;
	private String customAttribName;
	private Long customAttribOptionId;
	private String customAttribValue;
	private String recUpdateBy;
	private Date recUpdateDatetime;
	private String recCreateBy;
	private Date recCreateDatetime;
	private Vector<ItemAttributeDetailLanguage> itemAttributeDetailLanguages = new Vector<ItemAttributeDetailLanguage>(
			0);
	public Long getCustomAttribId() {
		return customAttribId;
	}
	public void setCustomAttribId(Long customAttribId) {
		this.customAttribId = customAttribId;
	}
	public String getCustomAttribName() {
		return customAttribName;
	}
	public void setCustomAttribName(String customAttribName) {
		this.customAttribName = customAttribName;
	}
	public Long getCustomAttribOptionId() {
		return customAttribOptionId;
	}
	public void setCustomAttribOptionId(Long customAttribOptionId) {
		this.customAttribOptionId = customAttribOptionId;
	}
	public String getCustomAttribValue() {
		return customAttribValue;
	}
	public void setCustomAttribValue(String customAttribValue) {
		this.customAttribValue = customAttribValue;
	}
	public String getRecUpdateBy() {
		return recUpdateBy;
	}
	public void setRecUpdateBy(String recUpdateBy) {
		this.recUpdateBy = recUpdateBy;
	}
	public Date getRecUpdateDatetime() {
		return recUpdateDatetime;
	}
	public void setRecUpdateDatetime(Date recUpdateDatetime) {
		this.recUpdateDatetime = recUpdateDatetime;
	}
	public String getRecCreateBy() {
		return recCreateBy;
	}
	public void setRecCreateBy(String recCreateBy) {
		this.recCreateBy = recCreateBy;
	}
	public Date getRecCreateDatetime() {
		return recCreateDatetime;
	}
	public void setRecCreateDatetime(Date recCreateDatetime) {
		this.recCreateDatetime = recCreateDatetime;
	}
	public Vector<ItemAttributeDetailLanguage> getItemAttributeDetailLanguages() {
		return itemAttributeDetailLanguages;
	}
	public void setItemAttributeDetailLanguages(
			Vector<ItemAttributeDetailLanguage> itemAttributeDetailLanguages) {
		this.itemAttributeDetailLanguages = itemAttributeDetailLanguages;
	}
}
