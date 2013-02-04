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

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.jada.admin.AdminMaintActionForm;

public class PollMaintActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = 4096246160897908475L;
	String mode;
	String pollHeaderId;
	String pollTopic;
	String pollPublishOn;
	String pollExpireOn;
	String published;
	String newPollOption;
	PollDetailForm pollDetails[];

	public PollDetailForm getPollDetail(int index) {
		return pollDetails[index];
	}
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		String POLLDETAIL = "pollDetail.*pollDetailId";
		int count = 0;
		Enumeration<?> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			if (name.matches(POLLDETAIL)) {
				count++;
			}
		}
		pollDetails = new PollDetailForm[count];
		for (int i = 0; i < pollDetails.length; i++) {
			pollDetails[i] = new PollDetailForm();
		}
	}
	
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public PollDetailForm[] getPollDetails() {
		return pollDetails;
	}
	public void setPollDetails(PollDetailForm[] pollDetails) {
		this.pollDetails = pollDetails;
	}
	public String getPollExpireOn() {
		return pollExpireOn;
	}
	public void setPollExpireOn(String pollExpireOn) {
		this.pollExpireOn = pollExpireOn;
	}
	public String getPollHeaderId() {
		return pollHeaderId;
	}
	public void setPollHeaderId(String pollHeaderId) {
		this.pollHeaderId = pollHeaderId;
	}
	public String getPollPublishOn() {
		return pollPublishOn;
	}
	public void setPollPublishOn(String pollPublishOn) {
		this.pollPublishOn = pollPublishOn;
	}
	public String getPollTopic() {
		return pollTopic;
	}
	public void setPollTopic(String pollTopic) {
		this.pollTopic = pollTopic;
	}
	public String getPublished() {
		return published;
	}
	public void setPublished(String published) {
		this.published = published;
	}
	public String getNewPollOption() {
		return newPollOption;
	}
	public void setNewPollOption(String newPollOption) {
		this.newPollOption = newPollOption;
	}
}
