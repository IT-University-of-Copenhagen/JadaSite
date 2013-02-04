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

import java.util.Hashtable;
import java.util.Vector;

public class DataInfo {
	Hashtable<String, String> attributes = new Hashtable<String, String>();
	public Hashtable<String, String> getAttributes() {
		return attributes;
	}
	public void setAttributes(Hashtable<String, String> attributes) {
		this.attributes = attributes;
	}
	public String getAttribute(String key) {
		return (String) attributes.get(key);
	}
	public void setAttribute(String key, String value) {
		attributes.put(key, value);
	}
	public int getSize(Vector<?> vector) {
		if (vector == null) {
			return 0;
		}
		return vector.size();
	}
}
