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

public class CommentInfo extends DataInfo {
	String commentId;
	String commentTitle;
	String comment;
	int agreeCount;
	int disagreeCount;
	String commentRating;
	String commentRatingPercentageNumber;
	String commentRatingPercentage;
	String moderation;
	String commentApproved;
	String commentUpdateName;
	String recUpdateBy;
	String recUpdateDatetime;
	String recCreateBy;
	String recCreateDatetime;
	public String getCommentUpdateName() {
		return commentUpdateName;
	}
	public void setCommentUpdateName(String commentUpdateName) {
		this.commentUpdateName = commentUpdateName;
	}
	public String getComment() {
		return comment;
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
	public String getCommentTitle() {
		return commentTitle;
	}
	public void setCommentTitle(String commentTitle) {
		this.commentTitle = commentTitle;
	}
	public int getAgreeCount() {
		return agreeCount;
	}
	public void setAgreeCount(int agreeCount) {
		this.agreeCount = agreeCount;
	}
	public int getDisagreeCount() {
		return disagreeCount;
	}
	public void setDisagreeCount(int disagreeCount) {
		this.disagreeCount = disagreeCount;
	}
	public String getModeration() {
		return moderation;
	}
	public void setModeration(String moderation) {
		this.moderation = moderation;
	}
	public String getCommentApproved() {
		return commentApproved;
	}
	public void setCommentApproved(String commentApproved) {
		this.commentApproved = commentApproved;
	}
	public String getCommentRating() {
		return commentRating;
	}
	public void setCommentRating(String commentRating) {
		this.commentRating = commentRating;
	}
	public String getCommentRatingPercentage() {
		return commentRatingPercentage;
	}
	public void setCommentRatingPercentage(String commentRatingPercentage) {
		this.commentRatingPercentage = commentRatingPercentage;
	}
	public String getCommentRatingPercentageNumber() {
		return commentRatingPercentageNumber;
	}
	public void setCommentRatingPercentageNumber(
			String commentRatingPercentageNumber) {
		this.commentRatingPercentageNumber = commentRatingPercentageNumber;
	}
}
