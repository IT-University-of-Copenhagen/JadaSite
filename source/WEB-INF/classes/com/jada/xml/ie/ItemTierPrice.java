package com.jada.xml.ie;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ItemTierPrice {
	private Long custClassId;
	private String custClassName;
	private Integer itemTierQty;
	private Date itemTierPricePublishOn;
	private Date itemTierPriceExpireOn;
	private String recUpdateBy;
	private Date recUpdateDatetime;
	private String recCreateBy;
	private Date recCreateDatetime;
	private Set<ItemTierPriceCurrency> itemTierPriceCurrencies = new HashSet<ItemTierPriceCurrency>(
			0);
	public Long getCustClassId() {
		return custClassId;
	}
	public void setCustClassId(Long custClassId) {
		this.custClassId = custClassId;
	}
	public String getCustClassName() {
		return custClassName;
	}
	public void setCustClassName(String custClassName) {
		this.custClassName = custClassName;
	}
	public Integer getItemTierQty() {
		return itemTierQty;
	}
	public void setItemTierQty(Integer itemTierQty) {
		this.itemTierQty = itemTierQty;
	}
	public Date getItemTierPricePublishOn() {
		return itemTierPricePublishOn;
	}
	public void setItemTierPricePublishOn(Date itemTierPricePublishOn) {
		this.itemTierPricePublishOn = itemTierPricePublishOn;
	}
	public Date getItemTierPriceExpireOn() {
		return itemTierPriceExpireOn;
	}
	public void setItemTierPriceExpireOn(Date itemTierPriceExpireOn) {
		this.itemTierPriceExpireOn = itemTierPriceExpireOn;
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
	public Set<ItemTierPriceCurrency> getItemTierPriceCurrencies() {
		return itemTierPriceCurrencies;
	}
	public void setItemTierPriceCurrencies(
			Set<ItemTierPriceCurrency> itemTierPriceCurrencies) {
		this.itemTierPriceCurrencies = itemTierPriceCurrencies;
	}
}
