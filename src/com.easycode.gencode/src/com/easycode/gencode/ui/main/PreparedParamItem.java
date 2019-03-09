package com.easycode.gencode.ui.main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.easycode.common.EclipseUtil;
import com.easycode.gencode.ui.SelectAllMouseListener;
import com.easycode.gencode.ui.elements.PreparedParamTreeContentProvider;
import com.easycode.gencode.ui.elements.PreparedParamTreeLabelProvider;
import com.easycode.gencode.core.javaparse.CompilationUnitParseUtil;
import com.easycode.gencode.core.javaparse.JavaSrcParse;
import com.easycode.resource.MultLang;

public class PreparedParamItem extends CTabItem
{
    Shell topShell = null;
    MainUi root = null;
    Combo fileSelect = null;
    Button reloadBut = null;
    Text preparedParam = null;
    TreeViewer preparedParamTree = null;

    PreparedParamTreeContentProvider preparedParamTreeContentProvider = new PreparedParamTreeContentProvider();
    PreparedParamTreeLabelProvider preparedParamTreeLabelProvider = new PreparedParamTreeLabelProvider();

    public PreparedParamItem(CTabFolder parent, int style, final MainUi root)
    {
        super(parent, style);
        topShell = parent.getShell();
        this.root = root;
        SashForm prepareParamTabItemSash = new SashForm(parent, SWT.VERTICAL);
        prepareParamTabItemSash.setLayout(new FormLayout());
        prepareParamTabItemSash.setLayoutData(root.bothFillData);

        Composite prepardCom = new Composite(prepareParamTabItemSash,
                SWT.COLOR_WHITE);
        GridLayout prepardComout = new GridLayout();
        prepardComout.numColumns = 2;
        prepardCom.setLayout(prepardComout);
        prepardCom.setLayoutData(root.bothFillData);

        ILoadPreparedParam load = root.loadPreparedParam;

        fileSelect = new Combo(prepardCom, SWT.FLAT | SWT.COLOR_WHITE
                | SWT.READ_ONLY);
        fileSelect.setItems(this.root.paramFrom);

        fileSelect.select(0);

        reloadBut = new Button(prepardCom, SWT.FLAT | SWT.COLOR_WHITE);
        reloadBut.setText(MultLang.getMultLang("code.012"));// 重新载入
        GridData reloadButGD = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        reloadBut.setLayoutData(reloadButGD);

        preparedParam = new Text(prepardCom, SWT.BORDER | SWT.MULTI
                | SWT.COLOR_WHITE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
        preparedParam
                .addMouseListener(new SelectAllMouseListener(preparedParam));
        GridData preparedParamData = new GridData(GridData.FILL_BOTH);
        preparedParamData.horizontalSpan = 2;
        preparedParam.setLayoutData(preparedParamData);
        preparedParam.setEditable(false);

        GridData preparedParamTreeGridData = new GridData(GridData.FILL_BOTH);
        preparedParamTreeGridData.horizontalSpan = 2;

        Composite preparedtreeComp = new Composite(prepareParamTabItemSash,
                SWT.NONE);

        preparedtreeComp.setLayout(this.root.commonLayout);
        preparedtreeComp.setLayoutData(preparedParamTreeGridData);

        preparedParamTree = new TreeViewer(preparedtreeComp, SWT.BORDER
                | SWT.MULTI | SWT.COLOR_WHITE | SWT.H_SCROLL | SWT.V_SCROLL
                | SWT.WRAP);
        preparedParamTree.getTree().setLayoutData(this.root.bothFillData);

        prepareParamTabItemSash.setWeights(new int[]
        { 70, 30 });
        this.setControl(prepareParamTabItemSash);
        init();
    }

    private void init()
    {
        if (fileSelect != null)
        {
            fileSelect.addSelectionListener(new SelectionListener()
            {

                public void widgetDefaultSelected(SelectionEvent arg0)
                {
                    // TODO Auto-generated method stub

                }

                public void widgetSelected(SelectionEvent arg0)
                {

                    Combo comb = (Combo) arg0.getSource();

                    ILoadPreparedParam load = root.loadPreparedParam;
                    
                    if (load instanceof LoadPOJOParam)
                    {
                        LoadPOJOParam p = (LoadPOJOParam)load;
                        p.init(root.templateItem.annoText.getText(),
                            root.curMudId, root.pkgSource, root.config);
 
                    }
                    else if(load instanceof LoadDBParam)
                    {
                        LoadDBParam db = (LoadDBParam)load;
                        String tab = fileSelect.getText();
                        
                        db.init(root.curMudId, tab,root.config);
                    }
                    else if(load instanceof LoadJsonParam)
                    {
                        LoadJsonParam db = (LoadJsonParam)load;
                        String tab = fileSelect.getText();
                        
                        db.init(root.curMudId);
                    }
                    try
                    {
                        root.prepardDataCtx = load.reload(); 
                                                            
                        preparedParam.setText(root.prepardDataCtx);
                        
                        preparedParamTreeContentProvider.init(preparedParam.getText());
                        preparedParamTree
                                .setContentProvider(preparedParamTreeContentProvider);
                        preparedParamTree.setLabelProvider(preparedParamTreeLabelProvider);
                        preparedParamTree.setInput("sys");
                        
                    }
                    catch (Exception e)
                    {

                        EclipseUtil.proExcept(topShell, e, "预设参数解析异常，控制台查看详情!");

                    }
                    

                }
            });
        }

        if (reloadBut != null)
        {
            reloadBut.addMouseListener(new MouseListener()
            {

                public void mouseDoubleClick(MouseEvent arg0)
                {
                }

                public void mouseDown(MouseEvent arg0)
                {
                }

                public void mouseUp(MouseEvent arg0)
                {
                    reloadJavaPreparedData(true);
                    // preparedParamTreeContentProvider =

                    // preparedParamTree.expandAll();
                }
            });
        }

    }

    public void reloadJavaPreparedData(boolean reload)// throws Exception
    {
        try
        {

            ILoadPreparedParam load = root.loadPreparedParam;
            if (load instanceof LoadPOJOParam)
            {
                ICompilationUnit unit = CompilationUnitParseUtil.getCompUnit(
                        root.pkgSource + "/"
                                + fileSelect.getText().replaceAll("\\.", "/")
                                + ".java", load.getProject());
                LoadPOJOParam p = (LoadPOJOParam)load;
                p.reset(root.templateItem.annoText.getText(), root.curMudId,
                        root.pkgSource, unit, root.config);
            }
            else if (load instanceof LoadDBParam)
            {
                LoadDBParam db = (LoadDBParam)load;
                String tab = fileSelect.getText();
               
                db.reset(root.curMudId, tab);
            }
            else if (load instanceof LoadJsonParam)
            {
                LoadJsonParam db = (LoadJsonParam)load;
                db.reset(root.curMudId);
            }
            preparedParam.setText(load.reload());

            preparedParamTreeContentProvider.init(preparedParam.getText());
            preparedParamTree
                    .setContentProvider(preparedParamTreeContentProvider);
            preparedParamTree.setLabelProvider(preparedParamTreeLabelProvider);
            preparedParamTree.setInput("sys");

        }
        catch (Exception e)
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            e.printStackTrace(ps);
            preparedParam.setText(baos.toString());
            try
            {
                baos.close();
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
            // throw e;
        }

    }

}
