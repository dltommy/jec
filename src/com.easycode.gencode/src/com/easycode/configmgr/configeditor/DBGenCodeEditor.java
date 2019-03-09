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

import net.sf.json.JSONObject;

import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.*;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.Workbench;

import com.easycode.common.EclipseUtil;
import com.easycode.common.FileUtil;
import com.easycode.common.StringUtil;
import com.easycode.common.XmlUtil;
import com.easycode.configmgr.ConfigMgrFactory;
import com.easycode.configmgr.model.Config;
import com.easycode.configmgr.model.Config.DB;
import com.easycode.gencode.ui.elements.ModelSelect;
import com.easycode.gencode.ui.elements.TableListCheckBox;
import com.easycode.gencode.ui.elements.TableListCheckBoxContentProvider;
import com.easycode.gencode.ui.elements.TableListCheckBoxLabelProvider;
import com.easycode.gencode.core.dbtool.DbMgr;
import com.easycode.gencode.core.dbtool.TableModel;
import com.easycode.gencode.core.javaparse.model.java.JavaClzModel;
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
public class DBGenCodeEditor extends MultiPageEditorPart implements
        IResourceChangeListener
{

    /** The text editor used in page 0. */
    // public TextEditor editor;
    public JCEditor editor;
    /** The font chosen in page 1. */
    private Font font;

    /** The text widget used in page 2. */
    private StyledText text;

    /**
     * Creates a multi-page editor example.
     */
    public DBGenCodeEditor()
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

    FormTitleInput mdlPath = null;
    //FormTitleInput commonLangInput = null;

 
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
 
        try
        {
            bean = ConfigMgrFactory.newByByFilePath(configPath).readOrCreate(
                    null);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        
        try
        {
            createPage2();
            //createPage1();
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    Text configInput = null;
    Combo driverComb = null;
    HashMap<Integer, DB> selectObj = new HashMap<Integer, DB>();
    Combo selDb = null;
    FormTitleInput userConfig = null;
    FormTitleInput pwdConfig = null;
    FormTitleInput dburlConfig = null;

    void createPage2() throws Exception
    { 
        FormToolkit toolkit = new FormToolkit(getContainer().getDisplay());
        Composite baseCom = toolkit.createComposite(getContainer());

        GridLayout baseComLayout = new GridLayout();
        GridData baseComLayoutData = new GridData(GridData.FILL_BOTH);
        // baseComLayout.numColumns = 2;

        baseCom.setLayout(baseComLayout);
        baseCom.setLayoutData(baseComLayoutData);

        Section topSelection = toolkit
                .createSection(baseCom, Section.TITLE_BAR);
        topSelection.setText(MultLang.getMultLang("code.045"));// 数据库工具
        GridLayout topSelectionLayout = new GridLayout();
        GridData topSelectionData = new GridData(GridData.FILL_HORIZONTAL);
        // topSelectionData.verticalSpan = 2;

        topSelection.setLayout(topSelectionLayout);
        topSelection.setLayoutData(topSelectionData);

        Composite topCom = toolkit.createComposite(topSelection);

        GridData topComLayoutData = new GridData(GridData.FILL_BOTH);
        topComLayoutData.horizontalSpan = 2;// .verticalSpan = 2;
        GridLayout topComLayout = new GridLayout();
        topComLayout.numColumns = 4;
        topCom.setLayout(topComLayout);
        topCom.setLayoutData(topComLayoutData);

        topSelection.setClient(topCom);

        toolkit.createLabel(topCom,  "名称 *");// 配置名
        configInput = toolkit.createText(topCom, "", SWT.NO);
        GridData configInputData = new GridData(GridData.BEGINNING);

        configInputData.horizontalSpan = 3;
        configInputData.widthHint = 80;
        configInput.setLayoutData(configInputData);
        // final FormTitleInput configName = new
        // FormTitleInput(toolkit,topCom,"ConfigName: *","", SWT.NO);

        dburlConfig = new FormTitleInput(toolkit, topCom, "URL: *", "", SWT.NO);

        // final FormTitleInput driverConfig = new
        // FormTitleInput(toolkit,topCom,"Driver: *","", SWT.NO);

        toolkit.createLabel(topCom, "Driver: *");

        driverComb = new Combo(topCom, SWT.READ_ONLY);
        GridData boData = new GridData(GridData.BEGINNING);
        boData.horizontalSpan = 1;
        driverComb.setLayoutData(boData);
        driverComb.setItems(new String[]
        { "", "com.mysql.jdbc.Driver", "oracle.jdbc.driver.OracleDriver" });
        driverComb.select(0);

        userConfig = new FormTitleInput(toolkit, topCom, "User: *", "", SWT.NO);
        pwdConfig = new FormTitleInput(toolkit, topCom, "PWD: *", "",
                SWT.PASSWORD);

        Composite topOptCom = toolkit.createComposite(topCom);
        GridData topOptComLayoutData = new GridData(
                GridData.HORIZONTAL_ALIGN_END);
        topOptComLayoutData.horizontalSpan = 4;
        RowLayout topOptLayout = new RowLayout(SWT.HORIZONTAL);
        topOptCom.setLayout(topOptLayout);
        topOptCom.setLayoutData(topOptComLayoutData);

        //final Label errTip = toolkit.createLabel(topOptCom,
         //       "                                                  ", SWT.NO);
        Button testBut = toolkit.createButton(topOptCom,
                MultLang.getMultLang("code.047"), SWT.NO);// 测试
        testBut.addMouseListener(new MouseListener()
        {

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
                    if("".equals(driverComb.getText().trim())
                            || "".equals(dburlConfig.getInput().trim())
                            ||"".equals(userConfig.getInput().trim())
                            ||"".equals(pwdConfig.getInput().trim())
                                          )
                                            {
                        EclipseUtil.popWarnBox(getContainer().getShell(),
                                MultLang.getMultLang("code.039"));
                        return;
                                            }
                                            
                    pass = DbMgr.testCon(driverComb.getText().trim(), dburlConfig
                            .getInput().trim(), pwdConfig.getInput().trim(),
                            userConfig.getInput().trim());
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
 
                    EclipseUtil.popWarnBox(getContainer().getShell(),
                            "链接成功！");
                }
                else
                {
                    EclipseUtil.popErrorBox(getContainer().getShell(),
                            "链接失败！");
                }
                // TODO Auto-generated method stub

            }
        });

        Button clearBut = toolkit.createButton(topOptCom,
               "清空", SWT.NO); 
        
        Button saveButt = toolkit.createButton(topOptCom,
                MultLang.getMultLang("code.019"), SWT.NO);

        Button delButt = toolkit.createButton(topOptCom, "删除", SWT.NO);

        Section midSelection = toolkit
                .createSection(baseCom, Section.TITLE_BAR);

        GridLayout midSelectionLayout = new GridLayout();
        GridData midSelectionData = new GridData(GridData.FILL_HORIZONTAL);
        midSelection.setLayout(midSelectionLayout);
        midSelection.setLayoutData(midSelectionData);

        midSelection.setText(MultLang.getMultLang("code.048"));//

        Composite midCom = toolkit.createComposite(midSelection);

        GridData midComLayoutData = new GridData(GridData.FILL_BOTH);

        final TableListCheckBoxContentProvider contentPrid = new TableListCheckBoxContentProvider();
        final TableListCheckBoxLabelProvider lablePrid = new TableListCheckBoxLabelProvider();

        GridLayout midComLayout = new GridLayout();
        midComLayout.numColumns = 3;

        midCom.setLayout(midComLayout);
        midCom.setLayoutData(midComLayoutData);

        toolkit.createLabel(midCom, "名称");// 配置
        selDb = new Combo(midCom, SWT.READ_ONLY);

        selDb.addSelectionListener(new SelectionListener()
        {

            public void widgetDefaultSelected(SelectionEvent arg0)
            {
                // TODO Auto-generated method stub

            }

            public void widgetSelected(SelectionEvent arg0)
            {
                int selectedIndex = selDb.getSelectionIndex();

                if (selectedIndex == 0)
                {

                    configInput.setText("");
                    dburlConfig.setText("");
                    driverComb.select(0);
                    userConfig.setText("");
                    pwdConfig.setText("");
                }
                else
                {
                    DB selectedBean = selectObj.get(selectedIndex);
                    configInput.setText(selectedBean.getName());
                    dburlConfig.setText(selectedBean.getUrl());
                    driverComb.setText(selectedBean.getDriver());
                    userConfig.setText(selectedBean.getUsername());
                    pwdConfig.setText(selectedBean.getPassword());
                }
            }

        });

        GridData selDbData = new GridData(GridData.BEGINNING);
        selDbData.horizontalSpan = 2;
        selDbData.widthHint = 50;
        selDb.setLayoutData(selDbData);

        Config bean = ConfigMgrFactory.newByByFilePath(configPath)
                .readOrCreate(fresh);
        int count = 0;
        List<DB> type = bean.getDbList();
        if (type != null && type.size() > 0)
        {
            if (type != null)
            {
                String selArry[] = new String[type.size() + 1];
                selArry[0] = "请选择";

                for (DB b : type)
                {
                    count++;

                    selArry[count] = b.getName();
                    selectObj.put(count, b);

                }
                selDb.setItems(selArry);
                selDb.select(0);

                {
                    configInput.setText("");

                    dburlConfig.setText("");

                    driverComb.setText("");

                    userConfig.setText("");

                    pwdConfig.setText("");

                }
            }
        }
        else
        {

            selDb.setItems(new String[]
            { "请选择" });
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

                // configName
                String configName = configInput.getText();
                if (configName == null || "".equals(configName.trim()))
                {
                    EclipseUtil.popWarnBox(getContainer().getShell(),
                            MultLang.getMultLang("code.039"));
                    return;
                }
                // url
                String url = dburlConfig.getInput();
                if (url == null || "".equals(url.trim()))
                {
 
                    EclipseUtil.popWarnBox(getContainer().getShell(),
                            MultLang.getMultLang("code.039"));
                    return;
                }
                // driver
                String driver = driverComb.getText();
                if (driver == null || "".equals(driver))
                {
                    EclipseUtil.popWarnBox(getContainer().getShell(),
                            MultLang.getMultLang("code.039"));
                    return;
                }
                // userName
                String userName = userConfig.getInput();
                if (userName == null || "".equals(userName))
                {
                    EclipseUtil.popWarnBox(getContainer().getShell(),
                            MultLang.getMultLang("code.039"));
                    return;
                }
                String pwd = pwdConfig.getInput();
                if (pwd == null || "".equals(pwd))
                {
                    EclipseUtil.popWarnBox(getContainer().getShell(),
                            MultLang.getMultLang("code.039"));
                    return;
                }

                // 先查找对象是否存在，如果已经存在则更新，不存在则新增
                int i = selDb.getSelectionIndex();
                DB upt = null;
                if (i > 0)
                {
                    upt = selectObj.get(i);
                    upt.setId(selectObj.get(i).getId());
                }
                else
                {
                    upt = new DB();
                    upt.setId(System.currentTimeMillis() + "");
                }
                upt.setDriver(driver);
                upt.setUsername(userName);
                upt.setPassword(pwd);
                upt.setUrl(url);
                upt.setName(configName);
                try
                {
                    ConfigMgrFactory.newByByFilePath(configPath)
                            .addOrUpdateDBConfig(upt, fresh);

                    Config bean = ConfigMgrFactory.newByByFilePath(configPath)
                            .readOrCreate(fresh);
                    refreshUiConfig();
                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

        });
        delButt.addMouseListener(new MouseListener()
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
                int i = selDb.getSelectionIndex();

                if (i > 0)
                {
                    boolean sure = MessageDialog.openConfirm( getContainer().getShell(), "确认", "确认删除该项？");
                    if(sure)
                    {
                        try
                        {
                            ConfigMgrFactory.newByByFilePath(configPath)
                                    .delDBConfig(selectObj.get(i).getId(), fresh);
                            refreshUiConfig();
                        }
                        catch (Exception e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                }
                else
                {
                    EclipseUtil.popWarnBox(getContainer().getShell(),
                            "请选择一个数据库配置！");
                }
            }

        });
        
        clearBut.addMouseListener(new MouseListener()
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
                    selDb.select(0);
 
                    configInput.setText("");

                    dburlConfig.setText("");

                    driverComb.setText("");

                    userConfig.setText("");

                    pwdConfig.setText("");

                
            }
            
        });
        final FormTitleInput inputQry = new FormTitleInput(toolkit, midCom,
                MultLang.getMultLang("code.050"), "%", SWT.NO);// 表名
        Button qryButt = toolkit.createButton(midCom,
                MultLang.getMultLang("code.051"), SWT.NO);// 查询

        Label selectTable = toolkit.createLabel(midCom,
                MultLang.getMultLang("code.052"));// 选择表

        GridData selectLayoutData = new GridData(GridData.BEGINNING);
        selectTable.setLayoutData(selectLayoutData);
        // Tree tree = toolkit.createTree(midCom, SWT.NO);

        Composite ctrlCom = toolkit.createComposite(midCom);
        GridLayout ctrlComLayout = new GridLayout();
        ctrlComLayout.numColumns = 4;
        GridData ctrlComGridData = new GridData(GridData.FILL_BOTH);
        ctrlComGridData.horizontalSpan = 2;
        ctrlCom.setLayout(ctrlComLayout);
        ctrlCom.setLayoutData(ctrlComGridData);

        final CheckboxTreeViewer treeview = new CheckboxTreeViewer(ctrlCom,
                SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);// new
                                                          // CheckboxTreeViewer(tree);

        GridData treeData = new GridData(GridData.VERTICAL_ALIGN_FILL);
        treeData.widthHint = 200;
        treeData.heightHint = 200;
        treeData.verticalSpan = 3;
        treeview.getTree().setLayoutData(treeData);

        optTips = toolkit.createText(ctrlCom, "", SWT.MULTI | SWT.H_SCROLL
                | SWT.V_SCROLL | SWT.READ_ONLY | SWT.WRAP);
        GridData textData = new GridData(GridData.FILL_BOTH);
        textData.horizontalSpan = 3;
        optTips.setVisible(false);
        optTips.setLayoutData(textData);

        final Combo projects = new Combo(ctrlCom, SWT.READ_ONLY);

        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = workspace.getRoot();
        IProject[] prjs = root.getProjects();

        String prjName[] = new String[prjs.length + 1];
        prjName[0] = "选择生成目录";
        int selectedIndex = 0;
        String curPrjName = getCurProjectName();
        for (int i = 0; i < prjs.length; i++)
        {
            prjName[i + 1] = prjs[i].getName();
            if (prjs[i].getName().equals(curPrjName))
            {
                selectedIndex = i + 1;
            }
        }

        projects.setItems(prjName);
        projects.select(selectedIndex);
        Button genButt = toolkit.createButton(ctrlCom,
                "代码生成", SWT.END);
        GridData genData = new GridData(GridData.VERTICAL_ALIGN_END
                | GridData.HORIZONTAL_ALIGN_END);
        // genData.horizontalSpan=2;
        genButt.setLayoutData(genData);
        treeview.setContentProvider(contentPrid);
        treeview.setLabelProvider(lablePrid);
        // treeview.setInput("root");

        treeview.addCheckStateListener(new ICheckStateListener()
        {

            public void checkStateChanged(CheckStateChangedEvent arg0)
            {
                boolean checked = arg0.getChecked();
                TableListCheckBox box = (TableListCheckBox) arg0.getElement();

                if (box.getChildList() != null)
                {
                    for (TableListCheckBox b : box.getChildList())
                    {
                        treeview.setChecked(b, checked);

                    }
                }
                 
            }
        });

        genButt.addMouseListener(new MouseListener()
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

                Object[] checked = treeview.getCheckedElements();
                List<String> tableList = new ArrayList<String>();

                if (checked.length >= 1)
                {
                    for (Object o : checked)
                    {
                        TableListCheckBox table = (TableListCheckBox) o;
                        String tableName = table.getTableName();
                        if (!"".equals(tableName))
                        {
                            tableList.add(tableName);
                        }
                    }
                }

                if (tableList.size() == 0)
                {
                    EclipseUtil.popWarnBox(getContainer().getShell(),
                            "请选择一个或多个表");
                    return;
                }

                if (projects.getSelectionIndex() == 0)
                {
                    EclipseUtil.popWarnBox(getContainer().getShell(),
                            "请选择代码生成目录！");
                    return;
                }

                IWorkspace workspace = ResourcesPlugin.getWorkspace();
                IWorkspaceRoot root = workspace.getRoot();

                IProject project = root.getProject(projects.getText());

                String projectPath = project.getLocation().toFile().getPath();

                String projectName = project.getName();

                String tableArray[] = new String[tableList.size()];
                for (int i = 0; i < tableList.size(); i++)
                {
                    tableArray[i] = tableList.get(i);
                }
                DB db = selectObj.get(selDb.getSelectionIndex());
                String json = "";
                try
                {
                    TableModel m = DbMgr.getTableInfo(db,
                            tableArray[0]);
                    m.jec.setPrjName(projectName);
                    if(db.getDriver().toUpperCase().indexOf("ORACLE") > -1)
                    {
                        m.jec.setParamFrom("oracle");
                    }
                    else if(db.getDriver().toUpperCase().indexOf("MYSQL") > -1)
                    {
                        m.jec.setParamFrom("mysql");
                    }
                    
                    DBCodeGenDialog d = new DBCodeGenDialog(new Shell(), m, tableArray, tableArray[0], projectPath, projectName);
                    d.open();
                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });

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

                TableListCheckBox root = new TableListCheckBox("");

                int selectIndex = selDb.getSelectionIndex();
                if(selectIndex == 0)
                {
                    EclipseUtil.popInfoBox(getContainer().getDisplay().getActiveShell(), "请选择一个数据库");
                    return;
                }
                DB typeBean = selectObj.get(selectIndex);
                
                TableListCheckBox[] objArr = null;
                try
                {
                    List<String> tabList = null;

                    if ("mysql".equalsIgnoreCase(typeBean.getDbtype()))
                    {
                        tabList = DbMgr.queryMysqlTabList(typeBean.getDriver(),
                                typeBean.getUrl(), typeBean.getPassword(),
                                typeBean.getUsername(), inputQry.getInput()
                                        .trim());
                    }
                    else
                    {
                        tabList = DbMgr.queryMysqlTabList(typeBean.getDriver(),
                                typeBean.getUrl(), typeBean.getPassword(),
                                typeBean.getUsername(), inputQry.getInput()
                                        .trim());
                    }

                    if (tabList != null)
                    {
                        objArr = new TableListCheckBox[tabList.size()];
                        for (int i = 0; i < tabList.size(); i++)
                        {
                            objArr[i] = new TableListCheckBox(tabList.get(i));
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
                 * treeview.setChecked(root, true); if(root.getChildList() !=
                 * null && root.getChildList().length > 0) {
                 * 
                 * }
                 */
            }

        });

        midSelection.setClient(midCom);

        int index = addPage(baseCom);
        setPageText(index, "数据库工具");
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
                this.file.refreshLocal(IResource.DEPTH_INFINITE,
                        new NullProgressMonitor());
            }
            catch (CoreException e)
            {

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
                                .getFile().getProject()
                                .equals(event.getResource()))
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

        String editorText = editor.getDocumentProvider()
                .getDocument(editor.getEditorInput()).get();

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

    private String getCurProjectName()
    {

        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow wind = workbench.getActiveWorkbenchWindow();
        IWorkbenchPage page = wind.getActivePage();
        // 当前项目路径
        // String curprojectPath = null;
        // 当前项目名称
        String curprojectName = null;
        if (page != null)
        {
            IEditorPart part = page.getActiveEditor();
            if (part != null)
            {

                if (true)
                {
                    IEditorInput input = part.getEditorInput();

                    if (input instanceof IFileEditorInput)
                    {

                        IFile file = ((IFileEditorInput) input).getFile();

                        // String filePath =
                        // file.getLocation().toFile().getPath();

                        IProject project = file.getProject();

                        // curprojectPath =
                        // project.getLocation().toFile().getPath();

                        curprojectName = project.getName();
                    }
                }
            }
        }
        if (curprojectName == null)
        {

            return getProjectName();
        }
        return curprojectName;
    }

    public static String getProjectName()
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

        if (project != null)
        {
            return project.getName();
        }
        return null;
    }

    private void refreshUiConfig()
    {
        int count = 0;
         
        try
        {
            this.bean = ConfigMgrFactory.newByByFilePath(configPath)
                    .readOrCreate(fresh);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        List<DB> type = bean.getDbList();
        if (type != null && type.size() > 0)
        {
            if (type != null)
            {
                String selArry[] = new String[type.size() + 1];
                selArry[0] = "请选择";

                for (DB b : type)
                {
                    count++;

                    selArry[count] = b.getName();
                    selectObj.put(count, b);
                    // count ++;
                }
                selDb.setItems(selArry);
                selDb.select(0);

                {
                    configInput.setText("");

                    dburlConfig.setText("");

                    driverComb.setText("");

                    userConfig.setText("");

                    pwdConfig.setText("");

                }
            }
        }
        else
        {

            selDb.setItems(new String[]
            { "请选择" });
            selDb.select(0);
        }

    }
}
