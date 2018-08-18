/**
 * 作 者:  
 * 日 期: 2011-11-17
 * 描 叙:
 */
package com.easycode.gencode.ui.elements;
 
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
 

/**
 * 功能描叙:
 * 编   码:  
 * 完成时间: 2011-11-17 下午08:54:34
 */
public class CheckBox
{
	private Button button = null;

	private String memo = null;
	private String value = null;

	public String toString()
	{
		return this.memo;
	}
	public boolean getChecked()
	{
		return this.button.getSelection();
	}
	public Button genCheckBox(Composite t)
	{
		this.button = new Button(t, SWT.CHECK | SWT.FLAT);
		this.button.setText(this.memo);
		return this.button;
	}
	public CheckBox(String memo, String value)
	{
		this.memo = memo;
		this.value = value;
	}
	public String getMemo()
	{
		return memo;
	}
	public void setMemo(String memo)
	{
		this.memo = memo;
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}

}
