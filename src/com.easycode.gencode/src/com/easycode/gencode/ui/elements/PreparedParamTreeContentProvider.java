package com.easycode.gencode.ui.elements;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class PreparedParamTreeContentProvider  implements ITreeContentProvider{

	private JSONObject map = null;
	public void init(String json)
	{
		map = JSONObject.fromObject(json);
	}
	
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}

	public Object[] getChildren(Object arg0) {
		// TODO Auto-generated method stub
		
		if (arg0 != null)
		{
			JSONKV e = (JSONKV)arg0;
			Object o = e.getValue();
			
			if(o instanceof JSONObject)
			{
				JSONObject js = (JSONObject)o;
				Iterator it = js.keys();

					
				JSONKV[] ret = new JSONKV[js.size()];
					
				    int i= 0;
					while(it.hasNext())
					{ 
						String key = (String)it.next();
						ret[i++] = new JSONKV(key,js.get(key));
					}
					return ret;
				
			}
			else if((o instanceof JSONArray))
			{
			 
				JSONArray a = (JSONArray)o;
				Object[] ret = new Object[a.size()];
				for(int i=0;i<a.size();i++)
				{
					
					ret[i]=new JSONKV((i)+"",a.get(i),true); 
				}
				return ret;
			}
			else if((o instanceof String))
			{
			 
				//JSONKV[] ret = new JSONKV[1];
				//ret[0]=new JSONKV("",(String)o); 
				return null;
			}
			
			//if(!o.isArray())
			//{
			//	return e.getKey();
			//}
			 
		}
		return null;
	}

	public Object[] getElements(Object arg0) {
		Set set = map.keySet();
 
		Iterator it = set.iterator();
		//p.size();
		JSONKV ret[] = new JSONKV[map.size()];
	    int i= 0;
		while(it.hasNext())
		{ 
			String key = (String)it.next();
			ret[i++] = new JSONKV(key,map.get(key));
		}
		return ret;
	}

	public Object getParent(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasChildren(Object arg0) {
		// TODO Auto-generated method stub

		if (arg0 != null)
		{
				JSONKV e = (JSONKV)arg0;

				if(e.getValue() instanceof String)
				{
					return false;
				}
				if(e.isArrayElement)
				{
					return true;
				}
				if(e.getValue() instanceof JSONObject)
				{
					JSONObject a = (JSONObject)e.getValue();
					if(a.size()==0)
					{
						return false;
					}
				}
				if(e.getValue() instanceof Boolean)
				{
					 
						return false;
					 
				}
				if(e.getValue() instanceof JSONArray)
				{
					JSONArray a = (JSONArray)e.getValue();
					if(a.size() == 0)
					{
						return false;
					}
				}
				return true;
			
		}
		else
		{
			return false;
		}
		
	}

	public static class JSONKV
	{
		String key;
		Object value;
		boolean isArrayElement =false; 
		public JSONKV(String key, Object value)
		{
			this.key = key;
			this.value = value;
		}
		public JSONKV(String key, Object value,boolean isArrayElement)
		{
			this.key = key;
			this.value = value;
			this.isArrayElement = isArrayElement;
		}
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
		
	}
}
