package com.easycode.jspeditor.editor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;

import com.easycode.common.EclipseUtil;

public class MultLangService
{
    private static final String PROP_FILE_KEY = "props"; 
    private static final String PROP_FLAG_KEY = "multflag"; 
    private String projectName;
    public MultLangService(String projectName)
    {
        this.projectName = projectName;
    }
    private Properties getProp() throws Exception
    {
        Properties ret = null;
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = workspace.getRoot();
        IResource res = root.findMember(new Path("/"+projectName+"/.settings"));

        //IResource res = root.findMember(new Path("/"+prjName+"/.settings"));
        IContainer container = (IContainer) res;
        IFile f = container.getFile(new Path("/jec.properties"));
        if(f.exists())
        {
            Properties p = new Properties();
            p.load(f.getContents());
            
            return p;
        }
 
        return new Properties();
    }
    public String[] getMultFileArray() throws Exception
    {
        String ret[] = null;
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = workspace.getRoot();
        IResource res = root.findMember(new Path("/"+projectName+"/.settings"));

        //IResource res = root.findMember(new Path("/"+prjName+"/.settings"));
        IContainer container = (IContainer) res;
        IFile f = container.getFile(new Path("/jec.properties"));
        String fileName = null;
        if(f.exists())
        {
            Properties p = new Properties();
            p.load(f.getContents());
            fileName = p.getProperty(PROP_FILE_KEY);
        }
        if(fileName != null && !"".equals(fileName))
        {
            ret = fileName.split(";");
        }
         
        return ret;
    }
    public int getMultFileCount() throws Exception
    {
        String ret[] = null;
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = workspace.getRoot();
        IResource res = root.findMember(new Path("/"+projectName+"/.settings"));

        //IResource res = root.findMember(new Path("/"+prjName+"/.settings"));
        IContainer container = (IContainer) res;
        IFile f = container.getFile(new Path("/jec.properties"));
        String fileName = null;
        if(f.exists())
        {
            Properties p = new Properties();
            p.load(f.getContents());
            fileName = p.getProperty(PROP_FILE_KEY);
        }
        if(fileName != null && !"".equals(fileName))
        {
            ret = fileName.split(";");
        }
        if(ret == null)
        {
            return 0;
        }
        return ret.length;
    }
    public String  getMultLangFlag() throws Exception
    { 
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = workspace.getRoot();
        IResource res = root.findMember(new Path("/"+projectName+"/.settings")); 
        IContainer container = (IContainer) res;
        IFile f = container.getFile(new Path("/jec.properties")); 
        if(f.exists())
        {
            Properties p = new Properties();
            p.load(f.getContents());
            return p.getProperty(PROP_FLAG_KEY);
        } 
        return "<s:text name=\"[EC.#key]\"/><%/*#value*/%>";
    }
    public void save(String[] files) throws Exception
    {
        String filePath = "/"+projectName+"/.settings/jec.properties";
        Properties p = this.getProp();
        
        StringBuilder fileCtx = new StringBuilder();
        for(String f:files)
        {
            if("".equals(f))
            {
                continue;
            }
            fileCtx.append(f+";");
        }
        p.setProperty(PROP_FILE_KEY, fileCtx.toString());
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = workspace.getRoot();
        IResource res = root.findMember(new Path("/"+projectName+"/.settings"));

        //IResource res = root.findMember(new Path("/"+prjName+"/.settings"));
        IContainer container = (IContainer) res;
        IFile f = container.getFile(new Path("/jec.properties"));
        //String fileName = null;
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        p.store(s, null);
        String ctx = s.toString();

        
        if(f.exists())
        {
            InputStream in = new ByteArrayInputStream(ctx.getBytes());
            f.setContents(in, true,false, new NullProgressMonitor());
        }
        else
        {
            EclipseUtil.createFile(filePath, ctx.getBytes(), new NullProgressMonitor(),  new EclipseUtil.ResultOutput()
            {
                public void output(String msg)
                {
                    
                    
                }
            });
        } 
         
    }
    public void saveMultFlag(String multFlag) throws Exception
    {
        String filePath = "/"+projectName+"/.settings/jec.properties";
        Properties p = this.getProp(); 
        
        p.setProperty(PROP_FLAG_KEY, multFlag.trim());
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = workspace.getRoot();
        IResource res = root.findMember(new Path("/"+projectName+"/.settings"));
        IContainer container = (IContainer) res;
        IFile f = container.getFile(new Path("/jec.properties"));
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        p.store(s, null);
        String ctx = s.toString();
        
        if(f.exists())
        {
            InputStream in = new ByteArrayInputStream(ctx.getBytes());
            f.setContents(in, true,false, new NullProgressMonitor());
        }
        else
        {
            EclipseUtil.createFile(filePath, ctx.getBytes(), new NullProgressMonitor(),  new EclipseUtil.ResultOutput()
            {
                public void output(String msg)
                {
                    
                    
                }
            });
        } 
    }
    
    public  String getKeyPrefix() throws Exception
    {
        String flag = getMultLangFlag();
        int keyB = flag.indexOf("[");
        int keyE = flag.indexOf("]");
        String keyMul = flag.substring(keyB+1, keyE);
        
        return keyMul.replace("#key", "");
    }
    public String getKeyMudl() throws Exception
    {
        String flag = getMultLangFlag();
        int keyB = flag.indexOf("[");
        int keyE = flag.indexOf("]");
        
        if (keyB > -1 && keyE > -1 && keyE > keyB)
        {
            return flag.substring(keyB + 1, keyE);
        }
        else
        {
            return "EC.#key";
        }
    }
}
