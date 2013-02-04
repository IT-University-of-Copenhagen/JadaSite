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

package com.jada.admin.customAttribute;

import java.util.Vector;

import com.jada.admin.AdminMaintActionForm;

public class CustomAttributeGroupListingActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = 1L;
	String mode;
	String srCustomAttribGroupName;
	String customAttribGroupIds[];
	Vector<?> customAttributeGroups;
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String[] getCustomAttribGroupIds() {
		return customAttribGroupIds;
	}
	public void setCustomAttribGroupIds(String[] customAttribGroupIds) {
		this.customAttribGroupIds = customAttribGroupIds;
	}
	public String getSrCustomAttribGroupName() {
		return srCustomAttribGroupName;
	}
	public void setSrCustomAttribGroupName(String srCustomAttribGroupName) {
		this.srCustomAttribGroupName = srCustomAttribGroupName;
	}
	public Vector<?> getCustomAttributeGroups() {
		return customAttributeGroups;
	}
	public void setCustomAttributeGroups(Vector<?> customAttributeGroups) {
		this.customAttributeGroups = customAttributeGroups;
	}
}
