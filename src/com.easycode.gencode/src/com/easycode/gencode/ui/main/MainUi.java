/**
 * 作 者: ouyangchao
 * 日 期: 2011-12-28
 * 描 叙:
 */
package com.easycode.gencode.ui.main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher; 


import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;

import org.eclipse.jface.viewers.ICheckStateListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.SashForm; 
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout; 
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite; 
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text; 

import com.easycode.gencode.service.GenView;
import com.easycode.gencode.ui.CheckBoxTreeContentProvider;
import com.easycode.gencode.ui.CheckBoxTreeLabelProvider;
import com.easycode.gencode.ui.IReload;

import com.easycode.javaparse.CompilationUnitParseUtil;
import com.easycode.common.EclipseConsoleUtil;
import com.easycode.common.EclipseUtil; 
import com.easycode.configmgr.model.Config;
import com.easycode.gencode.service.FileGen;
import com.easycode.javaparse.JavaSrcParse;
import com.easycode.templatemgr.ITemplateMgr;
import com.easycode.templatemgr.LocalTemplateMgr;
import com.easycode.templatemgr.RpcFactory;
import com.easycode.gencode.ui.elements.SelectCheckBox; 
 
import com.easycode.resource.MultLang;
import com.easycode.templatemgr.util.SrcUtil; 
import com.easycode.templatemgr.model.CodegenTemplate;
import com.easycode.templatemgr.model.PageData;
import com.easycode.gencode.ui.elements.ModelSelect;

/**
 * 功能描叙: 编 码:   完成时间: 2011-12-28 下午11:44:49
 */
public class MainUi implements MouseListener,IReload
{
    private PatternCompiler pc = new Perl5Compiler();
    private PatternMatcher pm = new Perl5Matcher();
    private String regMdl = "^(.*)#(.+?)#(.+?)#(.+?)$";
    private Pattern pattFile = pc.compile(regMdl,
            Perl5Compiler.CASE_INSENSITIVE_MASK);

    protected PatternCompiler docPc = new Perl5Compiler();
    protected PatternMatcher docPm = new Perl5Matcher();
    private String docRegx = "<ftl_doc>([\\s|\\S]*?)</ftl_doc>"; 
    public static String errWarnRegx = "<((ftl_warn)|(ftl_err))>([^<]*?)</((ftl_warn)|(ftl_err))>"; 
    protected Pattern docPattFile = docPc.compile(docRegx,
            Perl5Compiler.CASE_INSENSITIVE_MASK);
 
    protected String projectName = null;
    protected Config config = null; 
    protected CheckboxTreeViewer checkTree = null;
    protected CheckBoxTreeLabelProvider viewTreeLabel = new CheckBoxTreeLabelProvider();
    protected CheckBoxTreeContentProvider ctxPrv = new CheckBoxTreeContentProvider();
    
    protected FileGen curFtlModel = null;
     
    protected String currentSeed = null;
    protected Composite topCom = null;

    protected Integer currentPage = 1;
    protected Integer eachPageSize = 20;
    protected Integer totalPage = 0;

    // 查询条件
    protected Text queryCondition = null;
    protected Combo queryConditionType = null;

    // 查询模板类型
    protected Combo queryModelType = null;
    // 查询模板位置
    protected Combo queryModelPos = null;

    protected Button button = null;

    private List mudList = null;
    private CTabFolder rightMainTab = null;
 
    private HashMap<Integer, String> indexMuId = new HashMap<Integer, String>();
    private HashMap<Integer, String> localMuld = new HashMap<Integer, String>();
    private ListItemSelect listObj = null;
    private Label pageInfoLabel = null;
    private Button up = null;
    private Button down = null;
    
    protected String curReferMudId = null;
    protected String curMudId = null;
 
    protected java.util.List<String> checkdList = new ArrayList<String>(); 

    boolean ctrlMax = false;
    private Combo fileSelect = null;

    protected boolean isFirstMdf = false;
    protected String prepardDataCtx = null;

    protected String curOptSeed = null; 

    protected String serverMdlVersion = null;
    protected String pkgSource = null;
    protected ICompilationUnit compUnit = null;

    protected GridData bothFillData = new GridData(GridData.FILL_BOTH);
    
    protected GridLayout commonLayout = new GridLayout();
    private ModelSelect modelSelect = null;
    private String selectNode = null;
    private boolean byMudId = false;


    private SashForm leftsash = null;

    private Composite leftTopCom = null;
    private Composite treeComp = null;
    
    
    protected boolean isLocal=true;
    protected ITemplateMgr templateMgr = null;
    protected String projectPath;
    protected String[] paramFrom;
    protected ReviewExeItem tempMemoTabItem = null;
    protected PreparedParamItem preparedParamItem = null;
    protected UserParamItem userParamItem = null;
    protected TemplateMgrItem templateItem = null; 
    protected OutLineItem helpItem = null;
    public MainUi(ModelSelect modelSelect,final String projectPath,
            final Config config, final Composite topCom,
            final String projectName, final String paramFrom[],
            final String pkgSource, final ICompilationUnit compUnit)
            throws Exception
    {

    	templateMgr = new LocalTemplateMgr(config.getLocalTemplatePath(),projectPath);
    	
        String byTemplateId = null;
        this.compUnit = compUnit;
        this.pkgSource = pkgSource;
        this.projectPath = projectPath;
        this.modelSelect = modelSelect;
        this.config = config; 
        this.paramFrom = paramFrom;
        if (config == null)
        {
            MessageBox box = new MessageBox(topCom.getShell(), SWT.APPLICATION_MODAL
                    | SWT.ICON_ERROR);
            // 设置对话框的标题
            box.setText("Success");
            // 设置对话框显示的消息
            box.setMessage(MultLang.getMultLang("code.001"));// 找不到配置文件
            box.open();

            throw new Exception(MultLang.getMultLang("code.001"));// 找不到配置文件);
        }
         

        String configType[] = config.getCodeType().split(";");
        if (configType.length == 0)
        {
            configType = new String[]
            { "ALL" };
        }

        this.projectName = projectName;
        this.topCom = topCom;

        GridLayout toplayout = new GridLayout();
        GridData toplayoutData = new GridData(GridData.FILL_BOTH);
        toplayout.numColumns = 4;
        this.topCom.setLayout(toplayout);
        this.topCom.setLayoutData(toplayoutData);
        final SashForm topSash = new SashForm(topCom, SWT.HORIZONTAL);
        FillLayout sashCtxlayout = new FillLayout();

        GridData sashCtxData = new GridData(GridData.FILL_BOTH);
        sashCtxData.widthHint = 800;
        sashCtxData.heightHint = 700;
        topSash.setLayoutData(sashCtxData);

        topSash.setLayout(sashCtxlayout);

        final Composite leftComp = new Composite(topSash, SWT.COLOR_WHITE
                | SWT.HORIZONTAL | SWT.COLOR_WHITE);
 
        leftComp.setLayout(commonLayout);

        leftComp.setLayoutData(bothFillData);

        if (modelSelect != null)
        {
            if (modelSelect.getTemp() != null
                    && !"".equals(modelSelect.getTemp().trim()))
            {
                byMudId = true;
                byTemplateId = modelSelect.getTemp().trim();// "365b42c6-b853-424e-b6c1-376623ac87de";
                selectNode = modelSelect.getNode();// "node3_2";
                if (selectNode != null)
                {
                    selectNode = selectNode.trim();
                }
            }
        }
        if (!byMudId)
        {
            leftsash = new SashForm(leftComp, SWT.VERTICAL);
            leftTopCom = new Composite(leftsash, SWT.NO);


            leftTopCom.addMouseListener(new MouseListener(){
            public void mouseDoubleClick(MouseEvent arg0)
            {
                // TODO Auto-generated method stub
                if (leftsash.getMaximizedControl() == leftTopCom)
                {
                    leftsash.setMaximizedControl(null);
                    
 
                }
                else
                {
                    leftsash.setMaximizedControl(leftTopCom);
                }
                 
            }

            public void mouseDown(MouseEvent arg0)
            {
                // TODO Auto-generated method stub

            }

            public void mouseUp(MouseEvent arg0)
            {
                // TODO Auto-generated method stub

            }
            }
            );
        
            
        }
        else
        {
            leftTopCom = new Composite(leftComp, SWT.NO);
        }

        GridLayout queryComLayout = new GridLayout();
        queryComLayout.numColumns = 3;
        GridData queryComGridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        leftTopCom.setLayout(queryComLayout);
        leftTopCom.setLayoutData(queryComGridData);

        if (!byMudId)
        {

            queryCondition = new Text(leftTopCom, SWT.SINGLE | SWT.BORDER
                    | SWT.COLOR_WHITE);
 
            GridData bothGData = new GridData(GridData.FILL_HORIZONTAL);
            bothGData.horizontalSpan = 2;
            queryCondition.setLayoutData(bothGData);
            queryConditionType = new Combo(leftTopCom, SWT.NONE | SWT.READ_ONLY);
            queryConditionType.setItems(new String[]
            { "按标题", "按创建人", "按模板ID" });

            queryConditionType.select(0);

            // 查询模板类型
            queryModelType = new Combo(leftTopCom, SWT.NONE | SWT.READ_ONLY);
            queryModelType.setItems(configType);
            queryModelType.select(0);

            // 查询模板位置
            queryModelPos = new Combo(leftTopCom, SWT.NONE | SWT.READ_ONLY);
            queryModelPos.setItems(new String[]
            { "本地", "服务器" });
            queryModelPos.select(0);

            button = new Button(leftTopCom, SWT.NONE);
            button.setText("查询");// 所有
            GridData butlayoudata = new GridData(GridData.HORIZONTAL_ALIGN_END);
            button.setLayoutData(butlayoudata);

            button.addMouseListener(this);

            GridData leftsashdata = new GridData(GridData.FILL_BOTH);

            leftsash.setLayoutData(leftsashdata);

            leftsash.setLayout(new FillLayout());

 

            mudList = new List(leftTopCom, SWT.SINGLE | SWT.V_SCROLL
                    | SWT.H_SCROLL | SWT.BORDER);

            final GridData gData = new GridData(GridData.FILL_BOTH
                    | SWT.V_SCROLL | SWT.H_SCROLL);
            gData.horizontalSpan = 3;
            // gData.heightHint=130;

            mudList.setLayoutData(gData);

            Composite pageNav = new Composite(leftTopCom, SWT.NONE);
            GridLayout grdLayout = new GridLayout();
            GridData pageLayoutData = new GridData();

            grdLayout.numColumns = 3;
            pageLayoutData.horizontalSpan = 3;
            pageNav.setLayout(grdLayout);
            pageNav.setLayoutData(pageLayoutData);
            up = new Button(pageNav, SWT.FLAT | SWT.COLOR_WHITE);
            GridData gdatal = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
            up.setText(MultLang.getMultLang("code.017"));// 上一页
            up.setLayoutData(gdatal);
            up.addMouseListener(new MouseListener()
            {

                public void mouseDoubleClick(MouseEvent arg0)
                {
                }

                public void mouseDown(MouseEvent arg0)
                {
                }

                public void mouseUp(MouseEvent arg0)
                {
                    currentPage--;
                    if (currentPage == 0)
                    {
                        currentPage = 1;
                    }
                    queryMudls();
                }
            });
            down = new Button(pageNav, SWT.FLAT | SWT.COLOR_WHITE);
            GridData gdatar = new GridData(GridData.HORIZONTAL_ALIGN_END);
            down.setText(MultLang.getMultLang("code.018"));// 下一页
            down.setLayoutData(gdatar);
            down.addMouseListener(new MouseListener()
            {

                public void mouseDoubleClick(MouseEvent arg0)
                {
                }

                public void mouseDown(MouseEvent arg0)
                {
                }

                public void mouseUp(MouseEvent arg0)
                {
                    currentPage++;
                    if (currentPage >= totalPage)
                    {
                        currentPage = totalPage;
                    }
                    queryMudls();
                }
            });
            pageInfoLabel = new Label(pageNav, SWT.NO);
            GridData gdatam = new GridData(GridData.HORIZONTAL_ALIGN_END);

            pageInfoLabel.setLayoutData(gdatam);

        }

        if (leftsash != null)
        {
            treeComp = new Composite(leftsash, SWT.NONE);


            treeComp.addMouseListener(new MouseListener(){
            public void mouseDoubleClick(MouseEvent arg0)
            {
                // TODO Auto-generated method stub
                if (leftsash.getMaximizedControl() == treeComp)
                {
                    leftsash.setMaximizedControl(null);
                     
                }
                else
                {
                    leftsash.setMaximizedControl(treeComp);
                }
                 
            }

            public void mouseDown(MouseEvent arg0)
            {
                // TODO Auto-generated method stub

            }

            public void mouseUp(MouseEvent arg0)
            {
                // TODO Auto-generated method stub

            }
            }
            );
            
            
            leftsash.setWeights(new int[]
            { 30, 70 });
        }
        else
        {
            treeComp = new Composite(leftComp, SWT.NONE);
        }
        treeComp.setLayout(commonLayout);
        treeComp.setLayoutData(bothFillData);

        checkTree = new CheckboxTreeViewer(treeComp, SWT.BORDER);

        checkTree.addCheckStateListener(new ICheckStateListener()
        {

            public void checkStateChanged(CheckStateChangedEvent arg0)
            {
                boolean checked = arg0.getChecked();

                SelectCheckBox box = (SelectCheckBox) arg0.getElement();
                String ctlKey = box.getKey();

                if (curFtlModel.getCheckedBoxMap() != null)
                {

                    SelectCheckBox.syncCheckStatus(ctlKey, box, checked,
                            curFtlModel.getCheckedBoxMap(), checkTree);

                    Iterator<String> itStr = curFtlModel.getCheckedBoxMap()
                            .keySet().iterator();
                    while (itStr.hasNext())
                    {

                        String tmpKey = itStr.next();
                        SelectCheckBox tempBox = curFtlModel.getCheckedBoxMap()
                                .get(tmpKey);
                        if (tmpKey.equals(box.getKey()))
                        {
                            continue;
                        }
                        if (tempBox.getGroupList() != null
                                && tempBox.getGroupList().size() > 0)
                        {
                            SelectCheckBox.syncCheckStatus(ctlKey, tempBox,
                                    checkTree.getChecked(tempBox),
                                    curFtlModel.getCheckedBoxMap(), checkTree);
                        }
                    }

                }

                Object[] checkList = checkTree.getCheckedElements();
                // checkdList.clear();
                checkdList.clear();
                if (checkList != null)
                {
                    for (Object o : checkList)
                    {
                        SelectCheckBox b = (SelectCheckBox) o;
                        checkdList.add(b.getKey());
                    }
                }

                GenView reviewCode = new GenView(curMudId);
                reviewCode.setResultText(tempMemoTabItem.exeResult);
                reviewCode.setCurFtlModel(curFtlModel);
                reviewCode.setConfig(config);
                reviewCode.setLocale(isLocal);// =true;
                reviewCode.setCheckdList(checkdList);
                reviewCode.setUserParam(userParamItem.userParaText.getText().trim());
                reviewCode.setPrepatedParam(preparedParamItem.preparedParam.getText().trim());
                reviewCode.setTemplateSrc(templateItem.templateText.getText().trim());
                reviewCode.genView(projectPath);
                helpItem.redraw();
                tempMemoTabItem.reloadTemplateMemo();
            }
        });
        checkTree.getTree().setLayoutData(bothFillData);
        checkTree.setContentProvider(ctxPrv);
        checkTree.setLabelProvider(viewTreeLabel);

        // ////////右边框/////////////////////////////////////////////////////////////////
        final Composite compright = new Composite(topSash, SWT.COLOR_WHITE
                | SWT.HORIZONTAL | SWT.COLOR_WHITE);
        topSash.setWeights(new int[]
        { 10, 90 });

        compright.setLayout(commonLayout);

        compright.setLayoutData(bothFillData);

        rightMainTab = new CTabFolder(compright, SWT.NONE);

        rightMainTab.setLayoutData(bothFillData);

        rightMainTab.addCTabFolder2Listener(new CTabFolder2Adapter()
        {

            public void minimize(CTabFolderEvent event)
            {
                topSash.setMaximizedControl(null);
                ctrlMax = false;
            }

            public void maximize(CTabFolderEvent event)
            {
                topSash.setMaximizedControl(compright);
                ctrlMax = true;
            }

            public void restore(CTabFolderEvent Event)
            {
                topSash.setMaximizedControl(null);
                ctrlMax = false;
            }
        });
        ////
        tempMemoTabItem = new ReviewExeItem(rightMainTab, SWT.NONE, this);

        tempMemoTabItem.setText("预览＆执行");
        
      
        preparedParamItem = new PreparedParamItem(rightMainTab, SWT.BORDER, this);
        preparedParamItem.setText(MultLang.getMultLang("code.011"));// 预设参数

        userParamItem = new UserParamItem(rightMainTab, SWT.BORDER, this);
        userParamItem.setText("用户参数");// 用户参数

        templateItem = new TemplateMgrItem(rightMainTab, SWT.BORDER, this);
        templateItem.setText(MultLang.getMultLang("code.007"));// 模板管理
 
        
        helpItem = new OutLineItem(rightMainTab, SWT.BORDER, this);
        helpItem.setText(MultLang.getMultLang("code.085"));// 大纲

        rightMainTab.addMouseListener(new MouseListener()
        {

            public void mouseDoubleClick(MouseEvent arg0)
            {
                if (ctrlMax)
                {
                    topSash.setMaximizedControl(null);
                    ctrlMax = false;
                }
                else
                {
                    topSash.setMaximizedControl(compright);
                    ctrlMax = true;
                }
            }

            public void mouseDown(MouseEvent arg0)
            {
            }

            public void mouseUp(MouseEvent arg0)
            {
                Object[] checkList = checkTree.getCheckedElements();
                // checkdList.clear();
                checkdList.clear();
                if (checkList != null)
                {
                    for (Object o : checkList)
                    {
                        SelectCheckBox b = (SelectCheckBox) o;
                        checkdList.add(b.getKey());
                    }
                }
                // 模板说明
                if (rightMainTab.getSelectionIndex() == 0)
                {

                    // genTemplateMemo(tempalteMemo);
                }
                // 1:选择器
                else if (rightMainTab.getSelectionIndex() == 1)
                {

                }// 模板

                helpItem.redraw();

            }

        });

        GridData srcData = new GridData(GridData.FILL_HORIZONTAL
                | GridData.FILL_VERTICAL);
        srcData.verticalSpan = 2;

 
        
        rightMainTab.setSimple(false);
        rightMainTab.setUnselectedCloseVisible(true);
        rightMainTab.setUnselectedImageVisible(true); 
        rightMainTab.setMaximizeVisible(true);
        rightMainTab.setMinimizeVisible(true); 
        rightMainTab.setSelection(0);
        listObj = new ListItemSelect(this, indexMuId, templateItem.templateText);

        if (!byMudId)
        {
            queryMudls();
        }
        else
        {
            resetByTemplateId(byTemplateId, selectNode);
        }

    }



    public String getCurMudId()
    {
        return this.curMudId;
    }

    public String getCurrentSeed()
    {
        return this.currentSeed;
    }

    public HashMap getLocalIndexMuId()
    {
        return this.localMuld;
    }

    public void setCurrentSeed(String currentSeed)
    {
        this.currentSeed = currentSeed;
    }


    public void setMudId(String mudId)
    {
        this.curMudId = mudId;
    }

    public String getAnnoText()
    {
        return this.templateItem.getAnnoText();
    }

    public Map getSrcJson()
    {
        JavaSrcParse clzObj = new JavaSrcParse(templateItem.getText(), curMudId,
                pkgSource,

                CompilationUnitParseUtil.getCompUnit(
                        pkgSource + "/"
                                + fileSelect.getText().replaceAll("\\.", "/")
                                + ".java", compUnit.getResource().getProject()));

        Map<String, Object> map = new HashMap<String, Object>();
        try
        {
        	map = SrcUtil.josnToMap(clzObj.getClzJson(null, config));
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return map;
    }

    public void setParaText(String paraText)
    {
        this.userParamItem.userParaText.setText(paraText);
    }

    public void mouseDoubleClick(MouseEvent arg0)
    {
    }

    // 请空所有控件的值
    public void resetMain()
    {
        this.curMudId = null;
        // 参数
        userParamItem.userParaText.setText("");
        // 注释
        templateItem.annoText.setText("");
        // 标题
        templateItem.uptBookmark.setText("");
        // 类别
        templateItem.codeTypeCombo.setText("");
        // 模板说明
        // memoCtx.setText("");
        tempMemoTabItem.tempalteMemo.setText("");

        // 预设参数
        preparedParamItem.preparedParam.setText("");
        // 模板
        templateItem.templateText.setText("");

        templateItem.setText(MultLang.getMultLang("code.007"));// 模板

        ctxPrv.setCheckedBoxMap(null);
        checkTree.setContentProvider(ctxPrv);
        checkTree.setLabelProvider(viewTreeLabel);
        checkTree.setInput("root");
        checkTree.expandAll();

        // 大概
        // 代码选择

    }

    public void mouseDown(MouseEvent arg0)
    {
    }

    public void mouseUp(MouseEvent arg0)
    {
        this.currentPage = 1;
        queryMudls();
    }


    private boolean getSelectLocal()
    {
        
        if (queryModelPos != null)
        {
            if (queryModelPos.getSelectionIndex() == 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
         

    }

    protected synchronized void queryMudls()
    {
        resetMain();
        isLocal = getSelectLocal();
        if (byMudId) { return; }
        String querySeed = "";
        String queryId = "";
        String queryBookmark = "";
        // 标题
        if (queryConditionType.getSelectionIndex() == 0)
        {
            queryBookmark = queryCondition.getText().trim();
        }
        // 创建人
        else if (queryConditionType.getSelectionIndex() == 1)
        {
            querySeed = queryCondition.getText().trim();
        }
        // 模板ID
        else
        {
            queryId = queryCondition.getText().trim();
        }

        int beginPos = (this.currentPage - 1) * this.eachPageSize;
        String codeSelect = queryModelType.getText();// selectList.getTypeCode();
        if ("All".equalsIgnoreCase(codeSelect))// 所有
        {
            codeSelect = "";
        }

        mudList.removeAll();
        mudList.add("loading...");
        mudList.removeMouseListener(listObj);
        mudList.addMouseListener(listObj);

        try
        {

            String bookmark = null;
            String mulCtx = null;

            java.util.List<String> retList = new ArrayList<String>();
    

            if (isLocal)
            {
                templateItem.turnToLocal.setText("同步到服务器");// 

                try
                {

                    PageData<CodegenTemplate> tempList = templateMgr.queryPageList(querySeed, queryId, queryBookmark, mulCtx, codeSelect, beginPos, eachPageSize);
                    if(tempList != null && tempList.getRows()>0)
                    {
                    	
                    	for(CodegenTemplate t:tempList.getDataList())
                    	{
                    		 
                    		retList.add(t.getTitle() + "#" + t.getAuthor() + "#" + t.getId() + "#"
        							+ t.getId());
                    	}
                    	retList.add(0, tempList.getRows() + "");
                    }
                    else
                    {
                    	retList.add(0, "0");

                    }
                	retList.add(1, "");
                	retList.add(2, "");
                	retList.add(3, "");
                	retList.add(4, "");
                }
                catch (Exception e)
                {

                    e.printStackTrace();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(baos);
                    e.printStackTrace(ps);
                    try
                    {
                        baos.close();
                    }
                    catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }

                    MessageBox box = new MessageBox(topCom.getShell(),
                            SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
                    // 设置对话框的标题
                    box.setText("提示信息");
                    // 设置对话框显示的消息
                    box.setMessage(MultLang.getMultLang("code.076") + ":"
                            + baos.toString());// 操作失败
                    box.open();
                    return;
                
				
                }
            }
            else
            {
                // bookmark = serverBookmarkQry.getInput().trim();
                templateItem.turnToLocal.setText("同步到本地");//
                retList = RpcFactory.httpSrv(config.getSrvUrl())
                        .queryCodegenMudlsPageList2(config.getUserName(), querySeed, queryId,
                                queryBookmark, mulCtx, codeSelect, beginPos,
                                eachPageSize);

            }

            int md = Integer.parseInt(retList.get(0)) % eachPageSize;
            if (md > 0)
            {
                totalPage = Integer.parseInt(retList.get(0)) / eachPageSize + 1;
            }
            else
            {
                totalPage = Integer.parseInt(retList.get(0)) / eachPageSize;
            }
            this.up.setEnabled(true);
            this.down.setEnabled(true);
            String pageInfo = this.currentPage + "/" + totalPage;
            if (this.currentPage <= 1)
            {
                this.up.setEnabled(false);
                if (retList.size() == 0)
                {
                    pageInfo = "0/0";
                }
            }
            if (this.totalPage == 0)
            {
                pageInfo = "0/0";
                this.up.setEnabled(false);
                this.down.setEnabled(false);
            }
            else
            {
                if (this.currentPage == this.totalPage)
                {
                    this.down.setEnabled(false);
                }
            }
            pageInfoLabel.setText(pageInfo + "(Total:"
                    + Integer.parseInt(retList.get(0)) + ")");
            // System.err.println("翻页信息:" + pageInfo);
            indexMuId.clear();
            localMuld.clear();
            Integer indx = 0;
            mudList.removeAll();
            for (int i = 5; i < retList.size(); i++)
            {
                String temp = retList.get(i);
                int referIdPos = temp.lastIndexOf("#");
                // System.err.println("temp:" + temp);

                if (referIdPos == -1)
                {
                    continue;
                }
                else
                {

                    String referId = null;
                    String bookMark = null;
                    String mulId = null;
                    String mSeedId = null;
                    if (pm.contains(temp, pattFile))
                    {
                        // mdPList.get(i).getSeedId()+"#"+mdPList.get(i).getBookmark()+"#"+mdPList.get(i).getId()+"#"+mdPList.get(i).getReferId()
                        MatchResult maRes = pm.getMatch();
                        bookMark = maRes.group(1);
                        mSeedId = maRes.group(2);
                        mulId = maRes.group(3);
                        referId = maRes.group(4);
                    }

                    int totalSize = (this.currentPage - 1) * this.eachPageSize;
                    // if (mulId.equals(referId))
                    {

                        mudList.add(" " + (i - 4 + totalSize) + "." + bookMark
                                + " - " + mSeedId+"/"+mulId);

                    }

                    if (isLocal)
                    {
                        // System.err.println("local:" + mulId);
                        localMuld.put(indx, mulId);
                    }
                    else
                    {
                        // System.err.println("server:" + mulId);
                        indexMuId.put(indx, mulId);
                    }
                    indx++;
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            EclipseUtil.proExcept(topCom,e,e.getMessage());
        }

    }

    // 生成memo,同时更新树结构
    protected void reloadCurFtlModelAndTree() throws Exception
    {

        Object[] checkList = checkTree.getCheckedElements();
        // checkdList.clear();
        checkdList.clear();
        if (checkList != null)
        {
            for (Object o : checkList)
            {
                SelectCheckBox b = (SelectCheckBox) o;
                checkdList.add(b.getKey());
            }
        }

        if (curFtlModel == null)
        {
            curFtlModel = new FileGen(preparedParamItem.preparedParam.getText().trim(),
                    userParamItem.userParaText.getText().trim(), templateItem.templateText.getText()
                            .trim(), new ArrayList<String>());
        }
        else
        {
            curFtlModel.initParam(preparedParamItem.preparedParam.getText().trim(), userParamItem.userParaText
                    .getText().trim(), templateItem.templateText.getText().trim(),
                    checkdList);
        }

        try
        {
            curFtlModel.init(config.getSrvUrl(),
                    config.getLocalTemplatePath(), isLocal,projectPath);
            curFtlModel.updateTreeNode(config.getSrvUrl(),
                    config.getLocalTemplatePath(), isLocal, true,projectPath);

            if (selectNode != null && !"".equals(selectNode.trim()))
            {
                ctxPrv.setCheckedBoxMap(curFtlModel.getNodeTreeMap(selectNode));

            }
            else
            {
                ctxPrv.setCheckedBoxMap(getCurFtlModel().getCheckedBoxMap());
            }

            // ctxPrv.setCheckedBoxMap(getCurFtlModel()
            // .getCheckedBoxMap());

            // ctxPrv.setCheckedBoxMap(getCurFtlModel()
            // .getNodeTreeMap("node2_3"));

            //
            checkTree.setContentProvider(ctxPrv);

            checkTree.setLabelProvider(viewTreeLabel);

            checkTree.setInput("root");
            checkTree.expandAll();
            if (curFtlModel.getCheckedBoxMap() != null)
            {
                Iterator<String> it = curFtlModel.getCheckedBoxMap().keySet()
                        .iterator();
                while (it.hasNext())
                {
                    SelectCheckBox b = curFtlModel.getCheckedBoxMap().get(
                            it.next());
                    String key = b.getKey();
                    if (checkdList.contains(key))
                    {
                        checkTree.setChecked(b, true);
                    }
                }
            }

        }
        catch (Exception e2)
        {
            // TODO Auto-generated catch block
        	EclipseUtil.proExcept(topCom,e2);
            /*
            e2.printStackTrace();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            e2.printStackTrace(ps);

            ConsoleFactory.printToConsole(baos.toString(), true);
            try
            {
                baos.close();
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
            */
        }

    }



    public String getCurReferMudId()
    {
        return curReferMudId;
    }

    public void setCurReferMudId(String curReferMudId)
    {
        this.curReferMudId = curReferMudId;
    }

    public FileGen getCurFtlModel()
    {
        return curFtlModel;
    }

    public void setCurFtlModel(FileGen curFtlModel)
    {
        this.curFtlModel = curFtlModel;
    }

    public void resetByTemplateId(String templateId, String treeNodeValue)
    {

        // canvas.setData(null);
        // canvas.redraw();
        // canvas2.setData(null);
        // canvas2.redraw();
        // 先重置,再赋值
        templateItem.setText(MultLang.getMultLang("code.007"));// 模板
        // mdlHasEdit = false;
        isFirstMdf = true;
        // this.md.reloadPreparedData(true);

        this.checkdList.clear();

        this.setMudId(templateId);

        try
        {
            SimpleDateFormat fd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            CodegenTemplate md = null;
            if (this.isLocal)
            {
                String mdlPath = config.getLocalTemplatePath();
                //md = templateMgr.getMudByMudId(templateId);
                md = templateMgr.getCodegenTemplate(templateId, true);
                this.setServerMdlVersion(md.getVersion());
            }
            else
            {
                md = RpcFactory.httpSrv(config.getSrvUrl())
                        .getCodegenMudlsByMudId(config.getUserName(),templateId);
                this.setServerMdlVersion(fd.format(md.getLastUpdatedDate()));
            }
            if (md != null)
            {
                String txt = md.getTemplateCtx();
                String param = md.getParamDesc();
                String bookmark = md.getTitle();
                String referId = md.getReferId();
                this.setCurReferMudId(referId);
                this.setCurOptSeed(md.getAuthor());
                if (txt == null)
                {
                    txt = "";
                }
                if (param == null)
                {
                    param = "";
                }
                // this.src.setText(txt);

                this.setParaText(param);

                if (bookmark == null)
                {
                    templateItem.setUptbookmark("");
                }
                else
                {
                    templateItem.setUptbookmark(bookmark);
                }
                templateItem.setUptCodeType(md.getCodeType());
                templateItem.setAnnoText(md.getTemplateAnno() == null ? "" : md
                        .getTemplateAnno());
                templateItem.templateText.setText(txt);

                try
                {
                    // reloadPreparedData(true);
                    preparedParamItem.reloadPreparedData(true);
                    try
                    {
                        // reloadCurFtlModelAndTree();
                        curFtlModel = new FileGen(preparedParamItem.preparedParam.getText()
                                .trim(), userParamItem.userParaText.getText().trim(),
                                templateItem.templateText.getText().trim(),
                                new ArrayList<String>());
                        curFtlModel.updateTreeNode(config.getSrvUrl(),
                                config.getLocalTemplatePath(), isLocal, true, projectPath);
                       
                        tempMemoTabItem.reloadTemplateMemo();
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        PrintStream ps = new PrintStream(baos);
                        e.printStackTrace(ps);
                        EclipseConsoleUtil.printToConsole(baos.toString(), true);

                    }

                    if (treeNodeValue != null
                            && !"".equals(treeNodeValue.trim()))
                    {
                        ctxPrv.setCheckedBoxMap(curFtlModel
                                .getNodeTreeMap(treeNodeValue));

                    }
                    else
                    {
                        ctxPrv.setCheckedBoxMap(getCurFtlModel()
                                .getCheckedBoxMap());
                    }

                    //
                    checkTree.setContentProvider(ctxPrv);

                    checkTree.setLabelProvider(viewTreeLabel);
                    checkTree.setInput("root");
                    checkTree.expandAll();
                    // new FtlModel();

                }
                catch (Exception ex)
                {

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(baos);
                    ex.printStackTrace(ps);

                    EclipseConsoleUtil.printToConsole(baos.toString(), true);
                    try
                    {
                        baos.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    // throw ex;

                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }

    }

    public class ListItemSelect implements MouseListener
    {

        private HashMap<Integer, String> indexMap = null;
        private Text src = null;

        private MainUi md = null;

        private ListItemSelect(MainUi md,
                HashMap<Integer, String> indexMap, Text src)
        {
            this.indexMap = indexMap;
            this.src = src;
            this.md = md;
        }

        public void mouseDoubleClick(MouseEvent arg0)
        {
        }

        public void mouseDown(MouseEvent arg0)
        {
            this.md.resetMain();
            // 先重置,再赋值
            templateItem.setText(MultLang.getMultLang("code.007"));// 模板
            // mdlHasEdit = false;
            isFirstMdf = true;
            // this.md.reloadPreparedData(true);

            this.md.checkdList.clear();

            List src = (List) arg0.getSource();
            String locationDesc = null;
            String mudId = null;
            if (this.md.isLocal)
            {
                mudId = this.md.localMuld.get(src.getSelectionIndex());
                locationDesc = MultLang.getMultLang("code.026");// 本地
            }
            else
            {
                mudId = this.indexMap.get(src.getSelectionIndex());
                locationDesc = MultLang.getMultLang("code.027");// 服务器
            }
            md.setMudId(mudId);

            if (mudId == null) { return; }
            try
            {
                SimpleDateFormat fd = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                CodegenTemplate md = null;
                if (this.md.isLocal)
                {
                    String mdlPath = config.getLocalTemplatePath();
                    md = templateMgr.getCodegenTemplate(mudId, true);//(mudId);
                    this.md.setServerMdlVersion(md.getVersion());
                }
                else
                {
                    md = RpcFactory.httpSrv(config.getSrvUrl())
                            .getCodegenMudlsByMudId(config.getUserName(),mudId);
                    this.md.setServerMdlVersion(fd.format(md
                            .getLastUpdatedDate()));
                }
                if (md != null)
                {
                    String txt = md.getTemplateCtx();
                    String param = md.getParamDesc();
                    String bookmark = md.getTitle();
                    String referId = md.getReferId();
                    this.md.setCurReferMudId(referId);
                    this.md.setCurOptSeed(md.getAuthor());
                    if (txt == null)
                    {
                        txt = "";
                    }
                    if (param == null)
                    {
                        param = "";
                    }
                    this.src.setText(txt);

                    this.md.setParaText(param);

                    if (bookmark == null)
                    {
                        this.md.templateItem.setUptbookmark("");
                    }
                    else
                    {
                        this.md.templateItem.setUptbookmark(bookmark);
                    }
                    this.md.templateItem.setUptCodeType(md.getCodeType());
                    this.md.templateItem.setAnnoText(md.getTemplateAnno() == null ? "" : md
                            .getTemplateAnno());

                    try
                    {
                        preparedParamItem.reloadPreparedData(true);

                        // new FtlModel();

                    }
                    catch (Exception ex)
                    {

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        PrintStream ps = new PrintStream(baos);
                        ex.printStackTrace(ps);

                        EclipseConsoleUtil.printToConsole(baos.toString(), true);
                        try
                        {
                            baos.close();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }

                        // throw ex;

                    }
                   
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();

            }
            finally
            {
                try
                {
                    reloadCurFtlModelAndTree(); 
                    this.md.tempMemoTabItem.reloadTemplateMemo();
                    helpItem.redraw();
                }
                catch (Exception e)
                {
                	EclipseUtil.proExcept(topCom,e);

                }
            }
        }

        public void mouseUp(MouseEvent arg0)
        {
        }

        public void widgetDefaultSelected(SelectionEvent arg0)
        {
        }

        public void widgetSelected(SelectionEvent arg0)
        {
            List src = (List) arg0.getSource();
            String mudId = this.indexMap.get(src.getSelectionIndex());
        }

    }

    public void reloadLocal()
    {
        queryMudls();
        Iterator<Entry<Integer, String>> entryIt = null;
        if (isLocal && curMudId != null && localMuld != null)
        {
            entryIt = localMuld.entrySet().iterator();
            if (entryIt != null)
            {
                while (entryIt.hasNext())
                {
                    Entry<Integer, String> tmp = entryIt.next();
                    if (curMudId.equals(tmp.getValue()))
                    {
                        mudList.select(tmp.getKey());

                        break;
                    }
                }
            }
        }
    }

  
    public String getCurOptSeed()
    {
        return curOptSeed;
    }

    public void setCurOptSeed(String curOptSeed)
    {
        this.curOptSeed = curOptSeed;
    }

    public String getServerMdlVersion()
    {
        return serverMdlVersion;
    }

    public void setServerMdlVersion(String serverMdlVersion)
    {
        this.serverMdlVersion = serverMdlVersion;
    }

    
}
