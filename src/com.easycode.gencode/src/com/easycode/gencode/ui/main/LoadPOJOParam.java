package com.easycode.gencode.ui.main;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;

import com.easycode.configmgr.model.Config;
import com.easycode.gencode.core.javaparse.JavaSrcParse;

public class LoadPOJOParam implements ILoadPreparedParam
{

    private String defaultAnno;
    private String templateId;
    private String pkgSource;
    private ICompilationUnit compUnit;
    private Config prjConfig;
    private IProject project;
    
    public LoadPOJOParam(ICompilationUnit compUnit)
    {
        this.project = compUnit.getResource().getProject();
        this.compUnit = compUnit;
     
    }
    public void init(String defaultAnno, String templateId, String pkgSource, Config prjConfig )
    {
        this.defaultAnno = defaultAnno;
        this.templateId = templateId;
        this.pkgSource = pkgSource;
        this.prjConfig = prjConfig;
    }
    public void reset(String defaultAnno, String templateId, String pkgSource,ICompilationUnit compUnit, Config prjConfig )
    {
        this.defaultAnno = defaultAnno;
        this.templateId = templateId;
        this.pkgSource = pkgSource;
        this.compUnit = compUnit;
        this.prjConfig = prjConfig;
    }
    public String reload() throws Exception
    {
        
        JavaSrcParse clzObj = new JavaSrcParse(
                defaultAnno, templateId,
                pkgSource, compUnit);
        return clzObj.getClzJson(null, prjConfig);
        
        
    }
    public IProject getProject()
    {
        return project;
    }

}
