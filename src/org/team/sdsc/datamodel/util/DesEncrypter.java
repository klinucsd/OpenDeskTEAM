
package org.team.sdsc.datamodel.util;

import java.io.*;
import javax.crypto.*;  
import java.security.spec.*;
import javax.crypto.spec.*;

public class DesEncrypter {

    Cipher ecipher;
    Cipher dcipher;
    
    public DesEncrypter(SecretKey key) {
	// Create an 8-byte initialization vector
	byte[] iv = new byte[]{
	    (byte)0x8E, 0x12, 0x39, (byte)0x9C,
	    0x07, 0x72, 0x6F, 0x5A
	};
	AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
	try {
	    ecipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
	    dcipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    
	    // CBC requires an initialization vector
	    ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
	    dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
	} catch (java.security.InvalidAlgorithmParameterException e) {
	} catch (javax.crypto.NoSuchPaddingException e) {
	} catch (java.security.NoSuchAlgorithmException e) {
	} catch (java.security.InvalidKeyException e) {
	}
    }
    
    // Buffer used to transport the bytes from one stream to another
    byte[] buf = new byte[1024];
    
    public void encrypt(InputStream in, OutputStream out) {
	try {
	    // Bytes written to out will be encrypted
	    out = new CipherOutputStream(out, ecipher);
    
	    // Read in the cleartext bytes and write to out to encrypt
	    int numRead = 0;
	    while ((numRead = in.read(buf)) >= 0) {
		out.write(buf, 0, numRead);
	    }
	    out.close();
	    in.close();
	} catch (java.io.IOException e) {
	}
    }
    
    public void decrypt(InputStream in, OutputStream out) {
	try {
	    // Bytes read from in will be decrypted
	    in = new CipherInputStream(in, dcipher);
    
	    // Read in the decrypted bytes and write the cleartext to out
	    int numRead = 0;
	    while ((numRead = in.read(buf)) >= 0) {
		out.write(buf, 0, numRead);
	    }
	    out.close();
	    in.close();
	} catch (java.io.IOException e) {
	}
    }
}
