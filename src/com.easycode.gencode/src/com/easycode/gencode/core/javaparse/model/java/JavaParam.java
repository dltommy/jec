package com.easycode.gencode.core.javaparse.model.java;

import java.util.ArrayList;
import java.util.List;

public class JavaParam
{
    private String paramName = null;
    private JavaTypeModel type = null;
    private List<JavaAn> javaAnList = new ArrayList<JavaAn>();
    public JavaParam(String paramName,String className,boolean isArray)
    {
        this.paramName = paramName;
        this.type = JavaTypeModel.createJavaType(className, isArray);
        
    }
    public JavaParam(String paramName, JavaTypeModel type)
    {
        this.paramName = paramName;
        this.type = type;
    }
    public String getParamName()
    {
        return paramName;
    }
    public void setParamName(String paramName)
    {
        this.paramName = paramName;
    }
    public JavaTypeModel getType()
    {
        return type;
    }
    public void setType(JavaTypeModel type)
    {
        this.type = type;
    }
    public List<JavaAn> getJavaAnList()
    {
        return javaAnList;
    }
    public void setJavaAnList(List<JavaAn> javaAnList)
    {
        this.javaAnList = javaAnList;
    }
    
}
