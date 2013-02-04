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

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.axis.encoding.Base64;

public class AESEncoder {
	static int KEY_SIZE = 128;
	Cipher encryptCipher = null;
	Cipher decryptCipher = null;
	static AESEncoder instance = null;
	
	static public AESEncoder getInstance() {
		return instance;
	}
	
	static public void init(String encodedKey) throws Exception {
		instance = new AESEncoder(encodedKey);
	}
	
	public AESEncoder(String encodedKey) throws Exception {
		byte key[] = Base64.decode(encodedKey);
		SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
		encryptCipher = Cipher.getInstance("AES");
		encryptCipher.init(Cipher.ENCRYPT_MODE, keySpec);
		decryptCipher = Cipher.getInstance("AES");
		decryptCipher.init(Cipher.DECRYPT_MODE, keySpec);
	}
	
	public String encode(String input) throws Exception {
		byte result[] = encryptCipher.doFinal(input.getBytes());
		String encodedResult = Base64.encode(result);
		return encodedResult;
	}
	
	public String decode(String encodedInput) throws Exception {
		byte input[] = Base64.decode(encodedInput);
		byte result[] = decryptCipher.doFinal(input);
		return new String(result, "utf-8");
	}
	
	public static String generateKey() throws NoSuchAlgorithmException {
		KeyGenerator generator = KeyGenerator.getInstance("AES");
		generator.init(KEY_SIZE);
		SecretKey secertKey = generator.generateKey();
		byte key[] = secertKey.getEncoded();
		String encodedKey = Base64.encode(key);
		return encodedKey;
	}
	
	public static void main(String[] args) {
		try {
			AESEncoder.init("t7GcYbbdbKxZtV2ge6qpeQ==");
			
			String input = "admin";
			String encodedString = AESEncoder.getInstance().encode(input);
			System.out.println(encodedString);
			
			String decodedString = AESEncoder.getInstance().decode(encodedString);
			System.out.println(decodedString);
			System.out.println("done");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
