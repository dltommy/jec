/**
 * 作 者:  
 * 日 期: 2012-11-27
 * 描 叙:
 */
package com.easycode.gencode.action;
 
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.easycode.javaparse.JavaSrcParse;
import com.easycode.gencode.ui.IReload;
import com.easycode.gencode.ui.elements.ModelSelect;
import com.easycode.gencode.ui.main.LoadJsonParam;
import com.easycode.gencode.ui.main.LoadPOJOParam;
import com.easycode.gencode.ui.main.MainUi;
import com.easycode.configmgr.ConfigMgrFactory;
import com.easycode.configmgr.model.Config;

/**
 * 功能描叙:
 * 编   码:  
 * 完成时间: 2012-11-27 下午09:36:36
 */
public class ReviewJsonDialog  extends Dialog
{

	protected Shell parentShell; 
  
 
	private IFile file = null;
	private Config config = null;
 
	
	public ReviewJsonDialog(Shell parent, 
 
	          IFile file)
		{
	    super(parent, SWT.NONE);
	    
	      IReload maintance = null;
          this.file = file;
          this.parentShell = parent;
         
        try
        { 
            config = ConfigMgrFactory.newByPrjPath(null).readOrCreate(null);
           
    
			maintance = new MainUi(null,file.getProject().getLocation().toFile().getPath() , config, parent, file.getProject().getName(), 
			      new String[]{file.getName()},null, new LoadJsonParam(file));
			 
            if(maintance != null)
            {
                parent.pack();
                 
               // maintance.queryLocal();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            e.printStackTrace(ps);
            // baos.toString();i
                MessageBox box = new MessageBox( new Shell() ,SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
             //设置对话框的标题
             box.setText("Error");
             box.setMessage(baos.toString());
             box.open();
             //设置对话框显示的消息
             //内容部能为空
            return;
        }

		}
  




	public void open()
	{
		parentShell.pack();
		parentShell.open();
		parentShell.layout();

		/*
		 * Display display = getParent().getDisplay(); while
		 * (!parentShell.isDisposed()) { if (!display.readAndDispatch())
		 * display.sleep(); }
		 */
		// return result;
	}



}

