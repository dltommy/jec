package com.easycode.dbtopojo;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.easycode.javaparse.model.JECInfoModel;

public class TableModel
{
    private String table;
    private String alias;
    private String tableType;
    //private String templateId;
    private List<ColumnModel> colList = null;//new ArrayList<ColumnModel>();
    private String remark;
    public JECInfoModel jec = new JECInfoModel();
    private JSONObject an = new JSONObject();
    //private String prjName;
    public List<ColumnModel> getColList()
    {
        return colList;
    }

    public void setColList(List<ColumnModel> colList)
    {
        this.colList = colList;
    }

  
   public String getTable()
    {
        return table;
    }

    public void setTable(String table)
    {
        this.table = table;
    }

    public String getAlias()
    {
        return alias;
    }

    public void setAlias(String alias)
    {
        this.alias = alias;
    }

 
    public JECInfoModel getJec()
    {
        return jec;
    }

    public void setJec(JECInfoModel jec)
    {
        this.jec = jec;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public JSONObject getAn()
    {
        return an;
    }

    public void setAn(JSONObject an)
    {
        this.an = an;
    }

    public String getTableType()
    {
        return tableType;
    }

    public void setTableType(String tableType)
    {
        this.tableType = tableType;
    }
     
}
