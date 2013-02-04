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

public class CommentDisplayForm {
	String select;
	String commentId;
	String commentSource;
	String commentSourceTitle;
	String commentSourceId;
	String commentTitle;
	String comment;
	String agreeCount;
	String disagreeCount;
	String recUpdatedDatetime;
	String custEmail;
	String custPublicName;
	String commentModeration;
	String commentApproved;
	public String getCommentApproved() {
		return commentApproved;
	}
	public void setCommentApproved(String commentApproved) {
		this.commentApproved = commentApproved;
	}
	public String getCommentModeration() {
		return commentModeration;
	}
	public void setCommentModeration(String commentModeration) {
		this.commentModeration = commentModeration;
	}
	public String getComment() {
		return comment;
	}
	public String getCommentSourceId() {
		return commentSourceId;
	}
	public void setCommentSourceId(String commentSourceId) {
		this.commentSourceId = commentSourceId;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	public String getCommentSource() {
		return commentSource;
	}
	public void setCommentSource(String commentSource) {
		this.commentSource = commentSource;
	}
	public String getCommentSourceTitle() {
		return commentSourceTitle;
	}
	public void setCommentSourceTitle(String commentSourceTitle) {
		this.commentSourceTitle = commentSourceTitle;
	}
	public String getCommentTitle() {
		return commentTitle;
	}
	public void setCommentTitle(String commentTitle) {
		this.commentTitle = commentTitle;
	}
	public String getCustPublicName() {
		return custPublicName;
	}
	public void setCustPublicName(String custPublicName) {
		this.custPublicName = custPublicName;
	}
	public String getCustEmail() {
		return custEmail;
	}
	public void setCustEmail(String custEmail) {
		this.custEmail = custEmail;
	}
	public String getRecUpdatedDatetime() {
		return recUpdatedDatetime;
	}
	public void setRecUpdatedDatetime(String recUpdatedDatetime) {
		this.recUpdatedDatetime = recUpdatedDatetime;
	}
	public String getSelect() {
		return select;
	}
	public void setSelect(String select) {
		this.select = select;
	}
	public String getAgreeCount() {
		return agreeCount;
	}
	public void setAgreeCount(String agreeCount) {
		this.agreeCount = agreeCount;
	}
	public String getDisagreeCount() {
		return disagreeCount;
	}
	public void setDisagreeCount(String disagreeCount) {
		this.disagreeCount = disagreeCount;
	}
}
