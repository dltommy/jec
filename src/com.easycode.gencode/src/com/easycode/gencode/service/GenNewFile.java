/**
 * 作 者:  
 * 日 期: 2013-3-11
 * 描 叙:
 */
package com.easycode.gencode.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.easycode.common.EclipseUtil;
import com.easycode.configmgr.model.Config;
import com.easycode.resource.MultLang;
import com.easycode.templatemgr.FileGenModel;
import com.easycode.templatemgr.FileGenModel.FileModel;

/**
 * 功能描叙: 编 码:  完成时间: 2013-3-11 下午11:09:39
 */
public class GenNewFile 
{
 
    
	private Text resultText = null;
	private FileGen curFtlModel = null;
	private Config config;
	private boolean isLocale;
    private String projectPath;
	private Shell parentShell = null;
	public Shell getParentShell()
    {
        return parentShell;
    }

 
    public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}


	private String projectName = null;
	public String getProjectName()
	{
		return projectName;
	}

	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}

	public GenNewFile(Shell parentShell)
	{
         this.parentShell = parentShell;
 
	}
 
	public void genFile()
	{

		resultText.setText("");
		resultText.append("Begin...\n");


		final IWorkspace workspace = ResourcesPlugin
				.getWorkspace();
		final IWorkspaceRoot root = workspace.getRoot();
		
		final IContainer rootContainer = root;

		IWorkspaceRunnable operation = new IWorkspaceRunnable()
		{
			public void run(IProgressMonitor monitor)
					throws CoreException
			{
				if (curFtlModel == null)
				{
					resultText.append("End.\n");
					return;
				}
				try
				{
					curFtlModel.init(config.getSrvUrl(), config.getLocalTemplatePath(), isLocale, projectPath);
					curFtlModel.updateTreeNode(config.getSrvUrl(), config.getLocalTemplatePath(), isLocale,false, projectPath);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					PrintStream ps = new PrintStream(baos);
					e.printStackTrace(ps);
					resultText.append("Error:" + baos.toString()
							+ "\n");
					try
					{
						baos.close();
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
					resultText.append("End.\n");
					return;
				}

				FileGenModel newFile;
				try
				{
					//校验head部分,head部分单独校验 
					java.util.List<String> headWarnErrList = null;
					headWarnErrList = curFtlModel.getHeadWarnErrList();
					if(headWarnErrList != null && headWarnErrList.size() > 0)
					{
					    for(String tp:headWarnErrList)
					    {
					    	if(tp.startsWith("WARNING:"))
					    	{
							    boolean sure = MessageDialog.openConfirm( parentShell, MultLang.getMultLang("code.079"), tp);
							    if(!sure)
							    {
							    	resultText.append("End.\n");
							    	return;
							    }
					    	}
					    	else if(tp.startsWith("ERR:"))
					    	{
					    		MessageDialog.openError(parentShell, MultLang.getMultLang("code.080"), tp);
					    		resultText.append("End.\n");
					    		return;
					    	}

					    }

					    
					}
					
					
					String targetSrc = curFtlModel.genSrc();
					targetSrc = targetSrc.replaceAll("<ftl_head>[\\s|\\S]*?</ftl_head>", "");
					java.util.List<String> warnErrList = null;
					try
					{
						warnErrList = RegxUtil.getWarnErrPatList(targetSrc, "<((ftl_warn)|(ftl_err))>([^<]*?)</((ftl_warn)|(ftl_err))>");//warnRegx);
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(warnErrList != null && warnErrList.size() > 0)
					{
					    for(String tp:warnErrList)
					    {
					    	//tempSb.append(tp+"\n");
					    	if(tp.startsWith("WARNING:"))
					    	{
							    boolean sure = MessageDialog.openConfirm(parentShell, MultLang.getMultLang("code.079"), tp);
							    if(!sure)
							    {
							    	resultText.append("End.\n");
							    	return;
							    }
					    	}
					    	else if(tp.startsWith("ERR:"))
					    	{
					    		MessageDialog.openError(parentShell, MultLang.getMultLang("code.080"), tp);
					    		resultText.append("End.\n");
					    		return;
					    	}

					    }

					    
					}
					

					if(targetSrc != null)
					{
					    targetSrc = targetSrc.replaceAll("<ftl_doc>[\\s|\\S]*?</ftl_doc>", "");
					    targetSrc = targetSrc.replaceAll("<ftl_warn>[\\s|\\S]*?</ftl_warn>", "");
					    targetSrc = targetSrc.replaceAll("<ftl_err>[\\s|\\S]*?</ftl_err>", "");
					    targetSrc = targetSrc.replaceAll("<ftl_append[\\s]*?([^>]*?)>", "");
					    targetSrc = targetSrc.replaceAll("</ftl_append>", "");
					}
					newFile = new FileGenModel(targetSrc);
				}
				catch (Exception e)
				{
					e.printStackTrace();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					PrintStream ps = new PrintStream(baos);
					e.printStackTrace(ps);
					resultText.append("Error:" + baos.toString()
							+ "\n");
					resultText.append("End.\n");
					try
					{
						baos.close();
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
					return;
				}
 

				java.util.List<FileModel> fileList = newFile
						.getFileList(); 
				if (fileList != null)
				{
				
				    int idx=0;
                    final StringBuilder sb = new StringBuilder();
					for (FileModel f : fileList)
					{
                        idx++;
					    String fullPath = projectName +"/"+ f.getFilePath();
					    fullPath = fullPath.replace("//", "/");
					    
                        final String idxStr=idx+"";
					    try
                        {
                            EclipseUtil.createFile(fullPath, f.getFileCtx()
                                    .getBytes("UTF-8"), monitor, new EclipseUtil.ResultOutput()
                                    {
                                        public void output(String msg)
                                        {
                                            sb.append(idxStr+":"+msg);
                                            
                                        }
                                    });
                        }
                        catch (Exception e2)
                        {
 
                            EclipseUtil.proExcept(e2,"文件生成异常，请查看控制台日志！");
                        }


					}
					resultText.append(sb.toString());
 
					
					resultText.append("End.\n");
				}
				else
				{
					resultText.append("End.\n");
				}
			}
		};

		//System.err.println("yy:"+root.getProject("Test"));
		try
		{
			//workspace.run(operation, null, IWorkspace.AVOID_UPDATE, null);
			workspace.run(operation, null);
			
		}
		catch (CoreException e)
		{
			e.printStackTrace();
		}
		

	
		
	}
 
	public FileGen getCurFtlModel()
	{
		return curFtlModel;
	}
	public void setCurFtlModel(FileGen curFtlModel)
	{
		this.curFtlModel = curFtlModel;
	}
 
	public boolean isLocale()
	{
		return isLocale;
	}
	public void setLocale(boolean isLocale)
	{
		this.isLocale = isLocale;
	}

	public Config getConfig()
	{
		return config;
	}
	public void setConfig(Config config)
	{
		this.config = config;
	}
	public Text getResultText()
	{
		return resultText;
	}
	public void setResultText(Text resultText)
	{
		this.resultText = resultText;
	}
	
}
