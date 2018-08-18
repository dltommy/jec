/**
 * 作 者: dltommy
 * 日 期: 2011-11-21
 * 描 叙:
 */
package com.easycode.templatemgr;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
 
import com.easycode.templatemgr.util.SrcUtil;

/**
 * 功能描叙:
 * 编   码: 
 * 完成时间: 2011-11-21 下午08:33:04
 */
public class FileGenModel
{
    private String src = null;
    private List<FileModel> fileList = new ArrayList<FileModel>();
    private PatternCompiler pc=new Perl5Compiler(); 
 
    private String 	rexFile=".*?<ftl_file[\\s]+?([^>]+?)>([\\s|\\S|.]*?)</ftl_file>.*?";
    private PatternMatcher pm=new Perl5Matcher();
    private Pattern pattFile =null;

    
    public FileGenModel(String src) throws Exception
    {
    	this.src =src;
        try
		{
			pattFile =pc.compile(rexFile, Perl5Compiler.CASE_INSENSITIVE_MASK);
	        PatternMatcherInput fileInput=new PatternMatcherInput(this.src);
	        while(pm.contains(fileInput,pattFile))
	        {
	        	MatchResult rs = pm.getMatch();
	        	
	            this.fileList.add(new FileModel(rs.group(1).trim(),rs.group(2).trim()));
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
    }

    //生成预浏览
    public String genViewSrc()
    {
    	//String rootSrc = this.getProdSrc();
    	StringBuffer sb = new StringBuffer(this.getProdSrc());
    	if(this.fileList != null)
    	{
    		for(FileModel f:fileList)
    		{
    			sb.append("\n【"+f.getFilePath()+"】\n");
    			sb.append(f.getFileCtx());
    		}
    	}
    	
    	return sb.toString();
    }
    
    public String getProdSrc()
    {
        return this.src.replaceAll("<ftl_file[\\s|\\S|.]+?>[\\s|\\S|.]*?</ftl_file>", "");	
    }
    public static class FileModel
    {
    	private String display = null;
    	private Integer index = null;
        private String filePath = null;
        private String fileCtx = null;
        private String name=null;
        private String anchor = null;
        private String anchorTip = null;
        private String title = null;
        private String memo = null;
 
        public String getAnchor()
		{
			return anchor;
		}
		public void setAnchor(String anchor)
		{
			this.anchor = anchor;
		}
		public String getAnchorTip()
		{
			return anchorTip;
		}
		public void setAnchorTip(String anchorTip)
		{
			this.anchorTip = anchorTip;
		}
		public String getName()
		{
			return name;
		}
		public void setName(String name)
		{
			this.name = name;
		}
		public String getTitle()
		{
			return title;
		}
		public void setTitle(String title)
		{
			this.title = title;
		}
		public String getMemo()
		{
			return memo;
		}
		public void setMemo(String memo)
		{
			this.memo = memo;
		}
		public FileModel(String propStr, String fileCtx)
        {
			HashMap<String, String> propMap = new HashMap<String, String>();
			String json = propStr.trim();
			json = json.replaceAll("[\n\r\f]", "");

			String reg = "[\\s]+[a-z|A-Z]+?[\\s]*=";
			try
			{
				String split[] = json.split(reg);
			 
				if(split != null)
				{
				    for(String a:split)
				    { 
				    	//System.err.println(a);
				    	json = json.replace(a, a+",");
				    }
				}
                if(json.startsWith(","))
                {
                	json = json.substring(1);
                }
				SrcUtil.appendToMap(propMap, "{" + json.trim() + "}");
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	this.filePath = propMap.get("path");
        	this.anchor = propMap.get("anchor");
        	this.name = propMap.get("name");
        	this.anchorTip = propMap.get("anchorTip");
        	this.title = propMap.get("title");
        	this.memo = propMap.get("memo");
        	this.display=propMap.get("display");
        	 
        	this.fileCtx = fileCtx;
        }
		public String getFileCtx()
		{
			return fileCtx;
		}

		public void setFileCtx(String fileCtx)
		{
			this.fileCtx = fileCtx;
		}

		public String getFilePath()
		{
			return filePath;
		}

		public void setFilePath(String filePath)
		{
			this.filePath = filePath;
		}
		public Integer getIndex()
		{
			return index;
		}
		public void setIndex(Integer index)
		{
			this.index = index;
		}
		public String getDisplay()
		{
			return display;
		}
		public void setDisplay(String display)
		{
			this.display = display;
		}
    }


	public List<FileModel> getFileList()
	{
		return fileList;
	}


	public void setFileList(List<FileModel> fileList)
	{
		this.fileList = fileList;
	}


	public String getSrc()
	{
		return src;
	}


	public void setSrc(String src)
	{
		this.src = src;
	}

	public static void main(String arg[])
	{ 
	    
	}
}

