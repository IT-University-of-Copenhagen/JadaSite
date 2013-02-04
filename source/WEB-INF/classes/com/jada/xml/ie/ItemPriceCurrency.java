package com.jada.xml.ie;

import java.util.Date;

public class ItemPriceCurrency {
	private Float itemPrice;
	private char itemPriceTypeCode;
	private Date itemPricePublishOn;
	private Date itemPriceExpireOn;
	private String recUpdateBy;
	private Date recUpdateDatetime;
	private String recCreateBy;
	private Date recCreateDatetime;
	private Long siteCurrencyClassId;
	private String siteCurrencyClassName;
	public Float getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(Float itemPrice) {
		this.itemPrice = itemPrice;
	}
	public char getItemPriceTypeCode() {
		return itemPriceTypeCode;
	}
	public void setItemPriceTypeCode(char itemPriceTypeCode) {
		this.itemPriceTypeCode = itemPriceTypeCode;
	}
	public Date getItemPricePublishOn() {
		return itemPricePublishOn;
	}
	public void setItemPricePublishOn(Date itemPricePublishOn) {
		this.itemPricePublishOn = itemPricePublishOn;
	}
	public Date getItemPriceExpireOn() {
		return itemPriceExpireOn;
	}
	public void setItemPriceExpireOn(Date itemPriceExpireOn) {
		this.itemPriceExpireOn = itemPriceExpireOn;
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
	public Long getSiteCurrencyClassId() {
		return siteCurrencyClassId;
	}
	public void setSiteCurrencyClassId(Long siteCurrencyClassId) {
		this.siteCurrencyClassId = siteCurrencyClassId;
	}
	public String getSiteCurrencyClassName() {
		return siteCurrencyClassName;
	}
	public void setSiteCurrencyClassName(String siteCurrencyClassName) {
		this.siteCurrencyClassName = siteCurrencyClassName;
	}
}
