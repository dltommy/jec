package com.easycode.configmgr.model;

import java.util.List;

public class Config
{
	private String srvUrl;
	private String localTemplatePath;
	private String userName;
	private String password;
	private String codeType;
	
	private String commonLangProp;
	private String multLangFlag;
	private String langProp;
	private String multLangFilt;
	
	private List<DB> dbList = null;
	public Config()
	{
		
	}
	public Config(String userName,String password,String srvUrl,String localTemplatePath)
	{
		this.userName = userName;
		this.password = password;
		this.srvUrl = srvUrl;
		this.localTemplatePath = localTemplatePath;
	}
	public String getSrvUrl() {
		return srvUrl;
	}
	public void setSrvUrl(String srvUrl) {
		this.srvUrl = srvUrl;
	}
	public String getLocalTemplatePath() {
		return localTemplatePath;
	}
	public void setLocalTemplatePath(String localTemplatePath) {
		this.localTemplatePath = localTemplatePath;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	public String getCommonLangProp() {
		return commonLangProp;
	}
	public void setCommonLangProp(String commonLangProp) {
		this.commonLangProp = commonLangProp;
	}
	public String getMultLangFlag() {
		return multLangFlag;
	}
	public void setMultLangFlag(String multLangFlag) {
		this.multLangFlag = multLangFlag;
	}
	public String getLangProp() {
		return langProp;
	}
	public void setLangProp(String langProp) {
		this.langProp = langProp;
	}
	public String getMultLangFilt() {
		return multLangFilt;
	}
	public void setMultLangFilt(String multLangFilt) {
		this.multLangFilt = multLangFilt;
	}

	public List<DB> getDbList() {
		return dbList;
	}
	public void setDbList(List<DB> dbList) {
		this.dbList = dbList;
	}

	public static class DB
	{
		String username;
		String password;
		String driver;
		String url;
		String name;
		String dbtype;
		String id;
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getDriver() {
			return driver;
		}
		public void setDriver(String driver) {
			this.driver = driver;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getDbtype() {
			return dbtype;
		}
		public void setDbtype(String dbtype) {
			this.dbtype = dbtype;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
	}
}