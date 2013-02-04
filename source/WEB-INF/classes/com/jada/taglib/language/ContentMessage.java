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

package com.jada.taglib.language;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import com.jada.content.ContentBean;
import com.jada.content.ContentLookupDispatchAction;
import com.jada.system.Languages;

public class ContentMessage extends TagSupport {
	private static final long serialVersionUID = 7885074437213764830L;
	Logger logger = Logger.getLogger(ContentMessage.class);
	String value = null;
	String key = null;
	
	public int doEndTag() {
		try {
			if (key != null) {
				pageContext.getOut().print(getValueByKey());
			}
			else {
				pageContext.getOut().print(getValueByField());
			}
		}
		catch (Exception e) {
			logger.error("Problem in message tag", e);
		}
		return EVAL_PAGE;
	}
	
	public String getValueByKey() throws Exception {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		ContentBean contentBean = ContentLookupDispatchAction.getContentBean(request);
		return Languages.getLangTranValue(contentBean.getContentSessionKey().getLangId(), key);
	}
	
	public String getValueByField() throws Exception {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		ContentBean contentBean = ContentLookupDispatchAction.getContentBean(request);
		return Languages.getLangTranValueByEnglishValue(contentBean.getContentSessionKey().getLangId(), value);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}