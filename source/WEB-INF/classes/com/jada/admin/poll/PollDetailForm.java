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

import org.apache.struts.action.ActionForm;

public class PollDetailForm extends ActionForm {
	private static final long serialVersionUID = -7704028001264881695L;
	boolean remove;
	String seqNum;
	String seqNumError;
	String pollDetailId;
	String pollOption;
	String pollOptionError;
	String pollVoteCount;
	String pollPercentage;
	public String getPollPercentage() {
		return pollPercentage;
	}
	public void setPollPercentage(String pollPercentage) {
		this.pollPercentage = pollPercentage;
	}
	public String getPollDetailId() {
		return pollDetailId;
	}
	public void setPollDetailId(String pollDetailId) {
		this.pollDetailId = pollDetailId;
	}
	public String getPollOption() {
		return pollOption;
	}
	public void setPollOption(String pollOption) {
		this.pollOption = pollOption;
	}
	public String getPollVoteCount() {
		return pollVoteCount;
	}
	public void setPollVoteCount(String pollVoteCount) {
		this.pollVoteCount = pollVoteCount;
	}
	public boolean isRemove() {
		return remove;
	}
	public void setRemove(boolean remove) {
		this.remove = remove;
	}
	public String getSeqNum() {
		return seqNum;
	}
	public void setSeqNum(String seqNum) {
		this.seqNum = seqNum;
	}
	public String getPollOptionError() {
		return pollOptionError;
	}
	public void setPollOptionError(String pollOptionError) {
		this.pollOptionError = pollOptionError;
	}
	public String getSeqNumError() {
		return seqNumError;
	}
	public void setSeqNumError(String seqNumError) {
		this.seqNumError = seqNumError;
	}
}
