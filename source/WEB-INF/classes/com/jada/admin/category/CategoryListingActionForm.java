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

package com.jada.admin.category;

import org.apache.struts.action.ActionForm;

import com.jada.ui.dropdown.DropDownMenu;

public class CategoryListingActionForm extends ActionForm {
	private static final long serialVersionUID = 3712575811305864847L;
	DropDownMenu categoryTree;
	String catId;
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		this.catId = catId;
	}
	public DropDownMenu getCategoryTree() {
		return categoryTree;
	}
	public void setCategoryTree(DropDownMenu categoryTree) {
		this.categoryTree = categoryTree;
	}
}
