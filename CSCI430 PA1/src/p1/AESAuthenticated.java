package p1;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.AEADBadTagException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESAuthenticated {
	public static void main(String args[]) {
		try {
			userInput();
		} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void userInput() throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		SecretKey key = generateKey(128);
		GCMParameterSpec gcmspec = generateGCMspec();
		
		Scanner sc = new Scanner(System.in);
		
		key = generateKey(128);
		boolean encrypted = false;
		while(true) {
			System.out.println("Encrypt/Decrypt/End");
			String uin = sc.nextLine().toLowerCase();
			switch(uin) {
				case "encrypt":
					System.out.println("Input plaintext:");
					uin = sc.nextLine();
					System.out.println("Ciphertext: " + encrypt(uin, key, gcmspec));
					encrypted = true;
					break;
				case "decrypt":
					if(encrypted) {
						System.out.println("Input ciphertext+tag:");
						uin = sc.nextLine();
						try {
							String text = decrypt(uin, key, gcmspec);
							System.out.println("Text: " + text);
						} catch (AEADBadTagException e) {
							System.out.println(e.getMessage());
						} catch (IllegalArgumentException e) {
							System.out.println(e.getMessage());
						}
					} else {
						System.out.println("Can't decrypt without encrypting.");
					}
					break;
				case "end":
					sc.close();
					return;
			}
		}
	}
	
	public static String encrypt(String text, SecretKey key, GCMParameterSpec gcmspec) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getEncoded(), "AES"), gcmspec);
		byte[] ciphertext = cipher.doFinal(text.getBytes());
		return Base64.getEncoder().encodeToString(ciphertext);
	}
	
	public static String decrypt(String ciphertext, SecretKey key, GCMParameterSpec gcmspec) throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getEncoded(), "AES"), gcmspec);
		byte[] text = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
		return new String(text);
		
	}
	
	public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(n);
		SecretKey key = keyGenerator.generateKey();
		return key;
	}
	
	public static GCMParameterSpec generateGCMspec() {
		byte[] iv = new byte[12];
		new SecureRandom().nextBytes(iv);
		return new GCMParameterSpec(128, iv);
	}
}
