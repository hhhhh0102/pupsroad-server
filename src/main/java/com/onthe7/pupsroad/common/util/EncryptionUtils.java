package com.onthe7.pupsroad.common.util;

import com.onthe7.pupsroad.common.exception.InvalidDecryptionParameterException;
import com.onthe7.pupsroad.common.exception.InvalidEncryptionParameterException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionUtils {
	
	public static String getSecurePassword(String password, byte[] salt) {

		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt);
			byte[] bytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for (byte aByte : bytes) {
				sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}

	public static String encryptString(String value, String salt) {
		return getSecurePassword(value, salt.getBytes(StandardCharsets.UTF_8));
	}
	
	public static String twoWayEncryption(String value, String salt, String key) {
		try {
			Cipher aesCipherEncrypt = getCipher(salt, key, Cipher.ENCRYPT_MODE);

			// get the bytes
			byte[] bytes = StringUtils.getBytesUtf8(value);

			// encryptCursor the bytes
			byte[] encryptBytes = aesCipherEncrypt.doFinal(bytes);

			// encode 64 the encrypted bytes
			return Base64.encodeBase64URLSafeString(encryptBytes);
		} catch (Exception exception) {
			throw new InvalidEncryptionParameterException();
		}
	}

	private static Cipher getCipher(String salt, String key, int mode) throws Exception {
		byte[] saltB = salt.getBytes();
		int iterationsDecrypt = 10000;
		SecretKeyFactory factoryKeyDecrypt = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		SecretKey tmp2 = factoryKeyDecrypt
				.generateSecret(new PBEKeySpec(key.toCharArray(), saltB, iterationsDecrypt, 128));
		SecretKeySpec decryptKey = new SecretKeySpec(tmp2.getEncoded(), "AES");

		Cipher aesCipherDecrypt = Cipher.getInstance("AES/ECB/PKCS5Padding");
		aesCipherDecrypt.init(mode, decryptKey);
		return aesCipherDecrypt;
	}

	public static String twoWayDecryption(String value, String salt, String key) {
		try {
			Cipher aesCipherDecrypt = getCipher(salt, key, Cipher.DECRYPT_MODE);
			// get the bytes from encodedEncrypted string
			byte[] e64bytes = StringUtils.getBytesUtf8(value);

			// decode 64, now the bytes should be encrypted
			byte[] eBytes = Base64.decodeBase64(e64bytes);

			// decrypt the bytes
			byte[] cipherDecode = aesCipherDecrypt.doFinal(eBytes);

			// to string
			return StringUtils.newStringUtf8(cipherDecode);
		} catch (Exception exception) {
			throw new InvalidDecryptionParameterException();
		}
	}
}
