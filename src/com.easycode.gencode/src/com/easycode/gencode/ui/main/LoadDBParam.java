package com.easycode.gencode.ui.main;

import net.sf.json.JSONObject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;

import com.easycode.common.StringUtil;
import com.easycode.configmgr.model.Config;
import com.easycode.configmgr.model.Config.DB;
import com.easycode.dbtopojo.DbMgr;
import com.easycode.dbtopojo.TableModel; 

public class LoadDBParam implements ILoadPreparedParam
{

    String json = null;
    private IProject project;
    private Config prjConfig; 
    private String dbId; 
    private TableModel table;
     
    private DB db;
    public LoadDBParam(TableModel table, DB db )
    {
        this.table = table;
        this.db = db;
        //this.projectName = table.getPrjName();
        //this.tableName = table.getName(); 

    }
    public String reload() throws Exception
    {
        JSONObject t = JSONObject.fromObject(table);
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
        TableModel m = DbMgr.getTableInfo(db.getDriver(), db.getUrl(), db.getUsername(), db.getPassword(), tableName);
        m.jec.setPrjName(this.table.jec.getPrjName());
        m.jec.setTemplateId(templateId);
        m.jec.setParamFrom(this.table.jec.getParamFrom());
        this.table = m;
    }
 
}
