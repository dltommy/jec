/**
 * 作 者:  
 * 日 期: 2012-8-26
 * 描 叙:
 */
package com.easycode.gencode.action;
 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
 
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.easycode.gencode.ui.elements.TypeSelect; 
import com.easycode.gencode.ui.IReload;
import com.easycode.gencode.ui.SelectAllMouseListener; 
import com.easycode.resource.MultLang; 
import com.easycode.templatemgr.LocalTemplateMgr;
import com.easycode.templatemgr.RpcFactory;
 
   
import com.easycode.configmgr.IConfigMgr;
import com.easycode.configmgr.model.Config;

/**
 * 功能描叙:
 * 编   码:  
 * 完成时间: 2012-8-26 上午12:49:35
 */
public class CustomDialog extends Dialog implements MouseListener
{
	protected Object result;
	protected Shell parentShell;
	protected Text codeSrc = null;
	protected Text bookmark = null;
	protected Text jsonDesc = null;
	protected Text annoCtx = null;
	private String codeType = null;
	private String mudId = null;
	private Label optLabel = null;
	private SimpleDateFormat dateFt = new SimpleDateFormat("HH:mm:ss");
	private TypeSelect selectList = new TypeSelect();
	private List<String> queryTypeList = new ArrayList<String>();
	// 是否点击保存到本地按钮
	private boolean toLocale = false;
    private IReload rd = null;
	//private IConfigMgr prjConfig = null;
	private Config config = null;
	private String projectPath;
	
	private String templateType = null;
	// 是否点击保存到服务器按钮 
	public CustomDialog(Shell parent, String templateType, String projectPath,Config config,IReload rd)
	{
		this(parent,config,templateType);
		this.rd = rd;
		this.projectPath = projectPath;

	}
 
	public CustomDialog(Shell parent,Config config , String templateType)
		{


			super(parent, SWT.NONE);
		    
			this.config = config; 
			this.parentShell = parent;
		     this.templateType = templateType;
			queryTypeList.add(templateType); 
			/*
			try
			{
				String configType[] = config.getCodeType().split(
						";");
				for (String type : configType)
				{
					if ("".equals(type.trim())
							|| "all".equalsIgnoreCase(type.trim()))
					{
						continue;
					}
					queryTypeList.add(type);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			*/
			createContents();
		}
     
	public void open()
	{
		parentShell.open();
		parentShell.layout();
		//Display display = getParent().getDisplay();
		//while (!parentShell.isDisposed())
		//{
		//	if (!display.readAndDispatch())
		//		display.sleep();
		//}
		//return result;
	}

	public void setSrcCode(String code)
	{
		this.codeSrc.setText(code);
	}

	protected void createContents()
	{

		Label titleSrc = new Label(parentShell, SWT.COLOR_WHITE);
		titleSrc.setText(MultLang.getMultLang("code.007")+"*");//模板

		GridData srcData = new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL);
		srcData.widthHint = 500;
		srcData.heightHint = 300;
		codeSrc = new Text(parentShell, SWT.MULTI | SWT.BORDER
				| SWT.V_SCROLL | SWT.H_SCROLL | SWT.COLOR_WHITE);

		codeSrc.setLayoutData(srcData);
		Label titleAsign = new Label(parentShell, SWT.COLOR_WHITE);
		titleAsign.setText(MultLang.getMultLang("code.004")+"*");//标题
		codeSrc.addMouseListener(new SelectAllMouseListener(codeSrc));
		bookmark = new Text(parentShell, SWT.SINGLE | SWT.BORDER);
		GridData book = new GridData(GridData.FILL_HORIZONTAL);
		book.widthHint = 100;
		bookmark.setTextLimit(50);
		
		bookmark.setLayoutData(book);

		
		final GridData textGridData = new GridData(GridData.FILL_BOTH);
		textGridData.heightHint =30;
		textGridData.widthHint =100;
		Label desc = new Label(parentShell, SWT.COLOR_WHITE);
		desc.setText(MultLang.getMultLang("code.008"));//参数
		jsonDesc = new Text(parentShell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		//GridData bookdesc = new GridData(GridData.FILL_HORIZONTAL);
		//bookdesc.widthHint = 100;
		
		jsonDesc.setLayoutData(textGridData);

		Label annoLabel = new Label(parentShell, SWT.COLOR_WHITE);
		annoLabel.setText(MultLang.getMultLang("code.087"));
		annoCtx = new Text(parentShell,  SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
 
		annoCtx.setLayoutData(textGridData);
		annoCtx.addMouseListener(new SelectAllMouseListener(annoCtx));
		
		Label codeType = new Label(parentShell, SWT.NO);
		codeType.setText(MultLang.getMultLang("code.009"));//类别

		Composite composite = new Composite(parentShell, SWT.COLOR_WHITE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 10;
		final GridData gridData = new GridData(
				GridData.HORIZONTAL_ALIGN_FILL);
		composite.setLayout(layout);
		composite.setLayoutData(gridData);

		for (int i = 0; i < queryTypeList.size(); i++)
		{
			Button action = new Button(composite, SWT.RADIO | SWT.FLAT);
			action.setText(queryTypeList.get(i));
			if (i == 0)
			{
				action.setSelection(true);
				selectList.setTypeCode(queryTypeList.get(i));
			}
			action.addSelectionListener(selectList);
		}

		Composite optCom = new Composite(parentShell, SWT.COLOR_WHITE);
		GridLayout optlayout = new GridLayout();
		optlayout.numColumns = 5;
		final GridData optdData = new GridData(
				GridData.HORIZONTAL_ALIGN_END);
		optdData.horizontalSpan = 3;
		optCom.setLayout(optlayout);
		optCom.setLayoutData(optdData);

		optLabel = new Label(optCom, SWT.NO);
		optLabel.setText("                              ");

		final Button localButton = new Button(optCom, SWT.Activate);
		localButton.setText("保存到本地");
		localButton.setBounds(90, 50, 70, 30);
		localButton.addMouseListener(new MouseListener()
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
				if ("".equals(codeSrc.getText().trim()))
				{
					optLabel.setText("请填写模板内容!" + "("
							+ dateFt.format(new Date()) + ")");
					return;
				}
				if ("".equals(bookmark.getText().trim()))
				{
				    /*
					optLabel.setText("请填写书签!" + "("
							+ dateFt.format(new Date()) + ")");
					
					*/
	                MessageBox box = new MessageBox(parentShell,SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
	                //设置对话框的标题
	                box.setText("提示");
	                //设置对话框显示的消息
	                box.setMessage("请填写标题!"); 
	                box.open();
					return;
				}
				// TODO Auto-generated method stub
				/*
				 * try { prjConfig= ConfigMgr.getPrjConfig(projectPath); }
				 * catch (Exception e) {
				 * 
				 * e.printStackTrace(); }
				 */
				SimpleDateFormat fd = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");

				try
				{
					//LocalSeedMgr tempMgr = new LocalSeedMgr(config.getLocalTemplatePath()
					//		 , projectPath);
					LocalTemplateMgr tempMgr = new LocalTemplateMgr(config.getLocalTemplatePath(),projectPath);
					/*
					mudId = tempMgr.saveOrUpdateLocalMul(config.getUserName(),
						  codeSrc.getText(),
							bookmark.getText(), jsonDesc.getText(),
							selectList.getTypeCode(), fd
									.format(new Date()), mudId,annoCtx.getText().trim());
					
					*/
					
					if(mudId == null)
					{
						mudId = tempMgr.addTemplate(config.getUserName().trim(), codeSrc.getText().trim(), bookmark.getText().trim(), jsonDesc.getText().trim(), selectList.getTypeCode().trim(), annoCtx.getText().trim());
					}
					else
					{
						mudId = tempMgr.updateTemplate(mudId, codeSrc.getText().trim(), bookmark.getText().trim(), jsonDesc.getText().trim(), selectList.getTypeCode().trim(),  annoCtx.getText().trim());
					}
					 
					
					optLabel.setText("Success" + "("
							+ dateFt.format(new Date()) + ")");
					toLocale = true;
					
					 if(rd != null)
					 {
					     rd.reloadLocal();
					 }
					 
				    //parentShell.close();
				    //parentShell.dispose();
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					optLabel.setText("Fault" + "("
							+ dateFt.format(new Date()) + ")");
				}
			}
		}

		);
		final Button closeBut = new Button(optCom, SWT.Activate);
		closeBut.setText("关闭");
		closeBut.setBounds(90, 50, 70, 30);
		closeBut.addMouseListener(new MouseListener()
		{

			public void mouseDoubleClick(MouseEvent e)
			{
			}

			public void mouseDown(MouseEvent e)
			{
			}

			public void mouseUp(MouseEvent e)
			{
				parentShell.close();
			}
		});
		codeSrc.addMouseListener(new SelectAllMouseListener(codeSrc));

		jsonDesc.addMouseListener(new SelectAllMouseListener(jsonDesc));
		bookmark.addMouseListener(new SelectAllMouseListener(bookmark));
        annoCtx.setText("<#--tips:支持freemarker语法,正则表达式-->\nisPk='Y'/lineId,autoId,id;");
        codeSrc.setText(CommUtil.getResource("/com/easycode/gencode/action/init.ftl"));
        
        jsonDesc.setText("/*====tips:按标准json格式填写====*/\n{\n    key:'value'\n}");
        bookmark.setText("新增");
     
	}

	public void mouseDoubleClick(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

	public void mouseUp(MouseEvent e)
	{
		if ("".equals(this.codeSrc.getText().trim()))
		{
            MessageBox box = new MessageBox(parentShell,SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
            //设置对话框的标题
            box.setText("提示");
            //设置对话框显示的消息
            box.setMessage("请填写模板内容!"); 
            box.open();
			return;
		}
		if ("".equals(bookmark.getText().trim()))
		{
 
            MessageBox box = new MessageBox(parentShell,SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
            //设置对话框的标题
            box.setText("提示");
            //设置对话框显示的消息
            box.setMessage("请填写标题!"); 
            box.open();
			return;
		}
  
		//if (projectPath != null)
		{
			/*
			 * if(prjConfig == null) { try { prjConfig=
			 * ConfigMgr.getPrjConfig(projectPath); } catch (Exception e1) {
			 * e1.printStackTrace(); } }
			 */
			// prjConfig = this.getPrjConfig();
			String url = config.getSrvUrl();
			try
			{
				String tips = "";
				String res = null;
				if (mudId == null)
				{
					mudId = UUID.randomUUID().toString();
					 res = RpcFactory.httpSrv(url).addCodegenMudls(
							 config.getUserName(),
							 config.getPassword(),
							mudId,
							this.bookmark.getText(),
							this.codeSrc.getText(),
							this.jsonDesc.getText(),
							selectList.getTypeCode(),
							java.util.UUID.randomUUID().toString(),annoCtx.getText().trim());

				}
				else
				{
					 res = RpcFactory.httpSrv(url).addCodegenMudls(
							 config.getUserName(),
							 config.getPassword(),
							mudId,
							this.bookmark.getText(),
							this.codeSrc.getText(),
							this.jsonDesc.getText(),
							selectList.getTypeCode(), mudId,annoCtx.getText().trim());
				}
				 if("0001".equals(res) || res == null)
				 {
					 tips = "Err";
				 }
				 else
				 {
				    tips = "Success"; 
				 }
				optLabel.setText(tips+"(" + dateFt.format(new Date())
						+ ")");
			}
			catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
				optLabel.setText("Err(" + dateFt.format(new Date()) + ")");
			}
		}

	}

	public void mouseDown(MouseEvent e)
	{
	}
}
