/**
 * 作 者:  
 * 日 期: 2011-11-19
 * 描 叙:
 */
package com.easycode.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import net.sf.json.JSONObject;

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



    public static String formatOutput(String ctx)
    {
        return formatOutput(ctx,"\n");
    }

    public static String formatOutput(String ctx,String formatPatt)
    {
        String blank = "        ";
        if(ctx == null)
        {
            return null;
        }
        //双引号个数
        int doubleQuotationCount = 0;
        StringBuffer retBuf = new StringBuffer();
        List<String> leftList = new ArrayList<String>();
        leftList.add("{");
        leftList.add("[");
        //leftList.add("[");
        List<String> rightList = new ArrayList<String>();
        rightList.add("}");
        rightList.add("]");
        
        List<String> wrapList = new ArrayList<String>();
        wrapList.add(",");
        
        Stack<PosObj> stack = new Stack<PosObj>();
        PosObj curStackPos = null;
        //Stack<String> stack = new Stack<String>();
        for(int i = 0;i<ctx.length();i++)
        {
            String cur = ctx.substring(i, i + 1);
            if("\"".equals(cur))
            {
                doubleQuotationCount ++;
            }
            if (leftList.contains(cur) && doubleQuotationCount % 2 == 0)
            {
                retBuf.append(formatPatt);

                for (int k = 0; k < stack.size(); k++)
                {
                    retBuf.append(blank);
                }
                curStackPos = stack.push(new PosObj(i, cur));
            }
            else if (rightList.contains(cur) && doubleQuotationCount % 2 == 0)
            {
                retBuf.append(formatPatt);
                for (int k = 0; k < stack.size() - 1; k++)
                {
                    retBuf.append(blank);
                }
                curStackPos = stack.pop();
                curStackPos.pos = i;
            }
            //在引号里面的分行字符，不换行
            if(wrapList.contains(cur) && doubleQuotationCount % 2 == 0)
            {
                //先打逗号，再换行
                retBuf.append(cur);
                retBuf.append(formatPatt);
                
                for (int k = 0; k < stack.size(); k++)
                {
                    retBuf.append(blank);
                }
            }
            else
            {
                if (curStackPos != null)
                {
                    if (curStackPos.pos == i - 1)
                    {
                        retBuf.append(formatPatt);
                        for (int k = 0; k < stack.size(); k++)
                        {
                            retBuf.append(blank);
                        }
                        retBuf.append(cur);
                    }
                    else
                    {
                        retBuf.append(cur);
                    }
                }
                else
                {
                    retBuf.append(cur);
                }
            }

        } 
        return retBuf.toString();
    }

    public static String formatOutput(String ctx,int padLeftCount,int blankCount,String formatPatt)
    {
        String blank = "";
        for(int i=0;i<blankCount; i++)
        {
            blank +=" ";
        }
        String padLef = "";
        for(int i=0;i<padLeftCount;i++){
            padLef +=" ";
        }
        
        //String blank = "        ";
        if(ctx == null)
        {
            return null;
        }
        //双引号个数
        int doubleQuotationCount = 0;
        StringBuffer retBuf = new StringBuffer(padLef);
        List<String> leftList = new ArrayList<String>();
        leftList.add("{");
        leftList.add("[");
        //leftList.add("[");
        List<String> rightList = new ArrayList<String>();
        rightList.add("}");
        rightList.add("]");
        
        List<String> wrapList = new ArrayList<String>();
        wrapList.add(",");
        
        Stack<PosObj> stack = new Stack<PosObj>();
        PosObj curStackPos = null;
        //Stack<String> stack = new Stack<String>();
        for(int i = 0;i<ctx.length();i++)
        {
            String cur = ctx.substring(i, i + 1);
            if("\"".equals(cur))
            {
                doubleQuotationCount ++;
            }
            if (leftList.contains(cur) && doubleQuotationCount % 2 == 0)
            {
                retBuf.append(formatPatt);

                for (int k = 0; k < stack.size(); k++)
                {
                    retBuf.append(blank);
                }
                curStackPos = stack.push(new PosObj(i, cur));
            }
            else if (rightList.contains(cur) && doubleQuotationCount % 2 == 0)
            {
                retBuf.append(formatPatt);
                for (int k = 0; k < stack.size() - 1; k++)
                {
                    retBuf.append(blank);
                }
                curStackPos = stack.pop();
                curStackPos.pos = i;
            }
            //在引号里面的分行字符，不换行
            if(wrapList.contains(cur) && doubleQuotationCount % 2 == 0)
            {
                //先打逗号，再换行
                retBuf.append(cur);
                retBuf.append(formatPatt);
                
                for (int k = 0; k < stack.size(); k++)
                {
                    retBuf.append(blank);
                }
            }
            else
            {
                if (curStackPos != null)
                {
                    if (curStackPos.pos == i - 1)
                    {
                        retBuf.append(formatPatt);
                        for (int k = 0; k < stack.size(); k++)
                        {
                            retBuf.append(blank);
                        }
                        retBuf.append(cur);
                    }
                    else
                    {
                        retBuf.append(cur);
                    }
                }
                else
                {
                    retBuf.append(cur);
                }
            }

        } 
        String ret = retBuf.toString();
        ret = ret.replace(formatPatt, formatPatt+padLef);
        return ret;
    }
    public static Map josnToMap(String jsonObjStr) throws Exception
    {
        if(jsonObjStr == null || "".equals(jsonObjStr))
        {
            return new HashMap<String,String>();
        }
        JSONObject jsonObject = JSONObject.fromObject(jsonObjStr);
        return jsonObject;
    }
     
    public static void appendToMap(HashMap map, String jsonObjStr) throws Exception
    {
        
        JSONObject jsonObject = JSONObject.fromObject(jsonObjStr);
        if(map != null && jsonObject != null)
        {
            map.putAll(jsonObject);
        }
        

    }
   
    public static String getJsonStr(Object obj)
    {
        JSONObject arrayObj = JSONObject.fromObject(obj);
        return arrayObj.toString();
    }
	public static void main(String arg[])
	{
 
	    
	}
	
    private static class PosObj
    {
        public String letter;
        public int pos;
        public PosObj(int pos, String letter)
        {
            this.letter = letter;
            this.pos = pos;
        }
    } 


}

