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

package com.jada.admin;

import org.apache.struts.action.ActionForm;
import org.apache.struts.util.LabelValueBean;

public class AdminMaintActionForm extends ActionForm {
	private static final long serialVersionUID = 3409793402377579323L;
	String fromLocale;
	String toLocale;
	boolean translationEnable;
	boolean siteProfileClassDefault;
	Long siteProfileClassId;
	Long siteProfileClassDefaultId;
	LabelValueBean siteProfileClassBeans[];
	boolean siteCurrencyClassDefault;
	Long siteCurrencyClassId;
	Long siteCurrencyClassDefaultId;
	LabelValueBean siteCurrencyClassBeans[];
	boolean isStream;
	String streamData;
	public boolean isTranslationEnable() {
		return translationEnable;
	}
	public void setTranslationEnable(boolean translationEnable) {
		this.translationEnable = translationEnable;
	}
	public String getFromLocale() {
		return fromLocale;
	}
	public void setFromLocale(String fromLocale) {
		this.fromLocale = fromLocale;
	}
	public String getToLocale() {
		return toLocale;
	}
	public void setToLocale(String toLocale) {
		this.toLocale = toLocale;
	}
	public LabelValueBean[] getSiteProfileClassBeans() {
		return siteProfileClassBeans;
	}
	public void setSiteProfileClassBeans(LabelValueBean[] siteProfileClassBeans) {
		this.siteProfileClassBeans = siteProfileClassBeans;
	}
	public boolean isSiteProfileClassDefault() {
		return siteProfileClassDefault;
	}
	public void setSiteProfileClassDefault(boolean siteProfileClassDefault) {
		this.siteProfileClassDefault = siteProfileClassDefault;
	}
	public boolean isSiteCurrencyClassDefault() {
		return siteCurrencyClassDefault;
	}
	public void setSiteCurrencyClassDefault(boolean siteCurrencyClassDefault) {
		this.siteCurrencyClassDefault = siteCurrencyClassDefault;
	}
	public LabelValueBean[] getSiteCurrencyClassBeans() {
		return siteCurrencyClassBeans;
	}
	public void setSiteCurrencyClassBeans(LabelValueBean[] siteCurrencyClassBeans) {
		this.siteCurrencyClassBeans = siteCurrencyClassBeans;
	}
	public boolean isStream() {
		return isStream;
	}
	public void setStream(boolean isStream) {
		this.isStream = isStream;
	}
	public String getStreamData() {
		return streamData;
	}
	public void setStreamData(String streamData) {
		this.streamData = streamData;
	}
	public Long getSiteProfileClassDefaultId() {
		return siteProfileClassDefaultId;
	}
	public void setSiteProfileClassDefaultId(Long siteProfileClassDefaultId) {
		this.siteProfileClassDefaultId = siteProfileClassDefaultId;
	}
	public Long getSiteCurrencyClassDefaultId() {
		return siteCurrencyClassDefaultId;
	}
	public void setSiteCurrencyClassDefaultId(Long siteCurrencyClassDefaultId) {
		this.siteCurrencyClassDefaultId = siteCurrencyClassDefaultId;
	}
	public Long getSiteProfileClassId() {
		return siteProfileClassId;
	}
	public void setSiteProfileClassId(Long siteProfileClassId) {
		this.siteProfileClassId = siteProfileClassId;
	}
	public Long getSiteCurrencyClassId() {
		return siteCurrencyClassId;
	}
	public void setSiteCurrencyClassId(Long siteCurrencyClassId) {
		this.siteCurrencyClassId = siteCurrencyClassId;
	}
}
