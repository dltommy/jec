/**
 * 作 者: 欧阳超
 * 日 期: 2011-9-12
 * 描 叙:
 */
package com.easycode.configmgr.configeditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 功能描叙:
 * 编   码:  
 * 完成时间: 2011-9-12 上午08:30:59
 */
public class TitleInput
{

	private Label titleLabel = null;
	private Text input = null;
	//Composite comp = null;
    public TitleInput(Composite parent,String title)
    { 
    	titleLabel = new Label(parent, SWT.NO);
    	
    	titleLabel.setText(title);
    	input = new Text(parent,SWT.SINGLE | SWT.BORDER);
    	
		GridData inputData = new GridData(GridData.FILL_HORIZONTAL);
	 
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
    public void setTextLimit(int size)
    {
    	this.input.setTextLimit(size);
    }
    public TitleInput apendInput(Composite parent,String title)
    {
    	return new TitleInput(parent,title);
    }
}

