package com.easycode.gencode.core.ftlmethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.easycode.common.StringUtil;

import net.sf.json.JSONObject;

import freemarker.template.SimpleScalar;
import freemarker.template.SimpleSequence;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateModelIterator;
import freemarker.template.WrappingTemplateModel;

//$(valueOfKey(,{'a':'b'})
public class NewInstance implements TemplateMethodModelEx
{

    public HashMap root = null;

    public NewInstance(HashMap root)
    {
        this.root = root;
    }

    public Object exec(List args) throws TemplateModelException
    {
        SimpleScalar targetScalar = (SimpleScalar) args.get(0);
        List<String> excludeList = new ArrayList<String>();
        
        if (args.size() == 0)
        {
            throw new TemplateModelException("缺少参数！");
        }
        JSONObject setJsonObj = null;
        if(args.size()>1){
            Object setValue = args.get(1);

            if(setValue instanceof SimpleHash){
                setJsonObj = new JSONObject();
                SimpleHash hashValues = (SimpleHash) args.get(1);
                 TemplateModelIterator  it= hashValues.keys().iterator();
                 while(it.hasNext()){
                     TemplateModel m = it.next();
                     setJsonObj.put(m.toString(), hashValues.get(m.toString()));
                 }
                 
            }
            else{
                SimpleScalar values = (SimpleScalar) args.get(1); 
                setJsonObj = JSONObject.fromObject(values.getAsString());
            }
        }

        if(setJsonObj == null){
            setJsonObj = new JSONObject();
        }
        
        if(args.size()>=3){
            SimpleScalar exclude = (SimpleScalar) args.get(2);
            String[] excArray = exclude.getAsString().split(",");
            for(String t:excArray){
                excludeList.add(t);
            }
            
        }
        Map sys = (Map) root.get("sys");
        Map ref = (Map) sys.get("zzzReferObj");
        List<Map> propList = null;// (List<Map>) sys.get("propList");
        if (targetScalar == null || "".equals(targetScalar))
        {
 
                propList = (List<Map>) sys.get("propList");
        }
        else
        {

            String target = targetScalar.getAsString();
            Map targetMap = (Map) ref.get(target);
            propList = (List<Map>) targetMap.get("propList");
        }
        SimpleHash map = new SimpleHash ();
        for (Map propMap : propList)
        {
            String propName = (String) propMap.get("propName");
             if(excludeList.contains(propName)){
                 continue;
             }
            Map propType = (Map) propMap.get("propType");
            
            String pkgName = propType.get("pkgName").toString();
            String typeName = propType.get("typeName").toString();
            String referType = pkgName + "." + typeName;
            Map clsMap = (Map) ref.get(referType);
            //String pName = String.valueOf(propName);
            if (clsMap != null && clsMap.size() > 0)
            {
                SimpleHash subHashMap = new SimpleHash();
                map.put(propName, subHashMap);
                List<Map> list = (List<Map>) clsMap.get("propList");
                for (Map subProp : list)
                {
                    String subPropName = (String) subProp.get("propName");
                    if(excludeList.contains(subPropName)){
                        continue;
                    }
                    Object o = setJsonObj.get(propName);
                    
                    if(o instanceof Map){
                        Map subMapValue = (Map)o;
                        subHashMap.put(subPropName,
                                 subMapValue.get(subPropName));
                    }
                    else{
                        subHashMap.put(subPropName,
                                setJsonObj.get(propName + "." + subPropName));
                    }
                }
            }
            else
            {
                if(setJsonObj.get(propName) != null){
                    map.put(propName, setJsonObj.get(propName));
                }
                else{
   
                    if(referType.equals("java.lang.Integer")){
                        map.put(propName, 0);
                    }
                    else if(referType.equals("java.lang.Byte")){ 
                        map.put(propName, 0);
                    }
                    else if(referType.equals("java.lang.Short")){ 
                        map.put(propName, 0);
                    }
                    else if(referType.equals("java.lang.Long")){ 
                        map.put(propName, 0);
                    }
                    else if(referType.equals("java.lang.Double")){ 
                        map.put(propName, 0.0);
                    }
                    else if(referType.equals("java.lang.Float")){ 
                        map.put(propName, 0.0);
                    }
                    else if(referType.equals("java.lang.Boolean")){ 
                        map.put(propName, 0);
                    }
                    else  { 
                        map.put(propName, null);
                    }
                }

                
            }
        }
        return map;
    }

}
