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

package com.jada.content.syndication;

import java.io.IOException;

import com.sun.syndication.io.FeedException;


public class Reader {

	public void go() throws Exception {
		try {
			/* TODO Why is there hardcoding */
			SyndicationList list = SyndReader.getInstance("jada").getSyndicationList();
			SyndicationEntryInfo entries[] = list.getSyncdicationEntryInfos();
			for (int i = 0; i < entries.length; i++) {
				System.out.println("============");
				System.out.println(entries[i].getLink());
				System.out.println("-------");
				System.out.println("Title > " + entries[i].getTitle());
				System.out.println("Description > " + entries[i].getDescription());
				System.out.println("Published On > " + entries[i].getPublishDate());
				System.out.println("Updated On > " + entries[i].getUpdatedDate());
			}
		} catch (FeedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			SyndicationList list = SyndReader.getInstance("JavaContent").getSyndicationList();
			System.out.println(list.toString());
		} catch (FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
