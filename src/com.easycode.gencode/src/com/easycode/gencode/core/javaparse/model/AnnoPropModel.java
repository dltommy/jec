package com.easycode.gencode.core.javaparse.model;

import com.easycode.gencode.core.javaparse.model.java.JavaTypeModel;
import com.easycode.gencode.core.javaparse.model.java.PropModel;
 
public class AnnoPropModel implements IAnnoModel
{

    private String anno;
    private PropModel propModel;

    private Range annoRange = null;
    private Range codeRange = null;
    
    public AnnoPropModel(String clsName, String popName,boolean isArray)
    {
        propModel = new PropModel(clsName,popName,isArray);

    }
    public AnnoPropModel(String propName,JavaTypeModel javaTypeModel)
    {
        propModel = new PropModel(propName,javaTypeModel);
    }
    public String getAnno()
    {
        return anno;
    }
    public void setAnno(String anno)
    {
        this.anno = anno;
    }
    public PropModel getPropModel()
    {
        return propModel;
    }
    public void setPropModel(PropModel propModel)
    {
        this.propModel = propModel;
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
    public String getType()
    {
        // TODO Auto-generated method stub
        return IAnnoModel.PROP;
    }
    

}
