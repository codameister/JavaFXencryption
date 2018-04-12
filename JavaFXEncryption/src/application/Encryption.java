package application;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Encryption {

	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	
	public SecretKey key;
	public IvParameterSpec iv;
	//public byte[] ciphertextarray;
	public String decryptedmessage;
	public String ciphertext;
	private String keytext;
	private String IVtext;
	
	public void generatekeys() throws Exception {			
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", "BC");
		keyGenerator.init(128);
		key = keyGenerator.generateKey();
		SecureRandom random = new SecureRandom();
		byte[] buffer = new byte[16];
		random.nextBytes(buffer);
		iv = new IvParameterSpec(buffer);	
		
	}

	public String getkey(){
		keytext = Base64.getEncoder().encodeToString(key.getEncoded());
		return keytext;
	}
	
	public String getIV(){
		IVtext = Base64.getEncoder().encodeToString(iv.getIV());
		return IVtext;	
	}

	public String decrypt(String ciphertext, String keytext, String ivtext) throws Exception {
		
		byte[] decodediv = Base64.getDecoder().decode(ivtext);
		iv = new IvParameterSpec(decodediv);
		byte[] decodedkey = Base64.getDecoder().decode(keytext);
		key = new SecretKeySpec(decodedkey, 0, decodedkey.length, "AES"); 
		decryptedmessage = decryptWithAes(ciphertext, key, iv); 
		return decryptedmessage;
	}

	public String encrypt(String message, String keytext, String ivtext) throws Exception {
		byte[] decodediv = Base64.getDecoder().decode(ivtext);
		iv = new IvParameterSpec(decodediv);	
		byte[] decodedkey = Base64.getDecoder().decode(keytext);
		key = new SecretKeySpec(decodedkey, 0, decodedkey.length, "AES");
		byte[] ciphertextarray = encryptWithAes(message, key, iv);
		ciphertext = Base64.getEncoder().encodeToString(ciphertextarray);
		//ciphertext = new String(ciphertextarray);
		return ciphertext;
	}

	private byte[] encryptWithAes(String message, SecretKey key, IvParameterSpec iv)
		throws Exception {	
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
		aes.init(Cipher.ENCRYPT_MODE, key, iv);
		CipherOutputStream cipherOut = new CipherOutputStream(out, aes);
		OutputStreamWriter writer = new OutputStreamWriter(cipherOut);
		
		try {
			writer.write(message);
		}
		finally {
			writer.close();
		}	
		return out.toByteArray();
	}

	private String decryptWithAes(String ciphertext, SecretKey key, IvParameterSpec iv)
		throws Exception {

		byte[] ciphertextarray = Base64.getDecoder().decode(ciphertext.getBytes());
			
		//byte[] ciphertextarray = ciphertext.getBytes();
		
		ByteArrayInputStream in = new ByteArrayInputStream(ciphertextarray);
		Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
		aes.init(Cipher.DECRYPT_MODE, key, iv);
		CipherInputStream cipherIn = new CipherInputStream(in, aes);
		InputStreamReader reader = new InputStreamReader(cipherIn);
		BufferedReader bufferedReader = new BufferedReader(reader);
		try {
			return bufferedReader.readLine();
		}
		finally {
			bufferedReader.close();
		}
	}
}