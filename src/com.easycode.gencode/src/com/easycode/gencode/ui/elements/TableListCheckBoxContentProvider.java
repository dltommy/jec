/**
 * 作 者: tommy
 * 日 期: 2007-2-10
 * 描 叙:
 */
package com.easycode.gencode.ui.elements;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * 功能描叙:
 * 编   码:  
 * 完成时间: 2007-2-10 下午03:18:41
 */
public class TableListCheckBoxContentProvider implements ITreeContentProvider
{
	private TableListCheckBox root = null;
	public TableListCheckBoxContentProvider()
	{
		
	}

	public Object[] getChildren(Object arg0)
	{
		if(arg0 != null)
		{
			TableListCheckBox obj = (TableListCheckBox)arg0;
			if(obj.childList != null && obj.childList.length > 0)
			{
				return obj.childList;
			}
		}
        return null;
	}

	public Object getParent(Object arg0)
	{
		return null;
	}

	public boolean hasChildren(Object arg0)
	{
		if(arg0 != null)
		{
			TableListCheckBox obj = (TableListCheckBox)arg0;
			if(obj.childList != null && obj.childList.length > 0)
			{
				return true;
			}
			
		}
		return false;
	}

	public Object[] getElements(Object arg0)
	{
		return new TableListCheckBox[]{root};
	}

	public void dispose()
	{
		// TODO Auto-generated method stub
		
	}

	public void inputChanged(Viewer arg0, Object arg1, Object arg2)
	{
	}

	public TableListCheckBox getRoot()
	{
		return root;
	}

	public void setRoot(TableListCheckBox root)
	{
		this.root = root;
	}


}

