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

package com.jada.admin.language;

public class LangTranDetailForm {
	String langTranKey;
	String langTranEnglish;
	String langTranValue;
	String langTranValueError;
	public String getLangTranValueError() {
		return langTranValueError;
	}
	public void setLangTranValueError(String langTranValueError) {
		this.langTranValueError = langTranValueError;
	}
	public String getLangTranEnglish() {
		return langTranEnglish;
	}
	public void setLangTranEnglish(String langTranEnglish) {
		this.langTranEnglish = langTranEnglish;
	}
	public String getLangTranKey() {
		return langTranKey;
	}
	public void setLangTranKey(String langTranKey) {
		this.langTranKey = langTranKey;
	}
	public String getLangTranValue() {
		return langTranValue;
	}
	public void setLangTranValue(String langTranValue) {
		this.langTranValue = langTranValue;
	}
}
