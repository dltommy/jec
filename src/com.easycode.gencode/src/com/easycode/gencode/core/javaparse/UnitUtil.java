package com.easycode.gencode.core.javaparse;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.Signature;

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
        if(type.indexOf("[")>-1)
        {
            isArray = true;
            parseType = parseType.replace("[]", "");
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
    private static String getByTypeSig(String typeSig)
    {
         String ret = Signature.toString(typeSig);
         if("long".equals(ret))
         {
             
             return "Long";
         }
         else if("int".equals(ret))
         {
             return "Integer";
         }
         else if("short".equals(ret))
         {
             return "Short";
         }
         else if("byte".equals(ret))
         {
             return "Byte";
         }
         else if("boolean".equals(ret))
         {
             return "Boolean";
         }
         else if("float".equals(ret))
         {
             return "Float";
         }
         else if("double".equals(ret))
         {
             return "Double";
         }
         else
         {
             return ret;
         }
     /*

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
            String ret = typeSig.substring(1,typeSig.length()-1);
           
            List<String> srcList = new ArrayList<String>();
            if(ret.indexOf("<")>-1)
            {
                String gen= ret.substring(ret.indexOf("<")+1,ret.lastIndexOf(">"));
                gen = gen.replace("<", ";");
                gen = gen.replace(">", ";");
                String[] gens = gen.split(";");
                for(String j:gens)
                {
                    if(!"".equals(j))
                    {
                        srcList.add(j);
                    }
                }
            }
            for(String temp:srcList)
            {
                ret = ret.replaceFirst(temp, temp.substring(1));
            }
            ret = ret.replace(";", ",");
            ret = ret.replace(",>", ">");
            return ret; 
        }
       */
    }
}
