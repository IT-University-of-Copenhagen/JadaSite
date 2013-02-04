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

import com.jada.admin.AdminMaintActionForm;

public class CustomAttributeGroupMaintActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = -8449212130899158859L;
	String mode;
	String customAttribGroupId;
	String customAttribGroupName;
	String customAttribId;
	String seqNums[];
	String customAttribDetailIds[];
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getCustomAttribGroupId() {
		return customAttribGroupId;
	}
	public void setCustomAttribGroupId(String customAttribGroupId) {
		this.customAttribGroupId = customAttribGroupId;
	}
	public String getCustomAttribGroupName() {
		return customAttribGroupName;
	}
	public void setCustomAttribGroupName(String customAttribGroupName) {
		this.customAttribGroupName = customAttribGroupName;
	}
	public String getCustomAttribId() {
		return customAttribId;
	}
	public void setCustomAttribId(String customAttribId) {
		this.customAttribId = customAttribId;
	}
	public String[] getSeqNums() {
		return seqNums;
	}
	public void setSeqNums(String[] seqNums) {
		this.seqNums = seqNums;
	}
	public String[] getCustomAttribDetailIds() {
		return customAttribDetailIds;
	}
	public void setCustomAttribDetailIds(String[] customAttribDetailIds) {
		this.customAttribDetailIds = customAttribDetailIds;
	}
}
