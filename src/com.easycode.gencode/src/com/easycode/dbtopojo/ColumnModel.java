package com.easycode.dbtopojo;

import net.sf.json.JSONObject;

public class ColumnModel
{
    private String isPrimaryKey = "NO";
    private String column;
    private String alias;
    private int type;
    private String typeName;
 
    private String isNullable;
    private String remark;
    private int size;
    private String isAutoInc;
    private JSONObject an = new JSONObject();
    public String getColumn()
    {
        return column;
    }

    public void setColumn(String column)
    {
        this.column = column;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public String getAlias()
    {
        return alias;
    }

    public void setAlias(String alias)
    {
        this.alias = alias;
    }

 
 

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public String getIsNullable()
    {
        return isNullable;
    }

    public void setIsNullable(String isNullable)
    {
        this.isNullable = isNullable;
    }

    public String getIsAutoInc()
    {
        return isAutoInc;
    }

    public void setIsAutoInc(String isAutoInc)
    {
        this.isAutoInc = isAutoInc;
    }

    public String getIsPrimaryKey()
    {
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(String isPrimaryKey)
    {
        this.isPrimaryKey = isPrimaryKey;
    }

    public JSONObject getAn()
    {
        return an;
    }

    public void setAn(JSONObject an)
    {
        this.an = an;
    }

 
}
