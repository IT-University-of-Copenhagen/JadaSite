package com.jada.ie;

import java.io.StringReader;
import java.io.StringWriter;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.XMLContext;
import org.xml.sax.InputSource;

import com.jada.util.Constants;
import com.jada.xml.ie.ItemSimple;

public class ItemSimpleXmlTransformation extends ItemSimpleTransformationBase implements ItemSimpleTransformation {
	XMLContext xmlContext = null;
	
	public ItemSimpleXmlTransformation() throws MappingException {
		Mapping mapping = new Mapping();
		InputSource input = new InputSource(getClass().getResourceAsStream("/com/jada/xml/ie/ItemSimpleMapping.xml"));
		mapping.loadMapping(input);
		
		xmlContext = new XMLContext();
		xmlContext.addMapping(mapping);		
	}
	
	public String getFooter() {
		return null;
	}

	public String getHeader() {
		return null;
	}

	public String toExport(ItemSimple itemSimple) throws Exception {
		StringWriter writer = new StringWriter();
    	Marshaller marshaller = xmlContext.createMarshaller();
    	marshaller.setWriter(writer);
    	if (!isValid(itemSimple.getItemSellable())) {
    		itemSimple.setItemSellable(Constants.VALUE_NO);
    	}
    	if (!isValid(itemSimple.getPublished())) {
    		itemSimple.setPublished(Constants.VALUE_NO);
    	}
		for (ItemSimple i : itemSimple.getItemsUpSell()) {
			i.setItemSellable(' ');
			i.setPublished(' ');
		}
		marshaller.marshal(itemSimple);
		return writer.toString().replaceFirst("\n", "") + System.getProperty("line.separator");
	}

	public ItemSimple fromImport(String input) throws Exception {
		Unmarshaller unmarshaller = xmlContext.createUnmarshaller();
		unmarshaller.setClass(ItemSimple.class);
		StringReader reader = new StringReader(input);
		ItemSimple itemSimple = (ItemSimple) unmarshaller.unmarshal(reader);
		return itemSimple;
	}

	public String[] validate(String input) throws Exception {
		String[] messages = {};
		return messages;
	}
	
	public boolean isValid(char value) {
		if (value != ' ' && value != Constants.VALUE_YES && value != Constants.VALUE_NO) {
			return false;
		}
		return true;
	}
}
