package com.easycode.gencode.core.javaparse.model.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.easycode.common.BaseObject;

public class JavaMethodModel   extends BaseObject
{
    private String accessLimit = null;
    private String methodName = null;
    //private String methodDesc = null;
    private String[] exceptions = null;
    private JavaTypeModel returnType = null;
    private JavaParam[] paramArray = null;
    private String title = null;

    private List<JavaAn> javaAnList = new ArrayList<JavaAn>();
    private HashMap an = new HashMap();
 
     
    // interface,method
    
    private String type = null;
    
    public JavaMethodModel()
    {
        
    }
 
    
    public String getType()
    {
        return type;
    }
    public void setType(String type)
    {
        this.type = type;
    }
    public String getAccessLimit()
    {
        return accessLimit;
    }
    public void setAccessLimit(String accessLimit)
    {
        this.accessLimit = accessLimit;
    }
 
    public JavaParam[] getParamArray()
    {
        return paramArray;
    }
    public void setParamArray(JavaParam[] paramArray)
    {
        this.paramArray = paramArray;
    }
    public String getMethodName()
    {
        return methodName;
    }
    public void setMethodName(String methodName)
    {
        this.methodName = methodName;
    }
    public JavaTypeModel getReturnType()
    {
        return returnType;
    }
    public void setReturnType(JavaTypeModel returnType)
    {
        this.returnType = returnType;
    }


    public String[] getExceptions()
    {
        return exceptions;
    }


    public void setExceptions(String[] exceptions)
    {
        this.exceptions = exceptions;
    }


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

    public void addExtendProp(String key,Object value)
    {
        this.an.put(key, value);
    }

/*
	public String getMethodDesc() {
		return methodDesc;
	}


	public void setMethodDesc(String methodDesc) {
		this.methodDesc = methodDesc;
	}
*/

	public List<JavaAn> getJavaAnList() {
		return javaAnList;
	}


	public void setJavaAnList(List<JavaAn> javaAnList) {
		this.javaAnList = javaAnList;
	}

 
    
}
