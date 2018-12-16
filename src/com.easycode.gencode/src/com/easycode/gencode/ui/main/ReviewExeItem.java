package com.easycode.gencode.ui.main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.easycode.common.EclipseUtil;
import com.easycode.gencode.service.GenEditFile;
import com.easycode.gencode.service.GenNewFile;
import com.easycode.gencode.ui.SelectAllMouseListener;
import com.easycode.gencode.ui.elements.SelectCheckBox;

public class ReviewExeItem extends CTabItem
{

    Button addFile = null;
    Button updateFile = null;
    Button reloadMemo = null;
    MainUi root = null;
    Text exeResultText = null;
    Text exeResult = null;
    Text tempalteMemo = null;

    private Shell topShell = null;

    public ReviewExeItem(CTabFolder parent, int style, final MainUi root)
    {
        super(parent, style);
        topShell = parent.getShell();
        this.root = root;

        // TODO Auto-generated constructor stub
        SashForm tempMemoTabItemSash = new SashForm(parent, SWT.VERTICAL);
        tempMemoTabItemSash.setLayout(new FormLayout());
        tempMemoTabItemSash.setLayoutData(root.bothFillData);

        Composite compMemoSrc = new Composite(tempMemoTabItemSash, SWT.NONE);
        compMemoSrc.setLayout(root.commonLayout);
        compMemoSrc.setLayoutData(root.bothFillData);

        this.setControl(tempMemoTabItemSash);

        exeResult = new Text(compMemoSrc, SWT.MULTI | SWT.BORDER
                | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL | SWT.COLOR_WHITE);
        exeResult.addMouseListener(new SelectAllMouseListener(exeResult));
        exeResult.setLayoutData(root.bothFillData);

        CTabFolder tempMemoTabItemBotmTab = new CTabFolder(tempMemoTabItemSash,
                SWT.BORDER);

        CTabItem tempMemoTabItemBotmTabMemoItem = new CTabItem(
                tempMemoTabItemBotmTab, SWT.BORDER);
        tempMemoTabItemBotmTabMemoItem.setText("模板说明");

        CTabItem tempMemoTabItemBotmTabExeItem = new CTabItem(
                tempMemoTabItemBotmTab, SWT.BORDER);
        tempMemoTabItemBotmTabExeItem.setText("执行");

        Composite execResultCom = new Composite(tempMemoTabItemBotmTab,
                SWT.BORDER);
        execResultCom.setLayout(root.commonLayout);
        execResultCom.setLayoutData(root.bothFillData);

        exeResultText = new Text(execResultCom, SWT.MULTI | SWT.WRAP
                | SWT.H_SCROLL | SWT.V_SCROLL);

        exeResultText.setEditable(false);
        exeResultText.setLayoutData(root.bothFillData);
        exeResultText
                .addMouseListener(new SelectAllMouseListener(exeResultText));

        Composite viewButt = new Composite(execResultCom, SWT.NONE);

        viewButt.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));

        GridLayout viewButtLayout = new GridLayout();
        viewButtLayout.numColumns = 2;

        viewButt.setLayout(viewButtLayout);
        addFile = new Button(viewButt, SWT.NONE);
        addFile.setText("生成");
        addFile.setLayoutData(new GridData());

        updateFile = new Button(viewButt, SWT.NONE);
        updateFile.setText("修改");
        updateFile.setLayoutData(new GridData());

        tempMemoTabItemBotmTabExeItem.setControl(execResultCom);

        tempMemoTabItemBotmTab.setSelection(0);

        Composite tempMemoTabItemMemoComp = new Composite(
                tempMemoTabItemBotmTab, SWT.COLOR_WHITE);

        tempMemoTabItemMemoComp.setLayout(root.commonLayout);
        tempMemoTabItemMemoComp.setLayoutData(new GridData(
                GridData.HORIZONTAL_ALIGN_END));

        tempalteMemo = new Text(tempMemoTabItemMemoComp, SWT.READ_ONLY
                | SWT.BORDER | SWT.COLOR_WHITE | SWT.WRAP | SWT.V_SCROLL
                | SWT.H_SCROLL);
        tempalteMemo.addMouseListener(new SelectAllMouseListener(tempalteMemo));
        tempalteMemo.setLayoutData(root.bothFillData);

        reloadMemo = new Button(tempMemoTabItemMemoComp, SWT.COLOR_WHITE);
        reloadMemo.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        reloadMemo.setText("重新载入");

        tempMemoTabItemBotmTabMemoItem.setControl(tempMemoTabItemMemoComp);

        tempMemoTabItemSash.setWeights(new int[]
        { 70, 30 });
        init();
    }

    private void init()
    {
        addFile.addMouseListener(new MouseListener()
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

                addFile.setEnabled(false);
                updateFile.setEnabled(false);
                Object[] checkList = root.checkTree.getCheckedElements();

                java.util.List<String> checkdList = new ArrayList<String>();

                if (checkList != null)
                {
                    for (Object o : checkList)
                    {
                        SelectCheckBox b = (SelectCheckBox) o;
                        checkdList.add(b.getKey());
                    }
                }
                if (root.getCurMudId() == null)
                {
                    MessageBox box = new MessageBox(topShell,
                            SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
                    // 设置对话框的标题
                    box.setText("Error");
                    // 设置对话框显示的消息
                    box.setMessage("请选择模板!");// 用户名或密码错误！
                    box.open();
                    addFile.setEnabled(true);
                    updateFile.setEnabled(true);
                    return;
                }

                try
                {

                    root.getCurFtlModel().initParam(
                            root.preparedParamItem.preparedParam.getText().trim(),
                            root.userParamItem.userParaText.getText().trim(),
                            root.templateItem.templateText.getText().trim(), checkdList);
                }
                catch (Exception e)
                {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(baos);
                    e.printStackTrace(ps);

                    exeResultText.setText("error:\n" + baos.toString());
                    try
                    {
                        baos.close();
                    }
                    catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                    addFile.setEnabled(true);
                    updateFile.setEnabled(true);
                    return;
                }

                GenNewFile genFile = new GenNewFile(topShell);
                genFile.setResultText(exeResultText);
                genFile.setCurFtlModel(root.curFtlModel);
                genFile.setConfig(root.config);
                genFile.setLocale(true);
                genFile.setProjectPath(root.projectPath);
                genFile.setProjectName(root.projectName);
                genFile.genFile();
                addFile.setEnabled(true);
                updateFile.setEnabled(true);
            }
        });
        updateFile.addMouseListener(new MouseListener()
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
                addFile.setEnabled(false);
                updateFile.setEnabled(false);
                if (root.curMudId == null)
                {
                    MessageBox box = new MessageBox(topShell,
                            SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
                    // 设置对话框的标题
                    box.setText("Error");
                    // 设置对话框显示的消息
                    box.setMessage("请选择模板!");// 用户名或密码错误！
                    box.open();
                    addFile.setEnabled(true);
                    updateFile.setEnabled(true);
                    return;
                }
                Object[] checkList = root.checkTree.getCheckedElements();

                java.util.List<String> checkdList = new ArrayList<String>();

                if (checkList != null)
                {
                    for (Object o : checkList)
                    {
                        SelectCheckBox b = (SelectCheckBox) o;
                        checkdList.add(b.getKey());
                    }
                }

                try
                {
                    root.curFtlModel.initParam(root.preparedParamItem.preparedParam.getText()
                            .trim(), root.userParamItem.userParaText.getText().trim(),
                            root.templateItem.templateText.getText().trim(), checkdList);

                }
                catch (Exception e)
                {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(baos);
                    e.printStackTrace(ps);

                    exeResultText.setText("error:\n" + baos.toString());
                    try
                    {
                        baos.close();
                    }
                    catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                    addFile.setEnabled(true);
                    updateFile.setEnabled(true);
                    return;
                }

                GenEditFile editFile = new GenEditFile(root.curMudId);
                editFile.setProjectPath(root.projectPath);
                editFile.setResultText(exeResultText);
                editFile.setCurFtlModel(root.curFtlModel);
                editFile.setConfig(root.config);
                editFile.setLocale(true);
                editFile.setCheckdList(checkdList);
                editFile.setUserParam(root.userParamItem.userParaText.getText().trim());
                editFile.setPrepatedParam(root.preparedParamItem.preparedParam.getText().trim());
                editFile.setTemplateSrc(root.templateItem.templateText.getText().trim());
                editFile.setProjectName(root.projectName);
                editFile.editFile();
                addFile.setEnabled(true);
                updateFile.setEnabled(true);
            }
        });

        reloadMemo.addMouseListener(new MouseListener()
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
                if (root.curMudId == null)
                {
                    MessageBox box = new MessageBox(topShell,
                            SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
                    // 设置对话框的标题
                    box.setText("Error");
                    // 设置对话框显示的消息
                    box.setMessage("请选择模板!");// 用户名或密码错误！
                    box.open();
                    return;
                }
                try
                {
                    root.reloadCurFtlModelAndTree();
                     reloadTemplateMemo();
                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block

                    EclipseUtil.proExcept(topShell, e);
                }

                // TODO Auto-generated method stub

            }
        });

    }
    public void reloadTemplateMemo( )
    {
        tempalteMemo.setText("");
        PatternMatcherInput pmi = null;
        try
        {
            pmi = new PatternMatcherInput(root.curFtlModel.genSrc());
            while (root.docPm.contains(pmi, root.docPattFile))
            {
                MatchResult maRes = root.docPm.getMatch();
                // maRes.group(1);
                tempalteMemo.append(maRes.group(1) + "\n");
            }
        }
        catch (Exception e)
        {
            EclipseUtil.proExcept(topShell,e);
        
        }
        
    }
}
