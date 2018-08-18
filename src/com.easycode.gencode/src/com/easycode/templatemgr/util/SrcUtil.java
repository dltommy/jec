/**
 * 作 者: dltommy
 * 日 期: 2011-9-18
 * 描 叙:
 */
package com.easycode.templatemgr.util;
 
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.json.JSONArray;
import org.json.JSONException;

// import org.json.JSONObject;

// import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
 

/**
 * 功能描叙: 编 码: dltommy 完成时间: 2011-9-18 下午08:51:02
 */
public class SrcUtil<T>
{
 

	public static List<KVmodel<String, String>> josnToMapList(String jsonObjStr)
	{
		List<KVmodel<String, String>> retList = new ArrayList<KVmodel<String, String>>();
		JSONObject jsonObject = JSONObject.fromObject(jsonObjStr);

		for (Iterator iter = jsonObject.keys(); iter.hasNext();)
		{

			String key = (String) iter.next();

			retList.add(new KVmodel(key, jsonObject.get(key).toString()));
		}
		return retList;
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
		Iterator it = jsonObject.keys();
        while(it.hasNext())
        {
        	Object key = it.next();
        	map.put(key, jsonObject.get(key));
        }
	}
  
	public static String getJsonStr(Object obj)
	{
		JSONObject arrayObj = JSONObject.fromObject(obj);
		return arrayObj.toString();
	}


	public static class KVmodel<K, V>
	{
		private K key;
		private V value;

		public KVmodel(K key, V value)
			{
				this.key = key;
				this.value = value;
			}

		public K getKey()
		{
			return key;
		}

		public void setKey(K key)
		{
			this.key = key;
		}

		public V getValue()
		{
			return value;
		}

		public void setValue(V value)
		{
			this.value = value;
		}
	}


	public static String formatOutput(String ctx)
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
				retBuf.append("\n");

				for (int k = 0; k < stack.size(); k++)
				{
					retBuf.append(blank);
				}
				curStackPos = stack.push(new PosObj(i, cur));
			}
			else if (rightList.contains(cur) && doubleQuotationCount % 2 == 0)
			{
				retBuf.append("\n");
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
				retBuf.append("\n");
				
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
						retBuf.append("\n");
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
		//System.err.println(retBuf.toString());
		return retBuf.toString();
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
