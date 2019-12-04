package com.easycode.gencode.core.ftlmethod;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;

import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateModelIterator;

public class NewObject implements TemplateMethodModelEx
{

    
    public Object exec(List args) throws TemplateModelException
    {
        HashMap hm = new HashMap();
        // TODO Auto-generated method stub
        if(args.size() == 0){
            
            return new SimpleHash();
        }
        else {
            SimpleHash retMap = new SimpleHash();
          
            SimpleScalar values = (SimpleScalar) args.get(0); 
            JSONObject o=   JSONObject.fromObject(values.getAsString());
            Iterator r = o.keys();
            while(r.hasNext()){
                Object k = r.next();
                retMap.put(k.toString(), o.get(k));
            }
            return retMap;
        }
          
    }

}
