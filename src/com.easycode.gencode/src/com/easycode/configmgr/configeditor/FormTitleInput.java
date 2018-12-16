/**
 * 作 者: 
 * 日 期: 2007-2-1
 * 描 叙:
 */
package com.easycode.configmgr.configeditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * 功能描叙:
 * 编   码:  
 * 完成时间: 2007-2-1 下午06:11:41
 */
public class FormTitleInput
{

	private Label titleLabel = null;
	private Text input = null;
    public FormTitleInput(FormToolkit kit,Composite parent,String title, String txt, int type)
    {
    	titleLabel = kit.createLabel(parent, title);

    	input = kit.createText(parent, txt, type);//new Text(parent,SWT.SINGLE | SWT.BORDER);
    	
		GridData inputData = new GridData(GridData.FILL_HORIZONTAL);
		inputData.widthHint = 200;
    	input.setLayoutData(inputData);
    }
    public void setText(String txt)
    {
    	this.input.setText(txt);
    }
    public String getInput()
    {
    	return input.getText();
    }
    public TitleInput apendInput(Composite parent,String title)
    {
    	return new TitleInput(parent,title);
    }
    public void disableEdit()
    {
        this.input.setEditable(false);
    }
    public void setEnable(boolean ctrl)
    {
        this.input.setEnabled(ctrl);
    }
}

