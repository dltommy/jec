package com.easycode.gencode.ui.main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Canvas;

import com.easycode.common.EclipseConsoleUtil;
import com.easycode.gencode.service.RegxUtil;
import com.easycode.resource.MultLang;
import com.easycode.templatemgr.FileGenModel;
import com.easycode.templatemgr.util.SrcUtil;
import com.easycode.uml.figure.GenCodeDraw;
public class OutLineItem extends CTabItem
{

    Shell topShell = null;
    MainUi root = null;
    Canvas canvas = null;
    Canvas canvas2 = null;
    LightweightSystem lws = null;
    LightweightSystem lws2 = null;
      boolean canvasMax = false;
      boolean canvasMax2 = false;
      SashForm vasForm = null;
    public OutLineItem(CTabFolder parent, int style,  final MainUi root)
    {
        super(parent, style); 
        topShell = parent.getShell();
        this.root = root;
        Composite helpCom = new Composite(parent, SWT.NONE);
        helpCom.setBackground(parent.getDisplay().getSystemColor(
                SWT.COLOR_WHITE));
        helpCom.setLayout(root.commonLayout);
        helpCom.setLayoutData(root.bothFillData);
 
         vasForm = new SashForm(helpCom, SWT.VERTICAL);
 
        vasForm.setLayoutData(root.bothFillData);

        vasForm.setLayout(root.commonLayout);

   

        canvas = new Canvas(vasForm, SWT.BORDER);
        canvas.setLayout(root.commonLayout);

        canvas.setLayoutData(root.bothFillData);
        canvas.setBackground(parent.getDisplay().getSystemColor(
                SWT.COLOR_WHITE));
        lws = new LightweightSystem(canvas);

        canvas2 = new Canvas(vasForm, SWT.BORDER);
        canvas2.setLayout(root.commonLayout);
        canvas2.setLayoutData(root.bothFillData);
        canvas2.setBackground(parent.getDisplay().getSystemColor(
                SWT.COLOR_WHITE));
        lws2 = new LightweightSystem(canvas2);

        this.setControl(helpCom);
        
        init();
    }
    private void init()
    {
        canvas.addMouseListener(new MouseListener()
        {

            public void mouseDoubleClick(MouseEvent arg0)
            {
                // TODO Auto-generated method stub
                if (canvasMax2)
                {
                    vasForm.setMaximizedControl(canvas);
                    canvasMax = true;
                    return;
                }
                if (canvasMax == true)
                {
                    vasForm.setMaximizedControl(null);
                    canvasMax = false;
                }
                else
                {
                    vasForm.setMaximizedControl(canvas);
                    canvasMax = true;
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
        });

        canvas2.addMouseListener(new MouseListener()
        {

            public void mouseDoubleClick(MouseEvent arg0)
            {
                // TODO Auto-generated method stub
                if (canvasMax)
                {
                    vasForm.setMaximizedControl(canvas2);
                    canvasMax2 = true;
                    return;
                }
                if (canvasMax2 == true)
                {
                    vasForm.setMaximizedControl(null);
                    canvasMax2 = false;
                }
                else
                {
                    vasForm.setMaximizedControl(canvas2);
                    canvasMax2 = true;
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
        });

        vasForm.setWeights(new int[]
        { 50, 50 });

    }

    protected void redraw()
    {
        if(root.curFtlModel == null)
        {
            return;
        }
        if (this.getParent().getSelectionIndex() == 4)
        {
            lws.setContents(new ScrollPane());
            canvas.setData(null);
            canvas.redraw();

            lws2.setContents(new ScrollPane());
            canvas2.setData(null);
            canvas2.redraw();

            if (!"".equals(root.preparedParamItem.preparedParam.getText().trim()))
            {
                try
                {
                    Map<String, Object> map = SrcUtil.josnToMap(root.preparedParamItem.preparedParam.getText());
                    if (map.size() > 0)
                    {
                        GenCodeDraw.createUMLClass(map, canvas, lws);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            try
            {
                root.curFtlModel.initParam(root.preparedParamItem.preparedParam.getText().trim(),
                        root.userParamItem.userParaText.getText().trim(), root.templateItem.templateText.getText()
                                .trim(), root.checkdList);
            }
            catch (Exception e3)
            {
                // TODO Auto-generated catch block

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                e3.printStackTrace(ps);
                EclipseConsoleUtil.printToConsole(baos.toString(), true);
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
            if (root.curFtlModel != null)
            {
                try
                {
                    root.curFtlModel.init(root.config.getSrvUrl(),
                            root.config.getLocalTemplatePath(), root.isLocal, root.projectPath);
                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
            java.util.List<String> headWarnErrList = null;
            try
            {
                root.curFtlModel.updateTreeNode(root.config.getSrvUrl(),
                        root.config.getLocalTemplatePath(), root.isLocal, false, root.projectPath);
            }
            catch (Exception e2)
            {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }

            FileGenModel newFile = null;
            ;
            try
            {
                newFile = new FileGenModel(root.curFtlModel.genSrc());
            }
            catch (Exception e)
            {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos);
                e.printStackTrace(ps);
                // viewGenSrc.setText("异常:\n" + baos.toString());
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
            String reparedSrc = newFile.getProdSrc().trim();

            java.util.List<String> warnErrList = null;
            try
            {
                warnErrList = RegxUtil.getWarnErrPatList(reparedSrc,
                        root.errWarnRegx);// warnRegx);
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (warnErrList != null && warnErrList.size() > 0)
            {
                for (String tp : warnErrList)
                {
                    // tempSb.append(tp+"\n");
                    if (tp.startsWith("WARNING:"))
                    {
                        boolean sure = MessageDialog.openConfirm(topShell,
                                MultLang.getMultLang("code.079"), tp);
                        if (!sure)
                        {
                            // exeResult.append("End.\n");
                            return;
                        }
                    }
                    else if (tp.startsWith("ERR:"))
                    {
                        MessageDialog.openError(topShell,
                                MultLang.getMultLang("code.080"), tp);
                        // exeResult.append("End.\n");
                        return;
                    }
                }
            }
 
            reparedSrc = reparedSrc.replaceAll(
                    "<ftl_doc>[\\s|\\S]*?</ftl_doc>", "");

            reparedSrc = reparedSrc.replaceAll(
                    "<ftl_warn>[\\s|\\S]*?</ftl_warn>", "");
            reparedSrc = reparedSrc.replaceAll(
                    "<ftl_err>[\\s|\\S]*?</ftl_err>", "");

            GenCodeDraw.createGenFileFigure(newFile.getFileList(), canvas2,
                    lws2, 420, 810);
        }
    }
}
