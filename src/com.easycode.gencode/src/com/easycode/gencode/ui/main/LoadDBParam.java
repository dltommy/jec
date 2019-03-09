package com.easycode.gencode.ui.main;

import net.sf.json.JSONObject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;

import com.easycode.common.StringUtil;
import com.easycode.configmgr.model.Config;
import com.easycode.configmgr.model.Config.DB;
import com.easycode.gencode.core.dbtool.DbMgr;
import com.easycode.gencode.core.dbtool.TableModel;

public class LoadDBParam implements ILoadPreparedParam
{

    String json = null;
    private IProject project;
    private Config prjConfig; 
    private String dbId; 
    private TableModel table;
     
    //private DB db;
    public LoadDBParam(TableModel table)
    {
        this.table = table;
        //this.table.setDb(db);
        //this.db = db;
        //this.projectName = table.getPrjName();
        //this.tableName = table.getName(); 

    }
    public String reload() throws Exception
    {
        TableModel viewTable = (TableModel) table.clone();//浅拷贝
        DB viewDB = (DB) table.getDb().clone();
        viewDB.setPassword("");
        viewTable.setDb(viewDB);
        
        JSONObject t = JSONObject.fromObject(viewTable);

        
        this.json = t.toString();
        this.json = StringUtil.formatOutput(json);
        return this.json;
        
    }
    public IProject getProject()
    {
        return project;
    }
    public void init(String templateId, String tableName, Config prjConfig )
    {
        this.table.jec.setTemplateId(templateId);
        this.prjConfig = prjConfig;
        try
        {
            reset(templateId,tableName);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void reset(String templateId, String tableName) throws Exception
    {
 
        this.table.jec.setTemplateId(templateId);
        TableModel m = DbMgr.getTableInfo(table.getDb(), tableName);
        m.jec.setPrjName(this.table.jec.getPrjName());
        m.jec.setTemplateId(templateId);
        m.jec.setParamFrom(this.table.jec.getParamFrom());
        this.table = m;
         
    }
 
}
