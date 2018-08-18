/**
 * 作 者:  
 * 日 期: 2011-11-19
 * 描 叙:
 */
package com.easycode.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.commons.codec.binary.Base64;

/**
 * 功能描叙: 编 码:   完成时间: 2011-11-19 下午02:46:51
 */
public class StringUtil
{
	public final static String digest(String s)
	{
		String ret = null;
		try
		{
			// byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			// mdTemp.update(strTemp);
			byte[] md = mdTemp.digest(s.getBytes());
			md = Base64.encodeBase64(md);
			ret = new String(md);
			return ret.replaceAll("/", "#");

		}
		catch (Exception e)
		{
			return null;
		}
	}
    public final static String digest2(String s)
    {
        String ret = null;
        try
        {
            // byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            // mdTemp.update(strTemp);
            byte[] md = mdTemp.digest(s.getBytes());
            md = Base64.encodeBase64(md);
            ret = new String(md);
            return ret.toString();

        }
        catch (Exception e)
        {
            return null;
        }
    }
	public static String stringToMD5(String str)
	{
		try
		{
			byte[] strTemp = str.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			return toHexString(mdTemp.digest());
		}
		catch (Exception e)
		{
			return null;
		}
	}

 
	private static String toHexString(byte[] md)
	{
		char hexDigits[] =
		{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };
		int j = md.length;
		char str[] = new char[j * 2];
		for (int i = 0; i < j; i++)
		{
			byte byte0 = md[i];
			str[2 * i] = hexDigits[byte0 >>> 4 & 0xf];
			str[i * 2 + 1] = hexDigits[byte0 & 0xf];
		}
		return new String(str);
	}

	public static void main(String arg[])
	{
	    String version = StringUtil.digest2("421023198202188559");
	    System.err.println(version);
	    
	    String version2 = StringUtil.stringToMD5("abc");
	    System.err.println(version2);
	    
	}
	
	

	
}
