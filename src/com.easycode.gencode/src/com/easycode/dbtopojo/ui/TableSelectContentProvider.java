/**
 * 作 者: tommy
 * 日 期: 2007-2-10
 * 描 叙:
 */
package com.easycode.dbtopojo.ui;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * 功能描叙:
 * 编   码:  
 * 完成时间: 2007-2-10 下午03:18:41
 */
public class TableSelectContentProvider implements ITreeContentProvider
{
	private TableCheckBoxObj root = null;
	public TableSelectContentProvider()
	{
		
	}

	public Object[] getChildren(Object arg0)
	{
		if(arg0 != null)
		{
			TableCheckBoxObj obj = (TableCheckBoxObj)arg0;
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
			TableCheckBoxObj obj = (TableCheckBoxObj)arg0;
			if(obj.childList != null && obj.childList.length > 0)
			{
				return true;
			}
			
		}
		return false;
	}

	public Object[] getElements(Object arg0)
	{
		return new TableCheckBoxObj[]{root};
	}

	public void dispose()
	{
		// TODO Auto-generated method stub
		
	}

	public void inputChanged(Viewer arg0, Object arg1, Object arg2)
	{
	}

	public TableCheckBoxObj getRoot()
	{
		return root;
	}

	public void setRoot(TableCheckBoxObj root)
	{
		this.root = root;
	}


}

