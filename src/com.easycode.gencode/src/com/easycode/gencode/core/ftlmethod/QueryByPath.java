package com.easycode.gencode.core.ftlmethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import freemarker.template.SimpleHash;
import freemarker.template.SimpleCollection;
import freemarker.template.SimpleSequence;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateModelIterator;

//$(valueOfKey("{'a':'b'})
public class QueryByPath implements TemplateMethodModelEx {

 	public QueryByPath() {
	}

	public Object exec(List args) throws TemplateModelException {
		Object ret = null;
		Object retObj = null;

		if (args.size() <= 1) {
			throw  new TemplateModelException("参数个数异常！");
		} else {
			//List<Object> resultList = new ArrayList<Object>();
			ret = args.get(0);
			String params = String.valueOf(args.get(1));
			String matchs[] = params.split("\\.|/");
 
			ArrayList<String> condition = new ArrayList<String>();
			for(String m:matchs)
			{
				if("".equals(m.trim()))
				{
					continue;
				}
				condition.add(m);
			}
			List<Object> obj = parse(ret,condition);
			 
	         if(obj.size()>0)
	         {
	        	 return  obj.get(0);
	         }
	         else
	         {
	        	 return  null;
	         }
			
		}
	
	}
	
	private List<Object> parse(Object obj,List<String> match) throws TemplateModelException
	{
		List<Object> retList = new ArrayList<Object>();
		if(match.size() >=1)
		{
			if(obj == null)
			{
				 
			}
			else if(obj instanceof SimpleHash )
			{
				SimpleHash retHash = (SimpleHash)obj;
 
				 
				retList.addAll(parse(retHash.get(match.get(0)),match.subList(1, match.size())));
			}
			else if(obj instanceof SimpleSequence)
			{
				SimpleSequence seq = (SimpleSequence)obj;
				for(int i=0;i<seq.size();i++)
				{
					Object temp = seq.get(i);
					if(temp instanceof SimpleHash)
					{
						SimpleHash tHash = (SimpleHash)temp;
						retList.addAll(parse(tHash.get(match.get(0)),match.subList(1, match.size())));
					}
					
				}
			}
			else
			{
 
			}
 
		}
		else
		{ 
			retList.add(obj);
		}
 
		return retList;
	}
	 
}
