package com.easycode.gencode.core.ftlmethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.easycode.common.StringUtil;

import net.sf.json.JSONObject;

import freemarker.core.Environment;
import freemarker.template.SimpleNumber;
import freemarker.template.SimpleScalar;
import freemarker.template.SimpleSequence;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateModelIterator;
import freemarker.template.WrappingTemplateModel;

 
public class SetValue implements TemplateMethodModelEx
{

  
    public Object exec(List args) throws TemplateModelException
    {
        if (args.size() != 3)
        {
            throw new TemplateModelException("参数个数异常！");
        }
        SimpleHash srcObj = (SimpleHash) args.get(0);
        SimpleScalar pro = (SimpleScalar) args.get(1);
        
        Object vo = args.get(2);
        if(vo instanceof SimpleHash){
            SimpleHash proValue = (SimpleHash) args.get(2);
            srcObj.put(pro.getAsString(), proValue);
        }
        else if(vo instanceof SimpleScalar){
            SimpleScalar proValue = (SimpleScalar) args.get(2);
            srcObj.put(pro.getAsString(), proValue.getAsString());
        }
        else if(vo instanceof SimpleNumber){
            SimpleNumber proValue = (SimpleNumber) args.get(2);
            srcObj.put(pro.getAsString(), proValue.getAsNumber());
        }
        return srcObj;
    }

}
