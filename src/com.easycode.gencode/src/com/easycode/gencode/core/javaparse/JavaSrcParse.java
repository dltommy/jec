/**
 * 作 者: dltommy
 * 日 期: 2011-11-26
 * 描 叙:
 */
package com.easycode.gencode.core.javaparse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map; 
import java.util.Set;
import java.util.TreeSet;

 
import org.eclipse.core.resources.IFile; 
import org.eclipse.core.resources.IProject; 
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration; 
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException; 
 

import com.easycode.gencode.core.ConfigParseUtil;
import com.easycode.gencode.core.ConfigParseUtil.DefaultAnno;
import com.easycode.gencode.core.javaparse.constants.ClassAnnoConst;
import com.easycode.gencode.core.javaparse.model.AnnoClassModel;
import com.easycode.gencode.core.javaparse.model.AnnoMethodModel;
import com.easycode.gencode.core.javaparse.model.AnnoPropModel;
import com.easycode.gencode.core.javaparse.model.IAnnoModel; 
import com.easycode.gencode.core.javaparse.model.java.JavaClzModel;
import com.easycode.gencode.core.javaparse.model.java.JavaMethodModel;
import com.easycode.gencode.core.javaparse.model.java.JavaParam;
import com.easycode.gencode.core.javaparse.model.java.JavaTypeModel;
import com.easycode.gencode.core.javaparse.model.java.PropModel;
  
import com.easycode.common.StringUtil;
import com.easycode.configmgr.model.Config;

/**
 * 功能描叙: 解析class类
 * 编 码: dltommy 
 * 完成时间: 2011-11-26 
 * 下午10:44:48
 */
public class JavaSrcParse
{

    
    private String annoCtx = null;
    private String templateId = null;


    private String pkgSource = null;
 
 
    private ICompilationUnit compUnit = null;
    private List<AnnoPropModel> noAnnoPropList = new ArrayList<AnnoPropModel>();
    private List<AnnoMethodModel> noAnnoMethodList = new ArrayList<AnnoMethodModel>();

    private AnnoClassModel clsRange = null;
    private List<DefaultAnno> defaultAnnoList = null; 
 

    public JavaSrcParse(ICompilationUnit compUnit)
    {
 
        this.compUnit = compUnit;
 
        try
        {
            this.clsRange = CompilationUnitParseUtil.parseClsPosition(compUnit);
        }
        catch (Exception e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try
        {
            this.noAnnoPropList = CompilationUnitParseUtil.parsePropList(compUnit);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try
        {
            this.noAnnoMethodList = CompilationUnitParseUtil.parseMethodList(compUnit);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public JavaSrcParse(String annoCtx, String templateId, String pkgSource, ICompilationUnit compUnit)
    {

        this(compUnit);
        this.pkgSource = pkgSource;
        this.annoCtx = annoCtx;
        this.templateId = templateId;

    }

    /**
     * 
     * 功 能: 注释功能 实现流程:
     * 
     * @param fileType
     * @param prjConfig
     * @return
     * @throws Exception
     */
    private String getClzJsonwithoutAnno(String fileType,
            Config config)// ,
            throws Exception
    {

        try
        {
            JavaClzModel clz = JavaClzModel.genInitCls(compUnit, config.getUserName(), pkgSource);//this.parseClz(fileType, prjConfig);

            List<PropModel> propList = new ArrayList<PropModel>();
 
            if (noAnnoPropList != null)
            {
                for (AnnoPropModel annoPro : noAnnoPropList)
                {
                    propList.add(annoPro.getPropModel());
                }
            }
            clz.setPropList(propList);

            if (propList != null)
            {
                for (PropModel p : propList)
                {

                    if (p.getPropType().getPkgName().equals("java.lang")
                            || p.getPropType().getPkgName().equals("java.util"))
                    {
                        continue;
                    }

                    String propClzName = p.getPropType().getClzName();
                    // 泛型
                    if (propClzName.indexOf("<") > -1)
                    {
                        String referClz = propClzName.substring(
                                propClzName.indexOf("<") + 1,
                                propClzName.indexOf(">"));

                        propClzName = referClz;// clzName.substring(0,clzName.indexOf("<"));
                    }
                    if (propClzName.indexOf("[") > -1)
                    {
                        String referClz = propClzName.substring(0,
                                propClzName.length() - 2);
                        propClzName = referClz;
                    }
                    String pfrom[] = clz.getParamFrom();
                    List<String> impPkg = new ArrayList<String>();
                    if (pfrom != null && pfrom.length > 0)
                    {
                        for (String i : pfrom)
                        {
                            impPkg.add(i);
                        }
                    }

                }
            }
            String ret = StringUtil.getJsonStr(clz);
            return StringUtil.formatOutput(ret);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
        
            throw e;
        }

    }

    public String getClzJson(String fileType, Config config)
            throws Exception
    {
        Map annoMap = null;
        //不带注释解析
        String srcJson = this.getClzJsonwithoutAnno(fileType, config);
 
        annoMap = StringUtil.josnToMap(srcJson);
        defaultAnnoList = ConfigParseUtil.parse(this.annoCtx, annoMap);
        String ret = null;
        JavaClzModel newclz = JavaClzModel.genInitCls(compUnit, config.getUserName(), pkgSource); 

        newclz.setTemplate(this.templateId);
        String pkg = newclz.getPkg().getPkgName();//null;

        //List<String> impPkg = newclz.getImportPkg();//null;
        List<String> impPkg = Arrays.asList(newclz.getParamFrom());//;
        try
        {
            List<AnnoPropModel> newList = CompilationUnitParseUtil
                    .parsePropWithAnnoList(compUnit, defaultAnnoList);

            //属性列表
            List<PropModel> propList = new ArrayList<PropModel>();

            if (newList != null)
            {
                for (AnnoPropModel annoPro : newList)
                {
                    propList.add(annoPro.getPropModel());
                }
            }
            newclz.setPropList(propList);

            //方法列表
            List<JavaMethodModel> methodList = new ArrayList<JavaMethodModel>();
            List<AnnoMethodModel> methdAnnoList = CompilationUnitParseUtil.parseMethodWithAnnoList(compUnit,defaultAnnoList);
            if(methdAnnoList != null && methdAnnoList.size() > 0)
            {
                for(AnnoMethodModel m:methdAnnoList)
                {
                    methodList.add(m.getMethod());
                }
            }
            newclz.setMethodList(methodList);
            
            
            // 拼凑json
            StringBuffer sb = new StringBuffer("");
            String clzJson = "{";
            if (defaultAnnoList != null && defaultAnnoList.size() > 0)
            {
                for (DefaultAnno an : defaultAnnoList)
                {
                    if (DefaultAnno.CLZ_ANNO.equals(an.getAnnoType()))
                    {
                        sb.append(an.getAnnoName() + ":" + an.getAnnoValue());
                        sb.append(",");
                    }

                }
            }
            if (sb.length() > 0)
            {
                clzJson += sb.substring(0, sb.length() - 1);
            }
            clzJson += "}";
 
            Map<String, Object> defMap = StringUtil.josnToMap(clzJson);

            Iterator<String> defaultIt = defMap.keySet().iterator();

            while (defaultIt.hasNext())
            {
                String key = defaultIt.next();
                if (defMap.get(key) != null)
                {
                    setClzValue(newclz, key, defMap.get(key));
                }
            }

            
 
            HashMap<String, Object> clzMap = new HashMap<String, Object>();
            if (this.templateId != null && !"".equals(this.templateId))
            {
                clzMap.put("template", this.templateId);
            }
            if (this.clsRange.getAnnoRange() != null)
            {
                String jsonStr = compUnit.getSource().substring(
                        this.clsRange.getAnnoRange().getBegin(),
                        this.clsRange.getAnnoRange().getEnd());
                String json = null;
                int annoBegin = jsonStr.indexOf("<EasyCode>");
                
                if (annoBegin > -1)
                {

                    int annoEnd = jsonStr.lastIndexOf("</EasyCode>");
                    json = jsonStr.substring(annoBegin + 10, annoEnd);
                    json = json.replaceAll("[\n\r]", "");
                    json = json.replaceAll("\\*", "");
                    json = json.replaceAll("//", ""); 
                    StringUtil.appendToMap(clzMap,  json); 
                }


                Iterator<String> keyIt = clzMap.keySet().iterator();
                while (keyIt.hasNext())
                {
                    String key = keyIt.next();
                    if (clzMap.get(key) != null)
                    {
                        setClzValue(newclz, key, clzMap.get(key));
                    }

                }

            }
            
            ArrayList<JavaTypeModel> referTypeList = new ArrayList<JavaTypeModel>();
            Set<String> genericSet = new TreeSet<String>();
            
            if(methodList != null && methodList.size()>0)
            {
            	for(JavaMethodModel m:methodList)
            	{
            		JavaTypeModel returnModel = m.getReturnType();
            		if(returnModel != null)
            		{
            		    if(returnModel.getGeneric() != null && returnModel.getGeneric().length>0)
            		    {
            		        for(String t:returnModel.getGeneric())
            		        {
            		            genericSet.add(t);
            		        }
            		    }
            			if(returnModel.checkIsObject())
            			{
                    		referTypeList.add(returnModel);
            			}

            		}
            		JavaParam[] params= m.getParamArray();
            		if(params != null && params.length>0)
            		{
            			for(JavaParam param:params)
            			{
                            if(param.getType().getGeneric() != null && param.getType().getGeneric().length>0)
                            {
                                for(String t:param.getType().getGeneric())
                                {
                                    genericSet.add(t);
                                }
                            }
                        	if(param.getType().checkIsObject())
                        	{
                        		referTypeList.add(param.getType());
                        	}
            			}
            		}
            		
            	}
            }
 


            
            
            for (PropModel p : propList)
            {
            	if(p.getPropType().checkIsObject())
            	{
            		referTypeList.add(p.getPropType());
            	}
             
            	if(p.getPropType().getGeneric() != null && p.getPropType().getGeneric().length>0)
                {
                    for(String t:p.getPropType().getGeneric())
                    {
                        genericSet.add(t);
                    }
                }
            }
            
            List<HashMap> fileHashMapList = new ArrayList<HashMap>();
            HashMap<String,String> typePkgMap = new HashMap<String,String>();
            if(genericSet.size()>0)
            {
                Iterator<String> it = genericSet.iterator();
                while(it.hasNext())
                {
                    String v = it.next();
                    HashMap propMap = getPropIFilePath("",pkg, impPkg,
                            v);
                    
                    IFile propFile = (IFile)propMap.get("file");
                    if(propFile == null || !propFile.exists())
                    {
                        continue;
                    }
                    fileHashMapList.add(propMap);
                     
                    
                }
            }
            
           
            
            if(referTypeList != null && referTypeList.size()>0)
            {
                List<String> tempStr = new ArrayList<String>();
            	for(JavaTypeModel m:referTypeList)
            	{
                    String propClzName = JavaTypeModel.getTypeClsName(m.getClzName());//;

                    HashMap propMap = getPropIFilePath(m.getPkgName(),pkg, impPkg,
                            propClzName);
                     

                    
                    IFile propFile = (IFile)propMap.get("file");
                    if(propFile == null || !propFile.exists())
                    {
                        continue;
                    }
                    fileHashMapList.add(propMap);

                    m.setPkgName((String)propMap.get("pkg"));
                    m.setIsObject(true);
                    
                    
                    
                    if(tempStr.contains(propClzName))
                    {
                    	continue;
                    }
                    else
                    {
                    	tempStr.add(propClzName);
				 
                    }
                    
            	}

            }
             
            for(HashMap propMap:fileHashMapList)
            {
                IFile propFile = (IFile)propMap.get("file");
                if(propFile == null || !propFile.exists())
                {
                    continue;
                }
                String key = propMap.get("pkg")+"."+(String)propMap.get("clsName");
                if(newclz.getZzzReferObj() != null && newclz.getZzzReferObj().containsKey(key))
                {
                    continue;
                }
                
                ICompilationUnit newUnit = JavaCore
                        .createCompilationUnitFrom(propFile);
                
                JavaClzModel relatClz = JavaClzModel.genInitCls(
                        newUnit, config.getUserName(), pkgSource);// new
                                                                        // JavaClzModel();
                relatClz.setClzName((String)propMap.get("clsName"));

                List<AnnoPropModel> tmpList = CompilationUnitParseUtil
                        .parsePropList(newUnit);
                List<PropModel> pList = new ArrayList<PropModel>();

                if (tmpList != null) 
                {
                    for (AnnoPropModel annoPro : tmpList) 
                    {
                        pList.add(annoPro.getPropModel());
                    }
                }
                relatClz.setPropList(pList);
                newclz.putReferObj((String)propMap.get("pkg")+"."+(String)propMap.get("clsName"), relatClz);
                typePkgMap.put((String)propMap.get("clsName"), (String)propMap.get("pkg")+"."+(String)propMap.get("clsName"));
            }

            
            fillGeneric(methodList,typePkgMap);
            
            List<JavaTypeModel> propGenList = new ArrayList<JavaTypeModel>();
            for (PropModel p : propList)
            {
                fillPropGeneric(p,typePkgMap);
                
            } 
           
            ret = StringUtil.getJsonStr(newclz);
            return StringUtil.formatOutput(ret);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        }

    }
    
    private void fillPropGeneric(PropModel p, HashMap<String,String> referTypeMap)
    {
        if (p.getPropType().getGeneric() != null
                && p.getPropType().getGeneric().length > 0)
        {

            // for(int i=0;i<);
            String[] gens = p.getPropType().getGeneric();
            String[] newGens = new String[gens.length];
            for (int i = 0; i < gens.length; i++)
            {
                if (referTypeMap.get(gens[i]) == null)
                {
                    JavaTypeModel tm = JavaTypeModel.createJavaType(gens[i]);
                    newGens[i] = tm.getPkgName() + "." + tm.getClzName();
                }
                else
                {
                    newGens[i] = referTypeMap.get(gens[i]);
                }

            }
            p.getPropType().setGeneric(newGens);
        }

    }
    private void fillGeneric(List<JavaMethodModel> methodList, HashMap<String,String> referTypeMap)
    {
        if(methodList != null && methodList.size()>0)
        {
            for(JavaMethodModel m:methodList)
            {
                JavaTypeModel returnModel = m.getReturnType();
                if(returnModel != null)
                {
                    if(returnModel.getGeneric() != null && returnModel.getGeneric().length>0)
                    {
                       // for(int i=0;i<);
                       String[] gens=  returnModel.getGeneric();
                       String[] newGens = new String[gens.length];
                        for(int i=0;i<gens.length;i++)
                        {
                            if(referTypeMap.get(gens[i]) == null)
                            {
                                JavaTypeModel tm = JavaTypeModel.createJavaType(gens[i]);
                                newGens[i] = tm.getPkgName()+"."+tm.getClzName();
                            }
                            else
                            {
                                newGens[i] = referTypeMap.get(gens[i]);    
                            }
                            
                        }
                        returnModel.setGeneric(newGens);
                    }

                }
                JavaParam[] params= m.getParamArray();
                if(params != null && params.length>0)
                {
                    for(JavaParam param:params)
                    {
                        if(param.getType().getGeneric() != null && param.getType().getGeneric().length>0)
                        {
                            // for(int i=0;i<);
                            String[] gens=  param.getType().getGeneric();
                            String[] newGens = new String[gens.length];
                             for(int i=0;i<gens.length;i++)
                             {
                                 if(referTypeMap.get(gens[i]) == null)
                                 {
                                     JavaTypeModel tm = JavaTypeModel.createJavaType(gens[i]);
                                     newGens[i] = tm.getPkgName()+"."+tm.getClzName();
                                 }
                                 else
                                 {
                                     newGens[i] = referTypeMap.get(gens[i]);    
                                 }
                                 
                             }
                             param.getType().setGeneric(newGens);
                        }
 
                    }
                }
                
            }
        }
    }

    private HashMap getPropIFilePath(String defaultPkgName,String srcPkg, List<String> pkg, 
            String clsName)
    {
        HashMap retMap = new HashMap();
        IFile ret = null;
        String proppkg = null;
        IProject javaProject = this.compUnit.getResource()
                .getProject();
        retMap.put("clsName", clsName);
        if(defaultPkgName != null && !"".equalsIgnoreCase(defaultPkgName))
        {
            ret = javaProject.getFile( pkgSource+"/"+defaultPkgName.replaceAll("\\.", "/") + "/"+clsName+".java");
            
            if(ret != null && ret.exists())
            {
                
                retMap.put("file", ret);
            }
            else
            {
                retMap.put("file", null);
            }
            

            retMap.put("pkg", defaultPkgName);

            return retMap;
        }
        
        if (pkg == null || pkg.size() == 0)
        {
            pkg = new ArrayList<String>();
            if (srcPkg != null)
            {
                pkg.add(srcPkg + "." + clsName);
            }
        }

        for (String temp : pkg)
        {

            if (temp.endsWith(".*"))
            { 
                String tp = temp.substring(0, temp.length() - 2);
                ret = javaProject.getFile(pkgSource+"/"+tp.replaceAll("\\.", "/")+ ".java");
                proppkg = tp;
                if(ret != null && ret.exists())
                {
                    
                    break;
                }
            }
            else
            {
                if (temp.endsWith(clsName))
                { 
                    proppkg = temp.substring(0,
                            temp.length() - clsName.length() - 1);
                    ret = javaProject.getFile( pkgSource+"/"+temp.replaceAll("\\.", "/") + ".java");
                    if(ret != null && ret.exists())
                    {
                        
                        break;
                    }
                     
                }
            }
        }
        retMap.put("file", ret);
        retMap.put("pkg", proppkg);
        return retMap;
    }
    
    /**
     * 解析引入包 功 能: 实现流程:
     * 
     * @param impSrc
     * @return
     */
    private List<String> parseImpPkg(String impSrc)
    {
        List<String> retList = new ArrayList<String>();
        try
        {
            IImportDeclaration imps[] = compUnit.getImports();
            if (imps != null)
            {
                for (IImportDeclaration i : imps)
                {

                    retList.add(i.getElementName());
                }
            }
        }
        catch (JavaModelException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return retList;
    }

  
    public String[] getParamFrom()
    {
        try
        {
            JavaClzModel clz = JavaClzModel.genInitCls(compUnit, null, pkgSource);
            return clz.getParamFrom();
        }
        catch (Exception e1)
        {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return new String[0];
    }
 
    public String getAnnoCtx()
    {
        return annoCtx;
    }

    public void setAnnoCtx(String annoCtx)
    {
        this.annoCtx = annoCtx;
    }

    public String getTemplateId()
    {
        return templateId;
    }

    public void setTemplateId(String templateId)
    {
        this.templateId = templateId;
    }

    public String getPkgSource()
    {
        return pkgSource;
    }

    public void setPkgSource(String pkgSource)
    {
        this.pkgSource = pkgSource;
    }

    public List<IAnnoModel> getAnnoModelByRange(int begin,int end)
    {
        List<IAnnoModel> retList = new ArrayList<IAnnoModel>();
        if(noAnnoPropList != null && noAnnoPropList.size() > 0)
        {
            for(AnnoPropModel p:noAnnoPropList)
            {
                int tempBegin = p.getCodeRange().getBegin();
                int tempEnd = p.getCodeRange().getEnd();
                if( (begin< tempBegin && end < tempBegin) ||(begin> tempEnd && end > tempEnd))
                {
                    continue;
                }
                else
                {
                    retList.add(p);
                }
                        
            }
        }
        //private List<AnnoMethodModel> noAnnoMethodList = new ArrayList<AnnoMethodModel>();
 
        if(noAnnoMethodList != null && noAnnoMethodList.size() > 0)
        {
            for(AnnoMethodModel p:noAnnoMethodList)
            {
                int tempBegin = p.getCodeRange().getBegin();
                int tempEnd = p.getCodeRange().getEnd();
                if( (begin< tempBegin && end < tempBegin) ||(begin> tempEnd && end > tempEnd))
                {
                    continue;
                }
                else
                {
                    retList.add(p);
                }
                        
            }
        }
        if(clsRange != null)
        {

            int tempBegin = clsRange.getCodeRange().getBegin();
            int tempEnd = clsRange.getCodeRange().getEnd();
            if(begin<= tempBegin && end > tempBegin)
            {
                retList.add(clsRange);
            }
                    
        
        }
        //private AnnoClassModel clsRange = null;
        //
        return retList;
    }
    private static void setClzValue(JavaClzModel clz, String key, Object value)
    {
        /*
        if (key.equals(ClassAnnoConst.USER_DEFINE))
        {
            clz.setUserDefine((HashMap<String, Object>) value);
        }
        else 
            */
        if (key.equals(ClassAnnoConst.TEMPLATE))
        {
            clz.setTemplate((String) value);
        }
        else if (key.equals(ClassAnnoConst.AUTHOR))
        {
            clz.setAuthor((String) value);
        }
        else if (key.equals(ClassAnnoConst.CLZ_SOURCE_CODE))
        {
            clz.setClzSourceCode((String) value);
        }
        /*
        else if (key.equals(ClassAnnoConst.JSON1))
        {
            clz.setJson1((HashMap<String, Object>) value);
        }
        else if (key.equals(ClassAnnoConst.JSON2))
        {
            clz.setJson2((HashMap<String, Object>) value);
        }
        else if (key.equals(ClassAnnoConst.JSON3))
        {
            clz.setJson3((HashMap<String, Object>) value);
        }
        */
        else
        {
            clz.addExtendProp(key, value);
        }
    }
  
    /*
    private String getTypeClsName(String propClzName)
    { 
         
        if (propClzName.indexOf("<") > -1)
        {
            String referClz = propClzName.substring(
                    propClzName.indexOf("<") + 1,
                    propClzName.indexOf(">"));

            propClzName = referClz; 
        }
        if (propClzName.indexOf("[") > -1)
        {
            String referClz = propClzName.substring(0,
                    propClzName.length() - 2);
            propClzName = referClz;
        } 
        
        if (propClzName.indexOf("[") > -1)
        {
            String referClz = propClzName.substring(0,
                    propClzName.length() - 2);
            propClzName = referClz;
        } 
        return propClzName;
    }
    */

}
