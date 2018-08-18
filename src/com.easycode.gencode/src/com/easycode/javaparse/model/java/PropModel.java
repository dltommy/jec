/**
 * 作 者: 欧阳超
 * 日 期: 2011-8-12
 * 描 叙:
 */
package com.easycode.javaparse.model.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.easycode.common.BaseObject;
 

/**
 * 功能描叙:
 * 编   码: dltommy
 * 完成时间: 2011-8-12 下午11:01:46
 */
public class PropModel extends BaseObject
{
	public static String REFER_DEFAULT = "auto";
	 
	private String template = null;
    private JavaTypeModel propType;
    private String propName;
    private String colName;
    //private String isParentId;
    private HashMap an = new HashMap();
    //private String memo;

    private List<JavaAn> javaAnList = new ArrayList<JavaAn>();
    
    private String title = null;
    //private String referObj = null;
    private String refer = REFER_DEFAULT;
    //private HashMap<String,Object> userDefine = new HashMap<String,Object>();
    
    /**html查询元素*/
    //public HashMap ui =  new HashMap();
    //public HashMap ds =  new HashMap();
    //public HashMap da =  new HashMap(); 
     
    
    public PropModel(String clsName, String popName)
    {
    	//crud = new String(CRUD);
    	this.propType = JavaTypeModel.createJavaType(clsName);
    	if(clsName.indexOf("[")>-1 || popName.indexOf("[")>-1)
    	{
    		this.propType.setArray(true);
    	}

    	this.propName = popName;
    	this.colName = PropModel.genColName(propName);
    }
    public PropModel(String clsName, String popName, boolean isArray)
    {
        this.propType = JavaTypeModel.createJavaType(clsName,isArray);
  
        this.propName = popName;
        this.colName = PropModel.genColName(propName);
    }
    public void addExtendProp(String key,Object value)
    {
    	this.an.put(key, value);
    }
    public static String genColName(String name)
    {
    	int i = name.length();
    	String ret = "";
    	for(int j =0;j<i;j++)
    	{
    		int ch = name.charAt(j);
    		if(Character.isUpperCase(ch))
    		{
    			ret +="_"+name.substring(j,j+1).toUpperCase();
    		}
    		else
    		{
    			ret += name.substring(j,j+1).toUpperCase();
    		}
    	}
    	return ret;
    }
    /*
    public PropModel(String clzName,String pName, String propValue)
    {
    	this.propType = JavaTypeModel.createJavaType(clzName);
    	this.propName = pName;
    	this.colName = this.genColName(propName);
    	 
    }
    */
    

	public String getPropName()
	{
		return propName;
	}
	public void setPropName(String propName)
	{
		this.propName = propName;
    	this.colName = this.genColName(propName);
	}
	public JavaTypeModel getPropType()
	{
		return propType;
	}
	public void setPropType(JavaTypeModel propType)
	{
		this.propType = propType;
	}
	/*
	public String getPropValue()
	{
		return propValue;
	}
	public void setPropValue(String propValue)
	{
		this.propValue = propValue;
	}
	
	public String getAccessLimit()
	{
		return accessLimit;
	}
	public void setAccessLimit(String accessLimit)
	{
		this.accessLimit = accessLimit;
	}
*/

	public String getColName()
	{
		return this.colName;
	}
	public void setColName(String colName)
	{
		this.colName = colName;
	}
	/*
	public Boolean getIsArray()
	{
		return this.isArray;
	}
	public void setIsArray(Boolean isArray)
	{
		this.isArray = isArray;
	}
	public Boolean getIsList()
	{
		return isList;
	}
	public void setIsList(Boolean isList)
	{
		this.isList = isList;
	}
	
	public String getCheckJs()
	{
		return checkJs;
	}
	public void setCheckJs(String checkJs)
	{
		this.checkJs = checkJs;
	}

	public String getGenCode()
	{
		return genCode;
	}
	public void setGenCode(String genCode)
	{
		this.genCode = genCode;
	}
		*/
	/*
	public String getIsPk()
	{
		return isPk;
	}
	public void setIsPk(String isPk)
	{
		this.isPk = isPk;
	}

	public String getIsPersist()
	{
		return isPersist;
	}
	public void setIsPersist(String isPersist)
	{
		this.isPersist = isPersist;
	}

	public String getUiEvent()
	{
		return uiEvent;
	}
	public void setUiEvent(String uiEvent)
	{
		this.uiEvent = uiEvent;
	}
	*/
 
	/*
	public String getUiTitle()
	{
		return uiTitle;
	}
	public void setUiTitle(String uiTitle)
	{
		this.uiTitle = uiTitle;
	}

	public String getUserDefine()
	{
		return userDefine;
	}
	public void setUserDefine(String userDefine)
	{
		if(userDefine != null && "".equals(userDefine))
		{
			if(userDefine.trim().startsWith("{"))
			{
			    try
			    {
				    SrcUtil.josnToMap(userParam, userDefine);
			    }
			    catch (Exception e)
			    {
				    // TODO Auto-generated catch block
				    e.printStackTrace();
			    }
			}
		}

		this.userDefine = userDefine;
	}
	*/
	/*
	public HashMap<String, Object> getUserDefine()
	{
		return this.userDefine;
	}
	public void setUserDefine(HashMap<String, Object> userDefine)
	{
		this.userDefine = userDefine;
	}
	
	public String getIsParentId()
	{
		return isParentId;
	}
	public void setIsParentId(String isParentId)
	{
		this.isParentId = isParentId;
	}

	public String getNotAdd()
	{
		return notAdd;
	}
	public void setNotAdd(String notAdd)
	{
		this.notAdd = notAdd;
	}
	
	public String getNotNull()
	{
		return notNull;
	}
	public void setNotNull(String notNull)
	{
		this.notNull = notNull;
	}
	*/

	/*
	public String getOrderWay()
	{
		return orderWay;
	}
	public void setOrderWay(String orderWay)
	{
		this.orderWay = orderWay;
	}
	
	public String getGroup()
	{
		return group;
	}
	public void setGroup(String group)
	{
		this.group = group;
	}

	public String getAlias()
	{
		return alias;
	}
	public void setAlias(String alias)
	{
		this.alias = alias;
	}

	public String getCrud()
	{
		return crud;
	}
	public void setCrud(String crud)
	{
		this.crud = crud;
	}

	public String getExtend()
	{
		return extend;
	}
	public void setExtend(String extend)
	{
		this.extend = extend;
	}
	public String getIsUnique()
	{
		return isUnique;
	}
	public void setIsUnique(String isUnique)
	{
		this.isUnique = isUnique;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
		*/
	public HashMap getAn()
	{
		return an;
	}
	public void setAn(HashMap an)
	{
		this.an = an;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getTemplate()
	{
		return template;
	}
	public void setTemplate(String template)
	{
		this.template = template;
	}
  
	public String getRefer()
	{
		return refer;
	}
	public void setRefer(String refer)
	{
		this.refer = refer;
	}
	public List<JavaAn> getJavaAnList() {
		return javaAnList;
	}
	public void setJavaAnList(List<JavaAn> javaAnList) {
		this.javaAnList = javaAnList;
	}
 

}

