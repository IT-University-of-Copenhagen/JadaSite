package com.jada.translator;

import java.util.List;

import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfstring;

public class JavaArrayOfString extends ArrayOfstring {
	public void setString(List<String> string) {
		this.string = string;
	}
}
