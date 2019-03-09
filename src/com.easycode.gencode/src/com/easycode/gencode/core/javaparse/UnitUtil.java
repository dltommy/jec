package com.easycode.gencode.core.javaparse;

import java.util.List;

import com.easycode.gencode.core.javaparse.model.java.JavaTypeModel;

 
 

public class UnitUtil
{
    public static JavaTypeModel parseTypeDigest(String type,List<String> pkg)
    { 
        boolean isArray = false;
        if("V".equals(type))
        {
            return null;
        }
        String parseType = getByTypeSig(type);
        if(type.startsWith("["))
        {
            isArray = true;
        }
        else
        {
            isArray = false;
        }
        if(pkg != null && pkg.size()>0)
        {
            for(String t:pkg)
            {
                if(t.endsWith("."+parseType))
                {
                    parseType = t;
                    break;
                   
                }
            }
        }
        return JavaTypeModel.createJavaType(parseType,isArray);
    }
    public static String getByTypeSig(String typeSig)
    {
        if(typeSig == null || "".equals(typeSig))
        {
            return null;
        }
        if(typeSig.startsWith("["))
        {
              return typeSig.substring(2,typeSig.length()-1);                                
        }
        else if(typeSig.length() == 1)
        {
            if("J".equals(typeSig))
            {
                return "Long";
            }
            if("Z".equals(typeSig))
            {
                return "Boolean";
            }
            else if("I".equals(typeSig))
            {
                return "Integer";
            }
            else if("F".equals(typeSig))
            {
                return "Float";
            }
            else if("D".equals(typeSig))
            {
                return "Double";
            }
            else if("B".equals(typeSig))
            {
                return "Byte";
            }
            else if("C".equals(typeSig))
            {
                return "Character";
            }
            else
            {
                return typeSig;
            }
        }
        else
        {
            return typeSig.substring(1,typeSig.length()-1);
        }
       
    }
}
