package com.easycode.configmgr;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;

import com.easycode.common.XmlUtil;

public class ConfigMgrFactory 
{
	public static IConfigMgr newByPrjPath(String prjPath)
	{


        ConfigMgr mgr = new ConfigMgr(prjPath+File.separator+"JEC_CODE"+File.separator+"config.xml");
 
        
        int pos = prjPath.lastIndexOf(File.separator);
        final String prj = prjPath.substring(pos+1);
        XmlUtil.Callback back = new XmlUtil.Callback(){
            public void doback()
            {
                // TODO Auto-generated method stub
                try
                {
                    //项目第一次使用时,配置文件由io生成，生成后再刷新一次，使得文件在eclipse工作台上能看到。
                    IWorkspace workspace = ResourcesPlugin.getWorkspace();
                    IWorkspaceRoot root = workspace.getRoot();
                    IContainer container = (IContainer) root;
                    IFolder fd = root.getFolder(new Path(prj+"/JEC_CODE"));
                    fd.refreshLocal(IResource.FOLDER, new NullProgressMonitor());
                    IFile config = container.getFile(new Path(prj+"/JEC_CODE/config.xml"));
                    config.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
                  
                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }};
            try
            {
                mgr.readOrCreate(back);
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        return mgr;
	}
	public static IConfigMgr newByByFilePath(String filePath)
	{
		return new ConfigMgr(filePath);
	}
	
}
