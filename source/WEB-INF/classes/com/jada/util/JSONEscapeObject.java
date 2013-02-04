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

package com.jada.util;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONEscapeObject extends JSONObject {
	public JSONEscapeObject() throws JSONException {
		super();
	}
	
	public JSONEscapeObject(String value) throws JSONException {
		super(value);
	}
	
	public JSONObject put(String key, Object value) throws JSONException {
		return super.put(key, value);
	}
	
	public void addFieldValue(Object key, Object value) throws JSONException {
    	JSONEscapeObject jsonMessage = (JSONEscapeObject) this.get("fields");
    	if (jsonMessage == null) {
    		jsonMessage = new JSONEscapeObject();
    		this.put("fields", jsonMessage);
    	}
    	this.put("key", key);
    	this.put("value", value);
	}
	
	public void addKeyValue(Object key, Object value) throws JSONException {
		Vector<JSONEscapeObject> jsonMessages = new Vector<JSONEscapeObject>();
    	try {
    		JSONArray jsonArray = (JSONArray) this.get("messages");
    		if (jsonArray != null) {
    			for (int i = 0; i < jsonArray.length(); i++) {
    				jsonMessages.add((JSONEscapeObject) jsonArray.get(i));
    			}
    		}
    	}
    	catch (Exception e) {
    		
    	}
    	if (jsonMessages == null) {
    		jsonMessages = new Vector<JSONEscapeObject>();
    	}
    	JSONEscapeObject jsonMessage = new JSONEscapeObject();
    	jsonMessage.put("key", key);
    	jsonMessage.put("value", value);
    	jsonMessages.add(jsonMessage);
		this.put("messages", jsonMessages);
	}
    
    public String toHtmlString() {
    	String output = toString();
    	output = output.replaceAll("'", "\\\\'");
    	return output;
    }
}
