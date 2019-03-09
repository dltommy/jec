/**
 * 作 者:  
 * 日 期: 2012-11-27
 * 描 叙:
 */
package com.easycode.configmgr.configeditor;
 
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Dialog;
//import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import com.easycode.gencode.core.dbtool.DbMgr;
import com.easycode.gencode.core.dbtool.TableModel;
import com.easycode.gencode.core.javaparse.JavaSrcParse;
import com.easycode.gencode.ui.elements.ModelSelect;
import com.easycode.gencode.ui.main.ILoadPreparedParam;
import com.easycode.gencode.ui.main.LoadDBParam;
import com.easycode.gencode.ui.main.LoadPOJOParam;
import com.easycode.gencode.ui.main.MainUi;
import com.easycode.configmgr.ConfigMgrFactory;
import com.easycode.configmgr.model.Config;
import com.easycode.configmgr.model.Config.DB;

/**
 * 功能描叙:
 * 编   码:  
 * 完成时间: 2012-11-27 下午09:36:36
 */
public class DBCodeGenDialog  extends Dialog
{

	protected Shell parentShell; 
  
 
	private String pkgSource = null;
	private Config config = null;
 
	
	public DBCodeGenDialog(
	        Shell parent,  
	        TableModel table,
	         
	          String[] tables,
	          String selectedTable,
              String projectPath, 
              String projectName
	         )
		{
	    //super(parent, SWT.NONE);
	    super(parent);
	    
 
 
        
          this.parentShell = parent;
         
        try
        { 
            config = ConfigMgrFactory.newByPrjPath(projectPath).readOrCreate(null);
 
            
               
            MainUi maintance = new MainUi(null,projectPath, config, parent , 
                       
                       projectName, tables ,null,new LoadDBParam(table  ));
         
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

