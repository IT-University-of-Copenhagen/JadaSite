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

package com.jada.testing;

import java.util.Iterator;
import java.util.Vector;

import com.jada.jpa.entity.CustomAttributeDetail;
import com.jada.jpa.entity.CustomAttributeOption;
import com.jada.jpa.entity.Item;

public class DomTester {
	public DomTester() {
	}
	
	public void test1() {
		AttributeDetailOption[] attributes[] = new AttributeDetailOption[2][];
		
		CustomAttributeOption customAttributeOption = null;
		
		AttributeDetailOption options[] = new AttributeDetailOption[2];
		options[0] = new AttributeDetailOption();
		customAttributeOption = new CustomAttributeOption();
		customAttributeOption.setCustomAttribSkuCode("RED");
		options[0].setCustomAttributeOption(customAttributeOption);
		options[1] = new AttributeDetailOption();
		customAttributeOption = new CustomAttributeOption();
		customAttributeOption.setCustomAttribSkuCode("BLU");
		options[1].setCustomAttributeOption(customAttributeOption);
		attributes[0] = options;
		
		options = new AttributeDetailOption[3];
		options[0] = new AttributeDetailOption();
		customAttributeOption = new CustomAttributeOption();
		customAttributeOption.setCustomAttribSkuCode("L");
		options[0].setCustomAttributeOption(customAttributeOption);
		options[1] = new AttributeDetailOption();
		customAttributeOption = new CustomAttributeOption();
		customAttributeOption.setCustomAttribSkuCode("M");
		options[1].setCustomAttributeOption(customAttributeOption);
		options[2] = new AttributeDetailOption();
		customAttributeOption = new CustomAttributeOption();
		customAttributeOption.setCustomAttribSkuCode("S");
		options[2].setCustomAttributeOption(customAttributeOption);
		attributes[1] = options;

		Vector<Item> items = new Vector<Item>();
		combine(items, attributes, "X01", 0, new Vector<Object>());
		System.out.println("done");
	}

	
	public void combine(Vector<Item> items, AttributeDetailOption[] attributes[], String itemSkuCd, int index, Vector<?> itemOptions) {
		if (index >= attributes.length) {
			System.out.println(itemSkuCd);
			Item item = new Item();
			item.setItemSkuCd(itemSkuCd);
			items.add(item);
			return;
		}
		
		AttributeDetailOption attributeDetailOptions[] = attributes[index];
		for (int i = 0; i < attributeDetailOptions.length; i++) {
			Vector<AttributeDetailOption> nextItemOptions = new Vector<AttributeDetailOption>();
			Iterator<?> iterator = itemOptions.iterator();
			while (iterator.hasNext()) {
				AttributeDetailOption attributeDetailOption = (AttributeDetailOption) iterator.next();
				nextItemOptions.add(attributeDetailOption);
			}
			nextItemOptions.add(attributeDetailOptions[i]);
			combine(items, attributes, itemSkuCd + "-" + attributeDetailOptions[i].getCustomAttributeOption().getCustomAttribSkuCode(), index + 1, nextItemOptions);
		}
		return;
	}
	
	static public void main(String[] argv) {
		DomTester tester = new DomTester();
		tester.test1();
	}
	
	
	public class AttributeDetailOption {
		CustomAttributeOption customAttributeOption;
		CustomAttributeDetail customAttributeDetail;
		public CustomAttributeOption getCustomAttributeOption() {
			return customAttributeOption;
		}
		public void setCustomAttributeOption(CustomAttributeOption customAttributeOption) {
			this.customAttributeOption = customAttributeOption;
		}
		public CustomAttributeDetail getCustomAttributeDetail() {
			return customAttributeDetail;
		}
		public void setCustomAttributeDetail(CustomAttributeDetail customAttributeDetail) {
			this.customAttributeDetail = customAttributeDetail;
		}
	}
}
