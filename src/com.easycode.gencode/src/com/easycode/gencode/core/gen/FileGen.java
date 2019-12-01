/**
 * 作 者: dltommy
 * 日 期: 2011-11-9
 * 描 叙:
 */
package com.easycode.gencode.core.gen;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
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
import com.easycode.gencode.core.ftlmethod.FiltMatch;
import com.easycode.gencode.core.ftlmethod.FiltNotMatch;
import com.easycode.gencode.core.ftlmethod.NewInstanceStr;
 
import com.easycode.gencode.core.ftlmethod.QueryByPath;
import com.easycode.gencode.ui.elements.CodeTreeCheckBox;
import com.easycode.gencode.ui.main.MainUi;
import com.easycode.templatemgr.LocalTemplateMgr;
import com.easycode.templatemgr.RpcFactory;

import com.easycode.templatemgr.model.CodegenTemplate;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 功能描叙: 编 码: dltommy 完成时间: 2011-11-9 下午10:45:12
 */
public class FileGen
{
	private HashMap<String, CodeTreeCheckBox> checkedBoxMap = null;
	private List<String> checkList = null;
    
	private Map<String,Object> paraObj = null;
	private HashMap<String,String> configMap = new HashMap<String, String>();
	private Map<String, String> defineObj = null;
	private String ftlSrc = null;

	private String memo = null;

	public void setPreparedPara(String paraObj) throws Exception
	{
		Map<String,Object> map = new HashMap<String, Object>();
		map = StringUtil.josnToMap(paraObj);
		
		this.paraObj = map;
	}
	/**
	 * 用户参数
	 * @param definePara
	 * @throws Exception
	 */
	public void setDefinePara(String defineParaSrc) throws Exception
	{
		if(defineParaSrc != null)
		{
			if(!"".equals(defineParaSrc.trim()))
			{
			    String definePara = defineParaSrc.replaceAll("/\\*[\\s|\\S]*?\\*/", "");
				try
				{
				Map<String,String> map = StringUtil.josnToMap(definePara);
				this.defineObj = map;
				
				}
				catch(Exception e)
				{
					throw new Exception("用户参数格式异常:"+definePara+"\n"+e.getMessage());
					//e.printStackTrace();
				}
			}
		}

	}
	private FileGen()
	{
	
	}

 
	public FileGen(String paraObj,String definePara,String ftlSrc, List<String> checkList) throws Exception
	{
	    this();
	    this.initParam(paraObj, definePara, ftlSrc,checkList);
	}
	 public static FileGen newInstancex(String paraObj,String definePara,String ftlSrc, List<String> checkList) throws Exception
    {
	     FileGen instance = new FileGen();
        Map<String, Object> map = new HashMap<String, Object>();
        map = StringUtil.josnToMap(paraObj);
        instance.paraObj = map;

        if (definePara != null)
        {
            if (!"".equals(definePara.trim()))
            {
                try
                {
                    Map<String, String> usermap = new HashMap<String, String>();
                    usermap = StringUtil.josnToMap(definePara);
                    instance.defineObj = usermap;

                }
                catch (Exception e)
                {
                    throw new Exception("用户参数格式异常:" + definePara + "\n"
                            + e.getMessage());
                    // e.printStackTrace();
                }
            }
        }
        instance.checkList = checkList;
        instance.ftlSrc = ftlSrc;
        return instance;
    }
	 public void initParam(String paraObj,String defineParaSrc,String ftlSrc, List<String> checkList) throws Exception
    {
         Map<String, Object> map = new HashMap<String, Object>();
  
         if(paraObj != null && !"".equals(paraObj.trim()))
         {
             try
             {
            	 map = StringUtil.josnToMap(paraObj);
             }
             catch (Exception e)
             {
                 throw new Exception("异常:" + paraObj + "\n" + e.getMessage());
             }
         }
         this.paraObj = map;

         if (defineParaSrc != null && !"".equals(defineParaSrc.trim()))
         {
             String definePara = defineParaSrc.replaceAll("/\\*[\\s|\\S]*?\\*/", "");
             try
             {
                 Map<String, String> usermap = new HashMap<String, String>();
                 usermap = StringUtil.josnToMap(definePara);
                 this.defineObj = usermap;

             }
             catch (Exception e)
             {
                 throw new Exception("异常:" + definePara + "\n" + e.getMessage());
                 // e.printStackTrace();
             }

         }
         this.ftlSrc = ftlSrc;
 
        this.checkList = checkList;
 
    }
 
	/**
	 * 
	 * 功   能:
	 * 实现流程:
	 * @param ftlSrc
	 * @param ws
	 * @param mdlPath
	 * @param locale
	 * @param rebuildNode 是否需要 重新初始接点,如果该接点重新初始化了，则树控件要重新设置
	 * @throws Exception
	 */
	public void updateTreeNode(String ws, String mdlPath, boolean locale, boolean rebuildNode, String projectPath) throws Exception
	{
		if(rebuildNode)
		{
			checkedBoxMap = null;
		}

		// 解析json串
		if (ftlSrc == null)
		{
			ftlSrc = "";
		}
		try
		{
			 //String json = RegxUtil.getFirstRegFitCtx(this.ftlSrc,
			 //		"([\\s|\\S]*?</ftl_head>)");
			 //json = initStrCtx(json, ws, mdlPath, locale, projectPath);
 
			 String json  = this.ftlSrc;
			if (json != null && !"".equals(json))
			{

				
				json = this.initCtrl(json);// this.paraObj
				 
				if (rebuildNode)
				{
					try
					{
 
						checkedBoxMap = new CodeTreeCheckBox()
								.getThreadNode(json);
					}
					catch (Exception e)
					{
						e.printStackTrace();
						if (checkedBoxMap != null)
						{
							checkedBoxMap.clear();
						}
						throw e;
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			if(checkedBoxMap != null)
			{
			    checkedBoxMap.clear();
			}
			throw e;
		}
	}
	
	public void init(String ws, String mdlPath, boolean locale, String projectPath) throws Exception
	{
		this.ftlSrc = this.initStrCtx(this.ftlSrc,  ws,  mdlPath,  locale, projectPath);
	}
	private List<String> headWarnErrList = new ArrayList<String>();
	/**
	 * 
	 * 功   能:
	 * 实现流程:
	 * @param ftlSrc
	 * @param ws
	 * @param mdlPath
	 * @param locale
	 * @throws Exception
	 */
	private static String initStrCtx(String ftlSrc, String ws, String mdlPath, boolean locale,String projectPath) throws Exception
	{
		if(ftlSrc == null)
		{
			return null;
		}
		boolean cycle = true;
		int cycleCunt = 0;
		String retStr = new String(ftlSrc);
		while(cycle)
		{
			Object parseInclude[] = parseInclude(retStr, ws, locale, mdlPath,projectPath);
			cycle = (Boolean)parseInclude[0];
			retStr = (String)parseInclude[1];
			cycleCunt ++;
			if(cycleCunt >= 3)
			{
				break;
			}
		}
		return retStr;
	}


	private static Object[] parseInclude(String ctx, String wasUrl, boolean locale, String mdlPath,String projectPath)
	{
		LocalTemplateMgr tempMgr = new LocalTemplateMgr(mdlPath,projectPath);
		String includeRegx = "(<ftl_include[\\s]*?([r|l|\\s]id)[\\s]*?=[\\s]*?[\"|']([^/^>]*?)[\"|'][^/]*?/[^>]*?>)";
		 PatternCompiler pc = new Perl5Compiler();
		 PatternMatcher pm = new Perl5Matcher();
		 Pattern pattFile = null;
		try
		{
			pattFile = pc.compile(includeRegx, Perl5Compiler.CASE_INSENSITIVE_MASK);
		}
		catch (MalformedPatternException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PatternMatcherInput fileInput = new PatternMatcherInput(ctx);
		boolean contain = false;
		HashMap<String, String> replaceMap = new HashMap<String, String>();
		while(pm.contains(fileInput, pattFile))
		{
			contain = true;
			//读取
			MatchResult rs = pm.getMatch();
			String idStr = rs.group(2).trim();
			String id = rs.group(3).trim();
			try
			{
				CodegenTemplate md = null;
				if("id".equalsIgnoreCase(idStr))
				{
					if (locale)
					{
						md = tempMgr.getCodegenTemplate(id, true);//.getMudByMudId(id);
					}
					else
					{

						md = RpcFactory.httpSrv(wasUrl).getCodegenMudlsByMudId("jec",
								id);
					}
				}
				else if("lid".equalsIgnoreCase(idStr))
				{
					try
					{
					    md = tempMgr.getCodegenTemplate(id,true);//.getMudByMudId(id);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				else if("rid".equalsIgnoreCase(idStr))
				{
					try
					{
					    md = RpcFactory.httpSrv(wasUrl).getCodegenMudlsByMudId("jec",
							id);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				if(md == null)
				{
					String warn = "读取模板异常("+idStr+"='"+id+"')";
					replaceMap.put(rs.group(1), "<ftl_doc>" + warn + "</ftl_doc>" + warn);
				}
				else
				{
				    String mdCtx = md.getTemplateCtx();
				    String newCtx = mdCtx.replaceAll("<ftl_head>([\\s|\\S]+?)</ftl_head>", " ");
				    replaceMap.put(rs.group(1), newCtx);
				}
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		String retStr = new String(ctx);
		if(replaceMap.size() > 0)
		{
			Iterator<String> keyIt = replaceMap.keySet().iterator();
			while(keyIt.hasNext())
			{
				String key = keyIt.next();
				retStr = retStr.replaceAll(key, replaceMap.get(key));
			}
		}
		Object[] retObj = new Object[2];
		retObj[0] = contain;
		retObj[1]= retStr;
		return retObj;
	}
	 

	private String initCtrl(String headSrc) throws Exception
	{

		StringTemplateLoader strLoader = new StringTemplateLoader();
		strLoader.putTemplate("t1", headSrc);
		Configuration conf = new Configuration();
		conf.setNumberFormat("0.#");
		conf.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
		conf.setTemplateLoader(strLoader);
		conf.setClassicCompatible(true);
		conf.setEncoding(Locale.CHINA, "UTF-8");
		
		

		
		HashMap<String, Object> root = new HashMap<String, Object>();

		root.put("config", configMap);

		if (checkList != null)
		{
			//root.put("selectMethList", checkList);
			root.put("checkedList", checkList);
		}
		if(this.paraObj != null)
		{
			root.put("sys", this.paraObj);
		}

		if(this.defineObj != null)
		{
			root.put("user", this.defineObj);
		} 
		//root.put("propsFit", new PropsFit(root));
		//root.put("propsNotFit", new PropsNotFit(root));
		//root.put("getPropValue", new GetPropValue());
		root.put("filtMatch", new FiltMatch());
		root.put("filtNotMatch", new FiltNotMatch());
		root.put("queryByPath", new QueryByPath());
	    root.put("newInstanceStr", new NewInstanceStr(root));
		StringWriter sw = new StringWriter();
 
		Template temp = conf.getTemplate("t1");
			// temp.process(root, sw);
	    temp.process(root, sw);
 
		// System.out.println("生成编码内容:" + sw.toString());
		String json = sw.toString();
		
		try
		{
			json = RegxUtil.getFirstRegFitCtx(json, "<ftl_head>([\\s|\\S]*?)</ftl_head>");
		}
		catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "";
		}
		
		if(json == null || "".equals(json))
		{
		    return "";
		}
		try
		{
			headWarnErrList = RegxUtil.getWarnErrPatList(json, MainUi.errWarnRegx);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		json = json.replaceAll("<ftl_doc>[\\s|\\S]*?</ftl_doc>", "");
		json = json.replaceAll("<ftl_warn>[\\s|\\S]*?</ftl_warn>", "");
		json = json.replaceAll("<ftl_err>[\\s|\\S]*?</ftl_err>", "");
		json = json.replaceAll("[\t\n\r]", "");
		return json;//sw.toString();
		
		
	}
	public String getMemo()
	{
		if (this.memo == null)
		{
			return "";
		}
		return this.memo;
	}

	public String genSrc() throws Exception
	{
		StringTemplateLoader strLoader = new StringTemplateLoader();
		strLoader.putTemplate("t1", this.ftlSrc);
		Configuration conf = new Configuration();
		conf.setNumberFormat("0.#");
		conf.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
		conf.setTemplateLoader(strLoader);
		conf.setClassicCompatible(true);
		conf.setEncoding(Locale.CHINA, "UTF-8");
		HashMap<String, Object> root = new HashMap<String, Object>();
		root.put("config", configMap);
		
		if (checkList != null)
		{
			root.put("checkedList", checkList);
		}
		if(this.paraObj != null)
		{
			root.put("sys", this.paraObj);
		}

		if(this.defineObj != null)
		{
			root.put("user", this.defineObj);
		}
		//root.put("propsFit", new PropsFit(root));
		//root.put("propsNotFit", new PropsNotFit(root));
		//root.put("getPropValue", new GetPropValue());
		root.put("filtMatch", new FiltMatch());
		root.put("filtNotMatch", new FiltNotMatch());
		root.put("queryByPath", new QueryByPath());
	    root.put("newInstanceStr", new NewInstanceStr(root));
		StringWriter sw = new StringWriter();
		try
		{
			Template temp = conf.getTemplate("t1");
			// temp.process(root, sw);
			temp.process(root, sw);
		}
		catch (TemplateException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		 

		return sw.toString();
	}  
	public List<String> getCheckList()
	{
		return checkList;
	}

	public void setCheckList(List<String> checkList)
	{
		this.checkList = checkList;
	}
/*
	public static class TreeSelBoxNode
	{

		
		private String nodeName = null;
		private String key = null;
		private List<CheckBox> childList = null;

		public String getKey()
		{
			return this.key;
		}
		public TreeSelBoxNode()
			{
               
			}

		public void setKey(String key)
		{
			this.key = key;
		}
		public TreeSelBoxNode(String nodeName, List<CheckBox> childList, String key)
			{
				this.nodeName = nodeName;
				this.childList = childList;
				this.key = java.util.UUID.randomUUID().toString();
			}

		public List<CheckBox> getChildList()
		{
			return childList;
		}

		public void setChildList(List<CheckBox> childList)
		{
			this.childList = childList;
		}

		public String getNodeName()
		{
			return nodeName;
		}

		public void setNodeName(String nodeName)
		{
			this.nodeName = nodeName;
		}

		public String toString()
		{
			return this.nodeName;
		}
	}
    */
	public void setMemo(String memo)
	{
		this.memo = memo;
	}


	public HashMap<String, CodeTreeCheckBox> getNodeTreeMap(String key)
	{
		HashMap<String,CodeTreeCheckBox> retMap = new HashMap<String,CodeTreeCheckBox>();
		CodeTreeCheckBox node = checkedBoxMap.get(key);
		if(node == null)
		{
			return retMap;
		}
		 
		retMap.put(key, node);
		while(node.getParent() != null)
		{
			CodeTreeCheckBox parent = node.getParent(); 
			List<CodeTreeCheckBox> childList = new ArrayList<CodeTreeCheckBox>();
			
			childList.add(node);
			
			parent.setChildList(childList);
			retMap.put(parent.getKey(), parent);
			node = node.getParent();
		}
		return retMap;
	}
	
	public HashMap<String, CodeTreeCheckBox> getCheckedBoxMap()
	{
		return checkedBoxMap;
	}
	public void setCheckedBoxMap(HashMap<String, CodeTreeCheckBox> checkedBoxMap)
	{
		this.checkedBoxMap = checkedBoxMap;
	}
	public HashMap<String, String> getConfigMap()
	{
		return configMap;
	}
	public void setConfigMap(HashMap<String, String> configMap)
	{
		this.configMap = configMap;
	}
	public List<String> getHeadWarnErrList()
	{
		return headWarnErrList;
	}
	public void setHeadWarnErrList(List<String> headWarnErrList)
	{
		this.headWarnErrList = headWarnErrList;
	}
	public String getFtlSrc()
	{
		return ftlSrc;
	}
	public void setFtlSrc(String ftlSrc)
	{
		this.ftlSrc = ftlSrc;
	}
}
