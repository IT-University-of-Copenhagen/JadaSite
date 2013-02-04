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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Format {	
	public static String SITE_ENGINE_URL = "/content/frontend/contentAction.do";
	
	static SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");
	static SimpleDateFormat sortDateFormat = new SimpleDateFormat("yyyyMMdd");
	static SimpleDateFormat fullDateFormat = new SimpleDateFormat("MM/dd/yyyy");
	static SimpleDateFormat fullDatetimeformat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
	static public Date HIGHDATE;
	static public Date LOWDATE;
	static {
		dateformat.setLenient(false);
		sortDateFormat.setLenient(false);
		fullDatetimeformat.setLenient(false);
		try {
			LOWDATE = dateformat.parse("01/01/2000");
			HIGHDATE = dateformat.parse("12/31/2999");
		}
		catch (Exception e) {};
	}
	
	static DecimalFormat intformat = new DecimalFormat("###,###,##0");
	static DecimalFormat doubleformat = new DecimalFormat("###,###,##0.00");
	static DecimalFormat doubleformat4 = new DecimalFormat("###,###,##0.0000");
	static DecimalFormat simpleIntformat = new DecimalFormat("########0");
	static DecimalFormat simpleDoubleformat = new DecimalFormat("########0.00", new DecimalFormatSymbols(Locale.US));
	static DecimalFormat simpleDoubleformat4= new DecimalFormat("########0.0000", new DecimalFormatSymbols(Locale.US));

	static public boolean isNullOrEmpty(String input) {
		if (input == null || input.trim().length() == 0) {
			return true;
		}
		return false;
	}
	
	static synchronized public boolean isDate(String input) {
		try {
			dateformat.parse(input);
		}
		catch (ParseException exception) {
			return false;
		}
		return true;
	}
	
	static synchronized public Date getDate(String input) throws ParseException {
		return dateformat.parse(input);
	}
	
	static synchronized public String getDate(Date date) {
		if (date == null) {
			return null;
		}
		return dateformat.format(date);
	}
	
	static synchronized public String getSortDate(Date date) {
		return sortDateFormat.format(date);
	}
	
	static synchronized public String getFullDate(Date date) {
		return fullDateFormat.format(date);
	}
	
	static synchronized public String getFullDatetime(Date date) {
		return fullDatetimeformat.format(date);
	}
	
	static synchronized public int getInt(String input) throws RuntimeException {
		try {
			return simpleIntformat.parse(input).intValue();
		}
		catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	static public Integer getIntObj(String input) {
		if (isNullOrEmpty(input)) {
			return null;
		}
		return Integer.valueOf(input);
	}
	
	static public char getChar(String input) {
		if (input.length() == 0) {
			return ' ';
		}
		else {
			return input.charAt(0);
		}
	}
	
	static synchronized public String getInt(int input) {
		return intformat.format(input);
	}
	
	static synchronized public String getSimpleInt(int input) {
		return simpleIntformat.format(input);
	}
	
	static public String getIntObj(Integer input) {
		if (input == null) {
			return "";
		}
		return getInt(input.intValue());
	}

	static public String getSimpleIntObj(Integer input) {
		if (input == null) {
			return "";
		}
		return getSimpleInt(input.intValue());
	}
	
	static synchronized public boolean isInt(String input) {
		try {
			simpleIntformat.parse(input);
		}
		catch (ParseException e) {
			return false;
		}
		return true;
	}
	
	static synchronized public boolean isLong(String input) {
		try {
			simpleIntformat.parse(input);
		}
		catch (ParseException e) {
			return false;
		}
		return true;
	}

	static public Long getLong(String input) {
		return new Long(input);
	}
	
	static public String getLong(Long input) {
		return input.toString();
	}

	static public String getFloat(float input) {
		return getDouble(input);
	}

	static public String getFloat4(float input) {
		return getDouble4(input);
	}

	static public String getSimpleFloat(float input) {
		return getSimpleDouble(input);
	}

	static public String getSimpleFloat4(float input) {
		return getSimpleDouble4(input);
	}

	static public float getFloat(String input) {
		return (float) getDouble(input);
	}
	
	static public Float getFloatObj(String input) {
		if (isNullOrEmpty(input)) {
			return null;
		}
		return new Float((float) getDouble(input));
	}

	static public String getFloatObj(Float input) {
		if (input == null) {
			return "";
		}
		return getFloat(input.floatValue());
	}

	static public String getFloatObj4(Float input) {
		if (input == null) {
			return "";
		}
		return getFloat4(input.floatValue());
	}
	
	static public boolean isFloat(String input) {
		return isDouble(input);
	}
	
	static synchronized public String getDouble(double input) {
		return doubleformat.format(input);
	}
	
	static synchronized public String getDouble4(double input) {
		return doubleformat4.format(input);
	}

	static synchronized public String getSimpleDouble(double input) {
		return simpleDoubleformat.format(input);
	}

	static synchronized public String getSimpleDouble4(double input) {
		return simpleDoubleformat4.format(input);
	}

	static synchronized public double getDouble(String input) throws RuntimeException {
		try {
			return doubleformat.parse(input).doubleValue();
		}
		catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
	
	static public Double getDoubleObj(String input) {
		if (isNullOrEmpty(input)) {
			return null;
		}
		return new Double(getDouble(input));
	}
	
	static public String getDouble(Double input) {
		if (input == null) {
			return null;
		}
		return getDouble(input.doubleValue());
	}
	
	static synchronized public boolean isDouble(String input) {
		try {
			doubleformat.parse(input);
		}
		catch (ParseException e) {
			return false;
		}
		return true;
	}
	
	static public String getString(String input) {
		if (input == null) {
			return "";
		}
		return input;
	}
	
	static public String getNonNullString(String input) {
		if (input == null) {
			return "";
		}
		return input;
	}
/*
	static public String formatCurrency(String currencyCode, Float input) {
		// TODO Currency should be formatted via locale and currenyCode.
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
		return numberFormat.format(input);
	}
*/
	static public String formatCustomerName(String prefix, String firstName, String middleName, String lastName, String suffix) {
		String name = prefix;
		if (name.length() > 0) {
			name += " ";
		}
		name += firstName;
		if (Format.isNullOrEmpty(middleName)) {
			name += " " + middleName;
		}
		name += " " + lastName;
		if (suffix.length() > 0) {
			name += " " + suffix;
		}
		return name;
	}
}
