package p1;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Arrays;
import java.util.*;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class CBCMAC {
	public static void main(String args[]) {
		try {
			UserInput();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void UserInput() throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {
		Scanner sc = new Scanner(System.in);
		SecretKey key;
		
		key = generateKey(128);
		IvParameterSpec iv = generateIv(true);
		SecretKey key2 = generateKey(128);
		IvParameterSpec iv2 = generateIv(false);
		boolean encrypted = false;
		SecretKey key3 = generateKey(128);
		IvParameterSpec iv3 = generateIv(false);
		

		while(true) {
			System.out.println("Generate/Verify/End");
			String uin = sc.nextLine().toLowerCase();
			switch(uin) {
				case "generate":
					System.out.println("Input message:");
					uin = sc.nextLine();
					byte[] uinbits = uin.getBytes();
					byte[] cbc = encrypt(uinbits, key, iv);
					String message = Base64.getEncoder().encodeToString(encrypt(uinbits, key3, iv3));
					byte[] tag = Arrays.copyOf(cbc, 16);

					tag = encrypt(tag, key2, iv2);
					String tagMessage = Base64.getEncoder().encodeToString(tag);
					System.out.println("Ciphertext: " + message);
					System.out.println("Auth Tag: " + tagMessage);
					encrypted = true;
					break;
				case "verify":
					if(encrypted) {
						System.out.println("Input ciphertext:");
						uin = sc.nextLine();
						System.out.println("Input auth tag:");
						String uin2 = sc.nextLine();
						byte[] detag = decrypt(uin2, key2, iv2);
						message = new String(decrypt(uin, key3, iv3));
						byte[] messagebits = encrypt(message.getBytes(), key, iv);
						
						String detagString = new String(detag);
						String messagebitsString = new String(Arrays.copyOf(messagebits, 16));
						if(detagString.equals(messagebitsString)) {
							System.out.println("Text: " + message);
						} else {
							System.out.println("Authenticator tag mismatch.");
						}
						
					} else {
						System.out.println("Can't verify without generating.");
					}
					break;
				case "end":
					sc.close();
					return;
			}
		}
	}
	
	public static byte[] encrypt(byte[] text, SecretKey key, IvParameterSpec iv) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		byte[] ciphertext = cipher.doFinal(text);
		return ciphertext;
	}
	
	public static byte[] decrypt(String ciphertext, SecretKey key, IvParameterSpec iv) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		byte[] text = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
		return text;
	}
	
	public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(n);
		SecretKey key = keyGenerator.generateKey();
		return key;
	}
	
	public static IvParameterSpec generateIv(boolean zero) {
		byte[] iv = new byte[16];
		if(!zero) {
			new SecureRandom().nextBytes(iv);
		}
		return new IvParameterSpec(iv);
	}

}
