/**
 * 作 者:  
 * 日 期: 2011-9-4
 * 描 叙:
 */
package com.easycode.gencode.action;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
 
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jdt.internal.core.JavaModel;
import org.eclipse.jdt.internal.core.PackageFragmentRoot;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
 
 

  
/**
 * 功能描叙: 选中一个文件，右键弹出框
 * 编 码:  
 * 完成时间: 2011-9-4 下午06:38:23
 */
public class PopReviewAction implements IObjectActionDelegate
{
	private String projectPath = null;

   private String projectName = null;

   private String pkgSource = null;
   
	private String filePath = null;

	private ICompilationUnit compUnit = null;
	private IFile file = null;
	public PopReviewAction()
	{
	}

	public void run(IAction arg0)
	{

	    Shell pshell = Display.getCurrent().getFocusControl().getShell();
        Shell shell = new Shell(pshell,SWT.DIALOG_TRIM //| SWT.ON_TOP
               | SWT.COLOR_WHITE | SWT.RESIZE | SWT.MAX | SWT.MIN);
 
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;

		shell.setLayout(layout);
		shell.setLayoutData(new GridData(
				GridData.HORIZONTAL_ALIGN_FILL
						| GridData.VERTICAL_ALIGN_FILL));	
		this.setScreenPoint(shell);
		try
		{
		    if(filePath==null || !filePath.endsWith(".java"))
		    {


	            
	               this.setScreenPoint(shell);
	                ReviewJsonDialog window = new ReviewJsonDialog(shell, file);
	 
	                window.open();
		    }
		    else
		    { 
                setScreenPoint(shell);
               ReviewDialog window = new ReviewDialog(shell,null  ,filePath,projectPath,projectName,pkgSource,compUnit);
                 
               
               window.open();
                
		        //ReviewDialog window = new ReviewDialog(shell,null  ,filePath,projectPath,projectName,pkgSource,compUnit);   
		        
	            //window.open();
		    }

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}




	public void setActivePart(IAction arg0, IWorkbenchPart arg1)
	{
	}

	public void selectionChanged(IAction arg0, ISelection selection)
	{ 
		if (selection instanceof TreeSelection)
		{
			TreeSelection structSelection = (TreeSelection) selection;
			Object object = structSelection.getFirstElement();
	 
			//Object firstElement = structSelection.getFirstElement();
		    //if (!(firstElement instanceof IAdaptable)) {
		    //    return  ;
		    //}
			/*
			if (object instanceof IAdaptable ) {
			    IFile file = (IFile) ((IAdaptable) object).getAdapter(IFile.class);
			    this.file = file;
			}
			 */
		   
		   
			if(object instanceof CompilationUnit)
			{

			    compUnit = (CompilationUnit)object;
			    
		 
			 
			    filePath = compUnit.getResource().getLocation().toFile().getPath();//unit.getPath().toFile().getPath();
				try
				{
					IJavaProject javaProject = JavaCore.create(compUnit.getResource().getProject()); 
		

					IProject project = compUnit.getResource().getProject();

					IPackageFragmentRoot[] rs = javaProject.getPackageFragmentRoots();//.getAllPackageFragmentRoots();getPackageFragmentRoots
					for(IPackageFragmentRoot r:rs)
					{
						if(r.getKind() == IPackageFragmentRoot.K_SOURCE)
						{
							if (r.getResource().contains(compUnit.getResource()))
							{
			  
								String oneName = r.getResource().getName();
								IFolder fd = project.getFolder("/"+oneName);
								if(fd.exists())
								{
									
									this.pkgSource=fd.getProjectRelativePath().toString();
								}
								else
								{
									String twoName = r.getResource().getParent().getName();
									IFolder fd2= project.getFolder("/"+twoName+"/"+oneName);
									if(fd2.exists())
									{
										this.pkgSource=fd2.getProjectRelativePath().toString();
									}
									else
									{
										String threeName = r.getResource().getParent().getParent().getName();
										IFolder fd3= project.getFolder("/"+threeName+"/"+twoName+"/"+oneName);
										if(fd3.exists())
										{
											this.pkgSource=fd3.getProjectRelativePath().toString();
										}
										else
										{
											
										}
									}
								}
							}
						}
					}
				    this.projectPath = compUnit.getResource().getProject().getLocation().toFile().getPath();
			    	
				    this.projectName = compUnit.getResource().getProject().getName();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
			}
			else if (object instanceof File)
			{ 
				File c = (File) object;
				this.file = c;
				if(this.file.getName().endsWith(".java"))
				{
				    compUnit = JavaCore.createCompilationUnitFrom(c);    
				}
				
				
				this.projectPath = c.getProject().getLocation().toFile().getPath();
			    filePath = c.getLocation().toFile().getPath();
			    this.projectName = c.getProject().getName();
			    
			    
			    

			}

			
 
			


			
			
		}
	}
	private void setScreenPoint(Shell shell) 
	{ 
		
	  int width = shell.getMonitor().getClientArea().width; 
	  int height = shell.getMonitor().getClientArea().height; 
	  int x = shell.getSize().x; 
	  int y = shell.getSize().y; 
	  if(x > width) 
	  { 
	      shell.getSize().x = width; 
	  } 
	  if(y > height) 
	  { 
	      shell.getSize().y = height; 
	  } 
	  
	  shell.setLocation(0,0);
	  //shell.setLocation((width - x) / 2, (height - y) / 2); 
	} 
}
