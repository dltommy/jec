/**
 * 作 者:  
 * 日 期: 2007-2-10
 * 描 叙:
 */
package com.easycode.gencode.ui.elements;

/**
 * 功能描叙:
 * 编   码:  
 * 完成时间: 2007-2-10 下午03:20:53
 */
public class TableListCheckBox
{
	public String tableName;
	public TableListCheckBox[] childList = null;
	
    public TableListCheckBox[] getChildList()
	{
		return childList;
	}
	public void setChildList(TableListCheckBox[] childList)
	{
		this.childList = childList;
	}
	public TableListCheckBox(String tableName)
    {
    	this.tableName = tableName;
    }
	public String getTableName()
	{
		return tableName;
	}
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}
}

