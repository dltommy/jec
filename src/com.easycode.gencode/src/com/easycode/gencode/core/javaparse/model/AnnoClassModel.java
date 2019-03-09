package com.easycode.gencode.core.javaparse.model;

 

public class AnnoClassModel implements IAnnoModel
{

    private Range annoRange = null;
    private Range codeRange = null;
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
    public String getAnno()
    {
        // TODO Auto-generated method stub
        return null;
    }
    public String getType()
    {
        // TODO Auto-generated method stub
        return IAnnoModel.CLS;
    }
    

}
