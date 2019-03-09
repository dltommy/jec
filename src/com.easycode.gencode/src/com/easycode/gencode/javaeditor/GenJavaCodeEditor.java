package com.easycode.gencode.javaeditor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import java.io.StringWriter;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import java.util.StringTokenizer;

import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IContainer;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.ide.IDE;
  
import com.easycode.configmgr.ConfigMgrFactory;
import com.easycode.configmgr.model.Config;
import com.easycode.gencode.ui.elements.ModelSelect;
import com.easycode.gencode.ui.main.LoadPOJOParam;
import com.easycode.gencode.ui.main.MainUi;
import com.easycode.gencode.core.javaparse.JavaSrcParse;
import com.easycode.resource.MultLang;

/**
 * An example showing how to create a multi-page editor. This example has 3
 * pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class GenJavaCodeEditor extends MultiPageEditorPart implements
		IResourceChangeListener
{

	/** The text editor used in page 0. */
	//public TextEditor editor;
	//public JCEditor editor;
	public CompilationUnitEditor editor;
	/** The font chosen in page 1. */
	private Font font;

	private IProject project;
	/** The text widget used in page 2. */
	private StyledText text;

	/**
	 * Creates a multi-page editor example.
	 */
	public GenJavaCodeEditor()
		{
			super();
			ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
		}

	/**
	 * Creates page 0 of the multi-page editor, which contains a text editor.
	 */
	void createPage0()
	{
		try {
			editor =  new CompilationUnitEditor();// new TextEditor();////
			
			int index = addPage(editor, getEditorInput());
			setPageText(index, editor.getTitle());
			
			
		} catch (PartInitException e) {
			ErrorDialog.openError(
				getSite().getShell(),
				"Error creating nested text editor",
				null,
				e.getStatus());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public String getCurAnnoText()
	{
		return this.maintance.getAnnoText();
	}
	public Map getSrcJson()
	{
		return this.maintance.getSrcJson(this.project);
	}
	public String getCurMdlId()
	{
		return this.maintance.getCurMudId();
	}
	private MainUi maintance = null;
	/**
	 * Creates page 1 of the multi-page editor, which allows you to change the
	 * font used in page 2.
	 */
	void createPage1()
	{
		 Config prjConfig = null;
		// 工程目录
		 String projectPath = null;
		// 工程目录下的配置文件存放位置
		 //String projectConfigPath = null;
		 String workspacePath = null;
		 String projectName = null;
		

		FileEditorInput f = (FileEditorInput) editor.getEditorInput();

 
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IContainer rootContainer = root;
		workspacePath = rootContainer.getLocation().toFile().getPath();
		  
		String editFile = f.getFile().getLocation().toFile().getPath();
		
		ICompilationUnit unit = JavaCore.createCompilationUnitFrom(f.getFile());
		  
		this.project = f.getFile().getProject();
		
		projectPath = project.getLocation().toFile()
				.getPath(); 
		
 
		try
		{
			prjConfig = ConfigMgrFactory.newByPrjPath(projectPath).readOrCreate(null);
			 //prjConfig = ConfigMgr.getPrjConfig(projectPath);
				if (prjConfig == null)
				{
					Shell shell = new Shell();
					MessageDialog.openInformation(shell, "", MultLang.getMultLang("code.001"));//找不到配置文件
					throw new Exception("配置文件不存在");
				}
		}
		catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		    return;	
		}

		//int pos = projectPath.lastIndexOf(File.separator);
		projectName = f.getFile().getProject().getName();//projectPath.substring(pos + 1);
		Composite composite = new Composite(getContainer(), SWT.NONE);
		
		/*
		GridLayout toplayout = new GridLayout();
		GridData toplayoutData = new GridData(GridData.FILL_BOTH);
		toplayout.numColumns = 4;
		composite.setLayout(toplayout);
		composite.setLayoutData(toplayoutData);
		
		final Button reloadBut = new Button(composite, SWT.FLAT
				| SWT.COLOR_WHITE);
		reloadBut.setText("重新载入");
		
		int a = addPage(composite);
		setPageText(a, "CodeGen");
		if(true)
		{
			return;
		}
		*/
//new MdlMaintance(composite, projectPath, projectName, workspacePath,editFile);
		
		try
		{
			//String modelPkg = prjConfig.getPkgConfig().getModelPkg();
			JavaSrcParse clzObj = new JavaSrcParse(unit);

			clzObj.getClzJson(null,prjConfig);

			IJavaProject javaProject = JavaCore.create(f.getFile().getProject());
			String pkgSource = this.getPkgSource(javaProject);
            //projectPath,
			ModelSelect mSelect = null;
			maintance = new MainUi(mSelect,projectPath, prjConfig, composite,  projectName,
			        clzObj.getParamFrom() ,pkgSource,new LoadPOJOParam(unit));
			
			if(maintance != null)
			{
				composite.pack();
				
				// 查询本地存储
				

				// int index = addPage(composite);
				int index = addPage(composite);
				setPageText(index, "CodeGen");
				//maintance.queryLocal();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			e.printStackTrace(ps);
			// baos.toString();i
				MessageBox box = new MessageBox( new Shell() ,SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
		     //设置对话框的标题
		     box.setText("Error");
		     box.setMessage(baos.toString());
		     box.open();
		     //设置对话框显示的消息
		     //内容部能为空
			return;
		}

	}
    private String getPkgSource(IJavaProject javaProject) throws Exception
    {

		 
    	FileEditorInput f = (FileEditorInput) editor.getEditorInput();
    	IProject prj = f.getFile().getProject();
		IPackageFragmentRoot[] rs = javaProject.getPackageFragmentRoots();//.getAllPackageFragmentRoots();getPackageFragmentRoots
		for(IPackageFragmentRoot r:rs)
		{
			if(r.getKind() == IPackageFragmentRoot.K_SOURCE)
			{
				if (r.getResource().contains(f.getFile()))
				{
 

					String oneName = r.getResource().getName();
					IFolder fd = prj.getFolder("/"+oneName);
					if(fd.exists())
					{ 
						return fd.getProjectRelativePath().toString();
					}
					else
					{
						String twoName = r.getResource().getParent().getName();
						IFolder fd2= prj.getFolder("/"+twoName+"/"+oneName);
						if(fd2.exists())
						{
							return fd2.getProjectRelativePath().toString();
						}
						else
						{
							String threeName = r.getResource().getParent().getParent().getName();
							IFolder fd3= prj.getFolder("/"+threeName+"/"+twoName+"/"+oneName);
							if(fd3.exists())
							{
								return fd3.getProjectRelativePath().toString();
							}
							else
							{
								return null;
							}
						}
					}
				}
			}
		}
		return null;
 
	
    }
	/**
	 * Creates page 2 of the multi-page editor, which shows the sorted text.
	 */
	void createPage2()
	{
		
		Composite composite = new Composite(getContainer(), SWT.NONE);
		FillLayout layout = new FillLayout();
		composite.setLayout(layout);
		text = new StyledText(composite, SWT.H_SCROLL | SWT.V_SCROLL);
		text.setEditable(false);

		int index = addPage(composite);
		setPageText(index, "Preview");
	}

	/**
	 * Creates the pages of the multi-page editor.
	 */
	protected void createPages()
	{
		createPage0();
		createPage1();
	}

	/**
	 * The <code>MultiPageEditorPart</code> implementation of this
	 * <code>IWorkbenchPart</code> method disposes all nested editors.
	 * Subclasses may extend.
	 */
	public void dispose()
	{
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

	/**
	 * Saves the multi-page editor's document.
	 */
	public void doSave(IProgressMonitor monitor)
	{
		getEditor(0).doSave(monitor);
	}

	/**
	 * Saves the multi-page editor's document as another file. Also updates the
	 * text for page 0's tab, and updates this multi-page editor's input to
	 * correspond to the nested editor's.
	 */
	public void doSaveAs()
	{
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker)
	{
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}

	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	public void init(IEditorSite site, IEditorInput editorInput)
			throws PartInitException
	{
		if (!(editorInput instanceof IFileEditorInput))
			throw new PartInitException(
					"Invalid Input: Must be IFileEditorInput");
		super.init(site, editorInput);
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart.
	 */
	public boolean isSaveAsAllowed()
	{
		return true;
	}

	/**
	 * Calculates the contents of page 2 when the it is activated.
	 */
	protected void pageChange(int newPageIndex)
	{
		super.pageChange(newPageIndex);
		if (newPageIndex == 2)
		{
			sortWords();
		}
	}

	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event)
	{
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE)
		{
			Display.getDefault().asyncExec(new Runnable()
			{
				public void run()
				{
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow()
							.getPages();
					for (int i = 0; i < pages.length; i++)
					{
						if (((FileEditorInput) editor.getEditorInput())
								.getFile().getProject().equals(
										event.getResource()))
						{
							IEditorPart editorPart = pages[i].findEditor(editor
									.getEditorInput());
							pages[i].closeEditor(editorPart, true);
						}
					}
				}
			});
		}
	}

	/**
	 * Sets the font related data to be applied to the text in page 2.
	 */
	void setFont()
	{
		FontDialog fontDialog = new FontDialog(getSite().getShell());
		fontDialog.setFontList(text.getFont().getFontData());
		FontData fontData = fontDialog.open();
		if (fontData != null)
		{
			if (font != null)
				font.dispose();
			font = new Font(text.getDisplay(), fontData);
			text.setFont(font);
		}
	}

	/**
	 * Sorts the words in page 0, and shows them in page 2.
	 */
	void sortWords()
	{

		String editorText = editor.getDocumentProvider().getDocument(
				editor.getEditorInput()).get();

		StringTokenizer tokenizer = new StringTokenizer(editorText,
				" \t\n\r\f!@#\u0024%^&*()-_=+`~[]{};:'\",.<>/?|\\");
		ArrayList editorWords = new ArrayList();
		while (tokenizer.hasMoreTokens())
		{
			editorWords.add(tokenizer.nextToken());
		}

		Collections.sort(editorWords, Collator.getInstance());
		StringWriter displayText = new StringWriter();
		for (int i = 0; i < editorWords.size(); i++)
		{
			displayText.write(((String) editorWords.get(i)));
			displayText.write(System.getProperty("line.separator"));
		}
		text.setText(displayText.toString());
	}
}
/**
 * This class provides the content for the tree in FileTree
 */

class FileTreeContentProvider implements ITreeContentProvider {
  /**
   * Gets the children of the specified object
   * 
   * @param arg0
   *            the parent object
   * @return Object[]
   */
  public Object[] getChildren(Object arg0) {
    // Return the files and subdirectories in this directory
    return ((File) arg0).listFiles();
  }

  /**
   * Gets the parent of the specified object
   * 
   * @param arg0
   *            the object
   * @return Object
   */
  public Object getParent(Object arg0) {
    // Return this file's parent file
    return ((File) arg0).getParentFile();
  }

  /**
   * Returns whether the passed object has children
   * 
   * @param arg0
   *            the parent object
   * @return boolean
   */
  public boolean hasChildren(Object arg0) {
    // Get the children
    Object[] obj = getChildren(arg0);

    // Return whether the parent has children
    return obj == null ? false : obj.length > 0;
  }

  /**
   * Gets the root element(s) of the tree
   * 
   * @param arg0
   *            the input data
   * @return Object[]
   */
  public Object[] getElements(Object arg0) {
    // These are the root elements of the tree
    // We don't care what arg0 is, because we just want all
    // the root nodes in the file system
    return File.listRoots();
  }

  /**
   * Disposes any created resources
   */
  public void dispose() {
    // Nothing to dispose
  }

  /**
   * Called when the input changes
   * 
   * @param arg0
   *            the viewer
   * @param arg1
   *            the old input
   * @param arg2
   *            the new input
   */
  public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
    // Nothing to change
  }
}
