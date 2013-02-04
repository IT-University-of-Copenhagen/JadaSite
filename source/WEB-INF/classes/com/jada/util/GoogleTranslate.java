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

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

import com.jada.util.JSONEscapeObject;

public class GoogleTranslate {
	private static final String URL = "http://ajax.googleapis.com/ajax/services/language/translate?v=1.0&langpair=";
	private static final int TIMEOUT = 15000;
	private String from;
	private String to;
	private String delimitors = ",.;:?";
	private int limit = 800;
	
	public GoogleTranslate(String from, String to) {
		this.from = from;
		this.to = to;
	}
	
	public String translate(String input) throws Exception {
		String inputText = input;
		String translation = "";
		while (true) {
			String token = inputText;
			if (inputText.length() > limit) {
				token = inputText.substring(0, limit - 1);
				boolean found = false;
				for (int i = token.length() - 1; i >= 0; i--) {
					String s = token.substring(i, i + 1);
					if (delimitors.contains(s)) {
						found = true;
						token = token.substring(0, i + 1);
						break;
					}
				}
				if (!found) {
					return input;
				}
			}
			translation += send(token);
			if (inputText.length() == token.length()) {
				break;
			}
			inputText = inputText.substring(token.length());
		}
		return translation;
	}
	
	public String send(String input) throws Exception {
		if (input.length() == 0) {
			return "";
		}
		
		String url = URL;
		url += from;
		url += "%7C";
		url += to;
		input = URLEncoder.encode(input, "UTF-8");
		url += "&q=" + input;
		
		URL u = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) u.openConnection();
		connection.setConnectTimeout(TIMEOUT);
		connection.setReadTimeout(TIMEOUT);
        connection.setRequestProperty("Content-Type", "text/xml");
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.connect();
		
		String translation = "";
		InputStreamReader reader = new InputStreamReader(connection.getInputStream(), "UTF-8");
		StringBuffer sb = new StringBuffer();
		char c[] = new char[1024];
		while (true) {
			int length = reader.read(c);
			if (length == -1) {
				break;
			}
			sb.append(c, 0, length);
		}
		JSONEscapeObject json = new JSONEscapeObject(sb.toString());
		translation = ((JSONObject) json.get("responseData")).getString("translatedText");
		return translation;
	}
	
	public static void main(String[] args) {
	}
}
