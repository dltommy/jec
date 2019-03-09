package com.easycode.gencode.ui.main;

import org.eclipse.jface.viewers.TreeViewer;
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
import com.easycode.gencode.ui.SelectAllMouseListener;
import com.easycode.gencode.ui.elements.PreparedParamTreeContentProvider;
import com.easycode.gencode.ui.elements.PreparedParamTreeLabelProvider;

public class UserParamItem extends CTabItem
{

    private Shell topShell = null;
    Text userParaText = null;

    MainUi root = null;
    Button replyUserParam = null;
    Button formatBut = null;
    Button saveUserParam = null;
    TreeViewer userParamTree = null;
    PreparedParamTreeContentProvider userParamTreeContentProvider = new PreparedParamTreeContentProvider();
    PreparedParamTreeLabelProvider userParamTreeLabelProvider = new PreparedParamTreeLabelProvider();

    public UserParamItem(CTabFolder parent, int style, final MainUi root)
    {
        super(parent, style);
        this.topShell = parent.getShell();
        this.root = root;
        SashForm userParamTabItemSash = new SashForm(parent, SWT.VERTICAL);
        userParamTabItemSash.setLayout(new FormLayout());
        userParamTabItemSash.setLayoutData(root.bothFillData);

        this.setControl(userParamTabItemSash);

        Composite exeUserParamCom = new Composite(userParamTabItemSash,
                SWT.BORDER);

        exeUserParamCom.setLayout(this.root.commonLayout);
        exeUserParamCom.setLayoutData(this.root.bothFillData);

        userParaText = new Text(exeUserParamCom, SWT.MULTI | SWT.WRAP
                | SWT.H_SCROLL | SWT.V_SCROLL);

        userParaText.setLayoutData(this.root.bothFillData);
        userParaText.addMouseListener(new SelectAllMouseListener(userParaText));

        Composite userParamButt = new Composite(exeUserParamCom, SWT.NONE);

        userParamButt
                .setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));

        GridLayout userParamButtLayout = new GridLayout();
        userParamButtLayout.numColumns = 3;

        userParamButt.setLayout(userParamButtLayout);

        formatBut = new Button(userParamButt, SWT.NONE);

        formatBut.setText("格式化");

        replyUserParam = new Button(userParamButt, SWT.NONE);
        replyUserParam.setText("应用");
        replyUserParam.setLayoutData(new GridData());

        saveUserParam = new Button(userParamButt, SWT.NONE);
        saveUserParam.setText("保存");
        saveUserParam.setLayoutData(new GridData());
        GridData userParamTreeGridData = new GridData(GridData.FILL_BOTH);
        userParamTreeGridData.horizontalSpan = 3;
        Composite userParamtreeComp = new Composite(userParamTabItemSash,
                SWT.NONE);
        userParamtreeComp.setLayout(this.root.commonLayout);
        userParamtreeComp.setLayoutData(userParamTreeGridData);
        userParamTree = new TreeViewer(userParamtreeComp, SWT.BORDER
                | SWT.MULTI | SWT.COLOR_WHITE | SWT.H_SCROLL | SWT.V_SCROLL
                | SWT.WRAP);
        userParamTree.getTree().setLayoutData(this.root.bothFillData);

        userParamTabItemSash.setWeights(new int[]
        { 70, 30 });
        init();
    }

    private void init()
    {
        replyUserParam.addMouseListener(new MouseListener()
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
                    MessageBox box = new MessageBox(topShell.getShell(),
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

        saveUserParam.addMouseListener(new MouseListener()
        {

            public void mouseDoubleClick(MouseEvent arg0)
            {
            }

            public void mouseDown(MouseEvent arg0)
            {
            }

            public void mouseUp(MouseEvent arg0)
            {
                root.templateItem.saveMdl(true);
            }
        });
        GridData butlayoudata = new GridData(GridData.HORIZONTAL_ALIGN_END);
        formatBut.setLayoutData(butlayoudata);

        formatBut.addMouseListener(new MouseListener()
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
                    userParamTreeContentProvider.init(userParaText.getText());

                    userParamTree
                            .setContentProvider(userParamTreeContentProvider);
                    userParamTree.setLabelProvider(userParamTreeLabelProvider);
                    userParamTree.setInput("user");
                }
                catch (Exception e)
                {
                    EclipseUtil.proExcept(topShell, e, e.getMessage());
                }
            }

        });
    }
}
