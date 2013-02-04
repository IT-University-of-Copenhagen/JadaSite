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

package com.jada.admin.moderation;

import java.util.Enumeration;

import org.apache.struts.action.ActionMapping;

import com.jada.admin.AdminListingActionForm;

public class ModerationMaintActionForm extends AdminListingActionForm {
	private static final long serialVersionUID = -5639176842853440162L;
	String srRecCreateDatetimeStart;
	String srRecCreateDatetimeEnd;
	boolean srModerated;
	boolean srNotModerated;
	boolean srFlagged;
	boolean srNotFlagged;
	CommentDisplayForm[] comments;
	String contentId;
	String itemId;
	public void reset(ActionMapping mapping, javax.servlet.http.HttpServletRequest request) {
		String COMMENT = "comment.*commentId";
		int count = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(COMMENT)) {
				count++;
			}
		}
		comments = new CommentDisplayForm[count];
		for (int i = 0; i < comments.length; i++) {
			comments[i] = new CommentDisplayForm();
		}
		srModerated = false;
		srNotModerated = false;
		srFlagged = false;
		srNotFlagged = false;
	}
	public CommentDisplayForm[] getComments() {
		return comments;
	}
	public void setComments(CommentDisplayForm[] comments) {
		this.comments = comments;
	}
	public String getSrRecCreateDatetimeStart() {
		return srRecCreateDatetimeStart;
	}
	public void setSrRecCreateDatetimeStart(String srRecCreateDatetimeStart) {
		this.srRecCreateDatetimeStart = srRecCreateDatetimeStart;
	}
	public String getSrRecCreateDatetimeEnd() {
		return srRecCreateDatetimeEnd;
	}
	public void setSrRecCreateDatetimeEnd(String srRecCreateDatetimeEnd) {
		this.srRecCreateDatetimeEnd = srRecCreateDatetimeEnd;
	}
	public boolean isSrFlagged() {
		return srFlagged;
	}
	public void setSrFlagged(boolean srFlagged) {
		this.srFlagged = srFlagged;
	}
	public boolean isSrModerated() {
		return srModerated;
	}
	public void setSrModerated(boolean srModerated) {
		this.srModerated = srModerated;
	}
	public boolean isSrNotModerated() {
		return srNotModerated;
	}
	public void setSrNotModerated(boolean srNotModerated) {
		this.srNotModerated = srNotModerated;
	}
	public String getContentId() {
		return contentId;
	}
	public void setContentId(String contentId) {
		this.contentId = contentId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public boolean isSrNotFlagged() {
		return srNotFlagged;
	}
	public void setSrNotFlagged(boolean srNotFlagged) {
		this.srNotFlagged = srNotFlagged;
	}
}
