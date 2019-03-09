/**
 * 作 者:  
 * 日 期: 2012-4-6
 * 描 叙:
 */
package com.easycode.gencode.core.gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
 

import com.easycode.common.StringUtil;
import com.easycode.gencode.ui.elements.ModelSelect;
import com.easycode.Constants;

/**
 * 功能描叙:
 * 编   码:  
 * 完成时间: 2012-4-6 下午09:15:59
 */
public class RegxUtil
{
	
	public static String getFirstRegFitCtx(String srcCtx, String reg) throws Exception
	{
	     Pattern pat =null;
	     PatternCompiler pc=new Perl5Compiler(); 
	     PatternMatcher pm=new Perl5Matcher();
        try
		{
        	pat =pc.compile(reg, Perl5Compiler.CASE_INSENSITIVE_MASK);
	        PatternMatcherInput fileInput=new PatternMatcherInput(srcCtx);
	        if(pm.contains(fileInput,pat))
	        {
	        	MatchResult rs = pm.getMatch();
	        	return rs.group(1);
	          
	        }
		}
		catch (MalformedPatternException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
			
		}
		catch(Exception e)
		{
			throw e;
		}
    
		return null;
	}
	public static List<String> getWarnErrPatList(String srcCtx, String regx) throws Exception
	{
		List<String> retList = new ArrayList<String>();
	     Pattern pat =null;
	     PatternCompiler pc=new Perl5Compiler(); 
	     PatternMatcher pm=new Perl5Matcher();
        try
		{
        	pat =pc.compile(regx, Perl5Compiler.CASE_INSENSITIVE_MASK);
	        PatternMatcherInput fileInput=new PatternMatcherInput(srcCtx);
	        while(pm.contains(fileInput,pat))
	        {
	        	MatchResult rs = pm.getMatch();
	        	if(rs.group(1) != null)
	        	{
	        		//retList.add(rs.group(3));
	        		if(rs.group(2) != null)
		        	{
		        		retList.add("WARNING:"+rs.group(4));
		        	}
	        		else if(rs.group(3) != null)
	        		{
		        		retList.add("ERR:"+rs.group(4));
	        		}
	        	}
	        	//else
	        	
	          
	        }
		}
		catch (MalformedPatternException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
			
		}
		catch(Exception e)
		{
			throw e;
		}
    
		return retList;
	}
	public static HashMap<String,String> getInsertStringByGroup(String srcCtx, String regx) throws Exception
	{
	    //List<HashMap<String,String>> retList = new ArrayList<HashMap<String,String>> ();
        HashMap<String,String> tMap = new HashMap<String,String>();
		 StringBuffer sb = new StringBuffer();
	     Pattern pat =null;
	     PatternCompiler pc=new Perl5Compiler(); 
	     PatternMatcher pm=new Perl5Matcher();
        try
		{
        	pat =pc.compile(regx, Perl5Compiler.CASE_INSENSITIVE_MASK);
	        PatternMatcherInput fileInput=new PatternMatcherInput(srcCtx);
	        while(pm.contains(fileInput,pat))
	        {

	        	MatchResult rs = pm.getMatch();
	        	String key= Constants.appendCode;
	        	 
	        	if(rs.group(1) != null)
	        	{
	        	    Map<String, String> map = StringUtil.josnToMap("{" + rs.group(1) + "}");
	        	    key = map.get("target");
	        	}
	        	if(rs.group(2) != null)
	        	{
	        		//sb.append(rs.group(group));
	        	    tMap.put(key, rs.group(2));
	        		//sb.append("\n");
	        	}
	        }
		}
		catch (MalformedPatternException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
			
		}
		catch(Exception e)
		{
			throw e;
		}
    
		return tMap;
	}
	public static ModelSelect parseModelSelect(String ctx)
	{
		ModelSelect ret = null;
		try {
			String json  = RegxUtil.getFirstRegFitCtx(ctx, "<TreeNode>(.*?)</TreeNode>");
			if(json == null || "".equals(json))
			{
			    return null;
			}
			Map<String,String> newMap = StringUtil.josnToMap(json);
			if(newMap.size() > 0)
			{
				ret = new ModelSelect();
				ret.setNode(newMap.get("node"));
				ret.setSrc(newMap.get("src"));
				ret.setTemp(newMap.get("temp"));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	public static void main(String arg[])
	{
		 
		
	}

}
