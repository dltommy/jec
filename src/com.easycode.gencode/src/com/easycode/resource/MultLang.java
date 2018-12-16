/**
 * 作 者: dltommy
 * 日 期: 2012-3-3
 * 描 叙:
 */
package com.easycode.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.PropertyResourceBundle;


/**
 * 功能描叙:
 * 编   码: dltommy
 * 完成时间: 2012-3-3 下午06:41:41
 */
public class MultLang
{
    public static String getMultLang(String key)
    {
		InputStream is = MultLang.class.getResourceAsStream("lang.properties");
		try
		{
			PropertyResourceBundle pr = new PropertyResourceBundle(is);
			return pr.getString(key);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
    }
}

