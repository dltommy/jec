package com.easycode.javaparse.model;

import com.easycode.javaparse.model.java.JavaMethodModel;

 
 

public class AnnoMethodModel implements IAnnoModel
{

    private String anno;

    private JavaMethodModel method = null;
    private Range annoRange = null;
    private Range codeRange = null;
    public String getAnno()
    {
        return anno;
    }
    public void setAnno(String anno)
    {
        this.anno = anno;
    }

    public JavaMethodModel getMethod()
    {
        return method;
    }
    public void setMethod(JavaMethodModel method)
    {
        this.method = method;
    }
    public Range getAnnoRange()
    {
        return annoRange;
    }
    public void setAnnoRange(Range annoRange)
    {
        this.annoRange = annoRange;
    }
    public Range getCodeRange()
    {
        return codeRange;
    }
    public void setCodeRange(Range codeRange)
    {
        this.codeRange = codeRange;
    }
    public String toString()
    {
        String ret = super.toString();
        ret +=method.toString();
        return ret;
    }
    public String getType()
    {
        // TODO Auto-generated method stub
        return  IAnnoModel.METHOD;
    }

}
