/**
 * 作 者: 欧阳超
 * 日 期: 2011-11-23
 * 描 叙:
 */
package com.easycode.gencode.ui;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import com.easycode.gencode.ui.elements.SelectCheckBox;
 

//import client.core.ui.common.FtlModel.TreeSelBoxNode;

/**
 * 功能描叙: 编 码: 欧阳超 完成时间: 2011-11-23 下午07:38:14
 */
public class CheckBoxTreeLabelProvider implements ILabelProvider
{
	// The listeners
	private java.util.List listeners;

	// Images for tree nodes
	// private Image file;

	// private Image dir;

	/**
	 * Constructs a FileTreeLabelProvider
	 */
	public CheckBoxTreeLabelProvider()
		{
			// Create the list to hold the listeners
			listeners = new ArrayList();

			// Create the images
			/*
			 * try { file = new Image(null, new
			 * FileInputStream("images/file.gif")); dir = new Image(null, new
			 * FileInputStream( "images/directory.gif")); } catch
			 * (FileNotFoundException e) { }
			 */
		}

	/**
	 * /* public void setPreserveCase(boolean preserveCase) { // Since this
	 * attribute affects how the labels are computed, // notify all the
	 * listeners of the change. LabelProviderChangedEvent event = new
	 * LabelProviderChangedEvent(this); for (int i = 0, n = listeners.size(); i <
	 * n; i++) { ILabelProviderListener ilpl = (ILabelProviderListener)
	 * listeners .get(i); ilpl.labelProviderChanged(event); } }
	 */
	/**
	 * Gets the image to display for a node in the tree
	 * 
	 * @param arg0
	 *            the node
	 * @return Image
	 */
	public Image getImage(Object arg0)
	{
		// If the node represents a directory, return the directory image.
		// Otherwise, return the file image.
		return null;// ((CheckBox) arg0).isDirectory() ? dir : file;
	}

	/**
	 * Gets the text to display for a node in the tree
	 * 
	 * @param arg0
	 *            the node
	 * @return String
	 */
	public String getText(Object arg0)
	{
		// Get the name of the file

		if (arg0 != null)
		{
			SelectCheckBox b = (SelectCheckBox)arg0;
			return b.getValue();
		}
		else
		{
			return "";
		}
	}

	/**
	 * Adds a listener to this label provider
	 * 
	 * @param arg0
	 *            the listener
	 */
	public void addListener(ILabelProviderListener arg0)
	{
		listeners.add(arg0);
	}

	/**
	 * Called when this LabelProvider is being disposed
	 */
	public void dispose()
	{
		// Dispose the images
		/*
		 * if (dir != null) dir.dispose(); if (file != null) file.dispose();
		 */
	}

	/**
	 * Returns whether changes to the specified property on the specified
	 * element would affect the label for the element
	 * 
	 * @param arg0
	 *            the element
	 * @param arg1
	 *            the property
	 * @return boolean
	 */
	public boolean isLabelProperty(Object arg0, String arg1)
	{
		return false;
	}

	/**
	 * Removes the listener
	 * 
	 * @param arg0
	 *            the listener to remove
	 */
	public void removeListener(ILabelProviderListener arg0)
	{
		listeners.remove(arg0);
	}

}
