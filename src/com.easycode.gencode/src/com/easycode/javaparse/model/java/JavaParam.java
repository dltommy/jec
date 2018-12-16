package com.easycode.javaparse.model.java;

public class JavaParam
{
    private String paramName = null;
    private JavaTypeModel type = null;
    public JavaParam(String paramName,String className,boolean isArray)
    {
        this.paramName = paramName;
        this.type = JavaTypeModel.createJavaType(className, isArray);
        
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
    
}
