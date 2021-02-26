package p1;
import java.security.*;
import java.util.Base64;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

public class AESCBC {
	public static void main(String args[]) {
		try {
			userInput();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
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
	
	public static void userInput() throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
		Scanner sc = new Scanner(System.in);
		SecretKey key;
		
		key = generateKey(128);
		IvParameterSpec iv = generateIv();
		boolean encrypted = false;
		while(true) {
			System.out.println("Encrypt/Decrypt/End");
			String uin = sc.nextLine().toLowerCase();
			switch(uin) {
				case "encrypt":
					System.out.println("Input plaintext:");
					uin = sc.nextLine();
					System.out.println("Ciphertext: " + encrypt(uin, key, iv));
					encrypted = true;
					break;
				case "decrypt":
					if(encrypted) {
						System.out.println("Input ciphertext:");
						uin = sc.nextLine();
						System.out.println("Plaintext: " + decrypt(uin, key, iv));
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
	
	public static IvParameterSpec generateIv() {
		byte[] iv = new byte[16];
		new SecureRandom().nextBytes(iv);
		return new IvParameterSpec(iv);
	}
	public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(n);
		SecretKey key = keyGenerator.generateKey();
		return key;
	}
	
	public static String encrypt(String text, SecretKey key, IvParameterSpec iv) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		byte[] ciphertext = cipher.doFinal(text.getBytes());
		return Base64.getEncoder().encodeToString(ciphertext);
	}
	
	public static String decrypt(String ciphertext, SecretKey key, IvParameterSpec iv) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		byte[] text = cipher.doFinal(Base64.getDecoder().decode(ciphertext));
		return new String(text);
	}
}
