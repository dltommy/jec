package com.easycode.gencode.ui.main;

import java.io.InputStream;
import java.util.HashMap;

import net.sf.json.JSONObject;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ThisExpression;

import com.easycode.common.JECInfoModel;
import com.easycode.common.StringUtil;
import com.easycode.configmgr.model.Config;
import com.easycode.configmgr.model.Config.DB;
import com.easycode.gencode.core.dbtool.DbMgr;
import com.easycode.gencode.core.dbtool.TableModel;

public class LoadJsonParam implements ILoadPreparedParam
{

    private IFile file = null;
    private String templateId = null;
   
    public LoadJsonParam(IFile file)
    {
        this.file = file;

    }
    public String reload() throws Exception
    {
        byte[] bytes = new byte[0];
        InputStream inputStream = this.file.getContents();
        bytes = new byte[inputStream.available()];
        inputStream.read(bytes);
        String ctx = new String(bytes);
        JSONObject o = null;
        if(ctx == null || "".equals(ctx.trim()))
        {
            o = new JSONObject();
        }
        else
        {
            o = JSONObject.fromObject(ctx);
        }
        
        JsonModel jec = new JsonModel();
        jec.setTemplateId(this.templateId);
        jec.setPrjName(this.file.getProject().getName());
        jec.setParamFrom("json");
        jec.setFilePath(this.file.getProjectRelativePath().toFile().getPath());
        o.put("jec", jec);
        //jec.put("jec_templateId", this.templateId);
        //jec.put("jec_prjectName", this.file.getProject().getName());
        //jec.put("jec_filePath", this.file.getProjectRelativePath().toFile().getPath());
        
        return StringUtil.formatOutput(o.toString());
        
    }
    public IProject getProject()
    {
        return this.file.getProject();
    }
    public void init(String templateId)
    { 
         this.templateId = templateId;
       
         
    }
    public void reset(String templateId) throws Exception
    {
 
        this.templateId = templateId;
         
    }
 
    public static class JsonModel extends JECInfoModel
    {
        private String filePath;

        public String getFilePath()
        {
            return filePath;
        }

        public void setFilePath(String filePath)
        {
            this.filePath = filePath;
        }
        
    }
}
