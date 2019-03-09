/**
 * 作 者: 
 * 日 期: 2013-3-11
 * 描 叙:
 */
package com.easycode.gencode.core.gen;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
 
 
import com.easycode.Constants;
import com.easycode.common.FileUtil;
import com.easycode.common.EclipseUtil;
import com.easycode.configmgr.model.Config;
import com.easycode.resource.MultLang;
import com.easycode.templatemgr.FileGenModel;
import com.easycode.templatemgr.FileGenModel.FileModel;
 
/**
 * 功能描叙: 编 码:  完成时间: 2013-3-11 下午11:09:39
 */
public class GenEditFile 
{
 
	private String curMudId = null;
	private Text resultText = null;
	private FileGen curFtlModel = null;
	private Config config;
	private boolean isLocale;
	private List<String> checkdList = null;
	private String userParam = null;
	private String prepatedParam = null;
	private String templateSrc = null;
	private String projectName = null;
	private String projectPath = null;
	public String getProjectName()
	{
		return projectName;
	}

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public void setProjectName(String projectName)
	{
		this.projectName = projectName;
	}

	public GenEditFile(String curMudId)
	{
		this.curMudId = curMudId;
	}
 
	public void editFile()
	{
		if(curMudId == null)
		{
			MessageBox box = new MessageBox( new Shell() ,SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
		     //设置对话框的标题
		     box.setText("Error");
		     //设置对话框显示的消息
		     box.setMessage("请选择模板!");//用户名或密码错误！
		     box.open();
			return;
		}
		if (curFtlModel != null)
		{
			curFtlModel.setCheckList(checkdList);
		}

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
					curFtlModel.setFtlSrc(templateSrc);
					curFtlModel.setDefinePara(userParam);
					curFtlModel.setPreparedPara(prepatedParam);
					curFtlModel.init(config.getSrvUrl(), config.getLocalTemplatePath(), isLocale, projectPath);
					curFtlModel.updateTreeNode(config.getSrvUrl(), config.getLocalTemplatePath(), isLocale,false,projectPath);
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
					    	//tempSb.append(tp+"\n");
					    	if(tp.startsWith("WARNING:"))
					    	{
							    boolean sure = MessageDialog.openConfirm(new Shell(), MultLang.getMultLang("code.079"), tp);
							    if(!sure)
							    {
							    	resultText.append("End.\n");
							    	return;
							    }
					    	}
					    	else if(tp.startsWith("ERR:"))
					    	{
					    		MessageDialog.openError(new Shell(), MultLang.getMultLang("code.080"), tp);
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
							    boolean sure = MessageDialog.openConfirm(new Shell(), MultLang.getMultLang("code.079"), tp);
							    if(!sure)
							    {
							    	resultText.append("End.\n");
							    	return;
							    }
					    	}
					    	else if(tp.startsWith("ERR:"))
					    	{
					    		MessageDialog.openError(new Shell(), MultLang.getMultLang("code.080"), tp);
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

				java.util.List<IFile> ifileList = new ArrayList<IFile>();
 
				java.util.List<FileModel> fileList = newFile
						.getFileList();
                //找到跟res
				HashMap<String,String> freshDir = new HashMap<String,String>();
				if (fileList != null)
				{
					
					for (FileModel f : fileList)
					{
						String rootRes = null;
						String paths = f.getFilePath();

						String path[] = paths.split("/");
						String base = projectName + "/";
						if (path != null)
						{
							IResource res = null;
							IContainer container = null;
							for (int i = 0; i < path.length - 1; i++)
							{
								if ("".equals(path[i].trim()))
								{
									continue;
								}
								else
								{
									if(rootRes == null)
									{
										rootRes = path[i].trim();
										freshDir.put(rootRes, rootRes);
									}
								}
								base += path[i];
								res = root
										.findMember(new Path(
												base));
								if (res == null)
								{
									IFolder fd = rootContainer
											.getFolder(new Path(
													base));
									try
									{
									    fd.create(false, false,
											monitor);
									   
									}
									catch(Exception e)
									{
										e.printStackTrace();
										ByteArrayOutputStream baos = new ByteArrayOutputStream();
										PrintStream ps = new PrintStream(baos);
										e.printStackTrace(ps);
										resultText.append("Error:" + baos.toString()
												+ "\n");
									}
								
									res = root
											.findMember(new Path(
													base));
									res.refreshLocal(IResource.FOLDER, monitor);
									
								}
								
								base += "/";
								
								container = (IContainer) res;
								container.refreshLocal(IResource.FOLDER, monitor);
								
							}
							IFile file = null;
							if(path.length == 1)
							{
								//res = root
								//.findMember(new Path("/"));
								container = (IContainer) root;
								try
								{
								 file = container
									.getFile(new Path(projectName + "/"+path[0]));
								}
								catch(Exception e)
								{
									e.printStackTrace();
								}
							}
							else
							{
								 file = container
									.getFile(new Path(
											path[path.length - 1]));
							}

							if (!file.exists())
							{

								resultText
										.append("Warn:"
												+ file
														.getProjectRelativePath()
												+ " 不存在！\n");
	 
							}
							else
							{
								//String insertStr = null;
							     HashMap<String,String>  insertStr = null;
								try
								{
									 insertStr = RegxUtil.getInsertStringByGroup(f.getFileCtx(), Constants.appendRegx);
								}
								catch (Exception e)
								{
									// TODO Auto-generated catch block
									e.printStackTrace();
								}//
								if(insertStr != null && insertStr.size()>0)
								{
								    String newCtx = null;
                                    try
                                    {
                                    
                                        newCtx = FileUtil.getEditCtx(file.getContents(), insertStr,file.getCharset());
                                    }
                                    catch (Exception e2)
                                    {
                                        e2.printStackTrace();
                                        continue;
                                    }
                                    if(newCtx == null)
                                    {
                                        continue;
                                    }
								    InputStream in = null;
									try
									{
										in = new ByteArrayInputStream(
												newCtx
														.getBytes(file.getCharset()));
									}
									catch (Exception e1)
									{
										e1.printStackTrace();
									}
									try
									{
										file.setContents(in, true,true, monitor);
										resultText.append("finish:"+file.getProjectRelativePath()+".\n");
										ifileList.add(file);
									}
									catch(Exception e)
									{
										e.printStackTrace();
									}
									
								}
							}

						}

					}
					Iterator<String> itRefresh = freshDir.keySet().iterator();
					while(itRefresh.hasNext())
					{
						String temp = itRefresh.next();
						IResource refresh = root
						.findMember(new Path( projectName + "/"+temp));
						refresh.refreshLocal(IResource.FILE, monitor);
						refresh.refreshLocal(IResource.FOLDER, monitor);
						
						//
					}
					if (ifileList != null
							&& ifileList.size() > 0)
					{
					     
						EclipseUtil.openOnEclipse(ifileList);
					}
					
					
					resultText.append("End.\n");
				}
				else
				{
					resultText.append("End.\n");
				}
			}
		};
 
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
	public List<String> getCheckdList()
	{
		return checkdList;
	}
	public void setCheckdList(List<String> checkdList)
	{
		this.checkdList = checkdList;
	}
	public FileGen getCurFtlModel()
	{
		return curFtlModel;
	}
	public void setCurFtlModel(FileGen curFtlModel)
	{
		this.curFtlModel = curFtlModel;
	}
	public String getCurMudId()
	{
		return curMudId;
	}
	public void setCurMudId(String curMudId)
	{
		this.curMudId = curMudId;
	}
	public boolean isLocale()
	{
		return isLocale;
	}
	public void setLocale(boolean isLocale)
	{
		this.isLocale = isLocale;
	}
	public String getPrepatedParam()
	{
		return prepatedParam;
	}
	public void setPrepatedParam(String prepatedParam)
	{
		this.prepatedParam = prepatedParam;
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
	public String getTemplateSrc()
	{
		return templateSrc;
	}
	public void setTemplateSrc(String templateSrc)
	{
		this.templateSrc = templateSrc;
	}
	public String getUserParam()
	{
		return userParam;
	}
	public void setUserParam(String userParam)
	{
		this.userParam = userParam;
	}
}
