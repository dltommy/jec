/**
 * 作 者:  
 * 日 期: 2007-2-10
 * 描 叙:
 */
package com.easycode.dbtopojo.ui;

/**
 * 功能描叙:
 * 编   码:  
 * 完成时间: 2007-2-10 下午03:20:53
 */
public class TableCheckBoxObj
{
	public String tableName;
	public TableCheckBoxObj[] childList = null;
	
    public TableCheckBoxObj[] getChildList()
	{
		return childList;
	}
	public void setChildList(TableCheckBoxObj[] childList)
	{
		this.childList = childList;
	}
	public TableCheckBoxObj(String tableName)
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

