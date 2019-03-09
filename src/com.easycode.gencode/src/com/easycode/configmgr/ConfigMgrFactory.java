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
import org.eclipse.core.runtime.Platform;

import com.easycode.common.XmlUtil;

public class ConfigMgrFactory 
{
	public static IConfigMgr newByPrjPath(String prjPath)
	{
	    return newByDefault();
	}
	public static IConfigMgr newByByFilePath(String filePath)
	{
	    return newByDefault();
		//return new ConfigMgr(filePath);
	}
	 public static IConfigMgr newByDefault()
	 {
	        String tmpPath = Platform.getInstallLocation().getURL().getFile()+File.separator + "easycode_template"+File.separator+"config.xml";
	        return new ConfigMgr(tmpPath);
	 }
}
