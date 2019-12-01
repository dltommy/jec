package com.easycode.gencode.core.dbtool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import com.easycode.common.JECInfoModel;
import com.easycode.configmgr.model.Config.DB;

public class TableModel implements Cloneable
{
    private String table;
    private String alias;
    private String tableType;
    //private String templateId;
    private List<ColumnModel> colList = null;//new ArrayList<ColumnModel>();
    private String remark;
    private Long genTime;
    public JECInfoModel jec = new JECInfoModel();
    private JSONObject an = new JSONObject();
    public TableModel()
    {
       this.genTime = System.currentTimeMillis();
    }
    public DB getDb()
    {
        return db;
    }

    public void setDb(DB db)
    {
        this.db = db;
    }

    private DB db = new DB();
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
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
    public Long getGenTime()
    {
        return genTime;
    }
    public void setGenTime(Long genTime)
    {
        this.genTime = genTime;
    }

}
