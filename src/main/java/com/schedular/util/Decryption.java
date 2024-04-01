package com.schedular.util;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Decryption {
	Logger LOGGER = LogManager.getLogger(this.getClass());
	Cipher cipher;
	String decryptedText;

	public String passwordDecryption(String encryptedText) {
		try {
			KeyGenerator key = KeyGenerator.getInstance("AES");
			key.init(256);
			SecretKey secretkey = key.generateKey();
			cipher = Cipher.getInstance("AES");
			Base64.Decoder decoder = Base64.getDecoder();
			byte[] encryptedTextByte = decoder.decode(encryptedText);
			cipher.init(Cipher.DECRYPT_MODE, secretkey);
			byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
			decryptedText = new String(decryptedByte);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("NoSuchAlgorithmException is Occured :"+StackTrace.getMessage(e));
		} catch (Exception e) {
			LOGGER.error("Exception while Using passwordDecryption :"+StackTrace.getMessage(e));
		}
		return decryptedText;

	}

}
