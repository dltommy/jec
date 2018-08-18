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
 
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.core.resources.IFile;

import org.eclipse.core.internal.resources.File;
import org.eclipse.core.resources.IProject; 
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.easycode.common.FileUtil;
import com.easycode.configmgr.ConfigMgrFactory;
import com.easycode.configmgr.model.Config;
 

/**
 * 功能描叙: 编 码:  完成时间: 2011-9-4 下午06:38:23
 */
public class NewMdulAction implements IObjectActionDelegate
{
	private String projectPath = null;
	private Config config = null;

	private String exceptFile[] = new String[]
	{ "JPG", "ZIP", "JAR" };
	private String fileCtx = null;


	public NewMdulAction()
	{
	}

	public void run(IAction arg0)
	{

		String txt = null;
		//如果是在树结构点右键菜单
		if (fileCtx != null)
		{
			txt = fileCtx;
		}
		// 如果是在编辑模式下选择上下文情况
		else
		{
			// 取得工作台页面
			IWorkbench workbench = PlatformUI.getWorkbench();
			// 取得工作台窗口
			IWorkbenchWindow wind = workbench.getActiveWorkbenchWindow();
			IWorkbenchPage page = wind.getActivePage();
			IEditorPart part = page.getActiveEditor();
			ITextSelection it = null;
			 it = (TextSelection) (part.getSite()
					.getSelectionProvider().getSelection());
			txt = it.getText();
			
			IEditorInput input = part.getEditorInput();
			if (input instanceof IFileEditorInput)
			{
				IFile file = ((IFileEditorInput) input).getFile();
				
				IProject j = file.getProject();

				if (projectPath == null)
				{
					projectPath = j.getLocation().toFile().getPath();
				}
			}
		}
		try
		{
			config = ConfigMgrFactory.newByPrjPath(projectPath).readOrCreate(null);
			//prjConfig = ConfigMgr.newConfigByPrjPath(projectPath);//ConfigMgr.getPrjConfig(projectPath);
			System.err.println("读取配置文件:"+config.getUserName());
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
			
		}
		if (config == null)
		{
			//Shell shell = new Shell();
			//MessageDialog.openInformation(shell, "", "配置文件不存在！");
			return;
		}
		openOptDialog(txt);
	}

	private void openOptDialog(String txt)
	{
		Shell shell = new Shell(SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL
				| SWT.COLOR_WHITE);
		shell.setSize(800, 500);
		shell.setText("Template Edit");

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		//Color color = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
		shell.setLayout(layout);
		CustomDialog window = new CustomDialog(shell,config);
		window.setSrcCode(txt);
		this.fileCtx = null;
		window.open();
	}


	public void setActivePart(IAction arg0, IWorkbenchPart arg1)
	{
	}

	public void selectionChanged(IAction arg0, ISelection selection)
	{
		// TODO Auto-generated method stub
		//System.err.println("class:" + selection.getClass());
		this.fileCtx = null;
		if (selection instanceof TreeSelection)
		{
			TreeSelection structSelection = (TreeSelection) selection;
			Object object = structSelection.getFirstElement();
			//System.err.println("class2:" + object.getClass());
			if(object instanceof CompilationUnit)
			{
				CompilationUnit unit = (CompilationUnit)object;
				String filePath = unit.getResource().getLocation().toFile().getPath();//unit.getPath().toFile().getPath();
				
				System.err.println("filePath:"+filePath);
				try
				{
				    fileCtx = FileUtil.readFileCtx(filePath);
				    this.projectPath = unit.getResource().getProject().getLocation().toFile().getPath();
				    //System.err.println("工程路径"+this.projectPath);
					//openOptDialog(fileCtx);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
			}
			else if (object instanceof File)
			{
				File c = (File) object;
				this.projectPath = c.getProject().getLocation().toFile().getPath();
				// 取后缀名

				int pos = c.getName().lastIndexOf(".");
				if (pos != -1)
				{
					String suffer = c.getName().substring(pos + 1)
							.toUpperCase();
					for (String except : exceptFile)
					{
						if (except.equals(suffer))
						{
							return;
						}
					}
				}

				try
				{
					String filePath = c.getLocation().toFile().getPath();
					// InputStream is = c.getContents();
					InputStream is = new FileInputStream(new java.io.File(
							filePath));
					if (is != null)
					{
						java.io.BufferedInputStream bis = new java.io.BufferedInputStream(
								is);
						java.io.InputStreamReader isr = new java.io.InputStreamReader(
								bis);
						java.io.BufferedReader br = new java.io.BufferedReader(
								isr);
						StringBuffer sb = new StringBuffer();
						while (true)
						{
							String temp = br.readLine();
							if (temp != null)
							{
								sb.append(temp + "\n");
							}
							else
							{
								break;
							}
						}
						fileCtx = sb.toString();
					}
					else
					{
						fileCtx = "";
					}
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

}
