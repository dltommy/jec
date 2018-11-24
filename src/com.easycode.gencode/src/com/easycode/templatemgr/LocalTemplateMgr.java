/**
 * 作 者: dltommy
 * 日 期: 2011-9-25
 * 描 叙:
 */
package com.easycode.templatemgr;

import java.io.File;


import org.eclipse.core.runtime.Platform;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
  
import com.easycode.common.FileUtil;
import com.easycode.common.XmlUtil;
import com.easycode.templatemgr.model.CodegenTemplate;
import com.easycode.templatemgr.model.PageData;
import com.easycode.Constants;

/**
 * 功能描叙: 编 码: dltommy 完成时间: 2011-9-25 上午11:35:04
 */
public class LocalTemplateMgr implements ITemplateMgr
{
 /*
	public static LocalTemplateMgr createByPath(String templateRootPath, String projectPath)
	{
		return new LocalTemplateMgr(templateRootPath,projectPath);
	}
	*/
	private String totalConfigFile = null;
	private String templateFilePath = null;
	
	private final static String TEMPLATE_DIR = File.separator + "easycode_template" + File.separator + "template";
	private final static String TEMPLATE_FILE = File.separator + "easycode_template" + File.separator + "localTemplate.xml";

	 
	public LocalTemplateMgr(String templateRootPath, String projectPath) {
		
		
		 String tmpPath = Platform.getInstallLocation().getURL().getFile();

		 if(tmpPath.startsWith("/"))
	     {
			 tmpPath = tmpPath.substring(1,tmpPath.length());
	     }
		
		
	      String mdlPath = templateRootPath.replace(Constants.MDL_PATH_ECLIPSE, tmpPath);
		  if(projectPath != null && !"".equals(projectPath))
		  {
			 mdlPath = mdlPath.replace(Constants.MDL_PATH_PROJECT, projectPath);
	      }
 
		 
		this.totalConfigFile = mdlPath + TEMPLATE_FILE;
		this.templateFilePath = mdlPath + TEMPLATE_DIR;
	}

	public void deleteTemplate(String templateid) throws Exception {

		File file = new File(totalConfigFile);
		Document document = null;
		 Element itemsEle = null;
		if (file.exists()) {
			SAXBuilder sb = new SAXBuilder();
			document = sb.build(totalConfigFile);
			itemsEle = document.getRootElement();
		} else {
			 //throw new Exception("模板文件不存在！");
			return;
		}
	    List<Element> eleList = itemsEle.getChildren();
	    Element removeTemp = null;
	    for(Element itemEle:eleList)
	    {
	    	if(templateid.equals(itemEle.getAttribute("id").getValue()))
	    	{
	    		removeTemp = itemEle;
	    		break;
	    		 
	    	} 
	    }
	    if(removeTemp != null)
	    {
		    itemsEle.removeContent(removeTemp);
	    }

		  
		//saveXML(document,totalConfigFile);
	    XmlUtil.saveXML(document, totalConfigFile, null); 
		File tempFile = new File(templateFilePath + File.separator  + templateid + ".template");
		if(tempFile.exists())
		{
			tempFile.delete();
		}
		
		 tempFile = new File(templateFilePath + File.separator  + templateid + ".json");
		if(tempFile.exists())
		{
			tempFile.delete();
		}
		
		 tempFile = new File(templateFilePath + File.separator  + templateid + ".an");
		if(tempFile.exists())
		{
			tempFile.delete();
		}
		
	}

	 
	public String serverToLocal(String serverTemplateId, String author, String templateCtx,
			String title, String paras, String codeType, String version,
			  String anno) throws Exception {
		this.deleteTemplate(serverTemplateId);
		return this.addTemplate(serverTemplateId, author, templateCtx, title, paras, codeType, anno);
	 
	}
     
	public String addTemplate(String author, String mudCtx, String title,
			String paras, String codeType, String anno) throws Exception {
		return this.addTemplate(null, author, mudCtx, title, paras, codeType, anno);
	}

	private String addTemplate(String templateId,String author, String mudCtx, String title,
			String paras, String codeType, String anno) throws Exception {
		String id = UUID.randomUUID().toString();
		if(templateId != null && !"".equals(templateId))
		{
			id = templateId;
		}
		File file = new File(totalConfigFile);
		Document document = null;
		Element itemsEle = null;
		if (file.exists()) {
			SAXBuilder sb = new SAXBuilder();
			document = sb.build(totalConfigFile);
			itemsEle = document.getRootElement();
		} else {
			FileUtil.createFilePath(templateFilePath);
			itemsEle = new Element("items");
			document = new Document(itemsEle);
		}
		Element itemEle = new Element("item");
		itemsEle.addContent(itemEle);
		
		Attribute idAttr = new Attribute("id", id);
		itemEle.setAttribute(idAttr);
		Element titleEle = new Element("title");
		titleEle.setText(title);
		itemEle.addContent(titleEle);
		
		Date createDate = new Date();
		Element createdTimeEle = new Element("created_time");
		SimpleDateFormat fd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		createdTimeEle.setText(fd.format(createDate));
		itemEle.addContent(createdTimeEle);


		Element updatedEle = new Element("updated_time"); 
		updatedEle.setText(fd.format(createDate));
		itemEle.addContent(updatedEle);
		
		Element typeEle = new Element("type");
		typeEle.setText(codeType);
		itemEle.addContent(typeEle);

		Element authorEle = new Element("author");
		authorEle.setText(author.trim());
		itemEle.addContent(authorEle);
		
		XmlUtil.saveXML(document, totalConfigFile, null); 
		FileUtil.createFile(templateFilePath + File.separator  + id + ".template", mudCtx,
				true, true);
		FileUtil.createFile(templateFilePath + File.separator  + id + ".json", paras, true,
				true);
		FileUtil.createFile(templateFilePath + File.separator  + id + ".an", anno, true,
				true);

		return id;

	}
	public String updateTemplate(String templateid,  String mudCtx, String title,
			String paras, String codeType, String anno) throws Exception {
		 
		if(templateid == null || "".equals(templateid))
		{
			throw new Exception("templateid不能为空");
		}
		File file = new File(totalConfigFile);
		Document document = null;
		 Element itemsEle = null;
		if (file.exists()) {
			SAXBuilder sb = new SAXBuilder();
			document = sb.build(totalConfigFile);
			itemsEle = document.getRootElement();
		} else {
			 throw new Exception("模板文件不存在！");
		}
	    List<Element> eleList = itemsEle.getChildren();
	  
	    for(Element itemEle:eleList)
	    {
	    	if(templateid.equals(itemEle.getAttribute("id").getValue()))
	    	{
	    		List<Element> speList = itemEle.getChildren();
	    		for(Element e:speList)
	    		{
	    			if("title".equals(e.getName()))
	    			{
	    				e.setText(title.trim());
	    			}
	    			if("updated_time".equals(e.getName()))
	    			{
	    				SimpleDateFormat fd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    				e.setText(fd.format(new Date()));
	    			}
	    			if("type".equals(e.getName()))
	    			{
	    				e.setText(codeType.trim());
	    			}
	    		}
	    		 

	    	}
	    			
	    }
		  
		//saveXML(document,totalConfigFile);
	    XmlUtil.saveXML(document, totalConfigFile, null); 
		FileUtil.createFile(templateFilePath + File.separator  + templateid + ".template", mudCtx,
				true, true);
		FileUtil.createFile(templateFilePath + File.separator  + templateid + ".json", paras, true,
				true);
		FileUtil.createFile(templateFilePath + File.separator  + templateid + ".an", anno, true,
				true);

		return templateid;

	}
	public CodegenTemplate getCodegenTemplate(String templateId, boolean withDetail) throws Exception{

		CodegenTemplate ret = null;
		File file = new File(totalConfigFile);
		Document document = null;
		 Element itemsEle = null;
		if (file.exists()) {
			SAXBuilder sb = new SAXBuilder();
			document = sb.build(totalConfigFile);
			itemsEle = document.getRootElement();
		} else {
			 throw new Exception("模板文件不存在！");
		}
	    List<Element> eleList = itemsEle.getChildren();
	  
	    for(Element itemEle:eleList)
	    {
	    	if(templateId.equals(itemEle.getAttribute("id").getValue()))
	    	{
	    		ret = new CodegenTemplate();
	    		ret.setId(templateId);
	    		List<Element> speList = itemEle.getChildren();
	    		for(Element e:speList)
	    		{
	    			if("title".equals(e.getName()))
	    			{
	    				ret.setTitle(e.getValue());
	    			}
	      			if("created_time".equals(e.getName()))
	    			{
	    				ret.setCreatedTime(e.getValue());
	    			}
	    			if("updated_time".equals(e.getName()))
	    			{ 
	    				ret.setUpdatedTime(e.getValue());
	    			}
	    			if("type".equals(e.getName()))
	    			{
	    				ret.setCodeType(e.getValue());
	    			}
	    			if("author".equals(e.getName()))
	    			{
	    				ret.setAuthor(e.getValue());
	    			}
	  
	    		} 
	    		if(withDetail)
	    		{
		    		ret.setTemplateCtx(FileUtil.readFileCtx(templateFilePath + "/" + templateId + ".template"));
		    		ret.setTemplateAnno(FileUtil.readFileCtx(templateFilePath + "/" + templateId + ".an"));
		    		ret.setParamDesc(FileUtil.readFileCtx(templateFilePath + "/" + templateId + ".json"));
	    		}

	    		break;
	    	}
	    }
		
		return ret;
		 

	}
 
	public PageData<CodegenTemplate> queryPageList(String author,
			String templateId, String title, String mudCtx, String codeType,
			int beginPos, int eachPageSize) throws Exception {
		PageData<CodegenTemplate> ret = new PageData<CodegenTemplate>();
		List<CodegenTemplate> dataList = new ArrayList<CodegenTemplate>();
	 
		File file = new File(totalConfigFile);
		Document document = null;
		 Element itemsEle = null;
		if (file.exists()) {
			SAXBuilder sb = new SAXBuilder();
			document = sb.build(totalConfigFile);
			itemsEle = document.getRootElement();
		} else {
			 //throw new Exception("模板文件不存在！");
		    ret.setDataList(dataList);
		    ret.setRows(0);
			return ret;
		}
	    List<Element> eleList = itemsEle.getChildren();
	    int totalRow=0;
	    for(Element t:eleList)
	    {
	    	String tempId= t.getAttribute("id").getValue();
  
	    	String tempTitle = t.getChild("title").getValue();
	    	String tempAuthor = t.getChild("author").getValue();
	    	String tempType = t.getChild("type").getValue();
	    	
	    	if(!codeType.equals("ALL") && !codeType.equals(tempType))
	    	{
	    	    continue;
	    	}
	    	
	    	if(templateId != null && !"".equals(templateId.trim()))
	    	{ 
	    		if(!templateId.equals(tempId))
	    		{
	    			continue; 
	    		}
	    	}
	    	
	    	if(title != null && !"".equals(title.trim()))
	    	{ 
	    		if(!title.matches(tempTitle))
	    		{
	    			continue; 
	    		}
	    	}
	    	if(author != null && !"".equals(author.trim()))
	    	{ 
	    		if(!tempAuthor.equals(author))
	    		{
	    			continue; 
	    		}
	    	}
	    	totalRow++;
	    	if(totalRow >=beginPos && totalRow<beginPos+eachPageSize)
	    	{
		    	CodegenTemplate temp = new CodegenTemplate();
		    	temp.setAuthor(tempAuthor);
		    	temp.setCodeType(tempType);
		    	temp.setId(tempId);
		    	temp.setTitle(tempTitle);
		    	dataList.add(temp);
	    	}

	    	 
	    }
	    
	    
	    ret.setDataList(dataList);
	    ret.setRows(totalRow);
		return ret;
		 

	}
   
 
 
}
