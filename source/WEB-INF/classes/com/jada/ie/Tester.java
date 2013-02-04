package com.jada.ie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.jada.xml.ie.ItemSimple;

public class Tester {
	public void run() throws Exception {
		FileInputStream inputStream = new FileInputStream(new File("d:/tmp/jadaexport_20100321.csv"));
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		reader.readLine();
		while (true) {
			String input = reader.readLine();
			if (input == null) {
				break;
			}
			ItemSimpleCsvTransformation transformation = new ItemSimpleCsvTransformation();
			ItemSimple simple = transformation.fromImport(input);
			System.out.println(simple);
		}
	}
	public static void main(String[] args) {
		Tester tester = new Tester();
		try {
			tester.run();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
