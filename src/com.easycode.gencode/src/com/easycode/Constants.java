/**
 * 作 者: dltommy
 * 日 期: 2013-3-12
 * 描 叙:
 */
package com.easycode;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能描叙: 编 码: dltommy 完成时间: 2013-3-12 下午11:11:36
 */
public class Constants
{
    public final static String appendRegx = "<ftl_append[\\s]*?([^>]*?)>([\\s|\\S]*?)</ftl_append>";
    public final static String appendCode = "@EaseCodeForAppendGenCode";
    public final static String MDL_PATH_ECLIPSE = "$ECLIPSE_INSTALL_PATH";
    public final static String MDL_PATH_PROJECT = "$PROJECT_PATH";

    
    public final static String TEMPLATE_JAVA = "JAVA";
    public final static String TEMPLATE_JSON = "JSON";
    public final static String TEMPLATE_DB = "DB";

    
    public final static String[] TEMPLATE_TYPE = new String[]
    { TEMPLATE_JAVA, TEMPLATE_JSON, TEMPLATE_DB };
    public static int getTemplateIndex(String typeName)
    {
        for (int i = 0; i < TEMPLATE_TYPE.length; i++)
        {
            if (typeName.equalsIgnoreCase(TEMPLATE_TYPE[i]))
            {
                return i;
            }
        }
        return 0;
    }
     
    /*
    public static List<String> getTemplateTypeList()
    {
        List retList = new ArrayList<String>();
        for (String t : TEMPLATE_TYPE)
        {
            retList.add(t);
        }
        return retList;
    }
    */


}
