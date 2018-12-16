/**
 * 作 者: dltommy
 * 日 期: 2007-2-10
 * 描 叙:
 */
package com.easycode.dbtopojo;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.easycode.common.EclipseConsoleUtil;
import com.easycode.common.EclipseUtil;
import com.easycode.javaparse.model.java.JavaClzModel;
import com.easycode.javaparse.model.java.PropModel;

/**
 * 功能描叙: 编 码: dltommy 完成时间: 2007-2-10 下午01:49:10
 */
public class DbMgr extends ConnectionBase
{
 
    public static boolean testCon(String driver, String url, String pwd,
            String user)
    {
        Connection connection = null;
        try
        {
            connection = getCon(driver, url, user, pwd);
            if (connection != null)
            {
                return true;
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        finally
        {
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return false;

    }

    public static List<String> queryMysqlTabList(String driver, String url,
            String pwd, String user, String tableName) throws Exception
    {
        List<String> retList = new ArrayList<String>();
        Connection connection = null;
        try
        {
            connection = getCon(driver, url, user, pwd);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        }

        DatabaseMetaData meta = null;
        ResultSet rsTable = null;
        try
        {
            meta = connection.getMetaData();

            rsTable = meta.getTables(null, user.toUpperCase(),
                    tableName.toUpperCase(), new String[]
                    { "TABLE", "VIEW" });
            // 获取到的数据是以ResultSet形式返回
            while (rsTable.next())
            {
                // System.out.println(rsTable.getString(1)); // 第一列是Database名称
                // System.out.println(rsTable.getString(3)); //
                // 第二列是用户名称(有的表可能没有对应的用户)

                String targetName = rsTable.getString(3).toUpperCase();
                if (tableName != null && !"".equals(tableName.trim()))
                {
                    // if(targetName.startsWith(tableName.toUpperCase()))
                    {
                        retList.add(rsTable.getString(3));
                    }
                }
                else
                {
                    retList.add(rsTable.getString(3));
                }
                // System.out.println(rsTable.getString(3)); // 第三列就是表名称

            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        } // 获取数据库连接的元数据
          // 查询连接的所有Table(如果需要查询视图等,可以在最后的数组中添加VIEW...,依此类推)
        finally
        {

            if (rsTable != null)
            {
                try
                {
                    rsTable.close();
                }
                catch (SQLException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return retList;

    }

    public static TableModel queryTableInfo(String driver, String url,
            String pwd, String user, String tableName) throws Exception
    {
        TableModel retModel = null;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSetMetaData rsm = null;
        try
        {
            connection = getCon(driver, url, user, pwd);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        }
        String sql = "select * from " + tableName + " where 1 = 0";
        ps = connection.prepareStatement(sql);
        rs = ps.executeQuery();

        retModel = new TableModel();
        retModel.setTable(tableName);
        retModel.setAlias(toTableAliasName(tableName));

        rsm = rs.getMetaData();
        int num = rsm.getColumnCount();

        List<ColumnModel> modelList = new ArrayList<ColumnModel>();

        for (int i = 1; i <= num; i++)
        {
            ColumnModel newModel = new ColumnModel();

            String colName = rsm.getColumnName(i);
            newModel.setColumn(colName);

            newModel.setAlias(toPropAliasName(colName));
            newModel.setRemark(rsm.getColumnLabel(i));
            newModel.setIsNullable(rsm.isNullable(i) + "");
            newModel.setSize(rsm.getPrecision(i));
            newModel.setTypeName(rsm.getColumnTypeName(i));

            newModel.setType(rsm.getColumnType(i));

            // msg=str+rsm.getColumnName(i)+" ";

            modelList.add(newModel);
        }
        retModel.setColList(modelList);
        return retModel;
    }

    public static TableModel getTableInfo(String driver, String url,
            String user, String pwd, String table)
    {

        TableModel ret = new TableModel();
        ret.setTable(table);
        ret.setAlias(toTableAliasName(table));
        Connection conn = null;
        DatabaseMetaData dbmd = null;
        ResultSet resultSet = null;
        List<ColumnModel> modelList = new ArrayList<ColumnModel>();

        try
        {

            try
            {
                conn = getCon(driver, url, user, pwd);
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
                throw e;
            }

            dbmd = conn.getMetaData();
            resultSet = dbmd.getTables(null, "%", table, null);
 

            while (resultSet.next())
            {
                String tableName = resultSet.getString("TABLE_NAME");
                String tableRemarks = resultSet.getString("REMARKS");
                String tableType = resultSet.getString("TABLE_TYPE");
                ret.setTableType(tableType);
                if(tableRemarks != null)
                {
                    tableRemarks = tableRemarks.trim();
                    if(tableRemarks.startsWith("{") && tableRemarks.endsWith("}"))
                    {
                        try
                        {
                            ret.setAn(JSONObject.fromObject(tableRemarks));
                        }
                        catch(Exception e)
                        {
                            //e.printStackTrace();
                            EclipseConsoleUtil.printExcept(e, "警告：表注释解析json出现异常");
                        }
                    }
                }
                if (tableName.equalsIgnoreCase(table))
                {

                    ResultSet rs = dbmd.getColumns(null, dbmd.getUserName(),
                            tableName.toUpperCase(), "%");

                    while (rs.next())
                    {
                        ColumnModel c = new ColumnModel();
                        String colName = rs.getString("COLUMN_NAME");
                        c.setColumn(colName);
                        c.setAlias(toPropAliasName(colName));
                        String remarks = rs.getString("REMARKS");
                        if(remarks != null)
                        {
                            remarks = remarks.trim();
                            if(remarks.startsWith("{") && remarks.endsWith("}"))
                            {
                                try
                                {
                                    c.setAn(JSONObject.fromObject(remarks));
                                }
                                catch(Exception e)
                                {
                                    //e.printStackTrace();
                                    EclipseConsoleUtil.printExcept(e, "警告：【"+colName+"】列注释解析json出现异常");
                                }
                            }
                        }
                        
                        
                        c.setRemark(remarks);
                        
                        c.setType(rs.getInt("DATA_TYPE"));
                        String dbType = rs.getString("TYPE_NAME");
                        c.setTypeName(dbType);
                        int colSize = rs.getInt("COLUMN_SIZE");
                        c.setSize(colSize);
                        c.setIsNullable(rs.getString("IS_NULLABLE"));
                        
                        if (driver.toUpperCase().indexOf("MYSQL") > -1)
                        {
                            c.setIsAutoInc(rs.getString("IS_AUTOINCREMENT"));
                        }
                        else if (driver.toUpperCase().indexOf("ORACLE") > -1)
                        {
                            c.setIsAutoInc("NO");
                        }
                        modelList.add(c);
                    }
                    try
                    {
                        rs.close();
                    }
                    finally
                    {

                    }
                }
            }
            ret.setColList(modelList);
            try
            {
                resultSet.close();
            }
            finally
            {

            }
            resultSet = conn.getMetaData().getPrimaryKeys(null, dbmd.getUserName(), table);

            List<String> pkList = new ArrayList<String>();
            while (resultSet.next())
            {
                pkList.add(resultSet.getString("COLUMN_NAME"));
            }
            for (ColumnModel m : modelList)
            {
                for (String k : pkList)
                {
                    if (m.getColumn().equalsIgnoreCase(k))
                    {
                        m.setIsPrimaryKey("YES");
                    }
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (resultSet != null)
            {
                try
                {
                    resultSet.close();
                }
                catch (SQLException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (conn != null)
            {
                try
                {
                    conn.close();
                }
                catch (SQLException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }

        return ret;
    }

    public static JavaClzModel queryJavaClzInfo(String driver, String url,
            String pwd, String user, String tableName) throws Exception
    {
        JavaClzModel retModel = null;
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSetMetaData rsm = null;
        try
        {
            connection = getCon(driver, url, user, pwd);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        }
        String sql = "select * from " + tableName + " where 1 = 0";
        ps = connection.prepareStatement(sql);
        rs = ps.executeQuery();

        retModel = new JavaClzModel();
        retModel.setTabName(tableName);
        String clzName = "";
        String tempName[] = tableName.split("_");
        for (String t : tempName)
        {
            if ("".equals(t))
            {
                continue;
            }
            clzName = clzName + t.substring(0, 1).toUpperCase()
                    + t.substring(1, t.length()).toLowerCase();

        }
        retModel.setClzName(clzName);

        rsm = rs.getMetaData();
        int num = rsm.getColumnCount();
        // ColumnModel newModel = new ColumnModel();

        // List<ColumnModel> modelList = new ArrayList<ColumnModel>();

        for (int i = 1; i <= num; i++)
        {
            String propName = "";
            String tName[] = rsm.getColumnName(i).split("_");
            for (String t : tName)
            {
                if ("".equals(t))
                {
                    continue;
                }
                propName = propName + t.substring(0, 1).toUpperCase()
                        + t.substring(1, t.length()).toLowerCase();
            }
            propName = propName.substring(0, 1).toLowerCase()
                    + propName.substring(1, propName.length());
            String propClzName = "";
            int type = rsm.getColumnType(i);
            // System.err.println("=====================>"+type);
            if (type == Types.INTEGER)
            {
                propClzName = "String";
            }
            switch (type)
            {
            case Types.INTEGER:
                propClzName = "Integer";
                break;
            case Types.BIGINT:
                propClzName = "Long";
                break;
            case oracle.jdbc.OracleTypes.NUMBER:
                propClzName = "Long";
                break;
            case Types.BINARY:
                propClzName = null;
                break;
            case Types.BLOB:
                propClzName = null;
                break;
            case Types.CHAR:
                propClzName = "String";
                break;
            case Types.CLOB:
                propClzName = "String";
                break;
            case Types.DATE:
                propClzName = "java.util.Date";
                break;
            case Types.DECIMAL:
                propClzName = "Long";
                break;
            case Types.DOUBLE:
                propClzName = "Double";
                break;
            case Types.FLOAT:
                propClzName = "Float";
                break;
            case Types.VARCHAR:
                propClzName = "String";
                break;
            case Types.TIMESTAMP:
                propClzName = "java.util.Date";
                break;
            default:
                break;
            }
            if (propClzName != null)
            {
                PropModel p = new PropModel(propClzName, propName);
                retModel.addProp(p);
            }
        }

        return retModel;
    }

    private static String toTableAliasName(String name)
    {
        String ret = "";
        String tempName[] = name.split("_");
        for (String t : tempName)
        {
            if ("".equals(t))
            {
                continue;
            }
            ret = ret + t.substring(0, 1).toUpperCase()
                    + t.substring(1, t.length()).toLowerCase();

        }
        return ret;
    }

    private static String toPropAliasName(String name)
    {
        String ret = "";
        String tempName[] = name.split("_");
        int i = 0;
        for (String t : tempName)
        {
            if ("".equals(t))
            {
                continue;
            }
            if (i == 0)
            {
                ret = ret + t.substring(0, 1).toLowerCase()
                        + t.substring(1, t.length()).toLowerCase();
            }
            else
            {
                ret = ret + t.substring(0, 1).toUpperCase()
                        + t.substring(1, t.length()).toLowerCase();
            }

            i++;
        }
        return ret;
    }
}
