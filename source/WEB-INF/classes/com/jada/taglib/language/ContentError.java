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

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.jada.content.ContentBean;
import com.jada.content.ContentLookupDispatchAction;
import com.jada.system.Languages;

public class ContentError extends TagSupport {
	private static final long serialVersionUID = 4432502424593540157L;
	Logger logger = Logger.getLogger(ContentMessage.class);
	String field = null;
	
	public int doEndTag() {
		try {
			pageContext.getOut().print(getError());
		}
		catch (Exception e) {
			logger.error("Problem in message tag", e);
		}
		return EVAL_PAGE;
	}
	
	public String getError() throws Exception {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		ContentBean contentBean = ContentLookupDispatchAction.getContentBean(request);
		String value = "";
		ActionMessages messages = (ActionMessages) request.getAttribute("org.apache.struts.action.ACTION_MESSAGE");
		if (messages == null) {
			return value;
		}
		Iterator<?> iterator = messages.get(field);
		while (iterator.hasNext()) {
			ActionMessage message = (ActionMessage) iterator.next();
			if (value.length() > 0) {
				value += " ";
			}
			String text = Languages.getLangTranValue(contentBean.getContentSessionKey().getLangId(), message.getKey());
			String result = processPattern(message, text);	
			value += result;
		}
		return value;
	}
	
	public String processPattern(ActionMessage message, String text) {
		Pattern p = Pattern.compile("\\{.*\\}");
		String result = text;
		while (true) {
			Matcher matcher = p.matcher(result);
			if (!matcher.find()) {
				break;
			}
			String token = text.substring(matcher.start(), matcher.end());
			String posValue = token.substring(1, token.length() - 1).trim();
			int pos = Integer.valueOf(posValue);
			if (message.getValues() == null) {
				logger.error("Parameters missing for " + text);
				break;
			}
			if (message.getValues().length < pos + 1) {
				logger.error("Parameters " + pos + " missing for " + text);
				break;
			}
			result = result.replaceFirst("\\{.*\\}", (String) message.getValues()[pos]);
		}
		return result;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
}