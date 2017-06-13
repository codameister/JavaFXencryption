package application;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
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
	public byte[] ciphertextarray;
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
		IVtext = iv.toString();
		return IVtext;
		
	}

	public String decrypt(String ciphertext) throws Exception {
		decryptedmessage = decryptWithAes(ciphertextarray); 
		return decryptedmessage;
	}

	public String encrypt(String message) throws Exception {
		ciphertextarray = encryptWithAes(message, key, iv);
		ciphertext = new String(ciphertextarray);
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

private String decryptWithAes(byte[] cipertextarray)
		throws Exception {
		ByteArrayInputStream in = new ByteArrayInputStream(cipertextarray);
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