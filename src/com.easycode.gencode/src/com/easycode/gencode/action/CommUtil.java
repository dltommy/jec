/**
 * 作 者: dltommy
 * 日 期: 2012-12-1
 * 描 叙:
 */
package com.easycode.gencode.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
 
 

/**
 * 功能描叙:
 * 编   码: dltommy
 * 完成时间: 2012-12-1 下午01:38:42
 */
public class CommUtil
{
    public static String getResource(String path)
    {

		InputStream is = CommUtil.class
				.getResourceAsStream(path);

		InputStreamReader isr = null;
		try
		{
			isr = new InputStreamReader(is, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(isr);
		StringBuffer bs = new StringBuffer();
		while (true)
		{

			try
			{
				String tempStr = br.readLine();
				if (tempStr != null)
				{
					bs.append(tempStr + "\n");
				}
				else
				{
					break;
				}
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}

		}
		try
		{
			br.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bs.toString().trim();
	
    }
     
}

