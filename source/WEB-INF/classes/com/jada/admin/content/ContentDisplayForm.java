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

package com.jada.admin.content;

import org.apache.struts.action.ActionForm;

public class ContentDisplayForm extends ActionForm {
	private static final long serialVersionUID = 5294853547515424737L;
	public String encodeFields[] = {"contentTitle"};
	String remove;
	String contentId;
	String contentTitle;
	String published;
	String contentPublishOn;
	String contentExpireOn;
	
	public String getRemove() {
		return remove;
	}
	public void setRemove(String remove) {
		this.remove = remove;
	}
	public String getContentExpireOn() {
		return contentExpireOn;
	}
	public void setContentExpireOn(String contentExpireOn) {
		this.contentExpireOn = contentExpireOn;
	}
	public String getContentPublishOn() {
		return contentPublishOn;
	}
	public void setContentPublishOn(String contentPublishOn) {
		this.contentPublishOn = contentPublishOn;
	}
	public String getContentTitle() {
		return contentTitle;
	}
	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}
	public String getPublished() {
		return published;
	}
	public void setPublished(String published) {
		this.published = published;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
}