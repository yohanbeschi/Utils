package com.xenoage.utils.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class computes the MD5 sum of a given input stream or file.
 * 
 * @author Andreas Wenger
 */
public class MD5Sum {

	/**
	 * Computes the MD5 data from the data behind the given file.
	 */
	public static String GetMD5Sum(String filePath)
		throws IOException {
    File f = new File(filePath);
    InputStream is = new FileInputStream(f);
    String ret = GetMD5Sum(is);
    return ret;
	}
	
	/**
	 * Computes the MD5 data from the data behind the given input stream.
	 */
	public static String GetMD5Sum(InputStream is)
		throws IOException {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException ex) {
			throw new IOException(ex);
		}
    byte[] buffer=new byte[8192];
    int read=0;
    while ((read = is.read(buffer)) > 0)
    	md.update(buffer, 0, read);
    byte[] md5 = md.digest();
    BigInteger bi=new BigInteger(1, md5);
    String ret = bi.toString(16);
    return ret;
	}
	
}