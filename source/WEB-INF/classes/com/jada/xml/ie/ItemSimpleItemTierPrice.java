package com.jada.xml.ie;

import java.util.Date;

public class ItemSimpleItemTierPrice {
	private Long custClassId;
	private String custClassName;
	private Integer itemTierQty;
	private Float itemPrice;
	private Date itemTierPricePublishOn;
	private Date itemTierPriceExpireOn;
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
	public Float getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(Float itemPrice) {
		this.itemPrice = itemPrice;
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
}
