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

package com.jada.content;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import com.jada.jpa.entity.Language;
import com.jada.jpa.entity.SiteCurrency;
import com.jada.jpa.entity.SiteCurrencyClass;
import com.jada.jpa.entity.SiteProfile;

public class Formatter {
	NumberFormat currencyFormatter = null;
	NumberFormat numberFormatter = null;
	NumberFormat decimalFormatter = null;
	NumberFormat rawDecimalFormatter = null;
	DateFormat dateFormatter = null;
	DateFormat datetimeFormatter = null;
	DateFormat fullDateFormatter = null;
	DateFormat fullDatetimeFormatter = null;

	public Formatter(SiteProfile siteProfile, SiteCurrency siteCurrency) {
    	Language language = siteProfile.getSiteProfileClass().getLanguage();
    	rawDecimalFormatter = NumberFormat.getInstance(new Locale("en", "US"));
    	rawDecimalFormatter.setMinimumFractionDigits(2);
    	Locale locale = new Locale(language.getLangLocaleLanguage(), language.getLangLocaleCountry());
    	numberFormatter = NumberFormat.getInstance(locale);
    	numberFormatter.setMinimumFractionDigits(0);
    	decimalFormatter = NumberFormat.getInstance(locale);
    	decimalFormatter.setMinimumFractionDigits(2);
    	dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
    	datetimeFormatter = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, locale);
		fullDateFormatter = DateFormat.getDateInstance(DateFormat.LONG, locale);
		fullDatetimeFormatter = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		SiteCurrencyClass siteCurrencyClass = siteCurrency.getSiteCurrencyClass();
		Locale currencyLocale = new Locale(siteCurrencyClass.getCurrencyLocaleLanguage(), siteCurrencyClass.getCurrencyLocaleCountry());
    	currencyFormatter = NumberFormat.getCurrencyInstance(currencyLocale);
    	currencyFormatter.setMinimumFractionDigits(2);

	}
	
	public String formatNumber(int input) {
		return numberFormatter.format(input);
	}
	
	public String formatNumber(long input) {
		return numberFormatter.format(input);
	}
	
	public String formatRawDecimal(float input) {
		return rawDecimalFormatter.format(input);
	}
	
	public String formatDecimal(float input) {
		return decimalFormatter.format(input);
	}

	public String formatCurrency(float input) {
		return currencyFormatter.format(input);
	}
	
	public String formatDate(Date date) {
		return dateFormatter.format(date);
	}
	
	public String formatDatetime(Date date) {
		return datetimeFormatter.format(date);
	}

	public String formatFullDate(Date date) {
		return fullDateFormatter.format(date);
	}
	
	public String formatFullDatetime(Date date) {
		return fullDatetimeFormatter.format(date);
	}
}
