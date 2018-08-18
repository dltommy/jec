/**
 * 作 者: 欧阳超
 * 日 期: 2011-11-23
 * 描 叙:
 */
package com.easycode.gencode.ui;

import java.util.HashMap;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.easycode.gencode.ui.elements.SelectCheckBox;
 
/**
 * 功能描叙:
 * 编   码: 欧阳超
 * 完成时间: 2011-11-23 下午07:39:52
 */
public class CheckBoxTreeContentProvider implements ITreeContentProvider
{
	private HashMap<String, SelectCheckBox> checkedBoxMap = null;
	public void setCheckedBoxMap(HashMap<String, SelectCheckBox> checkedBoxMap)
	{
		this.checkedBoxMap = checkedBoxMap;
	}

	/**
	 * Gets the children of the specified object
	 * 
	 * @param arg0
	 *            the parent object
	 * @return Object[]
	 */
	public Object[] getChildren(Object arg0)
	{
		if(arg0 != null)
		{
		    return ((SelectCheckBox)arg0).getChildList().toArray();
		}
		else
		{
			return null;
		}
	}

	/**
	 * Gets the parent of the specified object
	 * 
	 * @param arg0
	 *            the object
	 * @return Object
	 */
	public Object getParent(Object arg0)
	{
		if(arg0 != null)
		{
		    return ((SelectCheckBox)arg0).getParent();// ((File) arg0).getParentFile();
		}
		else
		{
			return null;
		}
	}

	/**
	 * Returns whether the passed object has children
	 * 
	 * @param arg0
	 *            the parent object
	 * @return boolean
	 */
	public boolean hasChildren(Object arg0)
	{

		if(arg0 == null)
		{
			return false;
		}
		if(((SelectCheckBox)arg0).getChildList().size() > 0)
		{
			return true;
		}
		else
		{
		    return false;
		}
	}

	/**
	 * Gets the root element(s) of the tree
	 * 
	 * @param arg0
	 *            the input data
	 * @return Object[]
	 */
	public Object[] getElements(Object arg0)
	{

		if(checkedBoxMap != null)
		{
			return checkedBoxMap.get("root").getChildList().toArray();	
		}
		else
		{
			return new SelectCheckBox[]{new SelectCheckBox("null","   ")};
		}
		
	}

	/**
	 * Disposes any created resources
	 */
	public void dispose()
	{
		// Nothing to dispose
	}

	/**
	 * Called when the input changes
	 * 
	 * @param arg0
	 *            the viewer
	 * @param arg1
	 *            the old input
	 * @param arg2
	 *            the new input
	 */
	public void inputChanged(Viewer arg0, Object arg1, Object arg2)
	{
		// Nothing to change
	}
}

