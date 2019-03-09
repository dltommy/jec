/**
 * 作 者: tommy
 * 日 期: 2007-2-10
 * 描 叙:
 */
package com.easycode.gencode.ui.elements;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

/**
 * 功能描叙:
 * 编   码:  
 * 完成时间: 2007-2-10 下午03:19:31
 */
public class TableListCheckBoxLabelProvider implements ILabelProvider
{

	public Image getImage(Object arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getText(Object arg0)
	{
		if(arg0 != null)
		{
		    TableListCheckBox obj = (TableListCheckBox)arg0;
		    return obj.getTableName();
		}
		else
		{
		    return "";
		}
	}

	public void addListener(ILabelProviderListener arg0)
	{
		// TODO Auto-generated method stub
		
	}

	public void dispose()
	{
		// TODO Auto-generated method stub
		
	}

	public boolean isLabelProperty(Object arg0, String arg1)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public void removeListener(ILabelProviderListener arg0)
	{
		// TODO Auto-generated method stub
		
	}

}

