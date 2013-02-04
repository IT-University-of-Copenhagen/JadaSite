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

package com.jada.content.data;

import java.util.Vector;

public class ContentInfo extends DataInfo {
	String infoType = "content";
	String contentNaturalKey;
	String contentId;
	String contentTitle;
	String contentShortDesc;
	String contentDesc;
	String pageTitle;
	String metaKeywords;
	String metaDescription;
	String contentUrl;
	String contentCommentUrl;
	String contentCommentUpdateUrl;
	String contentDefaultImageUrl;
	String contentUpdateName;
	String recUpdateBy;
	String recUpdateDatetime;
	String recCreateBy;
	String recCreateDatetime;
	int commentCount;
	Vector<?> relatedContents;
	Vector<?> contentImageUrls;
	Vector<?> comments;
	Object custom;
	public Vector<?> getComments() {
		return comments;
	}
	public void setComments(Vector<?> comments) {
		this.comments = comments;
	}
	public String getContentDefaultImageUrl() {
		return contentDefaultImageUrl;
	}
	public void setContentDefaultImageUrl(String contentDefaultImageUrl) {
		this.contentDefaultImageUrl = contentDefaultImageUrl;
	}
	public String getContentDesc() {
		return contentDesc;
	}
	public void setContentDesc(String contentDesc) {
		this.contentDesc = contentDesc;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public Vector<?> getContentImageUrls() {
		return contentImageUrls;
	}
	public void setContentImageUrls(Vector<?> contentImageUrls) {
		this.contentImageUrls = contentImageUrls;
	}
	public String getContentShortDesc() {
		return contentShortDesc;
	}
	public void setContentShortDesc(String contentShortDesc) {
		this.contentShortDesc = contentShortDesc;
	}
	public String getContentTitle() {
		return contentTitle;
	}
	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}
	public String getContentUrl() {
		return contentUrl;
	}
	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
	public String getInfoType() {
		return infoType;
	}
	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public String getContentNaturalKey() {
		return contentNaturalKey;
	}
	public void setContentNaturalKey(String contentNaturalKey) {
		this.contentNaturalKey = contentNaturalKey;
	}
	public String getRecCreateBy() {
		return recCreateBy;
	}
	public void setRecCreateBy(String recCreateBy) {
		this.recCreateBy = recCreateBy;
	}
	public String getRecCreateDatetime() {
		return recCreateDatetime;
	}
	public void setRecCreateDatetime(String recCreateDatetime) {
		this.recCreateDatetime = recCreateDatetime;
	}
	public String getRecUpdateBy() {
		return recUpdateBy;
	}
	public void setRecUpdateBy(String recUpdateBy) {
		this.recUpdateBy = recUpdateBy;
	}
	public String getRecUpdateDatetime() {
		return recUpdateDatetime;
	}
	public void setRecUpdateDatetime(String recUpdateDatetime) {
		this.recUpdateDatetime = recUpdateDatetime;
	}
	public String getContentUpdateName() {
		return contentUpdateName;
	}
	public void setContentUpdateName(String contentUpdateName) {
		this.contentUpdateName = contentUpdateName;
	}
	public String getContentCommentUrl() {
		return contentCommentUrl;
	}
	public void setContentCommentUrl(String contentCommentUrl) {
		this.contentCommentUrl = contentCommentUrl;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public Vector<?> getRelatedContents() {
		return relatedContents;
	}
	public void setRelatedContents(Vector<?> relatedContents) {
		this.relatedContents = relatedContents;
	}
	public String getContentCommentUpdateUrl() {
		return contentCommentUpdateUrl;
	}
	public void setContentCommentUpdateUrl(String contentCommentUpdateUrl) {
		this.contentCommentUpdateUrl = contentCommentUpdateUrl;
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
	public Object getCustom() {
		return custom;
	}
	public void setCustom(Object custom) {
		this.custom = custom;
	}
}
