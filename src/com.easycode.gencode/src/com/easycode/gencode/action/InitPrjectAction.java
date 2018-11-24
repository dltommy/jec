/**
 * 作 者:  
 * 日 期: 2018-10-28
 * 描 叙:
 */
package com.easycode.gencode.action;

 
 
 
 
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
 
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;

import com.easycode.common.EclipseUtil;
import com.easycode.configmgr.ConfigMgrFactory;
import com.easycode.configmgr.configeditor.ConfigCodeEditor;
 
 

  
/**
 * 功能描叙: 编 码:  完成时间: 2018-10-28  
 */
public class InitPrjectAction implements IObjectActionDelegate
{
 
 
    private IProject project = null;
	public InitPrjectAction()
	{
	}

	public void run(IAction arg0)
	{
	    ConfigMgrFactory.newByDefault();
	    IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = workspace.getRoot();
        final IContainer container = (IContainer) root;
        
 
        try
        {
             IWorkbenchPage page = PlatformUI.getWorkbench()
                  .getActiveWorkbenchWindow().getActivePage();
 
            IFile n = container.getFile(new Path("/"+project.getName()+"/config.jec"));
            IDE.openEditor(page, n, "client.editors.gencode.cfg.ConfigEditor");
            /*
            if(true) return;
            final String path = project.getName()+"/JEC_CODE/config.jec";
            EclipseUtil.createFile(path, ""
                    .getBytes("UTF-8"), new NullProgressMonitor(), new EclipseUtil.ResultOutput()
                    {
                        public void output(String msg)
                        {
                            IWorkbenchPage page = PlatformUI.getWorkbench()
                                    .getActiveWorkbenchWindow().getActivePage();
                            ConfigCodeEditor r;
                            IFile f = container.getFile(new Path(path));
                            //IFile n = container.getFile(new Path("/"+project.getName()));
                            try
                            {
                                IFile n = container.getFile(new Path("/"+project.getName()+"/f.txt"));
                                IDE.openEditor(page, n, "client.editors.gencode.cfg.ConfigEditor");
                                
                               
                            }
                            catch (PartInitException e)
                            {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            
                        }
                    });
            */
        }
        catch (Exception e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
 
        
         /*
        try
        {
            fd.refreshLocal(IResource.FOLDER, new NullProgressMonitor());
            IFile config = container.getFile(new Path(project.getName()+"/JEC_CODE/config.xml"));
            if(config.exists())
            {
                
            }
            else
            {
                
            }
            config.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
             
        }
        catch (CoreException e)
        {
             
            e.printStackTrace();
        }
         */
         
	}



	public void setActivePart(IAction arg0, IWorkbenchPart arg1)
	{
	}

	public void selectionChanged(IAction arg0, ISelection selection)
	{
	    this.project = this.getProject();
	   
 
        
	}
 
    public static IProject getProject()
    {
        IProject project = null;

        ISelectionService selectionService = Workbench.getInstance()
                .getActiveWorkbenchWindow().getSelectionService();
        ISelection selection = selectionService.getSelection();
        if (selection instanceof IStructuredSelection)
        {
            Object element = ((IStructuredSelection) selection)
                    .getFirstElement();

            if (element instanceof IResource)
            {
                project = ((IResource) element).getProject();
            }
            else if (element instanceof PackageFragmentRootContainer)
            {
                IJavaProject jProject = ((PackageFragmentRootContainer) element)
                        .getJavaProject();
                project = jProject.getProject();
            }
            else if (element instanceof IJavaElement)
            {
                IJavaProject jProject = ((IJavaElement) element)
                        .getJavaProject();
                project = jProject.getProject();
            }
        }

        return project;
    }  
}
