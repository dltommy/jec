/**
 * 作 者: dltommy
 * 日 期: 2007-2-10
 * 描 叙:
 */
package com.easycode.dbtopojo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * 功能描叙:
 * 编   码: dltommy
 * 完成时间: 2007-2-10 下午01:40:16
 */
public class ConnectionBase
{
    protected static Connection getCon(String driver, String url, String user, String pwd) throws Exception
    {
    	Connection ret = null;
    	try
		{
			Class.forName(driver).newInstance();
			
			Properties props = new Properties();
			props.setProperty("user",user);props.setProperty("password",pwd); 
			//针对mysql设置
			props.setProperty("useInformationSchema", "true");
			//this.pConnection = DriverManager.getConnection(this.url, props); 
			
			ret = DriverManager.getConnection(url, props);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return ret;
    }
}

