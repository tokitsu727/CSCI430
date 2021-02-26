package p1;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

public class OneTimePad {
	public static void main(String args[]) {
		userInput();
	}
	
	public static void userInput() {
		Scanner sc = new Scanner(System.in);
		byte[] text;
		byte[] key = null;
		
		boolean encrypted = false;
		while(true) {
			System.out.println("Encrypt/Decrypt/End");
			String uin = sc.nextLine().toLowerCase();
			switch(uin) {
				case "encrypt":
					if(!encrypted) {
						System.out.println("Input plaintext:");
						uin = sc.nextLine();
						text = uin.getBytes();
						key = new byte[text.length];
						new SecureRandom().nextBytes(key);
						System.out.println("Ciphertext: " + encrypt(text, key));
						encrypted = true;
					} else {
						System.out.println("Can't encrypt twice in a row.");
					}
					break;
				case "decrypt":
					if(encrypted) {
						System.out.println("Input ciphertext:");
						uin = sc.nextLine();
						text = Base64.getDecoder().decode(uin);
						System.out.println("Plaintext: " + decrypt(text, key));
						key = null;
						encrypted = false;
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
	
	public static String encrypt(byte[] text, byte[] key) {
		byte[] encrypted = new byte[text.length];
		for(int i = 0; i < text.length; i++) {
			encrypted[i] = (byte)(text[i] ^ key[i]);
		}
		return Base64.getEncoder().encodeToString(encrypted);
	}
	
	public static String decrypt(byte[] text, byte[] key) {
		byte[] encrypted = new byte[text.length];
		for(int i = 0; i < text.length; i++) {
			encrypted[i] = (byte)(text[i] ^ key[i]);
		}
		return new String(encrypted);
	}
}
