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

package com.jada.admin.poll;

import java.util.Vector;

import com.jada.admin.AdminListingActionForm;

public class PollListingActionForm extends AdminListingActionForm {
	private static final long serialVersionUID = 4301715972900445120L;
	String srPollTopic;
	String srPublished;
	String srPollPublishOnStart;
	String srPollPublishOnEnd;
	String srPollExpireOnStart;
	String srPollExpireOnEnd;
	String srUpdateBy;
	String srCreateBy;
	Vector<?> polls;
	String srSelectUsers[];
    String pollHeaderIds[];
	public Vector<?> getPolls() {
		return polls;
	}
	public void setPolls(Vector<?> polls) {
		this.polls = polls;
	}
	public String getSrCreateBy() {
		return srCreateBy;
	}
	public void setSrCreateBy(String srCreateBy) {
		this.srCreateBy = srCreateBy;
	}
	public String getSrPollExpireOnEnd() {
		return srPollExpireOnEnd;
	}
	public void setSrPollExpireOnEnd(String srPollExpireOnEnd) {
		this.srPollExpireOnEnd = srPollExpireOnEnd;
	}
	public String getSrPollExpireOnStart() {
		return srPollExpireOnStart;
	}
	public void setSrPollExpireOnStart(String srPollExpireOnStart) {
		this.srPollExpireOnStart = srPollExpireOnStart;
	}
	public String getSrPollPublishOnEnd() {
		return srPollPublishOnEnd;
	}
	public void setSrPollPublishOnEnd(String srPollPublishOnEnd) {
		this.srPollPublishOnEnd = srPollPublishOnEnd;
	}
	public String getSrPollPublishOnStart() {
		return srPollPublishOnStart;
	}
	public void setSrPollPublishOnStart(String srPollPublishOnStart) {
		this.srPollPublishOnStart = srPollPublishOnStart;
	}
	public String getSrPollTopic() {
		return srPollTopic;
	}
	public void setSrPollTopic(String srPollTopic) {
		this.srPollTopic = srPollTopic;
	}
	public String getSrPublished() {
		return srPublished;
	}
	public void setSrPublished(String srPublished) {
		this.srPublished = srPublished;
	}
	public String getSrUpdateBy() {
		return srUpdateBy;
	}
	public void setSrUpdateBy(String srUpdateBy) {
		this.srUpdateBy = srUpdateBy;
	}
	public String[] getSrSelectUsers() {
		return srSelectUsers;
	}
	public void setSrSelectUsers(String[] srSelectUsers) {
		this.srSelectUsers = srSelectUsers;
	}
	public String[] getPollHeaderIds() {
		return pollHeaderIds;
	}
	public void setPollHeaderIds(String[] pollHeaderIds) {
		this.pollHeaderIds = pollHeaderIds;
	}
}
