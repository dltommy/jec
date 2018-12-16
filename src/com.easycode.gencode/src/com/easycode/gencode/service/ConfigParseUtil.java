/**
 * 作 者: 欧阳超
 * 日 期: 2012-7-12
 * 描 叙:
 */
package com.easycode.gencode.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.easycode.common.EclipseConsoleUtil;
import com.easycode.javaparse.model.java.JavaMethodModel;
//import com.easycode.javaparse.model.java.JavaTypeModel;
import com.easycode.javaparse.model.java.PropModel;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 功能描叙: 编 码: ouyangchao 
 * 完成时间: 2012-7-12 下午10:44:01
 */
public class ConfigParseUtil
{

    /**
     * 解析java属性上的文档注释
     * @param annoList
     * @param prop
     * @return
     */
    public static HashMap<String, String> parsePropAnno(
            List<DefaultAnno> annoList, PropModel prop)
    {
        HashMap<String, String> annoMap = new HashMap<String, String>();
        if (annoList != null && annoList.size() > 0)
        {
            for (DefaultAnno an : annoList)
            {
                if (DefaultAnno.PROP_ANNO.equals(an.getAnnoType()))
                {
                    if (an.getPropNames() != null)
                    {
                        String props[] = an.getPropNames().split(",");// yy="xx"/a,b,c
                        if (props.length > 0)
                        {
                            for (String a : props)
                            {
                                if (prop.getPropName().matches(a))
                                {
                                    annoMap.put(an.getAnnoName().trim(),
                                            an.getAnno());

                                }
                            }
                        }
                    }
                }
            }
        }
        return annoMap;
    }

    /**
     * 解析方法上的文档属性
     * @param annoList
     * @param method
     * @return
     */
    public static HashMap<String, String> parseMethodAnno(
            List<DefaultAnno> annoList, JavaMethodModel method)
    {
        HashMap<String, String> annoMap = new HashMap<String, String>();
        if (annoList != null && annoList.size() > 0)
        {
            for (DefaultAnno an : annoList)
            {
                if (DefaultAnno.PROP_ANNO.equals(an.getAnnoType()))
                {
                    if (an.getPropNames() != null)
                    {
                        String props[] = an.getPropNames().split(",");
                        if (props.length > 0)
                        {
                            for (String a : props)
                            {
                                if (method.getMethodName().matches(a)
                                        || method.getMethodDesc().matches(a))
                                {
                                    annoMap.put(an.getAnnoName().trim(),
                                            an.getAnno());

                                }
                            }
                        }
                    }
                }
            }
        }
        return annoMap;
    }

    /**
     * 解析class上的文档注释
     * @param annoList
     * @return
     */
    public static HashMap<String, String> parseClsAnno(
            List<DefaultAnno> annoList)
    {
        HashMap<String, String> annoMap = new HashMap<String, String>();
        if (annoList != null && annoList.size() > 0)
        {
            for (DefaultAnno an : annoList)
            {
                if (DefaultAnno.CLZ_ANNO.equals(an.getAnnoType()))
                {

                    annoMap.put(an.getAnnoName().trim(), an.getAnno());

                }
            }
        }
        return annoMap;

    }

    /**
     * 解析默认属性
     * @param annoStrSrc
     * @param paraObj
     * @return
     */
    public static List<DefaultAnno> parse(String annoStrSrc,
            Map<String, Object> paraObj)
    {

        if (annoStrSrc == null || "".equals(annoStrSrc))
        {
            return new ArrayList<DefaultAnno>();
        }
        StringTemplateLoader strLoader = new StringTemplateLoader();
        strLoader.putTemplate("t1", annoStrSrc);
        Configuration conf = new Configuration();
        conf.setNumberFormat("0.#");
        conf.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
        conf.setTemplateLoader(strLoader);
        conf.setClassicCompatible(true);
        conf.setEncoding(Locale.CHINA, "UTF-8");

        HashMap<String, Object> root = new HashMap<String, Object>();

        if (paraObj != null)
        {
            root.put("sys", paraObj);
        }

        StringWriter sw = new StringWriter();
        try
        {
            Template temp = conf.getTemplate("t1");
            // temp.process(root, sw);
            temp.process(root, sw);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            e.printStackTrace(ps);
            EclipseConsoleUtil.printToConsole(baos.toString(), true);
            // throw e;
        }

        EclipseConsoleUtil.printToConsole(
                "******************************************", false);
        EclipseConsoleUtil.printToConsole(sw.toString(), false);
        EclipseConsoleUtil.printToConsole(
                "******************************************", false);
        List<DefaultAnno> retList = new ArrayList<DefaultAnno>();
        if (sw.toString() == null || "".equals(sw.toString().trim()))
        {
            return retList;
        }

        String items[] = sw.toString().split(";");
        if (items.length > 0)
        {
            retList = new ArrayList<DefaultAnno>();
            for (String it : items)
            {
                String tp = it.trim();
                if ("".equals(tp))
                {
                    continue;
                }
                if (it.indexOf("//") > -1)
                {
                    String its[] = it.split("//");
                    retList.add(new DefaultAnno(DefaultAnno.METHOD_ANNO, its[0]
                            .trim(), "," + its[1].trim() + ","));
                }
                else
                {
                    String its[] = it.split("/");
                    if (its.length > 1)
                    {
                        retList.add(new DefaultAnno(DefaultAnno.PROP_ANNO,
                                its[0].trim(), "," + its[1].trim() + ","));
                    }
                    else
                    {
                        retList.add(new DefaultAnno(DefaultAnno.CLZ_ANNO,
                                its[0].trim(), null));
                    }
                }
            }
        }
        return retList;
    }

    public static class DefaultAnno
    {
        public static String PROP_ANNO = "Prop";
        public static String CLZ_ANNO = "Clz";
        public static String METHOD_ANNO = "Method";
        private String annoType;
        private String anno;
        private String annoName;
        private String annoValue;

        private String propNames;

        public DefaultAnno(String annoType, String anno, String propNames)
        {
            this.annoType = annoType;
            this.anno = anno;

            this.propNames = propNames;
            int sp = this.anno.indexOf("=");
            if(sp == -1)
            {
                sp = this.anno.indexOf(":");
            }
            if (sp > -1)
            {
                this.annoName = this.anno.substring(0, sp);
                this.annoValue = this.anno.substring(sp + 1).trim();
                if (this.annoValue.endsWith(","))
                {
                    this.annoValue = this.annoValue.substring(0,
                            this.annoValue.length() - 1);
                }
            }
            else
            {
                this.annoName = this.anno;
                this.annoValue = "";
            }
        }

        public String getAnno()
        {
            return anno;
        }

        public void setAnno(String anno)
        {
            this.anno = anno;
        }

        public String getPropNames()
        {
            return propNames;
        }

        public void setPropNames(String propNames)
        {
            this.propNames = propNames;
        }

        public String getAnnoName()
        {
            return annoName;
        }

        public void setAnnoName(String annoName)
        {
            this.annoName = annoName;
        }

        public String getAnnoValue()
        {
            return annoValue;
        }

        public void setAnnoValue(String annoValue)
        {
            this.annoValue = annoValue;
        }

        public String getAnnoType()
        {
            return annoType;
        }

        public void setAnnoType(String annoType)
        {
            this.annoType = annoType;
        }
    }

}
