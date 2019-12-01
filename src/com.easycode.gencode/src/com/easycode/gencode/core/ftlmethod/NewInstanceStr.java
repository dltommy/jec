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
public class NewInstanceStr implements TemplateMethodModelEx
{

    public HashMap root = null;

    public NewInstanceStr(HashMap root)
    {
        this.root = root;
    }


    public Object exec(List args) throws TemplateModelException
    {
        HashMap<String,Object> formatConfig = new HashMap<String,Object>();
        //距离左边
        formatConfig.put("left", 0);
        //行缩进
        formatConfig.put("pad", 4);
        //
        formatConfig.put("format","\n");
        Map sys = (Map) root.get("sys");
        Map ref = (Map) sys.get("zzzReferObj");

        if (args.size() < 1)
        {
            throw new TemplateModelException("参数个数异常！");
        }
        else
        {
            String format = null;
            List<String> excludeList = new ArrayList<String>();
            if(args.size()>=2){
                SimpleScalar exclude = (SimpleScalar) args.get(1);
                String[] excArray = exclude.getAsString().split(",");
                for(String t:excArray){
                    excludeList.add(t);
                }
                
            }
            if(args.size()==3){
                SimpleScalar formatScalar = (SimpleScalar) args.get(2);
                format = formatScalar.getAsString();
                JSONObject paramFormat = JSONObject.fromObject(format);
                if(paramFormat.get("left") != null){
                    formatConfig.put("left", paramFormat.get("left"));
                }
                if(paramFormat.get("pad") != null){
                    formatConfig.put("pad", paramFormat.get("pad"));
                }
                if(paramFormat.get("format") != null){
                    formatConfig.put("format", paramFormat.get("format"));
                }
            }
            HashMap<String, Object> map = new HashMap<String, Object>();
            Object t = args.get(0);
            JSONObject obj = null;
            if(t instanceof SimpleHash){
                obj = new JSONObject();
                SimpleHash hashValues = (SimpleHash) args.get(0);
                 TemplateModelIterator  it= hashValues.keys().iterator();
                 while(it.hasNext()){
                     TemplateModel m = it.next();
 
                     obj.put(m.toString(), hashValues.get(m.toString()));
                 }
                 
            }
            else{
                SimpleScalar values = (SimpleScalar) args.get(0); 
                  obj = JSONObject.fromObject(values.getAsString());
            }


            List<Map> propList = (List<Map>) sys.get("propList");

            for (Map propMap : propList)
            {
                String propName = (String) propMap.get("propName");
                 if(excludeList.contains(propName)){
                     continue;
                 }
                Map propType = (Map) propMap.get("propType");
                
                String pkgName = propType.get("pkgName").toString();
                String typeName = propType.get("typeName").toString();
                Map clsMap = (Map) ref.get(pkgName + "." + typeName);
                String pName = String.valueOf(propName);
                if (clsMap != null && clsMap.size() > 0)
                {
                    HashMap<String, Object> subHashMap = new HashMap<String, Object>();
                    map.put(pName, subHashMap);
                    List<Map> list = (List<Map>) clsMap.get("propList");
                    for (Map subProp : list)
                    {
                       
                        String subPropName = (String) subProp.get("propName");
                        if(excludeList.contains(subPropName)){
                            continue;
                        }
                        Object o = obj.get(pName);
                        
                        if(o instanceof Map){
                            Map subMapValue = (Map)o;
                            subHashMap.put(subPropName,
                                     subMapValue.get(subPropName));
                        }
                        else{
                            subHashMap.put(subPropName,
                                    obj.get(pName + "." + subPropName));
                        }

                    }
                }
                else
                {
                    map.put(pName, obj.get(pName));
                }
            }
            String formatOutput = null;
            String ret = JSONObject.fromObject(map).toString();
            if(!"".equals(format)){
                
                 formatOutput = StringUtil.formatOutput(ret,(Integer)formatConfig.get("left"), (Integer)formatConfig.get("pad") ,(String)formatConfig.get("format"));
            }
            else{
                formatOutput = ret;
            }

             
            return formatOutput;
        }

    }

}
