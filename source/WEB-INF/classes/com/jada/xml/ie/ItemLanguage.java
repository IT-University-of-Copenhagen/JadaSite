package com.jada.xml.ie;

import java.util.Date;
import java.util.Vector;

public class ItemLanguage {
	private String itemShortDesc;
	private String itemDesc;
	private String pageTitle;
	private String metaKeywords;
	private String metaDescription;
	private String itemImageOverride;
	private String recUpdateBy;
	private Date recUpdateDatetime;
	private String recCreateBy;
	private Date recCreateDatetime;
	private Vector<ItemImage> images = new Vector<ItemImage>(0);
	private Long siteProfileClassId;
	private String siteProfileClassName;
	public String getItemShortDesc() {
		return itemShortDesc;
	}
	public void setItemShortDesc(String itemShortDesc) {
		this.itemShortDesc = itemShortDesc;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
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
	public Vector<ItemImage> getImages() {
		return images;
	}
	public void setImages(Vector<ItemImage> images) {
		this.images = images;
	}
	public Long getSiteProfileClassId() {
		return siteProfileClassId;
	}
	public void setSiteProfileClassId(Long siteProfileClassId) {
		this.siteProfileClassId = siteProfileClassId;
	}
	public String getSiteProfileClassName() {
		return siteProfileClassName;
	}
	public void setSiteProfileClassName(String siteProfileClassName) {
		this.siteProfileClassName = siteProfileClassName;
	}
	public String getItemImageOverride() {
		return itemImageOverride;
	}
	public void setItemImageOverride(String itemImageOverride) {
		this.itemImageOverride = itemImageOverride;
	}
	public String getMetaKeywords() {
		return metaKeywords;
	}
	public void setMetaKeywords(String metaKeywords) {
		this.metaKeywords = metaKeywords;
	}
	public String getMetaDescription() {
		return metaDescription;
	}
	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}
}
