/**
 * 作 者: dltommy
 * 日 期: 2011-8-13
 * 描 叙:
 */
package com.easycode.gencode.core.javaparse.model.java;

import java.util.HashMap;


/**
 * 功能描叙:
 * 编   码: dltommy
 * 完成时间: 2011-8-13 下午09:49:24
 */
public class JavaTypeModel extends CommonType implements Cloneable
{
    public static Boolean ARRAY = true;
    public static Boolean NOT_ARRAY = false;
	
	private boolean isArray;
    private String clzName;
    private String typeName;
    private String pkgName;

    private String accessLimit = null;

    private String[] generic = null; 
    private Boolean isObject = false;
    private static HashMap<String,JavaTypeModel> BASE_TYPE = new  HashMap<String,JavaTypeModel>();
    
    static
    {
    	BASE_TYPE.put("String", new JavaTypeModel("java.lang.String"));
    	
    	BASE_TYPE.put("Integer", new JavaTypeModel("java.lang.Integer"));
    	BASE_TYPE.put("int",  new JavaTypeModel("java.lang.Integer"));

    	BASE_TYPE.put("Long",  new JavaTypeModel("java.lang.Long"));
    	BASE_TYPE.put("long",  new JavaTypeModel("java.lang.Long"));
    	
    	BASE_TYPE.put("float", new JavaTypeModel("java.lang.Float"));
    	BASE_TYPE.put("Float", new JavaTypeModel("java.lang.Float"));
    	
    	BASE_TYPE.put("Double", new JavaTypeModel("java.lang.Double"));
    	BASE_TYPE.put("double", new JavaTypeModel("java.lang.Double"));
    	
    	BASE_TYPE.put("byte", new JavaTypeModel("java.lang.Byte"));
    	BASE_TYPE.put("Byte", new JavaTypeModel("java.lang.Byte"));
    
        BASE_TYPE.put("Character", new JavaTypeModel("java.lang.Character"));
        BASE_TYPE.put("char", new JavaTypeModel("java.lang.Character"));
        
    	BASE_TYPE.put("boolean", new JavaTypeModel("java.lang.Boolean"));
    	BASE_TYPE.put("Boolean", new JavaTypeModel("java.lang.Boolean"));
    	
    	BASE_TYPE.put("List", new JavaTypeModel("java.util.List"));
    	
    	BASE_TYPE.put("Date", new JavaTypeModel("java.util.Date"));
        BASE_TYPE.put("HashMap", new JavaTypeModel("java.util.HashMap"));
    }
    private JavaTypeModel(String fullClzName)
    {
    	this(fullClzName,false);
    }
    private JavaTypeModel(String fullClzName, boolean isArray)
    {
    	this.clzName = fullClzName.substring(fullClzName.lastIndexOf(".")+1);
 
    	this.isArray = isArray;
    	if(this.clzName.indexOf("[")>-1)
    	{
    		this.isArray = true;
    	}
     
    	this.typeName = getTypeClsName(this.clzName);
    	
    	
    	int pos = fullClzName.lastIndexOf(".");
    	if(pos == -1)
    	{
    		this.pkgName = "";
    	}
    	else
    	{
    		String temp = fullClzName.substring(0,fullClzName.lastIndexOf("."));
        	this.pkgName = temp;
    	}
    }
 
    public static JavaTypeModel createJavaType(String clsName)
    {
    	return createJavaType(clsName,false);
    }
  
    public static JavaTypeModel createJavaType(String clsName, boolean isArray)
    {
        if(clsName != null && clsName.indexOf(".")>-1)
        {
            return new JavaTypeModel(clsName,isArray);
        }
    	JavaTypeModel m = BASE_TYPE.get(clsName);
    	if(m == null)
    	{
    		return new JavaTypeModel(clsName,isArray);
    	}
    	else
    	{
    		try
			{
    		    JavaTypeModel ret = (JavaTypeModel)m.clone();
    		    ret.setArray(isArray);
				return ret;
			}
			catch (CloneNotSupportedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
    	}
    }

	public String getClzName()
	{
		return clzName;
	}
	public void setClzName(String clzName)
	{
		this.clzName = clzName;
	}
	public String getPkgName()
	{
		return pkgName;
	}
	public void setPkgName(String pkgName)
	{
		this.pkgName = pkgName;
	}
	
    public static void main(String arg[])
    {
    	Integer l = null;
    	JavaTypeModel m = JavaTypeModel.createJavaType("java.sql.Date");// m = new JavaTypeModel("java.lang.Integer");
 
 
    }
 
	public Boolean getIsObject()
	{
		return isObject;
	}
	public void setIsObject(Boolean isObject)
	{
		this.isObject = isObject;
	}
	public String getAccessLimit()
	{
		return accessLimit;
	}
	public void setAccessLimit(String accessLimit)
	{
		this.accessLimit = accessLimit;
	}
	public boolean isArray()
	{
		return isArray;
	}
	public void setArray(boolean isArray)
	{
		this.isArray = isArray;
	}
	
	public boolean checkIsObject()
	{
        if (this.getPkgName().equals("java.lang")
                || this.getPkgName().equals("java.util"))
        {
            return false;
        }
        return true;
	}
    public String[] getGeneric()
    {
        return generic;
    }
    public void setGeneric(String[] generic)
    {
        this.generic = generic;
    }
    public String getTypeName()
    {
        return typeName;
    }
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }
 
  public static String getTypeClsName(String propClzName){
        
        // 泛型
        if (propClzName.indexOf("<") > -1)
        { 
           return propClzName.substring(0,propClzName.indexOf("<"));
        }
        if (propClzName.indexOf("[") > -1)
        {
            String referClz = propClzName.substring(0,
                    propClzName.length() - 2);
            propClzName = referClz;
        } 
        
        if (propClzName.indexOf("[") > -1)
        {
            String referClz = propClzName.substring(0,
                    propClzName.length() - 2);
            propClzName = referClz;
        } 
        return propClzName;
    
    }
}

