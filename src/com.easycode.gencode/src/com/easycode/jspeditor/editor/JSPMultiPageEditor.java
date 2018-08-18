package com.easycode.jspeditor.editor;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.*;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.ResourceListSelectionDialog;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.ide.IDE;

import com.easycode.common.Native2AsciiUtils;
import com.easycode.common.EclipseUtil;
import com.easycode.configmgr.ConfigMgrFactory;
import com.easycode.configmgr.model.Config;
import com.easycode.resource.MultLang;
import com.easycode.templatemgr.util.SrcUtil;

/**
 * An example showing how to create a multi-page editor. This example has 3
 * pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class JSPMultiPageEditor extends MultiPageEditorPart implements
		IResourceChangeListener
{

	/** The text editor used in page 0. */
	private TextEditor editor;
	private IProject project = null;
	private HashMap<String, String> propMap = new HashMap<String, String>();
	/** The font chosen in page 1. */
	private Font font;

	/** The text widget used in page 2. */
	private StyledText text;

	private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// public HashMap<String,String> mulLangMap = new HashMap<String, String>();
	/**
	 * Creates a multi-page editor example.
	 */
	public JSPMultiPageEditor()
		{
			super();
			ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
		}

	/**
	 * Creates page 0 of the multi-page editor, which contains a text editor.
	 */
	void createPage0()
	{
		try
		{
			editor = new TextEditor();
			int index = addPage(editor, getEditorInput());
			setPageText(index, editor.getTitle());
		}
		catch (PartInitException e)
		{
			ErrorDialog.openError(getSite().getShell(),
					"Error creating nested text editor", null, e.getStatus());
		}
	}

	/**
	 * Creates page 1 of the multi-page editor, which allows you to change the
	 * font used in page 2.
	 */
	FormToolkit toolkit = null;
	Text mulSrc = null;
	private String projectName = null;
	private String projectPath = null;
	private Config config = null;
	
	
	private List<String> commonKeyList = null;
	public void initCommList()
	{
		commonKeyList = new ArrayList<String>();
		if(config.getCommonLangProp() != null && !"".equalsIgnoreCase(config.getCommonLangProp().trim()))
		{ 
			String common[] = config.getCommonLangProp().split(";");

			for(String t:common)
			{
				if(project.getFile(t).exists())
				{
					try
					{
						PropertyResourceBundle p = new PropertyResourceBundle(project.getFile(t).getContents());
						Enumeration<String> enu = p.getKeys();

						while(enu.hasMoreElements())
						{
							//String key = enu.nextElement();
							commonKeyList.add(enu.nextElement());
							//commMap.put(p.getString(key), key);
						}
						
						//commList.add(new PropertyResourceBundle(project.getFile(t).getContents()));
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					MessageBox box = new MessageBox( new Shell() ,SWT.APPLICATION_MODAL | SWT.ICON_WARNING);
				     //设置对话框的标题
				     box.setText("WARNING");
				     //设置对话框显示的消息
				     box.setMessage(MultLang.getMultLang("code.084")+t);//"找不到公用资源文件"
				     
				     box.open();
				     
				     //return;
				}
			}
		}
	}
	
	
	
	
	
	
	
	void createPage1()
	{

		toolkit = new FormToolkit(getContainer().getDisplay());
		Composite topCom = toolkit.createComposite(getContainer());
		GridLayout layout = new GridLayout(); 
		topCom.setLayout(layout);
		topCom.setLayoutData(new GridData());

		
		SashForm sf = new SashForm(topCom,SWT.NO);
		sf.setLayoutData(new GridData(GridData.FILL_BOTH));
		sf.setLayout(new FillLayout());
		
		
		Composite leftCom = toolkit.createComposite(sf);
		GridLayout leftLayout = new GridLayout();
		leftLayout.numColumns = 2;
		leftCom.setLayout(leftLayout); 
		leftCom.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		
		mulSrc = toolkit.createText(leftCom, "", SWT.MULTI | SWT.BORDER
				| SWT.V_SCROLL | SWT.H_SCROLL | SWT.COLOR_WHITE); 
		GridData mulSrcDt = new GridData(GridData.FILL_BOTH); 
		mulSrc.setLayoutData(mulSrcDt);
		

		

		Composite turnCom = toolkit.createComposite(leftCom);
		GridLayout turnLayout = new GridLayout();

		turnCom.setLayout(turnLayout);
		GridData turndata = new GridData(GridData.VERTICAL_ALIGN_CENTER | GridData.HORIZONTAL_ALIGN_CENTER );

		turnCom.setLayoutData(turndata);
		Button turnRight = toolkit.createButton(turnCom, "->", SWT.NO);
		Button turnLeft = toolkit.createButton(turnCom, "<-", SWT.NO);

		Button bt = toolkit.createButton(leftCom, MultLang
				.getMultLang("code.056"), SWT.NO);// 重新提取
		
		Composite rightCom = toolkit.createComposite(sf);
		GridLayout rightLayout = new GridLayout();
		rightCom.setLayout(rightLayout);
		rightLayout.numColumns = 4;
		rightCom.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		
		final Text mulTarget = toolkit.createText(rightCom, "", SWT.MULTI
				| SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.COLOR_WHITE);
		GridData mulTargetDt = new GridData(GridData.FILL_BOTH);
		mulTargetDt.horizontalSpan = 4;
		mulTarget.setLayoutData(mulTargetDt);

		final Label mulLabel = toolkit.createLabel(rightCom, MultLang.getMultLang("code.065"));//资源文件

		FileEditorInput f = (FileEditorInput) getEditorInput();
		project = f.getFile().getProject();
		projectPath = project.getLocation().toFile()
		.getPath();
	    projectName = f.getFile().getProject().getName();
		//prjConfig = ConfigMgr.newConfigByPrjPath(projectPath);
		try {
			config = ConfigMgrFactory.newByPrjPath(projectPath).readOrCreate(null);
		} catch (Exception e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		String propFile = config.getLangProp();//.getCustSet().getTools().getLangProp();
		String propPath = "";
		String[] propFiles = null;
		if(propFile != null && !"".equals(propFile.trim()))
		{
			propPath=propFile.trim();
			propFiles = propPath.split(";");
		}
		

		//final Text fileSelect = toolkit.createText(rightCom, propPath, SWT.BORDER
		//		| SWT.COLOR_WHITE | SWT.READ_ONLY);

	
		final Combo fileSelect = new Combo(rightCom,SWT.NONE);
		
		
		fileSelect.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		GridData fileSelectGd = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		fileSelectGd.widthHint = 200;
		fileSelect.setLayoutData(fileSelectGd);
		if(propFiles != null && propFiles.length > 0)
		{
		    fileSelect.setItems(propFiles);
		    //fileSelect.select(0);
		    if(propFiles.length == 1)
		    {
		    	fileSelect.select(0);
		    }
		}
		
		
		
		
		
		Button chooseFile = toolkit.createButton(rightCom, "Choose", SWT.NO);
		chooseFile.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		chooseFile.addMouseListener(new MouseListener()
		{

			public void mouseDoubleClick(MouseEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			public void mouseDown(MouseEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			public void mouseUp(MouseEvent arg0)
			{
				// TODO Auto-generated method stub
				// handleBrowse(fileSelect);
				
				/*
				FileEditorInput f = (FileEditorInput) getEditorInput();
				String path = f.getFile().getProject().getLocation().toFile()
						.getPath();
				
				String selectedFile = fileSelect.getText();
				if(selectedFile !=null && !"".equals(selectedFile.trim()))
				{
					path = selectedFile.trim();
				}
				
				  FileDialog dialog = new FileDialog(new
				  Shell(),SWT.APPLICATION_MODAL|SWT.OPEN);
				  dialog.setFilterPath(path); 
				  
				  String[] filter = new
				  String[]{"*.properties"}; dialog.setFilterExtensions(filter);
				  String openStr = dialog.open();

				 
				 fileSelect.setText(openStr);
				 */
				/*
				 * ResourceSelectionDialog dialog = new
				 * ResourceSelectionDialog(new
				 * Shell(),ResourcesPlugin.getWorkspace().getRoot(),"ddd");
				 * dialog.open(); Object[] objs = dialog.getResult();
				 * if(objs!=null){ IFile[] files = new IFile[objs.length];
				 * for(int i=0;i<objs.length;i++){ files[i] = (IFile)objs[i]; }
				 */
				/*
				FileEditorInput f = (FileEditorInput) getEditorInput();
				ResourceListSelectionDialog d2 = new ResourceListSelectionDialog(
						new Shell(), new IFile[]
						{ f.getFile() });
				d2.setBlockOnOpen(true);
				d2.open();
				Object[] o2 = d2.getResult();
				if (o2 != null && o2.length>0)
				{
					for (int i = 0; i < o2.length; i++)
					{
						
						IFile selFile = (IFile)o2[i];
						fileSelect.setText(selFile.getProjectRelativePath().toFile().getPath());
					}
				}
				
			}
			
			*/

			ResourceListSelectionDialog d2 = new ResourceListSelectionDialog(
						getContainer().getShell(), ResourcesPlugin
								.getWorkspace().getRoot().getProject(
										projectName), IResource.FILE
								| IResource.FOLDER);
				d2.setBlockOnOpen(true);

				d2.open();
				Object[] o2 = d2.getResult();
				if (o2 != null)
				{
					for (int i = 0; i < o2.length; i++)
					{
						IFile selFile = (IFile) o2[i];
						//System.err.println("选中文件---------->"+selFile.getProjectRelativePath()
						//		.toFile().getPath());
						fileSelect.setText(selFile.getProjectRelativePath()
								.toFile().getPath());
					}
				}
	     
			
			
			
			
			
			
			
			
			

			/*
			 * ResourceSelectionDialog dialog = new ResourceSelectionDialog(new
			 * Shell(),ResourcesPlugin.getWorkspace().getRoot(),"ddd");
			 * 
			 * dialog.open(); Object[] objs = dialog.getResult();
			 * if(objs!=null){ for(int i=0;i<objs.length;i++){ IFile file =
			 * (IFile)objs[i]; System.out.println(file.getLocation()); } }
			 *  }
			 */
			}
		});
		//
		
		Button append = toolkit.createButton(rightCom, MultLang.getMultLang("code.057"), SWT.NO);//添加
		append.addMouseListener(new MouseListener(){

			
			public void mouseDoubleClick(MouseEvent arg0)
			{
				// TODO Auto-generated method stub
				
			}

			public void mouseDown(MouseEvent arg0)
			{
				// TODO Auto-generated method stub
				
			}

			public void mouseUp(MouseEvent arg0)
			{
				IContainer container = null;
				IProject Iproj =  ResourcesPlugin
					.getWorkspace().getRoot().getProject(
							projectName);
				

				
				if("".equals(mulTarget.getText().trim()))
				{
					MessageBox box = new MessageBox( new Shell() ,SWT.APPLICATION_MODAL | SWT.ICON_WARNING);
				     //设置对话框的标题
				     box.setText("Warn");
				     box.setMessage(MultLang.getMultLang("code.058"));//内容不能为空
				     box.open();
				     //设置对话框显示的消息

					return;
				}
						
				// TODO Auto-generated method stub
				FileEditorInput f = (FileEditorInput) getEditorInput();
				String projectPath = f.getFile().getProject().getLocation().toFile()
				.getPath();
				//IConfig prjConfig = ConfigMgr.newConfigByPrjPath(projectPath);
				Config config = null;
				try {
					config = ConfigMgrFactory.newByPrjPath(projectPath).readOrCreate(null);
				} catch (Exception e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				//
				

				
				//校对资源文件是否存在
				if("".equals(fileSelect.getText().trim()))
				{
					MessageBox box = new MessageBox( new Shell() ,SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
				     //设置对话框的标题
				     box.setText("Error");
				     //设置对话框显示的消息
				     box.setMessage(MultLang.getMultLang("code.078"));//"资源文件不存在！"
				     box.open();
				     return;
				}
				
				PropertyResourceBundle ptarget = null;
				
				
				IFile ifile = Iproj.getFile(fileSelect.getText());
				 
				List<String> keyList = new ArrayList<String>();
				//Set targetKeySet = null;
				if(!ifile.exists())
				{
					MessageBox box = new MessageBox( new Shell() ,SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
				     //设置对话框的标题
				     box.setText("Error");
				     //设置对话框显示的消息
				     box.setMessage(MultLang.getMultLang("code.059"));//"资源文件不存在！"
				     box.open();
				     return;
				}
				else
				{


					try
					{
						//ptarget = new PropertyResourceBundle(new java.io.FileInputStream(propFile));
						ptarget = new PropertyResourceBundle(new java.io.FileInputStream(ifile.getLocation().toFile().getPath()));//ifile.getContents()
						Enumeration<String> enu = ptarget.getKeys();
						while(enu != null && enu.hasMoreElements())
						{
							keyList.add(enu.nextElement());
						}
							
						//targetKeySet = ptarget.keySet();
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
						MessageBox box = new MessageBox( new Shell() ,SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
					     //设置对话框的标题
					     box.setText("Error");
					     //设置对话框显示的消息
					     box.setMessage(MultLang.getMultLang("code.060"));//资源文件读取异常！
					     box.open();
					     return;
					}

				}
				
				if(ptarget == null)
				{
					MessageBox box = new MessageBox( new Shell() ,SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
				     //设置对话框的标题
				     box.setText("Error");
				     //设置对话框显示的消息
				     box.setMessage(MultLang.getMultLang("code.061"));//资源文件读取异常！
				     box.open();
				     return;
				}
				StringBuffer tempBuffer = new StringBuffer();
				ByteArrayInputStream bin = new ByteArrayInputStream(mulTarget.getText().trim().getBytes());
				
				//System.err.println("src-------:"+mulTarget.getText().trim());
				try
				{
					PropertyResourceBundle p = new PropertyResourceBundle(bin);
					//ResourceBundle p = new ResourceBundle(bin);
					Enumeration<String> enu = p.getKeys();

					int dbKey = 0;
					int notDbKey = 0;
					while(enu.hasMoreElements())
					{
						String key = enu.nextElement();
						//System.err.println(ptarget.getString(key));
						/*
						Object value = null;
					    try
					    {
						    value = ptarget.getObject(key);
					    }
					    catch(Exception e)
					    {
					    	e.printStackTrace();
					    }
					    */

						if(keyList.contains(key))
						{
							dbKey++;
							tempBuffer.append("#"+key);
						}
						else
						{
							notDbKey++;
							tempBuffer.append(key);
						}
						//System.err.println("-->"+p.getString(key));
						//System.err.println("-->"+p.getObject(key));
						 tempBuffer.append("="+Native2AsciiUtils.native2Ascii(p.getString(key)));
						 tempBuffer.append("\n");
						/*
						 if(value != null)
						{
							
							MessageBox box = new MessageBox( new Shell() ,SWT.APPLICATION_MODAL | SWT.ICON_ERROR|SWT.NONE);
						     
						     box.setText("Error");
						    
						     box.setMessage(key+MultLang.getMultLang("code.062"));//"在目标文件已经存在！"
						     box.open();
						     
							return;
						}
						*/
					}
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try
				{
					StringBuilder sb = new StringBuilder();
					Date begindate = new Date();
					//FileOutputStream fout = new FileOutputStream(ifile.getLocation().toFile().getPath(),true);
					 
						sb.append("\n\n#----------------Begin "+config.getUserName()+" at "+ft.format(begindate)+"---------------------#\n" );
			          
						sb.append(tempBuffer );
						sb.append("#------------------End "+config.getUserName()+" at "+ft.format(begindate)+"---------------------#\n");
	 
					
						EclipseUtil.appendCtx(ifile, sb);
					//ifile.refreshLocal(IResource.FILE,null);
					final IFile fileTemp = ifile;
				       final IWorkspace workspace = ResourcesPlugin
				                .getWorkspace();
				       
				       IWorkspaceRunnable operation = new IWorkspaceRunnable()
				        {
				            public void run(IProgressMonitor monitor)
				                    throws CoreException
				            {
				                fileTemp.refreshLocal(IResource.FILE,monitor);
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
					
					
					
					
					String files[] = fileSelect.getItems();
					HashMap<String,String> filtMap = new HashMap<String, String>();

					for(String fl:files)
					{
						if("".equals(fl.trim()))
						{
							continue;
						}
					    filtMap.put(fl,fl);
					}
					filtMap.put(fileSelect.getText().trim(), fileSelect.getText().trim());
					Iterator<String> keyIt = filtMap.keySet().iterator();
					String tmpStr = "";
					while(keyIt.hasNext())
					{
						tmpStr=tmpStr+keyIt.next()+";";
					}
					config.setLangProp(tmpStr); 
					try
					{
						ConfigMgrFactory.newByPrjPath(projectPath).update(config,null);
					}
					catch (IOException e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					EclipseUtil.openOnEclipse(ifile);
					
					MessageBox box = new MessageBox( new Shell() ,SWT.APPLICATION_MODAL);
				     //设置对话框的标题
				     box.setText("Success");
				     //设置对话框显示的消息
				     box.setMessage(MultLang.getMultLang("code.063"));//"操作成功！"
				     box.open();
				     return;
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					MessageBox box = new MessageBox( new Shell() ,SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
				     //设置对话框的标题
				     box.setText("Error");
				     //设置对话框显示的消息
				     box.setMessage(MultLang.getMultLang("code.064"));//"未知异常！"
				     box.open();
				     return;
				} 
			}
			
		});
		bt.addMouseListener(new MouseListener()
		{

			public void mouseDoubleClick(MouseEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			public void mouseDown(MouseEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			public void mouseUp(MouseEvent arg0)
			{
				// TODO Auto-generated method stub
				filterMultLang.clear();
				mulSrc.setText("");
				String src = editor.getDocumentProvider().getDocument(
						editor.getEditorInput()).get();

				String reg = "({[\\s]*?k[\\s]*?:[\\s|\\S]+?,[\\s]*?v[\\s]*?:[\\s|\\S]+?})";
				PatternCompiler pc = new Perl5Compiler();
				PatternMatcher pm = new Perl5Matcher();
				Pattern pattFile = null;

				try
				{
					pattFile = pc.compile(reg,
							Perl5Compiler.CASE_INSENSITIVE_MASK);
				}
				catch (MalformedPatternException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String filtKey = config.getMultLangFilt();//.getCustSet().getTools().getMultFilt();
				String[] filts = filtKey.split("[,|;]");
				
				PatternMatcherInput fileInput = new PatternMatcherInput(src);
				while (pm.contains(fileInput, pattFile))
				{
					MatchResult rs = pm.getMatch();
					if (rs.group(1) != null)
					{
						String json = rs.group(1);
						HashMap<String, String> map = new HashMap<String, String>();
						try
						{
							SrcUtil.appendToMap(map, json);
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (map.size() > 0)
						{
							if (map.get("k") != null)
							{
								boolean isFilt = false;
								//两种情况下不提取
								//1:通用库里面已经存在了的.
								String key = map.get("k");
								if(filts.length>0)
								{
									for(String f:filts)
									{
										if (!"".equals(f.trim()))
										{
											String com = f.trim();

											if (key.startsWith(com))// ||
											{
												isFilt = true;
												break;
											}
										}
									}
								}
								//if(commonKeyList.contains(key))
								//{
									//isFilt = true;
								//}
                                if(isFilt)
                                {
                                	continue;
                                }
                                
								if (filterMultLang.get(map.get("k")) == null)
								{
									filterMultLang.put(map.get("k"), map
											.get("k"));
									mulSrc.append(map.get("k") + "="
											+ map.get("v"));
									mulSrc.append("\n");
								}

							}
						}
					}
				}
			}
		});
		turnRight.addMouseListener(new MouseListener()
		{

			public void mouseDoubleClick(MouseEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			public void mouseDown(MouseEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			public void mouseUp(MouseEvent arg0)
			{
				mulTarget.setText(Native2AsciiUtils.native2Ascii(mulSrc
						.getText()));

			}
		});
		turnLeft.addMouseListener(new MouseListener()
		{

			public void mouseDoubleClick(MouseEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			public void mouseDown(MouseEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			public void mouseUp(MouseEvent arg0)
			{
				mulSrc.setText(Native2AsciiUtils.ascii2Native(mulTarget
						.getText()));

			}
		});

		this.initCommList();
		int index = addPage(topCom);

		setPageText(index, "Tools");
	}

	HashMap<String, String> filterMultLang = new HashMap<String, String>();

	public void addMult(String key, String value)
	{
		
		if(!commonKeyList.contains(key) && filterMultLang.get(key) == null)
		{	
		 
		 
			filterMultLang.put(key, key);
			mulSrc.append(key + "=" + value);
			mulSrc.append("\n");
			
		
		}

      
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
		// createPage2();
	}

	private void handleBrowse(Text mytext)
	{
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				new Shell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				"请选择文件夹");

		if (dialog.open() == ContainerSelectionDialog.OK)
		{
			Object[] result = dialog.getResult();
			if (result.length == 1)
			{
				mytext.setText(((IPath) result[0]).toOSString());

			}
		}

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

	public TextEditor getEditor()
	{
		return editor;
	}

	public void setEditor(TextEditor editor)
	{
		this.editor = editor;
	}
}
