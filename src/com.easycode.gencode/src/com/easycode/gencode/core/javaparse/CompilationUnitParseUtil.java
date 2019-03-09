/**
 * 作 者:  
 * 日 期: 2011-11-26
 * 描 叙:
 */
package com.easycode.gencode.core.javaparse;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.SourceField;
import org.eclipse.jdt.internal.core.SourceMapper;
import org.eclipse.jdt.internal.core.SourceMethod;
import org.eclipse.jdt.internal.core.SourceType;

 
import com.easycode.gencode.core.javaparse.model.AnnoClassModel;
import com.easycode.gencode.core.javaparse.model.AnnoMethodModel;
import com.easycode.gencode.core.javaparse.model.AnnoPropModel;
import com.easycode.gencode.core.javaparse.model.Range; 
import com.easycode.gencode.core.ConfigParseUtil.DefaultAnno;
 
import com.easycode.common.StringUtil;
import com.easycode.gencode.core.javaparse.constants.MethodAnnoConst;
import com.easycode.gencode.core.javaparse.constants.PropAnnoConst;
import com.easycode.gencode.core.javaparse.model.java.JavaAn; 
import com.easycode.gencode.core.javaparse.model.java.JavaMethodModel;
import com.easycode.gencode.core.javaparse.model.java.JavaParam; 
import com.easycode.gencode.core.javaparse.model.java.PropModel; 

/**
 * 功能描叙: 编 码:   完成时间: 2011-11-26 下午10:44:48
 */
public class CompilationUnitParseUtil
{ 
    public static List<AnnoMethodModel> parseMethodWithAnnoList(ICompilationUnit compUnit,List<DefaultAnno> defaultAnnoList) throws Exception
    {
    	//CompilationUnit unit = CompilationUnit
        List<AnnoMethodModel> methdList = new ArrayList<AnnoMethodModel>();
        IJavaElement elemt[] = compUnit.getChildren();
         for (IJavaElement e : elemt)
         {
             IJavaModel m = e.getJavaModel();
             if (e.getElementType() == IJavaElement.TYPE)
             {
                 SourceType st = (SourceType) e;
                 for (IJavaElement k : st.getChildren())
                 {
                     if (k.getElementType() == IJavaElement.METHOD)
                     {
                         
                         AnnoMethodModel annoMethod = new AnnoMethodModel();
                         methdList.add(annoMethod);
                         JavaMethodModel mthd = new JavaMethodModel();

                         annoMethod.setMethod(mthd);
                         SourceMethod sm = (SourceMethod) k;
                         
                         
                         IAnnotation an[] = sm.getAnnotations();
                         List<JavaAn> javaAnList = new ArrayList<JavaAn>();
                         mthd.setJavaAnList(javaAnList);
                         
                         if(an != null && an.length>0)
                         {
                        	 for(IAnnotation a:an)
                        	 {
                        		 JavaAn jan = new JavaAn();
                        		 jan.setName(a.getElementName());
                        		 javaAnList.add(jan);
                        		IMemberValuePair  im[] = a.getMemberValuePairs();
                        		if(im != null && im.length>0)
                        		{
                        			for(IMemberValuePair r:im)
                        			{
                        				jan.getValueMap().put(r.getMemberName(), r.getValue());
                        			}
                        		}
                        	 }
                         }
                         
                         
                         
                         
                         
                         
                         String[] exceptList = null;
                         if(sm.getExceptionTypes() != null)
                         {
                             exceptList = new String[sm.getExceptionTypes().length];
                             for(int a=0;a<sm.getExceptionTypes().length;a++)
                             {
                                 String t = sm.getExceptionTypes()[a];
                                 exceptList[a]=t.substring(1,t.length()-1);
                             }
                             
                         }
                         mthd.setExceptions(exceptList); 
                         mthd.setReturnType(UnitUtil.parseTypeDigest(sm.getReturnType(),null));
                         
                         ISourceRange totalRange = sm.getSourceRange();
                         

                         
                         //设置名称
                         mthd.setMethodName(sm.getElementName());
                         
                         
                         
                         
                         JavaParam[] paramList = new JavaParam[sm.getParameters().length];
                         ILocalVariable p[] = sm.getParameters();
                         
                         String pName[] = sm.getParameterNames();
                         String pType[] = sm.getParameterTypes();
                         //StringBuilder sb = new StringBuilder();
                         for (int i = 0; i < p.length; i++)
                         {
                        	 String ts = p[i].toString();
                        	 int pos = ts.indexOf(" "); 
                        	 String type =pType[i]; 
                             String typeName =ts.substring(0,pos);//UnitUtil.getByTypeSig(type);
                             if(typeName == null)
                             {
                                 throw new Exception("解析参数类型异常！"+pName[i]);
                             }
                             if(type.startsWith("["))
                             {
                                 paramList[i] = new JavaParam(pName[i],typeName, true);

                             }
                             else  
                             {
                                 paramList[i] = new JavaParam(pName[i],typeName, false);
                             }

                         }
                         if(paramList != null && paramList.length>0)
                         {
                             mthd.setParamArray(paramList);
                         } 
                         if (sm.getJavadocRange() != null)
                         {

                             int tmpBegin = sm.getJavadocRange().getOffset();
                             int tmpEnd = tmpBegin
                                     + sm.getJavadocRange().getLength();
                                annoMethod.setAnnoRange(new Range(tmpBegin,tmpEnd));
                                annoMethod.setAnno(compUnit.getSource().substring(tmpBegin, tmpEnd));
                                annoMethod.setCodeRange(new Range(tmpEnd,totalRange.getOffset()+totalRange.getLength()));
  
                         }
                         else
                         {
                             annoMethod.setCodeRange(new Range(totalRange.getOffset(),totalRange.getOffset()+totalRange.getLength()));
                         }

                         String methdCtx = compUnit.getSource().substring(annoMethod.getCodeRange().getBegin(),annoMethod.getCodeRange().getEnd()).trim();
                         
                         methdCtx = methdCtx.replaceAll("//.*\r\n", "");
                    	 if(methdCtx.indexOf("{")>-1)
                    	 {
                    		 methdCtx = methdCtx.substring(0,methdCtx.indexOf("{"));
                    	 }
                         
                  
                          
                         int methodBegin = methdCtx.lastIndexOf("\r\n"); 
                         if(methodBegin >-1)
                         {
                        	 methdCtx = methdCtx.substring(methodBegin).trim();
                        	  
                         }
                    	 if(methdCtx.endsWith(";"))
                    	 {
                    		 methdCtx = methdCtx.substring(0,methdCtx.length()-1);
                    		 mthd.setType("interface");
                    	 }
                    	 else
                    	 {
                    		 mthd.setType("method");
                    	 }
 
                         
                         mthd.setMethodDesc(methdCtx);
                          
                         if(methdCtx.matches("protected[\\s]+[\\s\\S]+"))
                         {
                             mthd.setAccessLimit("protected");
                         }
                         else if(methdCtx.matches("private[\\s]+[\\s\\S]+"))
                         {
                             mthd.setAccessLimit("private");
                         }
                         else  if(methdCtx.matches("public[\\s]+[\\s\\S]+"))
                         {
                             mthd.setAccessLimit("public");
                         }

                         
                         
                         /////////////////////////////////////////////////////////////////
                         /*
                         String desc = sm.toString();
                         int pos = desc.indexOf(")");
                         desc = desc.substring(0,pos+1);
                         
                         String descA[] = desc.split("[,|\\)]");
                         StringBuilder sbt = new StringBuilder();
                         sbt.append(" ");
                         if(descA.length>1)
                         {
                        	 for(int i=0;i<descA.length;i++)
                        	 {
                        		 sbt.append(descA[i]);
                        		 sbt.append(" ");
                        		 sbt.append(paramList[i].getParamName());
                        		 if(i<descA.length-1)
                        		 {
                        			 sbt.append(",");
                        		 }
                        		
                        	 }
                        	 sbt.append(")");
                        	 mthd.setMethodDesc(sbt.toString());
                         }
                         else
                         {
                        	 if(paramList.length==1)
                        	 {
                        		 mthd.setMethodDesc(descA[0]+" "+paramList[0].getParamName()+")");
                        	 }
                        	 else
                        	 {
                        		 mthd.setMethodDesc(desc);
                        	 }
                        	 
                         }
                         */
                         
                          

                         
                         //mthd.setMethodDesc(mthd.getType()+" "+mthd.getMethodName()+"()")
                         Map<String, Object> map = null;
                         if(defaultAnnoList != null)
                         {
                             HashMap<String, String> annoPropMap = new HashMap<String, String>();
                             //先设置默认属性，再设置特定属性
                             if(defaultAnnoList != null && defaultAnnoList.size() > 0 )
                             {
                                 for(DefaultAnno pan:defaultAnnoList)
                                 {
                                     if(pan.getAnnoType().equals(DefaultAnno.METHOD_ANNO))
                                     {
                                     
                                         String props[] = pan.getPropNames().split(",");
                                         if(props.length>0)
                                         {
                                             for(String a:props)
                                             {
                                                 if(mthd.getMethodName().matches(a))
                                                 {
                                                     annoPropMap.put(pan.getAnnoName().trim(), pan.getAnnoValue());//pan.getAnno().replaceAll("#propName", newProp.getPropName()));
                                                      
                                                 }
                                             }
                                         }
                                     }
                                 }
                             }
                             Iterator<Entry<String, String>> enIt = annoPropMap.entrySet().iterator();
                             //拼凑json
                             StringBuffer sb = new StringBuffer("");
                             String json="{";
                             while(enIt.hasNext())
                             {
                                 Entry<String, String> curEnt = enIt.next();
                                 
                                 sb.append(curEnt.getKey()+":"+curEnt.getValue());
                                 sb.append(",");
                             }
                             if(sb.length()>0)
                             {
                                 json +=sb.substring(0,sb.length() -1);
                             }
                             json +="}"; 
                             

                             map = StringUtil.josnToMap(json);

                             
                             if (map.size() > 0)
                             {
                                 Iterator it = map.keySet().iterator();
                                 while (it.hasNext())
                                 {

                                     String key = (String) it.next();
                                     Object value = map.get(key);//.toString().trim();
                                     setMethodValue(mthd,key,value);
                                 }

                             }

                         }

                         map = new HashMap<String, Object>();
                         
                         String anno = null;//rs.group(2);
                         if(annoMethod.getAnnoRange() != null)
                         {
                             int annoBegin = annoMethod.getAnno().indexOf("<EasyCode>");
                             if(annoBegin > -1)
                             {
                                 int annoEnd = annoMethod.getAnno().lastIndexOf("</EasyCode>");
                                 anno = annoMethod.getAnno().substring(annoBegin+10,annoEnd);
                                 anno = anno.replaceAll("[\n\r]", "");
                                 anno = anno.replaceAll("\\*", "");
                                 anno = anno.replaceAll("//", "");
 
                                 map = StringUtil.josnToMap(anno );
                                 if (map.size() > 0)
                                 {
                                     Iterator it = map.keySet().iterator();
                                     while (it.hasNext())
                                     {

                                         String key = (String) it.next();
                                         Object value = map.get(key);//.toString().trim();
                                         setMethodValue(mthd,key,value);
                                     }

                                 }
                             }
                         }
 
                     }
                 }
             }
              

         }
        return methdList;
    }
    public static List<AnnoMethodModel> parseMethodList(ICompilationUnit compUnit) throws Exception
    {
        List<AnnoMethodModel> methdList = new ArrayList<AnnoMethodModel>();
        IJavaElement elemt[] = compUnit.getChildren();
         for (IJavaElement e : elemt)
         {
             IJavaModel m = e.getJavaModel();
             if (e.getElementType() == IJavaElement.TYPE)
             {
                 SourceType st = (SourceType) e;
                 for (IJavaElement k : st.getChildren())
                 {
                     if (k.getElementType() == IJavaElement.METHOD)
                     {
                         AnnoMethodModel annoMethod = new AnnoMethodModel();
                         methdList.add(annoMethod);
                         JavaMethodModel mthd = new JavaMethodModel();

                         annoMethod.setMethod(mthd);
                         SourceMethod sm = (SourceMethod) k;
                         String[] exceptList = null;
                         
                         
                         IAnnotation an[] = sm.getAnnotations();
                         List<JavaAn> javaAnList = new ArrayList<JavaAn>();
                         mthd.setJavaAnList(javaAnList);
                         
                         if(an != null && an.length>0)
                         {
                        	 for(IAnnotation a:an)
                        	 {
                        		 JavaAn jan = new JavaAn();
                        		 jan.setName(a.getElementName());
                        		 javaAnList.add(jan);
                        		IMemberValuePair  im[] = a.getMemberValuePairs();
                        		if(im != null && im.length>0)
                        		{
                        			for(IMemberValuePair r:im)
                        			{
                        				jan.getValueMap().put(r.getMemberName(), r.getValue());
                        			}
                        		}
                        	 }
                         }
                         
                         if(sm.getExceptionTypes() != null)
                         {
                             exceptList = new String[sm.getExceptionTypes().length];
                             for(int a=0;a<sm.getExceptionTypes().length;a++)
                             {
                                 String t = sm.getExceptionTypes()[a];
                                 exceptList[a]=t.substring(1,t.length()-1);
                             }
                             
                         }
                         mthd.setExceptions(exceptList);
 
                        
                         
                         mthd.setReturnType(UnitUtil.parseTypeDigest(sm.getReturnType(),null));
                         
                         ISourceRange totalRange = sm.getSourceRange();
                         
                         //设置名称
                         mthd.setMethodName(sm.getElementName());
                         if (sm.getJavadocRange() != null)
                         {

                             int tmpBegin = sm.getJavadocRange().getOffset();
                             int tmpEnd = tmpBegin
                                     + sm.getJavadocRange().getLength();
                                annoMethod.setAnnoRange(new Range(tmpBegin,tmpEnd));
                                
                                annoMethod.setCodeRange(new Range(tmpEnd,totalRange.getOffset()+totalRange.getLength()));
 
                         }
                         else
                         {
                             annoMethod.setCodeRange(new Range(totalRange.getOffset(),totalRange.getOffset()+totalRange.getLength()));
                         }
                         

                         JavaParam[] paramList = new JavaParam[sm.getParameters().length];
                         //设置参数
                         //annoMethod.getMethod().setParamArray(paramList);
                         ILocalVariable p[] = sm.getParameters();
                         String pName[] = sm.getParameterNames();
                         String pType[] = sm.getParameterTypes();
                         for (int i = 0; i < p.length; i++)
                         {
                            /*
                             String type =pType[i];

                             String typeName = UnitUtil.getByTypeSig(type);
                             if(typeName == null)
                             {
                                 throw new Exception("解析参数类型异常："+pName[i]);
                             }
                             if(type.startsWith("["))
                             {
                                 paramList[i] = new JavaParam(pName[i], typeName, true);
                             }
                             else 
                             {
                                 paramList[i] = new JavaParam(pName[i], typeName, false);
                                  
                             }
                             */

                        	 String ts = p[i].toString();
                        	 int pos = ts.indexOf(" "); 
                        	 String type =pType[i]; 
                             String typeName =ts.substring(0,pos);//UnitUtil.getByTypeSig(type);
                             if(typeName == null)
                             {
                                 throw new Exception("解析参数类型异常！"+pName[i]);
                             }
                             if(type.startsWith("["))
                             {
                                 paramList[i] = new JavaParam(pName[i],typeName, true);

                             }
                             else  
                             {
                                 paramList[i] = new JavaParam(pName[i],typeName, false);
                             }

                         

                         }
                         if(paramList != null && paramList.length>0)
                         {
                             mthd.setParamArray(paramList);
                         } 
                         if (sm.getJavadocRange() != null)
                         {

                             int tmpBegin = sm.getJavadocRange().getOffset();
                             int tmpEnd = tmpBegin
                                     + sm.getJavadocRange().getLength();
                                annoMethod.setAnnoRange(new Range(tmpBegin,tmpEnd));
                                annoMethod.setAnno(compUnit.getSource().substring(tmpBegin, tmpEnd));
                                annoMethod.setCodeRange(new Range(tmpEnd,totalRange.getOffset()+totalRange.getLength()));
  
                         }
                         else
                         {
                             annoMethod.setCodeRange(new Range(totalRange.getOffset(),totalRange.getOffset()+totalRange.getLength()));
                         }

                         String methdCtx = compUnit.getSource().substring(annoMethod.getCodeRange().getBegin(),annoMethod.getCodeRange().getEnd()).trim();
                         
                         methdCtx = methdCtx.replaceAll("//.*\r\n", "");
                    	 if(methdCtx.indexOf("{")>-1)
                    	 {
                    		 methdCtx = methdCtx.substring(0,methdCtx.indexOf("{"));
                    	 }
                         
                  
                          
                         int methodBegin = methdCtx.lastIndexOf("\r\n"); 
                         if(methodBegin >-1)
                         {
                        	 methdCtx = methdCtx.substring(methodBegin).trim();
                        	  
                         }
                    	 if(methdCtx.endsWith(";"))
                    	 {
                    		 methdCtx = methdCtx.substring(0,methdCtx.length()-1);
                    		 mthd.setType("interface");
                    	 }
                    	 else
                    	 {
                    		 mthd.setType("method");
                    	 }
 
                         
                         mthd.setMethodDesc(methdCtx);
                          
                         if(methdCtx.matches("protected[\\s]+[\\s\\S]+"))
                         {
                             mthd.setAccessLimit("protected");
                         }
                         else if(methdCtx.matches("private[\\s]+[\\s\\S]+"))
                         {
                             mthd.setAccessLimit("private");
                         }
                         else  if(methdCtx.matches("public[\\s]+[\\s\\S]+"))
                         {
                             mthd.setAccessLimit("public");
                         }
 
                     }
                 }
             }
              

         }
        return methdList;
    }
    public static AnnoClassModel  parseClsPosition(ICompilationUnit compUnit) throws Exception
    {
        AnnoClassModel ret = new AnnoClassModel();
        IJavaElement elemt[] = compUnit.getChildren();
 
        
        for (IJavaElement e : elemt)
        {
            IJavaModel m = e.getJavaModel();
            
            if (e.getElementType() == IJavaElement.TYPE)
            {
                SourceType st = (SourceType) e;
                int codeBegin = st.getSourceRange().getOffset();
                int codeEnd = st.getSourceRange().getOffset()+st.getSourceRange().getLength();
                if(st.getJavadocRange() != null)
                {
                    Range annoRange = new Range(st.getJavadocRange().getOffset(),st.getJavadocRange().getOffset()+st.getJavadocRange().getLength());
                    ret.setAnnoRange(annoRange);
                    codeBegin=annoRange.getEnd();
                    ret.setCodeRange(new Range(st.getJavadocRange().getOffset()+st.getJavadocRange().getLength(),codeEnd));
                }
                else
                {
                    ret.setCodeRange(new Range(codeBegin,codeEnd));
                }
            
                
            }
        }
        return ret;
    }

    public static List<AnnoPropModel> parsePropWithAnnoList(ICompilationUnit compUnit,List<DefaultAnno> defaultAnnoList) throws Exception
    {
        
        
        List<AnnoPropModel> retList = new ArrayList<AnnoPropModel>();
 
           IJavaElement elemt[] = compUnit.getChildren();

            for (IJavaElement e : elemt)
            {
                IJavaModel m = e.getJavaModel();
                if (e.getElementType() == IJavaElement.TYPE)
                {
                    SourceType st = (SourceType) e;
                    for (IJavaElement k : st.getChildren())
                    {
                        if (k.getElementType() == IJavaElement.FIELD)
                        {
                            
                            SourceField sf = (SourceField) k;
                            AnnoPropModel newProp = null;
                            String type = sf.getTypeSignature();
                            
                            String typeName = UnitUtil.getByTypeSig(type);
                            if(typeName == null)
                            {
                                throw new Exception("解析参数类型异常："+sf.getElementName());
                            }
                            if(type.startsWith("["))
                            {
                                newProp = new AnnoPropModel(typeName, sf.getElementName(),true);
                            }
                            else 
                            {
                                newProp = new AnnoPropModel(typeName, sf.getElementName(),false);      
                            }
                            
                            
                            IAnnotation an[] = sf.getAnnotations();
                            List<JavaAn> javaAnList = new ArrayList<JavaAn>();
                            newProp.getPropModel().setJavaAnList(javaAnList);
                            if(an != null && an.length>0)
                            {
                           	 for(IAnnotation a:an)
                           	 {
                           		 JavaAn jan = new JavaAn();
                           		 jan.setName(a.getElementName());
                           		 javaAnList.add(jan);
                           		IMemberValuePair  im[] = a.getMemberValuePairs();
                           		if(im != null && im.length>0)
                           		{
                           			for(IMemberValuePair r:im)
                           			{
                           				jan.getValueMap().put(r.getMemberName(), r.getValue());
                           			}
                           		}
                           	 }
                            } 
                            
                            
                            String source = sf.getSource();
                            ISourceRange ss = sf.getSourceRange();
                            ISourceRange sr = sf.getJavadocRange();
                            
                            
                            if(sr != null)
                            {
                                newProp.setAnnoRange(new Range(sr.getOffset(),sr.getOffset()+sr.getLength()));
                                String anno = compUnit.getSource().substring(sr.getOffset(), sr.getOffset()+sr.getLength());
                              
                                source = compUnit.getSource().substring(sr.getOffset()+sr.getLength(),ss.getOffset()+ss.getLength()).trim();
                                newProp.setAnno(anno);
                                newProp.setCodeRange(new Range(sr.getOffset()+sr.getLength(),ss.getOffset()+ss.getLength()));
                            }
                            else
                            {
                                newProp.setCodeRange(new Range(ss.getOffset(),ss.getOffset()+ss.getLength()));
                            }
                           
                            if(source.matches("protected[\\s].*"))
                            {
                                newProp.getPropModel().getPropType().setAccessLimit("protected");
                            }
                            else if(source.matches("private[\\s].*"))
                            {
                                newProp.getPropModel().getPropType().setAccessLimit("private");
                            }
                            else  if(source.matches("public[\\s].*"))
                            {
                                newProp.getPropModel().getPropType().setAccessLimit("public");
                            } 
                            Map<String, Object> map = new HashMap<String, Object>();
                            if(defaultAnnoList != null)
                            {
                                HashMap<String, String> annoPropMap = new HashMap<String, String>();
                                //先设置默认属性，再设置特定属性
                                if(defaultAnnoList != null && defaultAnnoList.size() > 0 )
                                {
                                    for(DefaultAnno pan:defaultAnnoList)
                                    {
                                        if(pan.getAnnoType().equals(DefaultAnno.CLZ_ANNO))
                                        {
                                            continue;
                                        }
                                        if(pan.getPropNames() != null)
                                        {
                                            String props[] = pan.getPropNames().split(",");
                                            if(props.length>0)
                                            {
                                                for(String a:props)
                                                {
                                                    if(newProp.getPropModel().getPropName().matches(a))
                                                    {
                                                        annoPropMap.put(pan.getAnnoName().trim(), pan.getAnnoValue());//pan.getAnno().replaceAll("#propName", newProp.getPropName()));
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                Iterator<Entry<String, String>> enIt = annoPropMap.entrySet().iterator();
                                //拼凑json
                                StringBuffer sb = new StringBuffer("");
                                String json="{";
                                while(enIt.hasNext())
                                {
                                    Entry<String, String> curEnt = enIt.next();
                                    
                                    sb.append(curEnt.getKey()+":"+curEnt.getValue());
                                    sb.append(",");
                                }
                                if(sb.length()>0)
                                {
                                    json +=sb.substring(0,sb.length() -1);
                                }
                                json +="}"; 
                                

                                map = StringUtil.josnToMap(json);
 
                                
                                if (map.size() > 0)
                                {
                                    Iterator it = map.keySet().iterator();
                                    while (it.hasNext())
                                    {

                                        String key = (String) it.next();
                                        Object value = map.get(key);//.toString().trim();
                                        setPropValue(newProp.getPropModel(),key,value);
                                    }

                                }

                            }

                            map = new HashMap<String, Object>();
                            
                            String anno = null;//rs.group(2);
                            if(newProp.getAnno() != null)
                            {
                                int annoBegin = newProp.getAnno().indexOf("<EasyCode>");
                                if(annoBegin > -1)
                                {
                                    int annoEnd = newProp.getAnno().lastIndexOf("</EasyCode>");
                                    anno = newProp.getAnno().substring(annoBegin+10,annoEnd);
                                    anno = anno.replaceAll("[\n\r]", "");
                                    anno = anno.replaceAll("\\*", "");
                                    anno = anno.replaceAll("//", "");
 
                                    map = StringUtil.josnToMap(anno);
                                    if (map.size() > 0)
                                    {
                                        Iterator it = map.keySet().iterator();
                                        while (it.hasNext())
                                        {

                                            String key = (String) it.next();
                                            Object value = map.get(key);//.toString().trim();
                                            setPropValue(newProp.getPropModel(),key,value);
                                        }

                                    }
                                }
                            }
                         
                                retList.add(newProp);
                        }
 
                    }
                }
 

            }
            return retList;
    
    }
	public static List<AnnoPropModel> parsePropList(ICompilationUnit compUnit) throws Exception
	{  
 
        List<AnnoPropModel> retList = new ArrayList<AnnoPropModel>();
 
	       IJavaElement elemt[] = compUnit.getChildren();

	        for (IJavaElement e : elemt)
	        {
	            IJavaModel m = e.getJavaModel();
	            if (e.getElementType() == IJavaElement.TYPE)
	            {
	                SourceType st = (SourceType) e;
	                for (IJavaElement k : st.getChildren())
	                {
	                    if (k.getElementType() == IJavaElement.FIELD)
	                    {
	                        
	                        SourceField sf = (SourceField) k; 
	                        AnnoPropModel newProp = null;
	                        String type = sf.getTypeSignature();
	                        
	                        String typeName = UnitUtil.getByTypeSig(type);
	                        if(type.startsWith("["))
	                        {
	                              newProp = new AnnoPropModel(typeName, sf.getElementName(),true);	                               
	                        }
	                        else
	                        {
	                            newProp = new AnnoPropModel(typeName, sf.getElementName(),false);
	                        }

	                      
	                        

                            IAnnotation an[] = sf.getAnnotations();
                            List<JavaAn> javaAnList = new ArrayList<JavaAn>();
                            newProp.getPropModel().setJavaAnList(javaAnList);
                            if(an != null && an.length>0)
                            {
                           	 for(IAnnotation a:an)
                           	 {
                           		 JavaAn jan = new JavaAn();
                           		 jan.setName(a.getElementName());
                           		 javaAnList.add(jan);
                           		IMemberValuePair  im[] = a.getMemberValuePairs();
                           		if(im != null && im.length>0)
                           		{
                           			for(IMemberValuePair r:im)
                           			{
                           				jan.getValueMap().put(r.getMemberName(), r.getValue());
                           			}
                           		}
                           	 }
                            } 
                            
	                        
	                        
	                        
	                        
	                        
	                        
	                        
	                        
                            String source = sf.getSource();
                            ISourceRange ss = sf.getSourceRange();
	                        ISourceRange sr = sf.getJavadocRange();
                            
                            
	                        if(sr != null)
	                        {
	                            newProp.setAnnoRange(new Range(sr.getOffset(),sr.getOffset()+sr.getLength()));
	                            String anno = compUnit.getSource().substring(sr.getOffset(), sr.getOffset()+sr.getLength());
	                         
	                            source = compUnit.getSource().substring(sr.getOffset()+sr.getLength(),ss.getOffset()+ss.getLength()).trim();
	                            newProp.setAnno(anno);
	                            newProp.setCodeRange(new Range(sr.getOffset()+sr.getLength(),ss.getOffset()+ss.getLength()));
	                        }
	                        else
	                        { 
	                            newProp.setCodeRange(new Range(ss.getOffset(),ss.getOffset()+ss.getLength()));
	                        }
	                       
	                        if(source != null)
	                        {
	                            
	                        
                            if(source.matches("protected[\\s].*"))
                            {
                                newProp.getPropModel().getPropType().setAccessLimit("protected");
                            }
                            else if(source.matches("private[\\s].*"))
                            {
                                newProp.getPropModel().getPropType().setAccessLimit("private");
                            }
                            else  if(source.matches("public[\\s].*"))
                            {
                                newProp.getPropModel().getPropType().setAccessLimit("public");
                            }
	                        }
                            retList.add(newProp);
	                    }
 
	                }
	            }
 

	        }
	        return retList;
	}
    
    private static void setPropValue(PropModel newProp,String key, Object value)
	{
	        /*
	        if (PropAnnoConst.USER_DEFINE.equals(key))
	        {
	            if (value != null)
	            {
	                newProp
	                        .setUserDefine((HashMap<String, Object>) value);
	            }
	        }
	        else 
	            */
	       if (PropAnnoConst.TITLE.equals(key))
	        {
	            newProp.setTitle((String)value);
	        }
	       /*
	        else if (PropAnnoConst.JSON1.equals(key))
	        {

	            if (value != null)
	            {
	                if (value instanceof HashMap)
	                {
	                    newProp
	                            .setJson1((HashMap<String, Object>) value);
	                }

	            }
	        }
	        else if(PropAnnoConst.JSON2.equals(key))
	        {
	            if (value != null)
	            {
	                if (value instanceof HashMap)
	                {
	                    newProp
	                            .setJson2((HashMap<String, Object>) value);
	                }

	            }
	        }
	        else if(PropAnnoConst.JSON3.equals(key))
	        {
	            if (value != null)
	            {
	                if (value instanceof HashMap)
	                {
	                    newProp
	                            .setJson3((HashMap<String, Object>) value);
	                }

	            }
	        }*/
	        else if (PropAnnoConst.COL_NAME.equals(key))
	        {
	            newProp.setColName((String)value);
	        }
	        /*
	        else if (PropAnnoConst.REFER_OBJ.equals(key))
	        {
	            newProp.setReferObj((String)value);
	        }
	        */
	        else if(PropAnnoConst.REFER.equals(key))
	        {
	            newProp.setRefer((String)value);
	        }
	        else if(PropAnnoConst.TEMPLATE.equals(key))
	        {
	            newProp.setTemplate((String)value);
	        }
	        else
	        {
	            newProp.addExtendProp(key, value);
	        }
	    }
   
    private static void setMethodValue(JavaMethodModel mthd ,String key, Object value)
    {
        if (MethodAnnoConst.TITLE.equals(key))
        {
            mthd.setTitle((String)value);
        }
 
        else
        {
            mthd.addExtendProp(key, value);
        }
    
    }
  
    public static ICompilationUnit  getCompUnit(String filePath,IProject prj)
    {
        
        ICompilationUnit newUnit = JavaCore
                .createCompilationUnitFrom(prj.getFile(filePath));
        return newUnit;
    }
    
    public static String parsePkgName(ICompilationUnit compUnit) throws JavaModelException
    {
        IPackageDeclaration pks[] = compUnit.getPackageDeclarations();
        String pkgName = null;
        if(pks != null && pks.length>0)
        {
            pkgName = pks[0].getElementName();
        }
        
        return pkgName;
    }
  
}
