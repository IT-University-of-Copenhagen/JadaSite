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

package com.jada.statistics;

import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.struts.util.LabelValueBean;

import com.jada.util.Format;

public class Statistics {
	static String STATISTICS_SERVER_UPTIME = "statistics.server.uptime";
	static String STATISTICS_SERVER_TOTAL_MEMORY = "statistics.server.total.memory";
	static String STATISTICS_SERVER_FREE_MEMORY = "statistics.server.free.memory";
	static String STATISTICS_THREAD_COUNT = "statistics.thread.count";
	static long uptime = System.currentTimeMillis();
	
	public LabelValueBean[] getServerStats() {
		LabelValueBean table[] = new LabelValueBean[3];
		table[0] = new LabelValueBean(STATISTICS_SERVER_UPTIME, Format.getFullDatetime(new Date(uptime)));
		table[1] = new LabelValueBean(STATISTICS_SERVER_TOTAL_MEMORY, Format.getLong(Runtime.getRuntime().totalMemory()) + " bytes");
		table[2] = new LabelValueBean(STATISTICS_SERVER_FREE_MEMORY, Format.getLong(Runtime.getRuntime().freeMemory()) + " bytes");
		return table;
	}
	
	public LabelValueBean[] getThreadStats() {
		LabelValueBean table[] = new LabelValueBean[Thread.activeCount() + 1];
		table[0] = new LabelValueBean(STATISTICS_THREAD_COUNT, Format.getInt(Thread.activeCount()));
		Thread thread[] = new Thread[Thread.activeCount()];
		Thread.enumerate(thread);
		for (int i = 0; i < thread.length; i++) {
			table[i + 1] = new LabelValueBean("Thread [" + i + "]", thread[i].toString());
		}
		return table;
	}
	
	public LabelValueBean[] getJvmStats() {
		LabelValueBean table[] = new LabelValueBean[System.getProperties().size()];
		Properties properties = System.getProperties();
		Enumeration<?> enumeration = properties.propertyNames();
		int i = 0;
		while (enumeration.hasMoreElements()) {
			String name = (String) enumeration.nextElement();
			String value = (String) properties.getProperty(name);
			table[i++] = new LabelValueBean(name, value);
		}
		return table;
	}
	
}
