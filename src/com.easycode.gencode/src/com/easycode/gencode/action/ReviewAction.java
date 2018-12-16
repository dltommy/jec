package com.easycode.gencode.action;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject; 
import org.eclipse.core.runtime.CoreException; 
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore; 
import org.eclipse.jface.action.IAction;
 
import org.eclipse.jface.viewers.ISelection;
 
import org.eclipse.swt.SWT;
 
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
 
import org.eclipse.swt.widgets.Display;
 
import org.eclipse.swt.widgets.Shell;
 
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
 
import org.eclipse.ui.part.FileEditorInput;
 

import com.easycode.common.FileUtil;
 
import com.easycode.gencode.service.RegxUtil;
 
 
import com.easycode.gencode.ui.elements.ModelSelect;
  

/**
 * Our sample action implements workbench action delegate. The action proxy will
 * be created by the workbench and shown in the UI. When the user tries to use
 * the action, this delegate will be created and execution will be delegated to
 * it.
 * 
 * @see IWorkbenchWindowActionDelegate
 */
public class ReviewAction implements IWorkbenchWindowActionDelegate
{
	private IWorkbenchWindow window;

	/**
	 * The constructor.
	 */
	public ReviewAction()
		{

		}

	/**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	//private String projectPathLoc = null; 
	 
	String projectPath = null;
	String projectName = null;
	String filePath = null;
	private ModelSelect modelSelect = null;
	private ICompilationUnit compUnit = null;
	private String javaSource =  null; 
	public void run(IAction action)
	{
		//this.selectionChanged(action, this.);

		if(projectPath==null || filePath == null)
		{
			//action.setEnabled(false);
			return;
		}
		
		
		IWorkbench workbench = PlatformUI.getWorkbench();
		
		IWorkbenchWindow wind = workbench.getActiveWorkbenchWindow();
		
		IWorkbenchPage page = wind.getActivePage();
	
		if (page != null)
		{
			IEditorPart part = page.getActiveEditor();
			
			if (part != null)
			{
				IEditorInput ipart = part.getEditorInput();
				if (ipart instanceof FileEditorInput)
				{

					FileEditorInput f = (FileEditorInput) part.getEditorInput();
					IProject proj = f.getFile().getProject();
					
					
                    String ctx;
					try {
						ctx = FileUtil.readFileFromStream(f.getFile().getContents());
						modelSelect = RegxUtil.parseModelSelect(ctx );
					if(modelSelect == null)
					{
					    if(!filePath.endsWith(".java"))
					    {
					        return;
					    }
					}
						
						if(modelSelect != null && modelSelect.getSrc() != null && !"".equals(modelSelect.getSrc()))
						{
							IFile srcFile = proj.getFile(modelSelect.getSrc());		
							if(srcFile != null && srcFile.exists())
							{
								ICompilationUnit newUnit = JavaCore
	                            .createCompilationUnitFrom(srcFile);
								
								modelSelect.setUnit(newUnit);
							}
						}
						

					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            		
				}
			}
		}
		 
        Shell pshell = Display.getCurrent().getFocusControl().getShell();
        
        
		Shell shell = new Shell(pshell, SWT.DIALOG_TRIM// | SWT.ON_TOP
				| SWT.COLOR_WHITE | SWT.RESIZE | SWT.MAX | SWT.MIN);
		 
		//shell.setSize(1000, 1000);
		if(modelSelect != null)
		{
		    String title ="模板ID:"+modelSelect.getTemp();
		    if(modelSelect.getNode() != null)
		    {
		        shell.setText(title+",节点:"+modelSelect.getNode());    
		    }
		    else
		    {
		        shell.setText(title);
		    }
		    
		}
		GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
                | GridData.VERTICAL_ALIGN_FILL);

		shell.setLayoutData(data);
		//shell.setText("Code Gen");

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;

		shell.setLayout(layout);
		shell.setLayoutData(data);


		//String fileName  = filePath.substring(filePath.lastIndexOf("/"));
		//editFile = f.getFile().getLocation().toFile().getPath();
		
        ReviewDialog window = new ReviewDialog(shell,modelSelect, filePath,projectPath,projectName,javaSource,compUnit);
        
        this.setScreenPoint(shell);
        window.open();
		/*
		if(filePath.endsWith(".java"))
		{
		    ReviewDialog window = new ReviewDialog(shell, false, filePath,projectPath,projectName,javaSource);
		    window.open();
		}
		else
		{
			String editFile = projectPath + "/test/com/easycode/codegen/mycom/model/mypu/TestModel.java";
	        ReviewDialog window = new ReviewDialog(shell, true,editFile,projectPath,projectName,"test");
	        window.open();
		}
		*/
		/*
		 * 		String editFile = null;
		// 鍙栧緱宸ヤ綔鍙伴〉闈�		IWorkbench workbench = PlatformUI.getWorkbench();
		
		// 鍙栧緱宸ヤ綔鍙扮獥鍙�		IWorkbenchWindow wind = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage page = wind.getActivePage();
		if (page != null)
		{
			IEditorPart part = page.getActiveEditor();
			if (part != null)
			{
				IEditorInput ipart = part.getEditorInput();
				if (ipart instanceof FileEditorInput)
				{

					FileEditorInput f = (FileEditorInput) part.getEditorInput();

					projectPathLoc = f.getFile().getProject().getLocation()
							.toFile().getPath();
 

					Shell shell = new Shell(SWT.DIALOG_TRIM | SWT.ON_TOP
							| SWT.COLOR_WHITE);
					shell.setSize(800, 700);
					shell.setText("Review");

					GridLayout layout = new GridLayout();
					layout.numColumns = 1;

					shell.setLayout(layout);
					shell.setLayoutData(new GridData(
							GridData.HORIZONTAL_ALIGN_FILL
									| GridData.VERTICAL_ALIGN_FILL));

					if (part instanceof GenJavaCodeEditor
							|| part instanceof CompilationUnitEditor)
					{

						editFile = f.getFile().getLocation().toFile().getPath();
						ReviewDialog window = new ReviewDialog(shell, false,editFile,projectPathLoc);
						window.open();
					}
					else
					{

						editFile = f.getFile().getProject().getLocation()
								.toFile()
								+ "/test/com/easycode/codegen/mycom/model/mypu/TestModel.java";
						ReviewDialog window = new ReviewDialog(shell, true,editFile,projectPathLoc);
						window.open();
					}

				}
				else
				{

				}

			}
		}
		*/
	}

	/**
	 * Selection in the workbench has been changed. We can change the state of
	 * the 'real' action here if we want, but this can only happen after the
	 * delegate has been created.
	 * 
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection)
	{
		//action.setEnabled(false);
	    //action.setEnabled(false);
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow wind = workbench.getActiveWorkbenchWindow();
        IWorkbenchPage page = wind.getActivePage();
        
        if (page != null)
        {
            IEditorPart part = page.getActiveEditor();
            if (part != null)
            {

                //if (part instanceof GenJavaCodeEditor
                //        || part instanceof CompilationUnitEditor)
                if(true)
                {
                    IEditorInput input = part.getEditorInput();
                    
                    if (input instanceof IFileEditorInput)
                    { 
                   
                        IFile file = ((IFileEditorInput) input).getFile();
                       
              
                        
                        if(file.getLocation() == null)
                        {
                            return;
                        }
                        filePath = file.getLocation().toFile().getPath();
                      
                        IProject project = file.getProject();

                        //if (projectPath == null)
                        {
                            projectPath = project.getLocation().toFile().getPath();
                        }
                        projectName = project.getName();
                        
                        if(file.getName().endsWith(".java"))
                        {
                           // return;

                            compUnit =  JavaCore.createCompilationUnitFrom(file);
      
                                try
                                {
                                    IJavaProject javaProject = JavaCore.create(project); 

                                    //IProject prj = unit.getResource().getProject();

                                    IPackageFragmentRoot[] rs = javaProject.getPackageFragmentRoots();//.getAllPackageFragmentRoots();getPackageFragmentRoots
                                    for(IPackageFragmentRoot r:rs)
                                    {
                                        if(r.getKind() == IPackageFragmentRoot.K_SOURCE)
                                        {
                                            if (r.getResource().contains(file))
                                            {


                                                String oneName = r.getResource().getName();
                                                IFolder fd = project.getFolder("/"+oneName);
                                                if(fd.exists())
                                                {
                                                     
                                                    this.javaSource=fd.getProjectRelativePath().toString();
                                                }
                                                else
                                                {
                                                    String twoName = r.getResource().getParent().getName();
                                                    IFolder fd2= project.getFolder("/"+twoName+"/"+oneName);
                                                    if(fd2.exists())
                                                    {
                                                        this.javaSource=fd2.getProjectRelativePath().toString();
                                                    }
                                                    else
                                                    {
                                                        String threeName = r.getResource().getParent().getParent().getName();
                                                        IFolder fd3= project.getFolder("/"+threeName+"/"+twoName+"/"+oneName);
                                                        if(fd3.exists())
                                                        {
                                                            this.javaSource=fd3.getProjectRelativePath().toString();
                                                        }
                                                        else
                                                        {
                                                            
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
             
                                }
                                catch(Exception e)
                                {
                                    e.printStackTrace();
                                }
     
                        }
                        
                    }
                    
                    action.setEnabled(true);
                }
                else
                {
                    //action.setEnabled(false);
                }
            }
            else
            {
                //action.setEnabled(false);
            }
        }
        else
        {
            //action.setEnabled(false);
        }
		 

        

        
        
        
        
        
        
        
	    /*
		if (selection instanceof TreeSelection)
		{
			TreeSelection structSelection = (TreeSelection) selection;
			Object object = structSelection.getFirstElement();
		 
			if(object instanceof CompilationUnit)
			{
				CompilationUnit unit = (CompilationUnit)object;
				filePath = unit.getResource().getLocation().toFile().getPath();//unit.getPath().toFile().getPath();
 
				try
				{
 
				    projectPath = unit.getResource().getProject().getLocation().toFile().getPath();
				    projectName = unit.getResource().getProject().getName();
					try
					{
						IJavaProject javaProject = JavaCore.create(unit.getResource().getProject()); 

						IProject prj = unit.getResource().getProject();

						IPackageFragmentRoot[] rs = javaProject.getPackageFragmentRoots();//.getAllPackageFragmentRoots();getPackageFragmentRoots
						for(IPackageFragmentRoot r:rs)
						{
							if(r.getKind() == IPackageFragmentRoot.K_SOURCE)
							{
								if (r.getResource().contains(unit.getResource()))
								{
	 

									String oneName = r.getResource().getName();
									IFolder fd = prj.getFolder("/"+oneName);
									if(fd.exists())
									{
										 
										this.javaSource=fd.getProjectRelativePath().toString();
									}
									else
									{
										String twoName = r.getResource().getParent().getName();
										IFolder fd2= prj.getFolder("/"+twoName+"/"+oneName);
										if(fd2.exists())
										{
											this.javaSource=fd2.getProjectRelativePath().toString();
										}
										else
										{
											String threeName = r.getResource().getParent().getParent().getName();
											IFolder fd3= prj.getFolder("/"+threeName+"/"+twoName+"/"+oneName);
											if(fd3.exists())
											{
												this.javaSource=fd3.getProjectRelativePath().toString();
											}
											else
											{
												
											}
										}
									}
								}
							}
						}
					    this.projectPath = unit.getResource().getProject().getLocation().toFile().getPath();
					    this.projectName = unit.getResource().getProject().getName();
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
			}
			else if (object instanceof File)
			{
				org.eclipse.core.internal.resources.File c = (org.eclipse.core.internal.resources.File) object;
				filePath = c.getLocation().toFile().getPath();
				projectPath = c.getProject().getLocation().toFile().getPath();
				projectName = c.getProject().getName();
			}
			if(filePath != null && filePath.endsWith(".java"))
			{
				action.setEnabled(true);
			}
			else
			{
				action.setEnabled(false);
			}
			
		}
		else
		{
			 
			action.setEnabled(false);
		}
		*/
		
	}

	/**
	 * We can use this method to dispose of any system resources we previously
	 * allocated.
	 * 
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose()
	{
	}

	/**
	 * We will cache window object in order to be able to provide parent shell
	 * for the message dialog.
	 * 
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window)
	{
		this.window = window;
		
		 
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