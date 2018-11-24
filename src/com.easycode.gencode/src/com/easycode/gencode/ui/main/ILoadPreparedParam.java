package com.easycode.gencode.ui.main;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;

import com.easycode.configmgr.model.Config;

public interface ILoadPreparedParam
{
    public String reload() throws Exception;
 
    //public void reset(String defaultAnno, String templateId, String pkgSource,ICompilationUnit compUnit, Config prjConfig ) throws Exception;
    public IProject getProject();
}
