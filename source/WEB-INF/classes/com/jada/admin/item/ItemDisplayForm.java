/*
 * Copyright 2007-2010 JadaSite.

 * This file is part of JadaSite.
 
 * JadaSite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * JadaSite is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with JadaSite.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.jada.admin.item;

import org.apache.struts.action.ActionForm;

public class ItemDisplayForm extends ActionForm {
	private static final long serialVersionUID = 3824060666671778429L;
	public String encodeFields[] = {"itemShortDesc"};
	String remove;
	String itemId;
	String itemNum;
	String itemSkuCd;
	String itemShortDesc;
	String published;
	String itemPublishOn;
	String itemExpireOn;
	
	public String getItemExpireOn() {
		return itemExpireOn;
	}
	public void setItemExpireOn(String itemExpireOn) {
		this.itemExpireOn = itemExpireOn;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemPublishOn() {
		return itemPublishOn;
	}
	public void setItemPublishOn(String itemPublishOn) {
		this.itemPublishOn = itemPublishOn;
	}
	public String getItemShortDesc() {
		return itemShortDesc;
	}
	public void setItemShortDesc(String itemShortDesc) {
		this.itemShortDesc = itemShortDesc;
	}
	public String getPublished() {
		return published;
	}
	public void setPublished(String published) {
		this.published = published;
	}
	public String getRemove() {
		return remove;
	}
	public void setRemove(String remove) {
		this.remove = remove;
	}
	public String getItemNum() {
		return itemNum;
	}
	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
	}
	public String getItemSkuCd() {
		return itemSkuCd;
	}
	public void setItemSkuCd(String itemSkuCd) {
		this.itemSkuCd = itemSkuCd;
	}
}
