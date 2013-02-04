package com.jada.ie;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.struts.util.MessageResources;

import sun.org.mozilla.javascript.internal.Context;
import sun.org.mozilla.javascript.internal.NativeJavaObject;

import com.jada.jpa.connection.JpaConnection;
import com.jada.jpa.entity.IeProfileDetail;
import com.jada.jpa.entity.IeProfileHeader;
import com.jada.jpa.entity.Site;
import com.jada.util.Constants;
import com.jada.util.Format;
import com.jada.xml.ie.ItemSimple;
import com.jada.xml.ie.ItemSimpleCategory;
import com.jada.xml.ie.ItemSimpleImage;
import com.jada.xml.ie.ItemSimpleItemAttributeDetail;
import com.jada.xml.ie.ItemSimpleItemTierPrice;

public class ItemSimpleCsvTransformation extends ItemSimpleTransformationBase implements ItemSimpleTransformation {
	MessageResources resources = MessageResources.getMessageResources("application");
	static char SEPERATOR = ',';
	static char QUOTE = '"';
	static char ESCAPE = '"';
		
	static int index = 1;
	static public ItemSimpleCsvMapping FIELDS_GENERAL[] = {
			new ItemSimpleCsvMapping(null, null, "siteProfileClassId", Long.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "siteProfileClassName", String.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "siteCurrencyClassId", Long.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "siteCurrencyClassName", String.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "itemId", Long.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "itemNum", String.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "itemUpcCd", String.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "itemSkuCd", String.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "itemTypeCd", String.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "itemSellable", Character.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "itemCost", Float.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "itemHitCounter", Integer.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "itemRating", Float.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "itemRatingCount", Integer.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "itemQty", Integer.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "itemBookedQty", Integer.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "itemPublishOn", Date.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "itemExpireOn", Date.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "published", Character.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "itemShortDesc", String.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "itemDesc", String.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "pageTitle", String.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "metaKeywords", String.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "metaDescription", String.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "itemPrice", Float.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "itemSpecPrice", Float.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "itemSpecPricePublishOn", Date.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "itemSpecPriceExpireOn", Date.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "shippingTypeId", Long.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "shippingTypeName", String.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "productClassId", Long.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "productClassName", String.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "itemParentId", Long.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "itemParentSkuCd", String.class, index++, null),			
			new ItemSimpleCsvMapping(null, null, "customAttributeGroupId", Long.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "customAttributeGroupName", String.class, index++, null),
			new ItemSimpleCsvMapping(null, null, "itemImageOverride", String.class, index++, null),
//			new ItemSimpleCsvMapping(null, null, "itemImageLocation", String.class, index++, null),
	};
	
	static public ItemSimpleCsvMapping FIELDS_CATEGORY[] = {
		new ItemSimpleCsvMapping("categories", 0, "catId", Long.class, index++, null),
		new ItemSimpleCsvMapping("categories", 0, "catShortTitle", String.class, index++, null),
	};
	
	static public ItemSimpleCsvMapping FIELDS_ITEM_RELATED[] = {
		new ItemSimpleCsvMapping("itemsRelated", 0, "itemId", Long.class, index++, null),
		new ItemSimpleCsvMapping("itemsRelated", 0, "itemSkuCd", String.class, index++, null),
	};
	
	static public ItemSimpleCsvMapping FIELDS_ITEM_UPSELL[] = {
		new ItemSimpleCsvMapping("itemsUpSell", 0, "itemId", Long.class, index++, null),
		new ItemSimpleCsvMapping("itemsUpSell", 0, "itemSkuCd", String.class, index++, null),
	};
	
	static public ItemSimpleCsvMapping FIELDS_ITEM_CROSSSELL[] = {
		new ItemSimpleCsvMapping("itemsCrossSell", 0, "itemId", Long.class, index++, null),
		new ItemSimpleCsvMapping("itemsCrossSell", 0, "itemSkuCd", String.class, index++, null),
	};
	
	static public ItemSimpleCsvMapping FIELDS_ITEM_TIERPRICE[] = {
		new ItemSimpleCsvMapping("itemTierPrices", 0, "custClassId", Long.class, index++, null),
		new ItemSimpleCsvMapping("itemTierPrices", 0, "itemTierQty", Integer.class, index++, null),
		new ItemSimpleCsvMapping("itemTierPrices", 0, "itemPrice", Float.class, index++, null),
		new ItemSimpleCsvMapping("itemTierPrices", 0, "itemTierPricePublishOn", Date.class, index++, null),
		new ItemSimpleCsvMapping("itemTierPrices", 0, "itemTierPriceExpireOn", Date.class, index++, null),
	};
	
	static public ItemSimpleCsvMapping FIELDS_ITEM_ATTRIBUTEDETAIL[] = {
		new ItemSimpleCsvMapping("itemAttributeDetails", 0, "customAttribId", Long.class, index++, null),
		new ItemSimpleCsvMapping("itemAttributeDetails", 0, "customAttribName", String.class, index++, null),
		new ItemSimpleCsvMapping("itemAttributeDetails", 0, "customAttribOptionId", Long.class, index++, null),
		new ItemSimpleCsvMapping("itemAttributeDetails", 0, "customAttribValue", String.class, index++, null),
		new ItemSimpleCsvMapping("itemAttributeDetails", 0, "itemAttribDetailValue", String.class, index++, null),
	};
	
	static public ItemSimpleCsvMapping FIELDS_ITEM_IMAGE[] = {
		new ItemSimpleCsvMapping("itemImages", 0, "itemImageLocation", String.class, index++, null),
		new ItemSimpleCsvMapping("itemImages", 0, "defaultImage", String.class, index++, null),
	};

	static public ItemSimpleCsvMapping FIELDS_OTHERS[] = {
		new ItemSimpleCsvMapping("others", 0, "other", String.class, index++, null)
	};

	int counterCategory = -1;
	int counterItemRelated = -1;
	int counterItemUpSell = -1;
	int counterItemCrossSell = -1;
	int counterItemTierPrice = -1;
	int counterItemAttributeDetail = -1;
	int counterItemImages = -1;
	int counterOthers = -1;
	
	String exportLocation = null;
	
	ScriptEngineManager factory = null;
	
	public ItemSimpleCsvMapping mappings[];
	
	public ItemSimpleCsvTransformation() {
		factory = new ScriptEngineManager();
	}
	
	public ItemSimpleCsvTransformation(Long ieProfileHeaderId, Site site) throws Exception {
		this(ieProfileHeaderId, site, Constants.IE_EXPORT_LOCATION_SERVER);
	}
	
	public ItemSimpleCsvTransformation(Long ieProfileHeaderId, Site site, String exportLocation) throws Exception {
		this();
		init(site);
		this.exportLocation = exportLocation;
		
		EntityManager em = JpaConnection.getInstance().getCurrentEntityManager();
		IeProfileHeader ieProfileHeader = (IeProfileHeader) em.find(IeProfileHeader.class, ieProfileHeaderId);
		Vector<ItemSimpleCsvMapping> vector = new Vector<ItemSimpleCsvMapping>();
		for (IeProfileDetail ieProfileDetail : ieProfileHeader.getIeProfileDetails()) {
			if (ieProfileDetail.getIeProfilePosition() == null && ieProfileDetail.getIeProfileFieldValue() == null) {
				continue;
			}
			ItemSimpleCsvMapping mapping = new ItemSimpleCsvMapping();
			mapping.setIeProfileGroupName(ieProfileDetail.getIeProfileGroupName());
			mapping.setIeProfileGroupIndex(ieProfileDetail.getIeProfileGroupIndex());
			mapping.setIeProfileFieldName(ieProfileDetail.getIeProfileFieldName());
			mapping.setIeProfilePosition(ieProfileDetail.getIeProfilePosition());
			mapping.setIeProfileFieldValue(ieProfileDetail.getIeProfileFieldValue());
			
			if (Format.isNullOrEmpty(ieProfileDetail.getIeProfileGroupName())) {
				for (ItemSimpleCsvMapping field : FIELDS_GENERAL) {
					if (field.getIeProfileFieldName().equals(ieProfileDetail.getIeProfileFieldName())) {
						mapping.setIeProfileFieldType(field.getIeProfileFieldType());
						break;
					}
				}
			}
			else if (ieProfileDetail.getIeProfileGroupName().equals("itemsRelated")) {
				for (ItemSimpleCsvMapping field : FIELDS_ITEM_RELATED) {
					if (field.getIeProfileFieldName().equals(ieProfileDetail.getIeProfileFieldName())) {
						mapping.setIeProfileFieldType(field.getIeProfileFieldType());
						break;
					}
				}
			}
			else if (ieProfileDetail.getIeProfileGroupName().equals("itemsUpSell")) {
				for (ItemSimpleCsvMapping field : FIELDS_ITEM_UPSELL) {
					if (field.getIeProfileFieldName().equals(ieProfileDetail.getIeProfileFieldName())) {
						mapping.setIeProfileFieldType(field.getIeProfileFieldType());
						break;
					}
				}
			}
			else if (ieProfileDetail.getIeProfileGroupName().equals("itemsCrossSell")) {
				for (ItemSimpleCsvMapping field : FIELDS_ITEM_CROSSSELL) {
					if (field.getIeProfileFieldName().equals(ieProfileDetail.getIeProfileFieldName())) {
						mapping.setIeProfileFieldType(field.getIeProfileFieldType());
						break;
					}
				}
			}
			else if (ieProfileDetail.getIeProfileGroupName().equals("itemTierPrices")) {
				for (ItemSimpleCsvMapping field : FIELDS_ITEM_TIERPRICE) {
					if (field.getIeProfileFieldName().equals(ieProfileDetail.getIeProfileFieldName())) {
						mapping.setIeProfileFieldType(field.getIeProfileFieldType());
						break;
					}
				}
			}
			else if (ieProfileDetail.getIeProfileGroupName().equals("itemAttributeDetails")) {
				for (ItemSimpleCsvMapping field : FIELDS_ITEM_ATTRIBUTEDETAIL) {
					if (field.getIeProfileFieldName().equals(ieProfileDetail.getIeProfileFieldName())) {
						mapping.setIeProfileFieldType(field.getIeProfileFieldType());
						break;
					}
				}
			}
			else if (ieProfileDetail.getIeProfileGroupName().equals("itemImages")) {
				for (ItemSimpleCsvMapping field : FIELDS_ITEM_IMAGE) {
					if (field.getIeProfileFieldName().equals(ieProfileDetail.getIeProfileFieldName())) {
						mapping.setIeProfileFieldType(field.getIeProfileFieldType());
						break;
					}
				}
			}
			else if (ieProfileDetail.getIeProfileGroupName().equals("others")) {
				for (ItemSimpleCsvMapping field : FIELDS_OTHERS) {
					if (field.getIeProfileFieldName().equals(ieProfileDetail.getIeProfileFieldName())) {
						mapping.setIeProfileFieldType(field.getIeProfileFieldType());
						break;
					}
				}
			}

			vector.add(mapping);
		}
		
		if (ieProfileHeader.getIeProfileType() == Constants.IE_PROFILE_TYPE_EXPORT) {
			Enumeration<ItemSimpleCsvMapping> enumeration = vector.elements();
			int max = 0;
			while (enumeration.hasMoreElements()) {
				ItemSimpleCsvMapping mapping = enumeration.nextElement();
				if (mapping.getIeProfilePosition() > max) {
					max = mapping.getIeProfilePosition();
				}
			}
			
			mappings = new ItemSimpleCsvMapping[max];
			enumeration = vector.elements();
			while (enumeration.hasMoreElements()) {
				ItemSimpleCsvMapping mapping = enumeration.nextElement();
				mappings[mapping.getIeProfilePosition() - 1] = mapping;
			}
		}
		else {
			mappings = new ItemSimpleCsvMapping[vector.size()];
			vector.copyInto(mappings);
		}
		
		initCounter();
	}
	
	public void initCounter() {
		for (ItemSimpleCsvMapping mapping : mappings) {
			if (mapping == null) {
				continue;
			}
			if (mapping.getIeProfileGroupName() == null) {
				continue;
			}
			if (mapping.getIeProfileGroupName().equals("categories")) {
				if (mapping.getIeProfileGroupIndex() > counterCategory) {
					counterCategory = mapping.getIeProfileGroupIndex();
				}
			}
			else if (mapping.getIeProfileGroupName().equals("itemsRelated")) {
				if (mapping.getIeProfileGroupIndex() > counterItemRelated) {
					counterItemRelated = mapping.getIeProfileGroupIndex();
				}
			}
			else if (mapping.getIeProfileGroupName().equals("itemsUpSell")) {
				if (mapping.getIeProfileGroupIndex() > counterItemUpSell) {
					counterItemUpSell = mapping.getIeProfileGroupIndex();
				}
			}
			else if (mapping.getIeProfileGroupName().equals("itemsCrossSell")) {
				if (mapping.getIeProfileGroupIndex() > counterItemCrossSell) {
					counterItemCrossSell = mapping.getIeProfileGroupIndex();
				}
			}
			else if (mapping.getIeProfileGroupName().equals("itemTierPrices")) {
				if (mapping.getIeProfileGroupIndex() > counterItemTierPrice) {
					counterItemTierPrice = mapping.getIeProfileGroupIndex();
				}
			}
			else if (mapping.getIeProfileGroupName().equals("itemAttributeDetails")) {
				if (mapping.getIeProfileGroupIndex() > counterItemAttributeDetail) {
					counterItemAttributeDetail = mapping.getIeProfileGroupIndex();
				}
			}
			else if (mapping.getIeProfileGroupName().equals("itemImages")) {
				if (mapping.getIeProfileGroupIndex() > counterItemImages) {
					counterItemImages = mapping.getIeProfileGroupIndex();
				}
			}
			else if (mapping.getIeProfileGroupName().equals("others")) {
				if (mapping.getIeProfileGroupIndex() > counterOthers) {
					counterOthers = mapping.getIeProfileGroupIndex();
				}
			}
		}
	}
	
	public String getHeader() {
		Vector<String> vector = new Vector<String>();
		for (ItemSimpleCsvMapping definition : mappings) {
			if (definition == null) {
				vector.add("");
				continue;
			}
			vector.add(definition.getIeProfileFieldName());
		}
		String tokens[] = new String[vector.size()];
		vector.copyInto(tokens);
		return toCsv(tokens);
	}
	
	public String getFooter() {
		return null;
	}
	
	public String toExport(ItemSimple itemSimple) throws Exception {
		Vector<String> vector = new Vector<String>();
		
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		engine.put("item", itemSimple);
		
		for (ItemSimpleCsvMapping definition : mappings) {
			if (definition == null) {
				vector.add("");
				continue;
			}
			
			boolean isGroup = !Format.isNullOrEmpty(definition.getIeProfileGroupName());
			int index = -1;
			if (isGroup) {
				index = definition.getIeProfileGroupIndex().intValue();
			}
			
			Object object = null;
			if (!Format.isNullOrEmpty(definition.getIeProfileFieldValue())) {
				engine.eval(definition.getIeProfileFieldValue());
				object = engine.get("value");
			}
			else {
				String ieProfileMethodName = "";
				if (!isGroup) {
					ieProfileMethodName = definition.getIeProfileFieldName();
				}
				else {
					ieProfileMethodName = definition.getIeProfileGroupName();
				}
				String methodName = "get" + ieProfileMethodName.substring(0, 1).toUpperCase() + ieProfileMethodName.substring(1);
				Method method = ItemSimple.class.getMethod(methodName, (Class<?>[]) null);
				object = method.invoke(itemSimple, (Object[]) null);
				if (isGroup) {
					Object children = null;
					if (object != null && Array.getLength(object) > index) {
						children = Array.get(object, index);
						ieProfileMethodName = definition.getIeProfileFieldName();
						methodName = "get" + ieProfileMethodName.substring(0, 1).toUpperCase() + ieProfileMethodName.substring(1);
						method = children.getClass().getMethod(methodName, (Class<?>[]) null);
						object = method.invoke(children, (Object[]) null);
					}
					else {
						object = null;
					}
				}
			}
			
			if (object == null) {
				vector.add(null);
				/*
				if (object instanceof String) {
					vector.add("null");
				}
				else {
					vector.add("");
				}
				*/
			}
			else {
				String value = "Unknown type for " + definition.getIeProfileFieldName();
				if (object instanceof String) {
					value = (String) object;
					value = value.replace('\n', ' ');
					value = value.replace('\r', ' ');
				}
				if (object instanceof Integer) {
					value = ((Integer) object).toString();
				}
				if (object instanceof Long) {
					value = ((Long) object).toString();
				}
				if (object instanceof Float) {
					value = ((Float) object).toString();
				}
				if (object instanceof Double) {
					value = ((Float) object).toString();
				}
				if (object instanceof Date) {
					value = Format.getDate((Date) object);
				}
				if (object instanceof Character) {
					value = String.valueOf((Character) object);
				}
				if (object instanceof org.exolab.castor.types.Date) {
					Date d = ((org.exolab.castor.types.Date) object).toDate();
					value = Format.getDate(d);
				}
				
				if (definition.getIeProfileFieldName().equals("itemImageLocation")) {
					boolean found = false;
					if (exportLocation.equals(Constants.IE_EXPORT_LOCATION_SERVER)) {
						ItemSimpleImage itemSimpleImages[] = itemSimple.getItemImages();
						if (itemSimpleImages != null) {
							if (index < itemSimpleImages.length) {
								String filename = value;
								FileOutputStream out = new FileOutputStream(new File(filename));
								out.write(itemSimpleImages[index].getImageValue());
								out.close();
								found = true;
							}
						}
					}
					if (!found) {
						value = "";
					}
				}
				vector.add(value);
			}
		}
		
		String tokens[] = new String[vector.size()];
		vector.copyInto(tokens);
		String value = this.toCsv(tokens) + "\n";
		return value;
	}
	
	public String[] validate(String input) throws IOException, ParseException, SecurityException, NoSuchFieldException, NoSuchMethodException {
		Vector<String> m = new Vector<String>();

		String tokens[] = fromCsv(input);
		for (ItemSimpleCsvMapping definition : mappings) {
			if (definition.getIeProfilePosition() == null) {
				continue;
			}
			if (tokens.length < definition.getIeProfilePosition()) {
				m.add(resources.getMessage("IE.ERROR.E005", definition.getIeProfilePosition(), definition.getIeProfileFieldName()));
				continue;
			}
			
			int index = definition.getIeProfilePosition() - 1;
			if (definition.getIeProfileFieldType() == String.class) {
			}
			else if (definition.getIeProfileFieldType() == Long.class) {
				if (tokens[index] != null) {
					if (!Format.isLong(tokens[index])) {
						m.add(resources.getMessage("IE.ERROR.E001", tokens[index], definition.getIeProfileFieldName(), "Long"));
					}
				}
			}
			else if (definition.getIeProfileFieldType() == Integer.class) {
				if (tokens[definition.getIeProfilePosition()] != null) {
					if (!Format.isInt(tokens[definition.getIeProfilePosition()])) {
						m.add(resources.getMessage("IE.ERROR.E001", tokens[definition.getIeProfilePosition()], definition.getIeProfileFieldName(), "Integer"));
					}
				}			}
			else if (definition.getIeProfileFieldType() == Float.class) {
				if (tokens[index] != null) {
					if (!Format.isInt(tokens[index])) {
						m.add(resources.getMessage("IE.ERROR.E001", tokens[index], definition.getIeProfileFieldName(), "Float"));
					}
				}
			}
			else if (definition.getIeProfileFieldType() == Double.class) {
				if (tokens[index] != null) {
					if (!Format.isInt(tokens[index])) {
						m.add(resources.getMessage("IE.ERROR.E001", tokens[index], definition.getIeProfileFieldName(), "Double"));
					}
				}
			}
			else if (definition.getIeProfileFieldType() == char.class) {
				if (tokens[index].length() > 1) {
					m.add(resources.getMessage("IE.ERROR.E001", tokens[index], definition.getIeProfileFieldName(), "char"));
				}
			}
			else if (definition.getIeProfileFieldType() == Date.class) {
				if (tokens[index] != null) {
					if (!Format.isDate(tokens[index])) {
						m.add(resources.getMessage("IE.ERROR.E001", tokens[index], definition.getIeProfileFieldName(), "Date"));
					}
				}
			}
		}
		
		String messages[] = new String[m.size()];
		m.copyInto(messages);
		return messages;
	}
	
	public ItemSimple fromImport(String input) throws Exception {
		ItemSimple itemSimple = new ItemSimple();
		ItemSimpleCategory categories[] = new ItemSimpleCategory[counterCategory + 1];
		for (int i = 0; i < categories.length; i++) {
			categories[i] = new ItemSimpleCategory();
		}
		itemSimple.setCategories(categories);
		ItemSimple itemsRelated[] = new ItemSimple[counterItemRelated + 1];
		for (int i = 0; i < itemsRelated.length; i++) {
			itemsRelated[i] = new ItemSimple();
		}
		itemSimple.setItemsRelated(itemsRelated);
		ItemSimple itemsUpSell[] = new ItemSimple[counterItemUpSell + 1];
		for (int i = 0; i < itemsUpSell.length; i++) {
			itemsUpSell[i] = new ItemSimple();
		}
		itemSimple.setItemsUpSell(itemsUpSell);
		ItemSimple itemsCrossSell[] = new ItemSimple[counterItemCrossSell + 1];
		for (int i = 0; i < itemsCrossSell.length; i++) {
			itemsCrossSell[i] = new ItemSimple();
		}
		itemSimple.setItemsCrossSell(itemsCrossSell);
		ItemSimpleItemTierPrice itemTierPrices[] = new ItemSimpleItemTierPrice[counterItemTierPrice + 1];
		for (int i = 0; i < itemTierPrices.length; i++) {
			itemTierPrices[i] = new ItemSimpleItemTierPrice();
		}
		itemSimple.setItemTierPrices(itemTierPrices);
		ItemSimpleItemAttributeDetail itemAttributeDetails[] = new ItemSimpleItemAttributeDetail[counterItemAttributeDetail + 1];
		for (int i = 0; i < itemAttributeDetails.length; i++) {
			itemAttributeDetails[i] = new ItemSimpleItemAttributeDetail();
		}
		itemSimple.setItemAttributeDetails(itemAttributeDetails);
		ItemSimpleImage itemImages[] = new ItemSimpleImage[counterItemImages + 1];
		for (int i = 0; i < itemImages.length; i++) {
			itemImages[i] = new ItemSimpleImage();
		}
		itemSimple.setItemImages(itemImages);
		
		String tokens[] = this.fromCsv(input);
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		engine.put("tokens", tokens);
		engine.put("value", "");
		
		int count = 0;
		for (ItemSimpleCsvMapping definition : mappings) {
			if (definition.getIeProfilePosition() == null && definition.getIeProfileFieldValue() == null) {
				continue;
			}
			
			boolean isGroup = false;
			if (!Format.isNullOrEmpty(definition.getIeProfileGroupName())) {
				isGroup = true;
			}
			
			Object object = itemSimple;
			if (isGroup) {
				String ieProfileGroupName = definition.getIeProfileGroupName();
				String methodName = "get" + ieProfileGroupName.substring(0, 1).toUpperCase() + ieProfileGroupName.substring(1);
				Method method = ItemSimple.class.getMethod(methodName, (Class[]) null);
				Object parameters[] = {};
				Object arrays[] = (Object[]) method.invoke(itemSimple, parameters);
				object = Array.get(arrays, definition.getIeProfileGroupIndex());
			}

			String ieProfileFieldName = definition.getIeProfileFieldName();
			String methodName = "set" + ieProfileFieldName.substring(0, 1).toUpperCase() + ieProfileFieldName.substring(1);
			Class<?> types[] = new Class[1];
			Field f = object.getClass().getDeclaredField(ieProfileFieldName);
			types[0] = f.getType();
			
			Method method = object.getClass().getMethod(methodName, types);
			Object parameters[] = {null};
			
			String fieldValue = "";
			if (definition.getIeProfilePosition() != null) {
				fieldValue = tokens[definition.getIeProfilePosition().intValue() - 1];
			}
			if (!Format.isNullOrEmpty(definition.getIeProfileFieldValue())) {
				try {
					engine.eval(definition.getIeProfileFieldValue());
				}
				catch (javax.script.ScriptException e) {
					String message = "[" + definition.getIeProfileFieldValue() + "] - [" + e.getMessage() + "]";
					throw new ItemCsvTransformationException(message);
				}
				Object result = engine.get("value");
				if (result instanceof String) {
					fieldValue = (String) result;
				}
				else {
					NativeJavaObject javaObject = (NativeJavaObject) result;
					fieldValue = (String) Context.jsToJava(javaObject, String.class);
				}
			}
			
			if (definition.getIeProfileFieldName().equals("itemImageLocation")) {
				if (!Format.isNullOrEmpty(fieldValue)) {
					ItemSimpleImage itemSimpleImage = (ItemSimpleImage) object;
					itemSimpleImage.setImageValue(getImage(fieldValue));
				}
			}
			
			boolean foundType = true;
			try {
				if (f.getType() == String.class) {
					if (fieldValue != null) {
						parameters[0] = fieldValue;		
					}
				}
				else if (f.getType() == Long.class) {
					if (fieldValue != null) {
						parameters[0] = Format.getLong(fieldValue);
					}
				}
				else if (f.getType() == Integer.class) {
					if (fieldValue != null) {
						parameters[0] = Integer.valueOf(fieldValue);
					}
				}
				else if (f.getType() == Float.class) {
					if (fieldValue != null) {
						parameters[0] = Float.valueOf(fieldValue);
					}
				}
				else if (f.getType() == Double.class) {
					if (fieldValue != null) {
						parameters[0] = Double.valueOf(fieldValue);
					}
				}
				else if (f.getType() == char.class) {
					if (fieldValue != null) {
						parameters[0] = fieldValue.charAt(0);
					}
					else {
						parameters[0] = ' ';
					}
				}
				else if (f.getType() == Date.class) {
					if (fieldValue != null) {
						parameters[0] = Format.getDate(fieldValue);
					}
				}
				else {
					foundType = false;
				}
			}
			catch (Exception e) {
				throw new Exception("Unable to format value '" + fieldValue + "' for field " + ieProfileFieldName);
			}
			
			if (!foundType) {
				throw new Exception("Type " + f.getType().toString() + " for field " + ieProfileFieldName + " not implemented");
			}
			method.invoke(object, parameters);
			count++;
		}
		populateDefaultValue(itemSimple);
		
		if (itemSimple.getSiteCurrencyClassId() != null) {
			if (itemSimple.getSiteCurrencyClassId().equals(site.getSiteCurrencyClassDefault().getSiteCurrencyClassId())) {
				itemSimple.setDefaultCurrency(true);
			}
		}
		else {
			if (itemSimple.getSiteCurrencyClassName().equals(site.getSiteCurrencyClassDefault().getSiteCurrencyClassName())) {
				itemSimple.setDefaultCurrency(true);
			}
		}
		if (itemSimple.getSiteProfileClassId() != null) {
			if (itemSimple.getSiteProfileClassId().equals(site.getSiteProfileClassDefault().getSiteProfileClassId())) {
				itemSimple.setDefaultProfile(true);
			}
		}
		else {
			if (itemSimple.getSiteProfileClassName().equals(site.getSiteProfileClassDefault().getSiteProfileClassName())) {
				itemSimple.setDefaultProfile(true);
			}
		}

		return itemSimple;
	}
	
	public byte[] getImage(String filename) throws Exception {
		File file = new File(filename);
		InputStream is = new FileInputStream(file);
		int size = (int) file.length();
		byte[] bytes = new byte[size];
		int offset = 0;
		int num = 0;
		while (offset < bytes.length && (num = is.read(bytes, offset, bytes.length-offset)) >= 0) { 
			offset += num;
		}
		is.close();
		return bytes;
	}

	public String toCsv(String tokens[]) {
		String value = "";
		for (String token : tokens) {
			if (value.length() > 0) {
				value += ",";
			}
			if (token == null) {
				continue;
			}
			token = token.replaceAll("\\\\", "\\\\\\\\");
			token = token.replaceAll("\"", "\\\\\"");
			value += "\"" + token + "\"";
		}
		return value;
	}
	
	public String[] fromCsv(String input) {
		String tokens[] = null;
		boolean inToken = false;
		boolean hasQuote = false;
		boolean hasValue = false;
		
		Vector<String> vector = new Vector<String>();
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c == '\\') {
				if (i < input.length() - 1) {
					c = input.charAt(i + 1);
					i++;
				}
			}
			switch (c) {
			case ',':
				if (inToken && hasQuote) {
					buffer.append(c);
					break;
				}
				if (hasValue) {
					vector.add(buffer.toString());
				}
				else {
					vector.add(null);
				}
				buffer = new StringBuffer();
				hasQuote = false;
				hasValue = false;
				break;
			case '"':
				if (buffer.length() == 0) {
					hasQuote = true;
				}
				
				if (!inToken) {
					inToken = true;
				}
				else {
					inToken = false;
					hasValue = true;
				}
				break;
			default:
				if (hasQuote) {
					buffer.append(c);
				}
				break;
			}
		}
		vector.add(buffer.toString());

		tokens = new String[vector.size()];
		vector.copyInto(tokens);
		
		return tokens;
	}
}
