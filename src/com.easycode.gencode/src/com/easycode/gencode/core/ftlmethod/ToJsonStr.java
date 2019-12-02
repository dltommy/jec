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
import freemarker.template.SimpleNumber;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateModelIterator;
import freemarker.template.WrappingTemplateModel;

//$(valueOfKey(,{'a':'b'})
public class ToJsonStr implements TemplateMethodModelEx
{

    

    public ToJsonStr( )
    {
     
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
 

        if (args.size() < 1)
        {
            throw new TemplateModelException("参数个数异常！");
        }
        else
        {
            SimpleHash src = (SimpleHash) args.get(0);
            HashMap srcJson = new HashMap();
            TemplateModelIterator  it= src.keys().iterator();
            while(it.hasNext()){
                TemplateModel m = it.next();
                if(src.get(m.toString()) != null)
                {
                    Object o = src.get(m.toString());
                    if(o instanceof SimpleHash){
                        HashMap sub = new HashMap();
                        SimpleHash subHash = (SimpleHash)o;
                        TemplateModelIterator  subit= subHash.keys().iterator();
                        while(subit.hasNext()){
                            TemplateModel subm = subit.next();
                            Object v = subHash.get(subm.toString());
                            if(v == null){
                                sub.put(subm.toString(), null);  
                            }
                            else if(v instanceof SimpleHash){
                                
                            }
                            else if(v instanceof SimpleScalar)
                            {
                                SimpleScalar sc = (SimpleScalar)v;
                                sub.put(subm.toString(), sc.getAsString());    
                            }
                            else if(v instanceof SimpleNumber)
                            {
                                SimpleNumber sc = (SimpleNumber)v;
                                sub.put(subm.toString(), sc.getAsNumber());    
                            }
                        }
                        srcJson.put(m.toString(), sub);
                    }
                    else if(o instanceof SimpleScalar){
                        SimpleScalar sc = (SimpleScalar)o;
                        srcJson.put(m.toString(), sc.getAsString());
                    }
                    else if(o instanceof SimpleNumber){
                        SimpleNumber sc = (SimpleNumber)o;
                        srcJson.put(m.toString(), sc.getAsNumber());
                    }
                }
                else{
                    srcJson.put(m.toString(), null);
                }
                
                
            }
            JSONObject jsonObj = JSONObject.fromObject(srcJson);
            if(args.size()==2){
                SimpleScalar formatScalar = (SimpleScalar) args.get(1);
                String format = formatScalar.getAsString();
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
                
                return StringUtil.formatOutput(jsonObj.toString(),(Integer)formatConfig.get("left"), (Integer)formatConfig.get("pad") ,(String)formatConfig.get("format"));
            }
            else{
                return StringUtil.formatOutput(jsonObj.toString());
            }
             
             
        }
         

    }

}
