package com.easycode.gencode.ui.main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;

import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.easycode.Constants;
import com.easycode.common.EclipseUtil;
import com.easycode.gencode.action.CustomDialog;
import com.easycode.gencode.ui.SelectAllMouseListener;
import com.easycode.resource.MultLang;
import com.easycode.templatemgr.RpcFactory;

public class TemplateMgrItem extends CTabItem
{
    Shell topShell = null;
    MainUi root = null;
    Text templateText = null;
    Text annoText = null;
    Text uptBookmark = null;
    Text codeTypeCombo = null;
    Button reply = null;
    Button save = null;
    Button turnToLocal = null;
    Button newButt = null;
    Button delButton = null;
    CTabFolder paramTab = null;

    public TemplateMgrItem(CTabFolder parent, int style, final MainUi root)
    {
        super(parent, style);
        topShell = parent.getShell();
        this.root = root;
        Composite templateCom = new Composite(parent, SWT.COLOR_WHITE);

        templateCom.setLayout(this.root.commonLayout);
        templateCom.setLayoutData(this.root.bothFillData);

        Composite buttCom = new Composite(templateCom, SWT.NO);
        GridLayout lout = new GridLayout();
        lout.numColumns = 4;
        buttCom.setLayout(lout);
        buttCom.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));

        templateText = new Text(templateCom, SWT.MULTI | SWT.BORDER
                | SWT.V_SCROLL | SWT.H_SCROLL | SWT.COLOR_WHITE);
        templateText.addMouseListener(new SelectAllMouseListener(templateText));
        templateText.setLayoutData(this.root.bothFillData);
        Composite otherCom = new Composite(templateCom, SWT.NO);
        otherCom.setLayout(this.root.commonLayout);
        otherCom.setLayoutData(this.root.bothFillData);

        paramTab = new CTabFolder(otherCom, SWT.BORDER);

        paramTab.setLayoutData(this.root.bothFillData);

        CTabItem bookmarkItem = new CTabItem(paramTab, SWT.NONE);
        bookmarkItem.setText(MultLang.getMultLang("code.004"));

        final GridData paramData = new GridData(GridData.FILL_BOTH);
        paramData.heightHint = 30;

        CTabItem anItem = new CTabItem(paramTab, SWT.NONE);
        anItem.setText("注释");

        CTabItem typeItem = new CTabItem(paramTab, SWT.NONE);
        typeItem.setText("类型");

        annoText = new Text(paramTab, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL
                | SWT.H_SCROLL | SWT.COLOR_WHITE);
        annoText.addMouseListener(new SelectAllMouseListener(annoText));
        annoText.setLayoutData(paramData);
        anItem.setControl(annoText);

        uptBookmark = new Text(paramTab, SWT.SINGLE | SWT.BORDER);// 标题
        uptBookmark.setTextLimit(50);
        bookmarkItem.setControl(uptBookmark);

        codeTypeCombo = new Text(paramTab, SWT.SINGLE | SWT.BORDER);
        codeTypeCombo.setTextLimit(20);
        typeItem.setControl(codeTypeCombo);

        paramTab.setSelection(0);

        this.setControl(templateCom);

        Composite optComp = new Composite(otherCom, SWT.NO);
        GridLayout sublayoutoptComp = new GridLayout();
        GridData subgridDataoptComp = new GridData(
                GridData.HORIZONTAL_ALIGN_END);

        sublayoutoptComp.numColumns = 6;
        optComp.setLayout(sublayoutoptComp);
        optComp.setLayoutData(subgridDataoptComp);

        reply = new Button(optComp, SWT.NO);
        reply.setText("应用");
        reply.setLayoutData(new GridData());

        save = new Button(optComp, SWT.FLAT | SWT.COLOR_WHITE);
        save.setText(MultLang.getMultLang("code.019"));
        save.setLayoutData(new GridData());

        turnToLocal = new Button(optComp, SWT.FLAT | SWT.COLOR_WHITE);
        turnToLocal.setText(MultLang.getMultLang("code.003") + "  ");// 同步到本地

        newButt = new Button(optComp, SWT.NO);
        newButt.setText(MultLang.getMultLang("code.088"));// 新增
        newButt.setLayoutData(new GridData());
        delButton = new Button(optComp, SWT.Activate);

        delButton.setLayoutData(new GridData());
        delButton.setText(MultLang.getMultLang("code.020"));// 删除
        init(this);
    }

    private void init(final TemplateMgrItem item)
    {
        templateText.addModifyListener(new ModifyListener()
        {

            public void modifyText(ModifyEvent arg0)
            {
                // TODO Auto-generated method stub

                if (root.isFirstMdf == true)
                {
                    // hasEdit = true;
                    item.setText(MultLang.getMultLang("code.007"));// 模板
                    root.isFirstMdf = false;
                }
                else
                {
                    item.setText("*" + MultLang.getMultLang("code.007"));// 模板
                }
            }
        });

        reply.addMouseListener(new MouseListener()
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

                if (root.curMudId == null)
                {
                    MessageBox box = new MessageBox(topShell,
                            SWT.APPLICATION_MODAL | SWT.ICON_ERROR);

                    box.setText("Error");

                    box.setMessage("请选择模板!");
                    box.open();
                    return;
                }

                try
                {
                    root.reloadCurFtlModelAndTree();

                    root.tempMemoTabItem.reloadTemplateMemo();
                }
                catch (Exception e)
                {
                    EclipseUtil.proExcept(topShell, e);
                }

            }
        });

        save.addMouseListener(new MouseListener()
        {

            public void mouseDoubleClick(MouseEvent arg0)
            {
            }

            public void mouseDown(MouseEvent arg0)
            {
            }

            public void mouseUp(MouseEvent arg0)
            {
                saveMdl(true);
            }
        });

        turnToLocal.addMouseListener(new MouseListener()
        {

            public void mouseDoubleClick(MouseEvent arg0)
            {
            }

            public void mouseDown(MouseEvent arg0)
            {
            }

            public void mouseUp(MouseEvent arg0)
            {
                if (root.curMudId == null || "".equals(root.curMudId))
                {

                    MessageBox box = new MessageBox(topShell,
                            SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
                    // 设置对话框的标题
                    box.setText("提示信息");
                    // 设置对话框显示的消息
                    box.setMessage("请选择一个模板");
                    box.open();

                    return;
                }

                String wsUrl = root.config.getSrvUrl();
                if (root.isLocal)
                {

                    // 添加到服务器
                    // System.err.println("添加到服务器");
                    String res = null;
                    try
                    {
                        res = RpcFactory.httpSrv(wsUrl).addCodegenMudls(
                                root.config.getUserName(),
                                root.config.getPassword(), root.curMudId,
                                uptBookmark.getText(), templateText.getText(),
                                root.userParamItem.userParaText.getText(),
                                getUptCodeType(), root.curReferMudId,
                                annoText.getText());
                        if (res == null)
                        {
                            // optResult.setText("Err("
                            // + dateFt.format(new Date()) + ")");

                            MessageBox box = new MessageBox(topShell,
                                    SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
                            // 设置对话框的标题
                            box.setText("提示信息");
                            // 设置对话框显示的消息
                            box.setMessage(MultLang.getMultLang("code.076"));// 操作失败
                            box.open();
                            return;
                        }
                        else if ("0001".equals(res))
                        {
                            // optResult.setText("Err pwd(" + dateFt.format(new
                            // Date())
                            // + ")");
                            MessageBox box = new MessageBox(topShell,
                                    SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
                            // 设置对话框的标题
                            box.setText("提示信息");
                            // 设置对话框显示的消息
                            box.setMessage(MultLang.getMultLang("code.075"));// 用户名或密码错误！
                            box.open();
                            return;
                        }
                        else if ("0002".equals(res))
                        {
                            MessageBox box = new MessageBox(topShell,
                                    SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
                            // 设置对话框的标题
                            box.setText("提示信息");
                            // 设置对话框显示的消息
                            box.setMessage(MultLang.getMultLang("code.077"));// 权限异常
                            box.open();
                            return;
                        }
                        else if ("0003".equals(res))
                        {
                            // optResult.setText("Err("
                            // + dateFt.format(new Date()) + ")");
                            MessageBox box = new MessageBox(topShell,
                                    SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
                            // 设置对话框的标题
                            box.setText("提示信息");
                            // 设置对话框显示的消息
                            box.setMessage(MultLang.getMultLang("code.076")
                                    + ":0003");// 操作失败
                            box.open();
                            return;
                        }
                        else if ("0000".equals(res))
                        {

                            // optResult.setText("Success("
                            // + dateFt.format(new Date()) + ")");
                            MessageBox box = new MessageBox(topShell,
                                    SWT.APPLICATION_MODAL
                                            | SWT.ICON_INFORMATION);
                            // 设置对话框的标题
                            box.setText("提示信息");
                            // 设置对话框显示的消息
                            box.setMessage(MultLang.getMultLang("code.081"));// 同步成功
                            box.open();
                            return;
                        }
                        else
                        {
                            MessageBox box = new MessageBox(topShell,
                                    SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
                            // 设置对话框的标题
                            box.setText("提示信息");
                            // 设置对话框显示的消息
                            box.setMessage(MultLang.getMultLang("code.076"));// 操作失败
                            box.open();
                            return;
                        }
                    }
                    catch (Exception e)
                    {
                        // optResult.setText("Err("
                        // + dateFt.format(new Date()) + ")");
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

                        MessageBox box = new MessageBox(topShell,
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
                    // 确保referId不变
                    SimpleDateFormat fd = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");

                    try
                    {

                        root.templateMgr.serverToLocal(root.curMudId,
                                root.curOptSeed, templateText.getText(),
                                uptBookmark.getText(),
                                root.userParamItem.userParaText.getText(),
                                root.queryModelType.getText(),
                                root.serverMdlVersion, annoText.getText());

                    }
                    catch (Exception e)
                    {

                        // optResult.setText("Err("
                        // + dateFt.format(new Date()) + ")");
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

                        MessageBox box = new MessageBox(topShell,
                                SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
                        // 设置对话框的标题
                        box.setText("提示信息");
                        // 设置对话框显示的消息
                        box.setMessage(MultLang.getMultLang("code.076") + ":"
                                + baos.toString());// 操作失败
                        box.open();
                        return;

                    }
                    // optResult.setText("Success(" + dateFt.format(new Date())
                    // + ")");
                    MessageBox box = new MessageBox(topShell,
                            SWT.APPLICATION_MODAL | SWT.ICON_INFORMATION);
                    // 设置对话框的标题
                    box.setText("提示信息");
                    // 设置对话框显示的消息
                    box.setMessage(MultLang.getMultLang("code.081"));// 同步成功
                    box.open();
                }
            }
        });

        newButt.addMouseListener(new MouseListener()
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
                Shell shell = new Shell(topShell, SWT.DIALOG_TRIM
                        | SWT.APPLICATION_MODAL | SWT.COLOR_WHITE);
                shell.setSize(800, 600);
                shell.setText(MultLang.getMultLang("code.088"));// 新增

                GridLayout layout = new GridLayout();
                layout.numColumns = 2;
                // Color color =
                // Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
                shell.setLayout(layout);
                
                
                String type = Constants.TEMPLATE_JAVA;
                
                
                if(root.loadPreparedParam instanceof LoadPOJOParam)
                {
                    type = Constants.TEMPLATE_JAVA;
                }
                else if(root.loadPreparedParam instanceof LoadDBParam)
                {
                    type = Constants.TEMPLATE_DB;
                }
                else if(root.loadPreparedParam instanceof LoadJsonParam)
                {
                    type = Constants.TEMPLATE_JSON;
                }
                
                
                
                
                CustomDialog window = new CustomDialog(shell, type, root.projectPath,
                        root.config, root);

                window.open();

            }
        });

        delButton.addMouseListener(new MouseListener()
        {

            public void mouseDoubleClick(MouseEvent arg0)
            {
            }

            public void mouseDown(MouseEvent arg0)
            {
            }

            public void mouseUp(MouseEvent arg0)
            {
                if (root.curMudId == null)
                {
                    MessageBox box = new MessageBox(topShell,
                            SWT.APPLICATION_MODAL | SWT.ICON_WARNING);
                    // 设置对话框的标题
                    box.setText("提示信息");
                    // 设置对话框显示的消息
                    box.setMessage("请选择要删除的模板!");
                    box.open();
                    return;
                }
                boolean delete = MessageDialog.openConfirm(topShell,
                        MultLang.getMultLang("code.020"),
                        MultLang.getMultLang("code.021"));// 您确定要删除该记录吗？
                if (!delete)
                {
                    return;
                }

                String wsUrl = root.config.getSrvUrl();

                // 本地
                if (root.isLocal)
                {
                    try
                    {
                        // templateMgr.deleteLocalMul(config.getUserName(),
                        // curMudId );
                        root.templateMgr.deleteTemplate(root.curMudId);
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    // optResult.setText("Delete Local Success("
                    // + dateFt.format(new Date()) + ")");

                    root.queryMudls();
                    // resetMain();
                    MessageBox box = new MessageBox(topShell,
                            SWT.APPLICATION_MODAL | SWT.ICON_INFORMATION);
                    // 设置对话框的标题
                    box.setText("提示信息");
                    // 设置对话框显示的消息
                    box.setMessage("Success!");
                    box.open();

                }
                else
                {

                    String retCode = null;
                    try
                    {
                        retCode = RpcFactory.httpSrv(wsUrl).deleteCodegenMudls(
                                root.config.getUserName(),
                                root.config.getPassword(), root.curMudId);
                        Shell shell = new Shell(SWT.DIALOG_TRIM
                                | SWT.APPLICATION_MODAL);

                        if ("0000".equals(retCode))
                        {

                            root.queryMudls();
                            // resetMain();
                            MessageBox box = new MessageBox(topShell,
                                    SWT.APPLICATION_MODAL
                                            | SWT.ICON_INFORMATION);
                            // 设置对话框的标题
                            box.setText("提示信息");
                            // 设置对话框显示的消息
                            box.setMessage("Success!");
                            box.open();
                        }
                        else if ("0001".equals(retCode))
                        {

                            MessageBox box = new MessageBox(topShell,
                                    SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
                            // 设置对话框的标题
                            box.setText("提示信息");
                            // 设置对话框显示的消息
                            box.setMessage("禁止删除！");
                            box.open();
                        }
                        else if ("0003".equals(retCode))
                        {
                            // optResult.setText("Err Pwd("
                            // + dateFt.format(new Date())
                            // + ")");
                            MessageBox box = new MessageBox(topShell,
                                    SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
                            // 设置对话框的标题
                            box.setText("提示信息");
                            // 设置对话框显示的消息
                            box.setMessage("密码错误！");
                            box.open();
                        }
                        else

                        {
                            // optResult.setText("Delete False("
                            // + dateFt.format(new Date())
                            // + ")");
                            MessageBox box = new MessageBox(topShell,
                                    SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
                            // 设置对话框的标题
                            box.setText("提示信息");
                            // 设置对话框显示的消息
                            box.setMessage("操作失败！");
                            box.open();
                        }

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
                // }
            }
        });
    }

    protected void saveMdl(boolean needQuery)
    {

        if (root.curMudId == null)
        {

            MessageBox box = new MessageBox(topShell, SWT.APPLICATION_MODAL
                    | SWT.ICON_WARNING);
            // 设置对话框的标题
            box.setText("警告");
            // 设置对话框显示的消息
            box.setMessage("请选择要保存的模板!");
            box.open();
            return;
        }

        // 标题不能为空
        if ("".equals(uptBookmark.getText().trim()))
        {
            paramTab.setSelection(0);
            MessageBox box = new MessageBox(topShell, SWT.APPLICATION_MODAL
                    | SWT.ICON_INFORMATION);
            // 设置对话框的标题
            box.setText("操作失败");
            // 设置对话框显示的消息
            box.setMessage("标题不能为空！");
            box.open();
            return;
        }
        if ("".equals(templateText.getText().trim()))
        {

            MessageBox box = new MessageBox(topShell, SWT.APPLICATION_MODAL
                    | SWT.ICON_INFORMATION);
            // 设置对话框的标题
            box.setText("操作失败");
            // 设置对话框显示的消息
            box.setMessage("模板不能为空！");
            box.open();
            return;
        }
        // 模板内容不能为空
        // 模板类型不能为空

        if ("".equals(getUptCodeType().trim()))
        {
            paramTab.setSelection(3);
            MessageBox box = new MessageBox(topShell, SWT.APPLICATION_MODAL
                    | SWT.ICON_INFORMATION);
            // 设置对话框的标题
            box.setText("操作失败");
            // 设置对话框显示的消息
            box.setMessage("类型不能为空！");
            box.open();
            return;
        }

        /*
         * 
         * if (projectPath != null) { String dir = projectPath + "/GenConfig/";
         * File f = new File(dir); if (!f.exists()) { f.mkdir(); }
         */
        String wsUrl = root.config.getSrvUrl();
        try
        {

            SimpleDateFormat fd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (root.isLocal)
            {
                /*
                 * templateMgr.updateLocalMuld(templateText.getText(),
                 * uptBookmark.getText(), userParaText.getText(),
                 * getUptCodeType(), fd.format(new Date()), curMudId,
                 * annoText.getText());
                 */

                root.templateMgr.updateTemplate(root.curMudId, templateText
                        .getText().trim(), uptBookmark.getText().trim(),
                        root.userParamItem.userParaText.getText().trim(),
                        getUptCodeType(), annoText.getText().trim());

                this.setText(MultLang.getMultLang("code.007"));// 模板
                // mdlHasEdit = false;

                if (needQuery)
                {

                    root.queryMudls();

                }

                MessageBox box = new MessageBox(topShell, SWT.APPLICATION_MODAL
                        | SWT.ICON_INFORMATION);
                // 设置对话框的标题
                box.setText("Success");
                // 设置对话框显示的消息
                box.setMessage("Success!");
                box.open();

            }

            else
            {

                String ret = RpcFactory.httpSrv(wsUrl).updateCodegenMudls(
                        root.config.getUserName(), root.config.getPassword(),
                        root.curMudId, uptBookmark.getText(),
                        templateText.getText(),
                        root.userParamItem.userParaText.getText(),
                        getUptCodeType(), annoText.getText());
                if ("0000".equals(ret))
                {

                    this.setText(MultLang.getMultLang("code.007"));// 模板

                    if (needQuery)
                    {
                        root.queryMudls();
                    }
                    MessageBox box = new MessageBox(topShell,
                            SWT.APPLICATION_MODAL | SWT.ICON_INFORMATION);
                    // 设置对话框的标题
                    box.setText("Success");
                    // 设置对话框显示的消息
                    box.setMessage("Success!");
                    box.open();
                }
                else if ("0001".equals(ret))
                {

                    MessageBox box = new MessageBox(topShell,
                            SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
                    // 设置对话框的标题
                    box.setText("Error");
                    // 设置对话框显示的消息
                    box.setMessage("只能操作自己创建的模板！");
                    box.open();
                }
                else
                {

                    MessageBox box = new MessageBox(topShell,
                            SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
                    // 设置对话框的标题
                    box.setText("Error");
                    // 设置对话框显示的消息
                    box.setMessage("Err");
                    box.open();
                }
            }

        }
        catch (Exception e1)
        {
            e1.printStackTrace();

            MessageBox box = new MessageBox(topShell, SWT.APPLICATION_MODAL
                    | SWT.ICON_ERROR);
            // 设置对话框的标题
            box.setText("Error");
            // 设置对话框显示的消息
            box.setMessage("Err");
            box.open();
        }
        // }

    }

    public void setUptCodeType(String type)
    {
        if (type == null)
        {
            codeTypeCombo.setText("");
        }
        else
        {
            codeTypeCombo.setText(type);
        }
    }

    public String getUptCodeType()
    {
        return codeTypeCombo.getText().trim();
    }

    public void setUptbookmark(String uptCtx)
    {
        uptBookmark.setText(uptCtx);
    }

    public void setAnnoText(String annoText)
    {
        this.annoText.setText(annoText);
    }

    public String getAnnoText()
    {
        return annoText.getText();
    }

}
