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

import java.util.Enumeration;
import org.apache.struts.action.ActionMapping;

import com.jada.admin.AdminListingActionForm;
import com.jada.ui.dropdown.DropDownMenu;

public class ContentListingActionForm extends AdminListingActionForm {
	private static final long serialVersionUID = 2862571916712811454L;
	String srContentTitle;
	String srPublished;
	String srContentPublishOnStart;
	String srContentPublishOnEnd;
	String srContentExpireOnStart;
	String srContentExpireOnEnd;
	String srUpdateBy;
	String srCreateBy;
	String srCatIds[];
	String srSelectUsers[];
	String jsonCategoryTree;
	DropDownMenu srCategoryTree;
    String srSelectedCategories[];
    
    ContentDisplayForm contents[];
    String contentIds[];
	
    public ContentDisplayForm getContent(int index) {
    	return contents[index];
    }
	public void reset(ActionMapping mapping, javax.servlet.http.HttpServletRequest request) {
		srSelectedCategories = null;
		String CONTENTDETAIL = "content.*contentId";
		int count = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(CONTENTDETAIL)) {
				count++;
			}
		}
		contents = new ContentDisplayForm[count];
		for (int i = 0; i < contents.length; i++) {
			contents[i] = new ContentDisplayForm();
		}
	}
	public String[] getSrSelectUsers() {
		return srSelectUsers;
	}
	public void setSrSelectUsers(String[] srSelectUsers) {
		this.srSelectUsers = srSelectUsers;
	}
	public String getSrContentExpireOnEnd() {
		return srContentExpireOnEnd;
	}
	public void setSrContentExpireOnEnd(String srContentExpireOnEnd) {
		this.srContentExpireOnEnd = srContentExpireOnEnd;
	}
	public String getSrContentExpireOnStart() {
		return srContentExpireOnStart;
	}
	public void setSrContentExpireOnStart(String srContentExpireOnStart) {
		this.srContentExpireOnStart = srContentExpireOnStart;
	}
	public String getSrContentPublishOnEnd() {
		return srContentPublishOnEnd;
	}
	public void setSrContentPublishOnEnd(String srContentPublishOnEnd) {
		this.srContentPublishOnEnd = srContentPublishOnEnd;
	}
	public String getSrContentPublishOnStart() {
		return srContentPublishOnStart;
	}
	public void setSrContentPublishOnStart(String srContentPublishOnStart) {
		this.srContentPublishOnStart = srContentPublishOnStart;
	}
	public String getSrContentTitle() {
		return srContentTitle;
	}
	public void setSrContentTitle(String srContentTitle) {
		this.srContentTitle = srContentTitle;
	}
	public String getSrCreateBy() {
		return srCreateBy;
	}
	public void setSrCreateBy(String srCreateBy) {
		this.srCreateBy = srCreateBy;
	}
	public String[] getSrCatIds() {
		return srCatIds;
	}
	public void setSrCatIds(String[] srCatIds) {
		this.srCatIds = srCatIds;
	}
	public String getSrUpdateBy() {
		return srUpdateBy;
	}
	public void setSrUpdateBy(String srUpdateBy) {
		this.srUpdateBy = srUpdateBy;
	}
	public ContentDisplayForm[] getContents() {
		return contents;
	}
	public void setContents(ContentDisplayForm[] contents) {
		this.contents = contents;
	}
	public String[] getContentIds() {
		return contentIds;
	}
	public void setContentIds(String[] contentIds) {
		this.contentIds = contentIds;
	}
	public String getSrPublished() {
		return srPublished;
	}
	public void setSrPublished(String srPublished) {
		this.srPublished = srPublished;
	}
	public String getJsonCategoryTree() {
		return jsonCategoryTree;
	}
	public void setJsonCategoryTree(String jsonCategoryTree) {
		this.jsonCategoryTree = jsonCategoryTree;
	}
	public DropDownMenu getSrCategoryTree() {
		return srCategoryTree;
	}
	public void setSrCategoryTree(DropDownMenu srCategoryTree) {
		this.srCategoryTree = srCategoryTree;
	}
	public String[] getSrSelectedCategories() {
		return srSelectedCategories;
	}
	public void setSrSelectedCategories(String[] srSelectedCategories) {
		this.srSelectedCategories = srSelectedCategories;
	}
}
