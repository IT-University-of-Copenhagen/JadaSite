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

package com.jada.admin.indexing;

import com.jada.admin.AdminMaintActionForm;
import com.jada.xml.info.IndexerInfo;

public class IndexingActionForm extends AdminMaintActionForm {
	private static final long serialVersionUID = -8742241138563552856L;
	boolean inProgress;
	String message;
	IndexerInfo indexerInfo;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public IndexerInfo getIndexerInfo() {
		return indexerInfo;
	}
	public void setIndexerInfo(IndexerInfo indexerInfo) {
		this.indexerInfo = indexerInfo;
	}
	public boolean isInProgress() {
		return inProgress;
	}
	public void setInProgress(boolean inProgress) {
		this.inProgress = inProgress;
	}
}
