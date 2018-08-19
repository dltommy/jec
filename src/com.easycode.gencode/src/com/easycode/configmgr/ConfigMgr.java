package com.easycode.configmgr;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.easycode.common.FileUtil;
import com.easycode.common.XmlUtil;
import com.easycode.configmgr.model.Config;
import com.easycode.configmgr.model.Config.DB;
import com.easycode.resource.MultLang;

public class ConfigMgr implements IConfigMgr{
	 

	private String filePath;
	protected ConfigMgr(String filePath)
	{
		this.filePath = filePath;
	}
	public void addOrUpdateDBConfig(DB dbconfig, XmlUtil.Callback callback) throws Exception
    {
		File file = new File(this.filePath);
		Document document = null;
		boolean exists = false;
		if (file.exists()) {
			SAXBuilder sb = new SAXBuilder();
			document = sb.build(this.filePath);
			Element root = document.getRootElement();
			Element dbsItem = root.getChild("dbs");
			if (dbsItem != null) {
				List<Element> dbsList = dbsItem.getChildren();

				for (Element db : dbsList) {
					if (db.getAttribute("id").getValue()
							.equals(dbconfig.getId())) {
						exists = true;
						
						//db.getAttribute("type").setValue(
						//		dbconfig.getDbtype());
						db.getAttribute("name").setValue(
								dbconfig.getName());
						
						db.getChild("username").setText(
								dbconfig.getUsername());
						db.getChild("password").setText(
								dbconfig.getPassword());
						db.getChild("driver").setText(
								dbconfig.getDriver());
						db.getChild("url").setText(dbconfig.getUrl());
						break;
					}
				}
				//有dbs节点，无对应db元素

			}
			else//无dbs节点，无对应db元素
			{
				dbsItem = new Element("dbs");
				root.addContent(dbsItem);
				 
			}
			if(!exists)
			{
				Element db = new Element("db");
				dbsItem.addContent(db);
				db.setAttribute(new Attribute("name",dbconfig.getName()));
				//db.setAttribute(new Attribute("type",dbconfig.getDbtype()));
				db.setAttribute(new Attribute("id",UUID.randomUUID().toString()));
				
				Element username = new Element("username");
				username.setText(dbconfig.getUsername());
				db.addContent(username);
				Element password = new Element("password");
				password.setText(dbconfig.getPassword());
				db.addContent(password);
				Element driver = new Element("driver");
				driver.setText(dbconfig.getDriver());
				db.addContent(driver);
				Element url = new Element("url");
				url.setText(dbconfig.getUrl());
				db.addContent(url);
			}
		}
		//this.saveXML(document);
		XmlUtil.saveXML(document, this.filePath,callback);

	}
	public DB readDBConfig(String dbname) throws Exception
 {
		DB ret = null;
		File file = new File(this.filePath);
		Document document = null;
	 
		if (file.exists()) {
			SAXBuilder sb = new SAXBuilder();
			document = sb.build(this.filePath);
			Element root = document.getRootElement();
			Element dbsItem = root.getChild("dbs");
			if (dbsItem != null) {
				List<Element> dbsList = dbsItem.getChildren();
				for (Element db : dbsList) {
					if (db.getAttribute("name").getValue()
							.equals(dbname)) {
						 ret = new DB();
						 ret.setName(dbname);
						 ret.setDbtype(db.getAttributeValue("type"));
						 ret.setId(db.getAttributeValue("id"));
						 ret.setDriver(db.getChildText("driver"));
						 ret.setUsername(db.getChildText("username"));
						 ret.setPassword(db.getChildText("password"));
						 ret.setUrl(db.getChildText("url"));
					 
						 return ret;
					}
				} 
			}
			 
			 
		}
		return ret;

	}
	/**
	 * 存在则直接返回，不存在则新增
	 * @return
	 */
    public Config readOrCreate(XmlUtil.Callback callback) throws Exception
    {
    	  File f = new File(this.filePath);
    	  if(f.exists())
    	  {
    		  return readConfig();
    	  }
    	  else
    	  {
    		  return newConfig(callback);
    	  } 
    }
    private Config newConfig(XmlUtil.Callback callback) throws Exception
    {
    	Config ret = new Config();
    	ret.setLocalTemplatePath("$ECLIPSE_INSTALL_PATH/JEC_TEMPLATE");
    	ret.setSrvUrl(MultLang.getMultLang("code.100"));
    	ret.setUserName("guest");
    	ret.setPassword("guest");
    	ret.setCodeType("JAVA;HTML");
    	
    	
    	ret.setCommonLangProp("");
    	ret.setMultLangFlag("<s:text name=\"[#prefix.#key]\"/>");
    	ret.setLangProp("");
    	ret.setMultLangFilt("");
    	Element config = new Element("config");
		Document document = new Document(config);
		Element srvUrl = new Element("srv_url");
		srvUrl.setText(ret.getSrvUrl());
		config.addContent(srvUrl); 
		
		Element localTemplatePath = new Element("local_template_path");
		localTemplatePath.setText(ret.getLocalTemplatePath());
		config.addContent(localTemplatePath); 

		Element codeTypeEle = new Element("code_type");
		codeTypeEle.setText(ret.getCodeType());
		config.addContent(codeTypeEle); 
		 
		Element langPropEle = new Element("lang_prop");
		langPropEle.setText(ret.getLangProp());
		config.addContent(langPropEle); 
		
		Element commonLangPropEle = new Element("common_lang_prop");
		commonLangPropEle.setText(ret.getCommonLangProp());
		config.addContent(commonLangPropEle);
		
		Element multLangFiltEle = new Element("mult_lang_filt");
		multLangFiltEle.setText(ret.getMultLangFilt());
		config.addContent(multLangFiltEle); 
		
		Element multLangFlagEle = new Element("mult_lang_flag");
		multLangFlagEle.setText(ret.getMultLangFlag());
		config.addContent(multLangFlagEle); 
		
		Element auth = new Element("auth");
		Element userName = new Element("user_name");
		userName.setText(ret.getUserName());
		Element password = new Element("password");
		password.setText(ret.getPassword());
		auth.addContent(userName);
		auth.addContent(password);
		
		config.addContent(auth);
 
		File f1 = new File(this.filePath);

		int pathIndex = this.filePath.lastIndexOf("config.xml");
		FileUtil.createFilePath(this.filePath.substring(0, pathIndex));
		XMLOutputter xo = new XMLOutputter();
		FileWriter fw = new FileWriter(f1);
		xo.output(document, fw);
		fw.close();
		
		
		if(callback != null)
		{
		    callback.doback();
		}
		
		
		return ret;
    }
    private Config readConfig() throws Exception
    {
		SAXBuilder sax = new SAXBuilder();
		Config ret = new Config();
		Document doc = sax.build(new File(this.filePath));
		Element root = doc.getRootElement();
		List<Element> configItems = root.getChildren();
		
		ret.setSrvUrl(root.getChildText("srv_url").trim());
		ret.setLocalTemplatePath(root.getChildTextTrim("local_template_path"));
		Element authEle = root.getChild("auth");
		ret.setUserName(authEle.getChildTextTrim("user_name"));
		ret.setPassword(authEle.getChildTextTrim("password"));
		
		ret.setLangProp(root.getChildTextTrim("lang_prop"));
		ret.setCommonLangProp(root.getChildTextTrim("common_lang_prop"));
		ret.setCodeType(root.getChildTextTrim("code_type"));
		ret.setMultLangFilt(root.getChildTextTrim("mult_lang_filt"));
		ret.setMultLangFlag(root.getChildTextTrim("mult_lang_flag"));
		
		Element dbsEle = root.getChild("dbs");
		if(dbsEle != null)
		{
			List<Element> dbEle = dbsEle.getChildren("db");
			if(dbEle != null && dbEle.size()>0)
			{
				ArrayList<DB> dbList = new ArrayList<DB>();
				for(Element e:dbEle)
				{
					DB db = new DB();
					db.setDbtype(e.getAttributeValue("type"));
					db.setName(e.getAttributeValue("name"));
					db.setDriver(e.getChildTextTrim("driver"));
					db.setUrl(e.getChildTextTrim("url"));
					db.setUsername(e.getChildTextTrim("username"));
					db.setPassword(e.getChildTextTrim("password"));
					dbList.add(db);
				}
				ret.setDbList(dbList);
			}
		}
		 
		return ret;
    }
    public void update(Config config, XmlUtil.Callback callback) throws Exception
    {
    	File file = new File(this.filePath);
		Document document = null;
		  
		if (file.exists()) {
			SAXBuilder sb = new SAXBuilder();
			document = sb.build(this.filePath);
			Element root = document.getRootElement();
			List<Element> configItems = root.getChildren();
			for(Element e:configItems)
			{
				if("srv_url".equals(e.getName()))
				{
					e.setText(config.getSrvUrl().trim());
				}
				else if("auth".equals(e.getName()))
				{
					List<Element> authList = e.getChildren();
					for(Element a:authList)
					{
						if("user_name".equals(a.getName()))
						{
							a.setText(config.getUserName().trim());
						}
						if("password".equals(a.getName()))
						{
							a.setText(config.getPassword().trim()); 
						}
					}
					
				}
				else if("local_template_path".equals(e.getName()))
				{
					e.setText(config.getLocalTemplatePath().trim());
				}
				else if("lang_prop".equals(e.getName()))//废弃
				{
					e.setText(config.getLangProp().trim());
				}
				else if("common_lang_prop".equals(e.getName()))
				{
					e.setText(config.getCommonLangProp().trim());
				}
				else if("code_type".equals(e.getName()))
				{
					e.setText(config.getCodeType().trim());
				}
				else if("mult_lang_filt".equals(e.getName()))
				{
					e.setText(config.getMultLangFilt().trim());
				}
				else if("mult_lang_flag".equals(e.getName()))
				{
					e.setText(config.getMultLangFlag().trim());
				} 
			}
		} else {
			 throw new Exception("模板文件不存在！");
		}
		XmlUtil.saveXML(document,this.filePath,callback);
	  
    }
	 

    public static void main(String arg[])
    {
    	try {
    		IConfigMgr m = ConfigMgrFactory.newByByFilePath("c:\\GenInitConfig.xml");
    		m.readOrCreate(null);
    		DB db = new DB();
    		db.setDbtype("mysql");
    		db.setUrl("url");
    		db.setDriver("d");
    		db.setName("bb");
    		db.setPassword("pwd");
    		db.setUsername("username");
    		 m.addOrUpdateDBConfig(db,null); 
			 m.update(new Config("userName","password","url","location"),null);
			 Config conf = ConfigMgrFactory.newByPrjPath("c:/t").readOrCreate(null);
			 System.err.println(conf.getUserName());
			 System.err.println(conf.getDbList().size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
