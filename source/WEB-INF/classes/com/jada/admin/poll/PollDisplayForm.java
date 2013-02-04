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

public class PollDisplayForm {
	String pollHeaderId;
	String pollTopic;
	String published;
	String pollPublishOn;
	String pollExpireOn;
	public String getPollHeaderId() {
		return pollHeaderId;
	}
	public void setPollHeaderId(String pollHeaderId) {
		this.pollHeaderId = pollHeaderId;
	}
	public String getPollExpireOn() {
		return pollExpireOn;
	}
	public void setPollExpireOn(String pollExpireOn) {
		this.pollExpireOn = pollExpireOn;
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
}
