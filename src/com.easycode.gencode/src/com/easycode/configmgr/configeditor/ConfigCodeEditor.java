package com.easycode.configmgr.configeditor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.*;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.ide.IDE;

  
import com.easycode.common.EclipseUtil;
import com.easycode.common.FileUtil;
import com.easycode.common.XmlUtil;
import com.easycode.configmgr.ConfigMgrFactory;
import com.easycode.configmgr.model.Config;
import com.easycode.configmgr.model.Config.DB;
import com.easycode.dbtopojo.DbMgr; 
import com.easycode.dbtopojo.ui.TableCheckBoxObj;
import com.easycode.dbtopojo.ui.TableSelectContentProvider;
import com.easycode.dbtopojo.ui.TableSelectLabelProvider;
import com.easycode.javaparse.model.java.JavaClzModel;
import com.easycode.javaparse.model.java.JavaClzModel.Pkg;
import com.easycode.resource.MultLang; 
import com.easycode.templatemgr.RpcFactory;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * An example showing how to create a multi-page editor. This example has 3
 * pages:
 * <ul>
 * <li>page 0 contains a nested text editor.
 * <li>page 1 allows you to change the font used in page 2
 * <li>page 2 shows the words in page 0 in sorted order
 * </ul>
 */
public class ConfigCodeEditor extends MultiPageEditorPart implements
		IResourceChangeListener
{

	/** The text editor used in page 0. */
	//public TextEditor editor;
	public JCEditor editor;
	/** The font chosen in page 1. */
	private Font font;

	/** The text widget used in page 2. */
	private StyledText text;

	/**
	 * Creates a multi-page editor example.
	 */
	public ConfigCodeEditor()
		{
			super();
			ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
		}

	/**
	 * Creates page 0 of the multi-page editor, which contains a text editor.
	 */

	/**
	 * Creates page 1 of the multi-page editor, which allows you to change the
	 * font used in page 2.
	 */
	void createPage1()
	{
		final SimpleDateFormat dateFt = new SimpleDateFormat("HH:mm:ss");

		
		FormToolkit toolkit = new FormToolkit(getContainer().getDisplay());
		final Composite topCom = toolkit.createComposite(getContainer());
		
		GridLayout topComLayout = new GridLayout();
		
		GridData topComLayoutData = new GridData(GridData.FILL_BOTH);
		topComLayout.numColumns = 2;
		
		topCom.setLayout(topComLayout);
		topCom.setLayoutData(topComLayoutData);
		Section leftSelection = toolkit.createSection(topCom,Section.TITLE_BAR);
		leftSelection.setText("User Register!");
		leftSelection.setLayout(new GridLayout());
		leftSelection.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite leftCom = toolkit.createComposite(leftSelection);


		
		//leftCom.setToolTipText("用户注册");
		GridData leftComLayoutData = new GridData(GridData.FILL_BOTH);
		GridLayout leftComLayout = new GridLayout();
		leftComLayout.numColumns =2;
		leftCom.setLayout(leftComLayout);
		leftCom.setLayoutData(leftComLayoutData);
		final FormTitleInput idInput = new FormTitleInput(toolkit,leftCom,"ID *",bean.getUserName(),SWT.NO);
		final FormTitleInput pwdInput = new FormTitleInput(toolkit,leftCom,MultLang.getMultLang("code.033")+" *",bean.getPassword(), SWT.PASSWORD);//密码
		
		Composite leftButCom = toolkit.createComposite(leftCom);
		
		GridData leftButComLayoutData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		leftButComLayoutData.horizontalSpan = 2;
		
		leftButCom.setLayoutData(leftButComLayoutData);
		leftButCom.setLayout(new FillLayout());
		
		
		
		
		//GridData tipsLayoutData = new GridData(GridData.VERTICAL_ALIGN_CENTER);
		//tipsLayoutData.horizontalSpan = 2;
		//final Label tips = toolkit.createLabel(leftCom, "                                    ");
		
		//tipsLayoutData.widthHint=300;
		//tips.setLayoutData(tipsLayoutData);
		
		
		leftSelection.setClient(leftCom);
		
		
		Button regBut = toolkit.createButton(leftButCom, MultLang.getMultLang("code.034"), SWT.NO);//注册
		Button saveUserBut = toolkit.createButton(leftButCom,MultLang.getMultLang("code.019"), SWT.NO);//保存


		Section rightSelection = toolkit.createSection(topCom,Section.TITLE_BAR);
		rightSelection.setText(MultLang.getMultLang("code.035"));//配置项编辑
		rightSelection.setLayout(new GridLayout());
		rightSelection.setLayoutData(new GridData(GridData.FILL_BOTH));
		 
		
		
		Composite rightCom = toolkit.createComposite(rightSelection);
		GridData rightComLayoutData = new GridData(GridData.FILL_BOTH);
		GridLayout rightComLayout = new GridLayout();
		rightComLayout.numColumns =2;
		rightCom.setLayout(rightComLayout);
		rightCom.setLayoutData(rightComLayoutData);
		rightSelection.setClient(rightCom);

		
		final FormTitleInput mdlPath = new FormTitleInput(toolkit,rightCom,MultLang.getMultLang("code.036")+" *",bean.getLocalTemplatePath(), SWT.NO);
		final FormTitleInput urlConfig = new FormTitleInput(toolkit,rightCom,"URL",bean.getSrvUrl().trim(), SWT.NO);
		final FormTitleInput typeConfig = new FormTitleInput(toolkit,rightCom,MultLang.getMultLang("code.009")+" *",bean.getCodeType(),SWT.NO);
		
		String commonLang = "";
		if(bean.getCommonLangProp() != null)
		{
			commonLang = bean.getCommonLangProp();//.getCustSet().getTools().getCommonLang();
		}
		final FormTitleInput commonLangInput = new FormTitleInput(toolkit,rightCom,"多语言资源文件",commonLang,SWT.NO);
		
		String langProp = "";
		if(bean.getLangProp() != null)
		{
			langProp = bean.getLangProp();
		}
		//final FormTitleInput langPropInput = new FormTitleInput(toolkit,rightCom,MultLang.getMultLang("code.083"),langProp,SWT.NO);//资源文件
		
		//langPropInput.disableEdit();
		final FormTitleInput mutLangConfig = new FormTitleInput(toolkit,rightCom,MultLang.getMultLang("code.038")+" *",bean.getMultLangFlag(),SWT.NO);	

		
		String exceptFlag = "";
		if(bean.getMultLangFilt() != null)
		{
			exceptFlag=bean.getMultLangFilt();
		}
		//final FormTitleInput filtKey = new FormTitleInput(toolkit,rightCom,MultLang.getMultLang("code.082"),exceptFlag,SWT.NO);//过滤标签
		
		//GridData tipsrightLayoutData = new GridData(GridData.VERTICAL_ALIGN_CENTER);
		//tipsrightLayoutData.horizontalSpan = 2;
		//tipsrightLayoutData.widthHint=300;
		//final Label tipsRight = toolkit.createLabel(rightCom, "  ");
		
		//tipsRight.setLayoutData(tipsrightLayoutData);
		
		
		Button saveBut = toolkit.createButton(rightCom, MultLang.getMultLang("code.019"), SWT.NO);//保存
		GridData saveLayoutdata = new GridData(GridData.HORIZONTAL_ALIGN_END);
		saveLayoutdata.horizontalSpan=2;
		saveBut.setLayoutData(saveLayoutdata);
		
		//
		
		saveBut.addMouseListener(
				new MouseListener()
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
						if("".equals(mdlPath.getInput().trim())
						    ||   "".equals(typeConfig.getInput().trim())
						    || "".equals(mutLangConfig.getInput().trim())
						        )
						{
							EclipseUtil.popErrorBox(topCom.getShell(), MultLang.getMultLang("code.039"));//
							//.setText(MultLang.getMultLang("code.039")+"("+dateFt.format(new Date())+")");//必填项不能为空
							return;
						}
			 
		 
						bean.setLocalTemplatePath(mdlPath.getInput().trim());
						
						bean.setCodeType(typeConfig.getInput().trim());
						bean.setSrvUrl(urlConfig.getInput().trim());
						//bean.setLangProp(langPropInput.getInput());

						bean.setCommonLangProp(commonLangInput.getInput().trim());
						bean.setMultLangFlag(mutLangConfig.getInput().trim());
						//bean.setMultLangFilt(filtKey.getInput().trim());
						try
						{ 
							ConfigMgrFactory.newByByFilePath(configPath).update(bean, fresh);
							//tipsRight.setText("Succee("+dateFt.format(new Date())+")");//保存成功
							EclipseUtil.popInfoBox(topCom.getShell(),"保存成功");
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
							EclipseUtil.proExcept(topCom, e);
							//EclipseUtil.popErrorBox(topCom.getShell(), e.getMessage());
						}
					}
					
				}
		);
		
		regBut.addMouseListener(new MouseListener()
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

				try
				{
					if("".equals(idInput.getInput().trim()) || "".equals(pwdInput.getInput().trim()))
					{
						//tips.setText(MultLang.getMultLang("code.039")+"("+dateFt.format(new Date())+")");//ID不能为空
						EclipseUtil.popErrorBox(topCom.getShell(), "账号或密码不能为空！");
						return;
					}
		 
					String ret = RpcFactory.httpSrv(urlConfig.getInput().trim()).regCodegenSeed(idInput.getInput().trim(), pwdInput.getInput().trim());
					if("0000".equals(ret))
					{

						bean.setUserName(idInput.getInput().trim());
						bean.setPassword(pwdInput.getInput().trim());
						ConfigMgrFactory.newByByFilePath(configPath).update(bean, fresh);
						EclipseUtil.popInfoBox(topCom.getShell(), MultLang.getMultLang("code.040"));
					}
					else if("0001".equals(ret))
					{
						EclipseUtil.popErrorBox(topCom.getShell(), MultLang.getMultLang("code.041"));
					}
					else
					{ 
						EclipseUtil.popErrorBox(topCom.getShell(), MultLang.getMultLang("code.042")+ret);
					}
				}
				catch (Exception e)
				{ 
					EclipseUtil.proExcept(topCom, e);
				}
			}
			
		});
		saveUserBut.addMouseListener(new MouseListener(){

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
				if("".equals(idInput.getInput().trim()) || "".equals(pwdInput.getInput().trim()))
				{ 
					EclipseUtil.popErrorBox(topCom.getShell(), "用户名或密码不能为空！");
					return;
				}
		 
				bean.setUserName(idInput.getInput().trim());
				bean.setPassword(pwdInput.getInput().trim());
				try
				{
					//bean.save(new File(configPath));
					ConfigMgrFactory.newByByFilePath(configPath).update(bean, fresh);
					//tips.setText("Success("+dateFt.format(new Date())+")");
					EclipseUtil.popInfoBox(topCom.getShell(), "操作成功！");
				}
				catch (Exception e)
				{ 
					EclipseUtil.proExcept(topCom, e);
				}
			}});
		

		
		// 查询本地存储
		//maintance.queryLocal();

		//int index = addPage(composite);
		int index = addPage(topCom);
		setPageText(index, "ItemConfig");
	}

	/**
	 * Creates page 2 of the multi-page editor, which shows the sorted text.
	 */


	/**
	 * Creates the pages of the multi-page editor.
	 */
	private Config bean = null;
    private String configPath = null;
	Text optTips = null;
	private FreshFile fresh = null;
	protected void createPages()  
	{
		//createPage0();
		
		FileEditorInput f = (FileEditorInput)getEditorInput();
		 
		
		configPath = f.getFile().getLocation().toFile().getPath();
		fresh = new FreshFile(f.getFile());
		try {
			bean = ConfigMgrFactory.newByByFilePath(configPath).readOrCreate(fresh);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		createPage1();
		try {
			createPage2();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	void createPage2() throws Exception
	{
		//String curConfigId = null; 
		//HashMap<String,XmlDbTypeBean> configMap = this.readDbConfig();
		final SimpleDateFormat dateFt = new SimpleDateFormat("HH:mm:ss");
		final FileEditorInput f = (FileEditorInput)getEditorInput();
		final String configPath = f.getFile().getLocation().toFile().getPath();
		//System.err.println("文件路径"+f.getFile().getLocation().toFile().getPath());

		//final IConfig bean = ConfigMgr.newConfig(f.getFile().getLocation().toFile().getPath());

	 
		FormToolkit toolkit = new FormToolkit(getContainer().getDisplay());
		Composite baseCom = toolkit.createComposite(getContainer());
		

		
		
		GridLayout baseComLayout = new GridLayout();
		GridData baseComLayoutData = new GridData(GridData.FILL_BOTH);
		//baseComLayout.numColumns = 2;
		
		baseCom.setLayout(baseComLayout);
		baseCom.setLayoutData(baseComLayoutData);
		
		
		Section topSelection = toolkit.createSection(baseCom,Section.TITLE_BAR);
		topSelection.setText(MultLang.getMultLang("code.045"));//数据库工具
		GridLayout topSelectionLayout = new GridLayout();
		GridData  topSelectionData = new GridData(GridData.FILL_HORIZONTAL);
		//topSelectionData.verticalSpan = 2;
		
		topSelection.setLayout(topSelectionLayout);
		topSelection.setLayoutData(topSelectionData);
		
		Composite topCom = toolkit.createComposite(topSelection);
		
		GridData topComLayoutData = new GridData(GridData.FILL_BOTH);
		topComLayoutData.horizontalSpan=2;//.verticalSpan = 2;
		GridLayout topComLayout = new GridLayout();
		topComLayout.numColumns =4;
		topCom.setLayout(topComLayout);
		topCom.setLayoutData(topComLayoutData);
		
		topSelection.setClient(topCom);
		
		
		toolkit.createLabel(topCom, MultLang.getMultLang("code.046")+" *");//配置名
		final Text configInput = toolkit.createText(topCom, "", SWT.NO);
		GridData  configInputData = new GridData(GridData.BEGINNING);
		
		configInputData.horizontalSpan = 3;
		configInputData.widthHint=80;
		configInput.setLayoutData(configInputData);
		//final FormTitleInput configName = new FormTitleInput(toolkit,topCom,"ConfigName: *","", SWT.NO);
		

		final FormTitleInput urlConfig = new FormTitleInput(toolkit,topCom,"URL: *","", SWT.NO);
		
		//final FormTitleInput driverConfig = new FormTitleInput(toolkit,topCom,"Driver: *","", SWT.NO);
		
		
		
		toolkit.createLabel(topCom, "Driver: *");

		final Combo driverComb = new Combo(topCom, SWT.READ_ONLY);
		GridData  boData = new GridData(GridData.BEGINNING);
		boData.horizontalSpan= 1;
		driverComb.setLayoutData(boData);
		driverComb.setItems(new String[]{"","com.mysql.jdbc.Driver","oracle.jdbc.driver.OracleDriver"});
		driverComb.select(0);
		
		
		
		
		final FormTitleInput userConfig = new FormTitleInput(toolkit,topCom,"User: *","", SWT.NO);
		final FormTitleInput pwdConfig = new FormTitleInput(toolkit,topCom,"PWD: *","", SWT.PASSWORD);

        
		Composite topOptCom = toolkit.createComposite(topCom);
		GridData topOptComLayoutData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		topOptComLayoutData.horizontalSpan=4;
		RowLayout topOptLayout = new RowLayout(SWT.HORIZONTAL);
		topOptCom.setLayout(topOptLayout);
		topOptCom.setLayoutData(topOptComLayoutData);
		
		
		
		final Label errTip = toolkit.createLabel(topOptCom, "                                                  ",SWT.NO);
		Button testBut = toolkit.createButton(topOptCom, MultLang.getMultLang("code.047"), SWT.NO);//测试
		testBut.addMouseListener(new MouseListener(){

			public void mouseDoubleClick(MouseEvent arg0)
			{
				
			}

			public void mouseDown(MouseEvent arg0)
			{
				// TODO Auto-generated method stub
				
			}

			public void mouseUp(MouseEvent arg0)
			{
				boolean pass = false;
				try
				{
					System.err.println("----->"+driverComb.getText());
					pass = DbMgr.testCon(driverComb.getText().trim(),
							urlConfig.getInput().trim(), pwdConfig.getInput()
									.trim(), userConfig.getInput().trim());
				}
				catch (Exception e)
				{
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					PrintStream ps = new PrintStream(baos);
					e.printStackTrace(ps);
					optTips.setVisible(true);
					optTips.setText("");
					optTips.append(baos.toString());
					try
					{
						baos.close();
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}

				}
				if (pass)
				{
					errTip.setText("connection success.("
							+ dateFt.format(new Date()) + ")");
				}
				else
				{
					errTip.setText("connection false.("
							+ dateFt.format(new Date()) + ")");
				}
				// TODO Auto-generated method stub

			}});
		
		
		Button saveButt = toolkit.createButton(topOptCom, MultLang.getMultLang("code.019"), SWT.NO);
		//toolkit.createButton(topCom, "Connect", SWT.NO);
		
		
		Section midSelection = toolkit.createSection(baseCom,Section.TITLE_BAR);
		
		GridLayout midSelectionLayout = new GridLayout();
		GridData  midSelectionData = new GridData(GridData.FILL_HORIZONTAL);
		midSelection.setLayout(midSelectionLayout);
		midSelection.setLayoutData(midSelectionData);
		
		midSelection.setText(MultLang.getMultLang("code.048"));//POJO类生成

		Composite midCom = toolkit.createComposite(midSelection);
		
		GridData midComLayoutData = new GridData(GridData.FILL_BOTH);
		
		
		final TableSelectContentProvider contentPrid = new TableSelectContentProvider();
		final TableSelectLabelProvider lablePrid = new TableSelectLabelProvider();
		
		GridLayout midComLayout = new GridLayout();
		midComLayout.numColumns = 3;
		
		midCom.setLayout(midComLayout);
		midCom.setLayoutData(midComLayoutData);
		
		 
		 
		toolkit.createLabel(midCom, MultLang.getMultLang("code.049"));
		final Combo selDb = new Combo(midCom, SWT.READ_ONLY);
		final HashMap<Integer,DB> selectObj = new HashMap<Integer, DB>();
		selDb.addSelectionListener(new SelectionListener(){

			public void widgetDefaultSelected(SelectionEvent arg0)
			{
				// TODO Auto-generated method stub
				
			}

			public void widgetSelected(SelectionEvent arg0)
			{ 
				int selectedIndex = selDb.getSelectionIndex();
				
				if(selectedIndex == 0)
				{
					 
					configInput.setText("");
					urlConfig.setText("");
					driverComb.select(0);
					userConfig.setText("");
					pwdConfig.setText("");
				}
				else
				{
					DB selectedBean = selectObj.get(selectedIndex);
					configInput.setText(selectedBean.getName());
					urlConfig.setText(selectedBean.getUrl());
					driverComb.setText(selectedBean.getDriver());
					userConfig.setText(selectedBean.getUsername());
					pwdConfig.setText(selectedBean.getPassword());
				}
			}
			
		});
		
		GridData  selDbData = new GridData(GridData.BEGINNING);
		selDbData.horizontalSpan= 2;
		selDbData.widthHint=50;
		selDb.setLayoutData(selDbData);
 
		
		
	    Config bean = ConfigMgrFactory.newByByFilePath(configPath).readOrCreate(fresh);
	    int count = 0; 
		List<DB> type =  bean.getDbList();
		if(type != null && type.size()>0)
		{ 
			if(type != null)
			{
				String selArry[] = new String[type.size()+1];
				selArry[0] = "新 增";
				 
				for(DB b:type)
				{
					count++;

					selArry[count] = b.getName();
			    	selectObj.put(count, b);
			    	 
				}
				selDb.setItems(selArry);	
				selDb.select(0);
			 
				{  
					configInput.setText("");
					 
					urlConfig.setText("");
					 
					driverComb.setText("");
				 
					userConfig.setText("");
					 
					pwdConfig.setText("");
					 
				}
			}
		}
		else
		{
			 
			 selDb.setItems(new String[]{"新 增"});	
			 selDb.select(0);
		}
	    
	    
 

		saveButt.addMouseListener(new MouseListener()
		{

			public void mouseDoubleClick(MouseEvent arg0)
			{

			}

			public void mouseDown(MouseEvent arg0)
			{

			}

			public void mouseUp(MouseEvent arg0)
			{
				//DBType
				
				/*
				String dbTypeSelect = bo.getText();
				if(dbTypeSelect == null || "".equals(dbTypeSelect.trim()))
				{
					errTip.setText(MultLang.getMultLang("code.039")+"("+dateFt.format(new Date())+")");
					return;
				}
				*/
				//configName
				String configName = configInput.getText();
				if(configName == null || "".equals(configName.trim()))
				{
					errTip.setText(MultLang.getMultLang("code.039")+"("+dateFt.format(new Date())+")");
					return;
				}
				//url
				String url = urlConfig.getInput();
				if(url == null || "".equals(url.trim()))
				{
					errTip.setText(MultLang.getMultLang("code.039")+"("+dateFt.format(new Date())+")");
					return;
				}
				//driver
				String driver = driverComb.getText();
				if(driver == null || "".equals(driver))
				{
					errTip.setText(MultLang.getMultLang("code.039")+"("+dateFt.format(new Date())+")");
					return;
				}
				//userName
				String userName = userConfig.getInput();
				if(userName == null || "".equals(userName))
				{
					errTip.setText(MultLang.getMultLang("code.039")+"("+dateFt.format(new Date())+")");
					return;
				}
				String pwd = pwdConfig.getInput();
				if(pwd==null || "".equals(pwd))
				{
					errTip.setText(MultLang.getMultLang("code.039")+"("+dateFt.format(new Date())+")");
					return;
				}
				
				//先查找对象是否存在，如果已经存在则更新，不存在则新增
				int i = selDb.getSelectionIndex();
				if(i > 0)
				{
					//DB stypeBean = selectObj.get(i);
					//DB stypeBean.getId()
					DB upt = new DB();
					upt.setId(selectObj.get(i).getId());
					upt.setDriver(driver);
					upt.setUsername(userName);
					upt.setPassword(pwd);
					upt.setUrl(url);
					upt.setName(configName);
					try {
						ConfigMgrFactory.newByByFilePath(configPath).addOrUpdateDBConfig(upt,fresh);
						
					    Config bean = ConfigMgrFactory.newByByFilePath(configPath).readOrCreate(fresh);
					    int count = 0; 
						List<DB> type =  bean.getDbList();
						if(type != null && type.size()>0)
						{ 
							if(type != null)
							{
								String selArry[] = new String[type.size()+1];
								selArry[0] = "新 增";
								 
								for(DB b:type)
								{
									count++;

									selArry[count] = b.getName();
							    	selectObj.put(count, b);
							    	//count ++;
								}
								selDb.setItems(selArry);	
								selDb.select(0);
							 
								{  
									configInput.setText("");
									 
									urlConfig.setText("");
									 
									driverComb.setText("");
								 
									userConfig.setText("");
									 
									pwdConfig.setText("");
									 
								}
							}
						}
						else
						{
							 
							 selDb.setItems(new String[]{"新 增"});	
							 selDb.select(0);
						}
						
						
						
						
						
						
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					 
				}
				
	 
			    
				
			}

		});
		
		
		final FormTitleInput inputQry = new FormTitleInput(toolkit,midCom,MultLang.getMultLang("code.050"),"%", SWT.NO);//表名
		Button qryButt = toolkit.createButton(midCom, MultLang.getMultLang("code.051"), SWT.NO);//查询
		
		
		Label selectTable = toolkit.createLabel(midCom, MultLang.getMultLang("code.052"));//选择表
		
		GridData selectLayoutData = new GridData(GridData.BEGINNING);
		//selectLayoutData.verticalSpan=2;
		//selectLayoutData.widthHint=100;
		selectTable.setLayoutData(selectLayoutData);
		//Tree tree = toolkit.createTree(midCom, SWT.NO);
		
		
		Composite ctrlCom = toolkit.createComposite(midCom);
		GridLayout ctrlComLayout = new GridLayout();
		ctrlComLayout.numColumns = 4;
		GridData ctrlComGridData = new GridData(GridData.FILL_BOTH);
		ctrlComGridData.horizontalSpan = 2;
		ctrlCom.setLayout(ctrlComLayout);
		ctrlCom.setLayoutData(ctrlComGridData);
		
		
		final CheckboxTreeViewer treeview = new CheckboxTreeViewer(ctrlCom,SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);//new CheckboxTreeViewer(tree);
		
         
		GridData treeData = new GridData(GridData.VERTICAL_ALIGN_FILL);
		treeData.widthHint=200;
		treeData.heightHint=200;
		treeData.verticalSpan=3;
		treeview.getTree().setLayoutData(treeData);
		
		
		optTips = toolkit.createText(ctrlCom, "",SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.READ_ONLY | SWT.WRAP);
		GridData textData = new GridData(GridData.FILL_BOTH);
		textData.horizontalSpan=3;
		optTips.setVisible(false);
		optTips.setLayoutData(textData);
		
		final FormTitleInput pkgInput = new FormTitleInput(toolkit,ctrlCom,MultLang.getMultLang("code.053"),"", SWT.NO);//包
		
		Button genButt = toolkit.createButton(ctrlCom, MultLang.getMultLang("code.054"), SWT.END);
		GridData genData = new GridData(GridData.VERTICAL_ALIGN_END | GridData.HORIZONTAL_ALIGN_END);
		//genData.horizontalSpan=2;
		genButt.setLayoutData(genData);
		treeview.setContentProvider(contentPrid);
		treeview.setLabelProvider(lablePrid);
		//treeview.setInput("root");
		
		
		treeview.addCheckStateListener(new ICheckStateListener()
		{

			public void checkStateChanged(CheckStateChangedEvent arg0)
			{
				boolean checked = arg0.getChecked();
				TableCheckBoxObj box = (TableCheckBoxObj) arg0.getElement();

				//java.util.List<SelectCheckBox> retList = new ArrayList<SelectCheckBox>();
				//SelectCheckBox.getSubList(box, retList);

				if (box.getChildList() != null)
				{
					for (TableCheckBoxObj b : box.getChildList() )
					{
						treeview.setChecked(b, checked);

					}
				}
				//System.err.println("选中:" + box.getValue());
			}
		});
		
		
		
		genButt.addMouseListener(new MouseListener(){

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
				if("".equals(pkgInput.getInput().trim()))
				{
					optTips.setVisible(true);
					optTips.setText(MultLang.getMultLang("code.055"));//包不能为空！
					return;
				}
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				int selectedIndex = selDb.getSelectionIndex();
				DB selectedBean = selectObj.get(selectedIndex);
				
				//获取选中的对象.
				Object[] checkObj =   (Object[])treeview.getCheckedElements();
				if(checkObj != null)
				{
				
					optTips.setVisible(true);
					optTips.setText("");
					optTips.append("开始...\n");
					for(Object j:checkObj)
					{
						TableCheckBoxObj selObj = (TableCheckBoxObj)j;
						String tabName = selObj.getTableName();
						if("".equals(tabName.trim()))
						{
							continue;
						}
						try
						{
						   
							JavaClzModel model = DbMgr.queryJavaClzInfo(selectedBean.getDriver(), selectedBean.getUrl(), selectedBean.getPassword(), selectedBean.getUsername(), tabName.trim());
							model.setPkg(new Pkg(pkgInput.getInput().trim()));
							//开始生产文件
							InputStream is = ConfigCodeEditor.class.getResourceAsStream("POJO.ftl");
							java.io.BufferedInputStream bis = new java.io.BufferedInputStream(is);
							java.io.InputStreamReader isr = new java.io.InputStreamReader(bis);
							java.io.BufferedReader br = new java.io.BufferedReader(isr);
							
							StringBuffer tempCtxBf = new StringBuffer();
							String temp = null;
							while(true)
							{
								temp = br.readLine();
								if(temp != null)
								{
									tempCtxBf.append(temp+"\n");
								}
								else
								{
									break;
								}
							}
							try
							{
							    genPojo(tempCtxBf.toString(), model);
							}
							catch(Exception e)
							{
								optTips.append("Err Generate:"+model.getClzName());
								e.printStackTrace();
							}
							optTips.append("OK:GenConfig/AUTO_GEN/"+model.getPkg().getPkgName()+"."+model.getClzName());
							optTips.append("\n");
							
						}
						catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					optTips.append("结束...\n");
				}
				
			}});
		
		qryButt.addMouseListener(new MouseListener()
		{

			public void mouseDoubleClick(MouseEvent arg0)
			{

			}

			public void mouseDown(MouseEvent arg0)
			{
			}

			public void mouseUp(MouseEvent arg0)
			{

				/*
				TableCheckBoxObj[] objArr = new TableCheckBoxObj[10];
				for(int i=0;i<10;i++)
				{
					TableCheckBoxObj o = new TableCheckBoxObj("select"+i);
					objArr[i]=o;
				}
				*/
				TableCheckBoxObj root = new TableCheckBoxObj("");
				
				
				int selectIndex = selDb.getSelectionIndex();
				DB typeBean = selectObj.get(selectIndex);
				TableCheckBoxObj[] objArr = null;
				try
				{
					List<String> tabList = null;
					
					if("mysql".equalsIgnoreCase(typeBean.getDbtype()))
					{		
					    tabList = DbMgr.queryMysqlTabList(typeBean.getDriver(), typeBean.getUrl(), typeBean.getPassword(), typeBean.getUsername(), inputQry.getInput().trim());
					}
					else
					{
						tabList = DbMgr.queryMysqlTabList(typeBean.getDriver(), typeBean.getUrl(), typeBean.getPassword(), typeBean.getUsername(), inputQry.getInput().trim());
					}
					
					if(tabList != null)
			        {
			        	objArr = new TableCheckBoxObj[tabList.size()];
			        	for(int i=0;i< tabList.size();i++)
			        	{
			        		objArr[i]=new TableCheckBoxObj(tabList.get(i));
			        	}
			        }
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				root.setChildList(objArr);
				contentPrid.setRoot(root);
				treeview.setInput("root");
				treeview.expandAll();
				treeview.setAllChecked(true);
				
				/*
				 * treeview.setChecked(root, true);
				if(root.getChildList() != null && root.getChildList().length > 0)
				{
					
				}
				*/
			}

		});
		
		midSelection.setClient(midCom);
		
		
		

		
		int index = addPage(baseCom);
		setPageText(index, "Database Tool");
	}
 
	public String genPojo(String tempCtx, JavaClzModel clzModel) throws Exception
	{
		StringTemplateLoader strLoader = new StringTemplateLoader();
		strLoader.putTemplate("t1", tempCtx);
		Configuration conf = new Configuration();
		conf.setNumberFormat("0.#");
		conf.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
		conf.setTemplateLoader(strLoader);
		conf.setClassicCompatible(true);
		conf.setEncoding(Locale.CHINA, "UTF-8");
		HashMap<String, Object> root = new HashMap<String, Object>();

		if(clzModel != null)
		{
			root.put("sys", clzModel);
		}
		StringWriter sw = new StringWriter();
		try
		{
			Template temp = conf.getTemplate("t1");
			// temp.process(root, sw);
			temp.process(root, sw);
		}
		catch (TemplateException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getFTLInstructionStack();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//创建文件
		FileEditorInput f = (FileEditorInput)getEditorInput();
		final String configPath = f.getFile().getLocation().toFile().getParentFile().toString();
		String relPath = clzModel.getPkg().getPkgName().replaceAll("\\.", "/");
		String path = configPath+"/AUTO_GEN/"+relPath+"/"+clzModel.getClzName()+".java";
		//System.err.println("生产文件:"+path);
		FileUtil.createFile(path, sw.toString(), true, true);
		return null;
	}
	
    public class FreshFile implements XmlUtil.Callback
    {

        private IFile file = null;
        public FreshFile(IFile file)
        {
            this.file = file;
        }
        public void doback()
        {
            try
            {
                this.file.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
            }
            catch (CoreException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
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
}


