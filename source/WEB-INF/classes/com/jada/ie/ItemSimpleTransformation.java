package com.jada.ie;

import com.jada.xml.ie.ItemSimple;

public interface ItemSimpleTransformation {
	public String getHeader();
	public String getFooter();
	public String toExport(ItemSimple itemSimples) throws Exception;
	public ItemSimple fromImport(String input) throws Exception;
	public String[] validate(String input) throws Exception;
}
