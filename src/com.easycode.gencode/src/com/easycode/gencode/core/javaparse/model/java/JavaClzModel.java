/**
 * 作 者: dltommy
 * 日 期: 2011-9-18
 * 描 叙:
 */
package com.easycode.gencode.core.javaparse.model.java;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.ImportContainer;
import org.eclipse.jdt.internal.core.SourceType;
 

import com.easycode.common.FileUtil;
import com.easycode.common.BaseObject;
import com.easycode.common.JECInfoModel;

/**
 * 功能描叙:
 * 编   码: dltommy
 * 完成时间: 2011-9-18 下午10:23:17
 */
public class JavaClzModel extends BaseObject
{
 
    //private List<MethodModel> methdList = new ArrayList<MethodModel>();
    private HashMap<String, Object> zzzReferObj = new HashMap<String, Object>();

    private String clzSourceCode;
    private String genTime = null;
    //去掉
    private String prjPath;
    //去掉
    private String srcRoot;
	private List<PropModel> propList = new ArrayList<PropModel>();
	
	private List<JavaMethodModel> methodList = new ArrayList<JavaMethodModel>();
    private String clzName = null;
    private String tabName = null;
	private List<String> importPkg = new ArrayList<String>();
	
    private String author = null;
 
    private Pkg pkg = new Pkg();
  	private String template = null;
 
    private String paramFrom[] = null;

    //区分接口和类
    private String type = null;
    
    //private HashMap<String,Object> userDefine = new HashMap<String,Object>(); 
    private HashMap an = new HashMap<String,String>(); 
     
    private List<JavaAn> javaAnList = new ArrayList<JavaAn>();
    public JECInfoModel jec = new JECInfoModel();
    
    /**
     * 
     * @param compUnit
     * @param seedId
     * @param pkgSource
     * @return
     * @throws Exception
     */
    public static JavaClzModel genInitCls(ICompilationUnit compUnit ,String seedId,  String pkgSource) throws Exception
    {
        
        JavaClzModel clz = new JavaClzModel();
        IJavaElement elemt[] = compUnit.getChildren();
        clz.setGenTime( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        
        
        
        
        for (IJavaElement e : elemt)
        {
            IJavaModel m = e.getJavaModel();
         
            if (e.getElementType() == IJavaElement.TYPE)
            {
                SourceType st = (SourceType) e;
 
                IAnnotation an[] = st.getAnnotations();
                List<JavaAn> javaAnList = new ArrayList<JavaAn>();
                clz.setJavaAnList(javaAnList);
                
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
                clz.setClzName(st.getElementName());
 
                String checkType = null;
                
                int sbegin = st.getSourceRange().getOffset();
                int send = st.getSourceRange().getOffset()+st.getSourceRange().getLength();
                if (st.getJavadocRange() != null)
                {
                    //文档注释开始结束位置
                    int begin = st.getJavadocRange().getOffset();
                    int end = begin + st.getJavadocRange().getLength();
                    
                    
                    //源代码开始结束位置
                    sbegin = st.getSourceRange().getOffset()+end;
                    
                    
                    
                    String codeStr = compUnit.getSource().substring(end,send);
                    
                    
                    int endCheck = codeStr.indexOf("{");
                    
                     checkType =codeStr.substring(0,endCheck);

                }
                else
                {
                    
                    String codeStr = compUnit.getSource().substring(sbegin,send);
                        int endCheck =codeStr.indexOf("{");
                        checkType = codeStr.substring(0,endCheck);
                    

                }
 
                if(checkType.matches("[\\s\\S]+[\\s]+interface[\\s]+[\\s\\S]+"))
                {
                    clz.setType("interface");
                }
                else
                {
                    clz.setType("class");
                } 
            }
 
        }


 
        //clz.setPrjPath(compUnit.getJavaProject().getPath().toFile().getAbsolutePath());
        clz.setAuthor(seedId);
        clz.setClzSourceCode(pkgSource);
        String pkg = null;

        List<String> impPkg = new ArrayList<String>();

        try
        {
         
            IPackageDeclaration pkgs[] = compUnit.getPackageDeclarations();
            if (pkgs != null && pkgs.length > 0)
            {
                pkg = pkgs[0].getElementName();
            }
            if (pkg != null)
            {
                clz.setPkg( new Pkg(pkg));//.getPkg().setPkgName(pkg);
            }
           
       
            try
            {
                IImportDeclaration imps[] = compUnit.getImports();
                if (imps != null)
                {
                    for (IImportDeclaration i : imps)
                    {

                        impPkg.add(i.getElementName());
                    }
                }
            }
            catch (JavaModelException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            
            
            
            
            
            
            
            // List<String> spePkg = new ArrayList<String>();
            // 找出以.*结尾的包
            if (impPkg != null)
            {
                impPkg.add(pkg + ".*");
            }
            HashMap<String, String> paramList = new HashMap<String, String>();
            IProject javaProject =  compUnit.getResource() .getProject();
            for (String p : impPkg)
            {
                if(p.endsWith(".*"))
                {
                    continue;
                }
                IFile file = javaProject.getFile(pkgSource+"/"+p.replaceAll("\\.", "/")+ ".java");
                if(file != null && file.exists())
                {
                    paramList.put(p, p);
                }

            }
            IResource res[] = compUnit.getResource().getParent().members();
            for (IResource i : res)
            {
                if (i.getType() == IResource.FILE
                        && i.getName().endsWith(".java"))
                { 
                    String filePkg = pkg
                            + "."
                            + i.getName()
                                    .substring(0, i.getName().length() - 5);
                    paramList.put(filePkg, filePkg);
                }
            }

            paramList.remove(pkg + "." + clz.getClzName());
            String paraFileArry[] = new String[paramList.size() + 1];
            paraFileArry[0] = pkg + "." + clz.getClzName();

            int index = 1;
            if (paramList.size() > 0)
            {
                Iterator<String> it = paramList.keySet().iterator();
                while (it.hasNext())
                {
                    paraFileArry[index] = it.next();
                    index++;
                }
            }

            clz.setParamFrom(paraFileArry);

            //this.paramFrom = paraFileArry;

            if (clz.getParamFrom() == null || clz.getParamFrom().length == 0)
            {
                clz.setParamFrom(new String[]
                { pkg + "." + clz.getClzName() });
               //this.paramFrom = new String[]
                //{ pkg + "." + clz.getClzName() };
            }

            return clz;
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw e;
        }

    
    }
   public JavaClzModel()
   {
       this.genTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
 
   }
    
    public String getGenTime()
    {
        return genTime;
    }

    public void setGenTime(String genTime)
    {
        this.genTime = genTime;
    }

    public String getClzSourceCode()
    {
        return clzSourceCode;
    }
    public void setClzSourceCode(String clzSourceCode)
    {
        this.clzSourceCode = clzSourceCode;
    }
    public String getAuthor()
    {
        return author;
    }
    public void setAuthor(String author)
    {
        this.author = author;
    }
	public void addExtendProp(String key, Object value)
    {
    	this.an.put(key, value);
    }
	public void impPkg(String pkg)
    {
    	this.importPkg.remove(pkg);
    	this.importPkg.add(pkg);
    }
    public void addProp(PropModel p)
    {
    	this.propList.add(p);
    }
    /*
    public void addMethd(MethodModel methd)
    {
    	this.methdList.add(methd);
    }
*/
	public List<String> getImportPkg()
	{
		return importPkg;
	}
	public void setImportPkg(List<String> importPkg)
	{
		this.importPkg = importPkg;
	}

	public void putReferObj(String name, Object o)
	{
		this.zzzReferObj.put(name, o);
	}
	public List<PropModel> getPropList()
	{
		return propList;
	}
	/*
	public void setPropList(List<PropModel> propList)
	{
		this.propList = propList;
	}
	*/
	public String getClzName()
	{
		return clzName;
	}
	public void setClzName(String clzName)
	{
		this.clzName = clzName;
		if(clzName != null)
		{
		    this.tabName = genColName(clzName);
		}
	}
	public String getTabName()
	{
		return tabName;
	}
	public void setTabName(String tabName)
	{
		this.tabName = tabName;
	}
	
    public List<JavaMethodModel> getMethodList()
    {
        return methodList;
    }

    public void setMethodList(List<JavaMethodModel> methodList)
    {
        this.methodList = methodList;
    }

    private String genColName(String name)
    {
    	int i = name.length();
    	String ret = "";
    	for(int j =0;j<i;j++)
    	{
    		int ch = name.charAt(j);
    		if(Character.isUpperCase(ch))
    		{
    			if(j>0)
    			{
    			    ret +="_"+name.substring(j,j+1).toUpperCase();
    			}
    			else
    			{
    				ret += name.substring(j,j+1).toUpperCase();
    			}
    		}
    		else
    		{
    			ret += name.substring(j,j+1).toUpperCase();
    		}
    	}
    	return ret;
    }
 
	public HashMap<String, Object> getZzzReferObj()
	{
		return zzzReferObj;
	}
	public void setZzzReferObj(HashMap<String, Object> zzzReferObj)
	{
		this.zzzReferObj = zzzReferObj;
	}
	public void setPropList(List<PropModel> propList)
	{
		this.propList = propList;
	}
	/*
	public String getIsEntity()
	{
		return isEntity;
	}
	public void setIsEntity(String isEntity)
	{
		this.isEntity = isEntity;
	}
	*/

	public String getPrjPath()
	{
		return prjPath;
	}
	public void setPrjPath(String prjPath)
	{
	    jec.setPrjName(prjPath);
		this.prjPath = prjPath;
	}
	public String getSrcRoot()
	{
		return srcRoot;
	}
	public void setSrcRoot(String srcRoot)
	{
		this.srcRoot = srcRoot;
	}
	/*
	public HashMap getDataStruct()
	{
		return dataStruct;
	}

	public void setDataStruct(HashMap dataStruct)
	{
		this.dataStruct = dataStruct;
	}
	*/
	public String[] getParamFrom()
	{
		return paramFrom;
	}
	public void setParamFrom(String[] paramFrom)
	{
		this.paramFrom = paramFrom;
	}
	public HashMap getAn()
	{
		return an;
	}
	public void setAn(HashMap an)
	{
		this.an = an;
	}
	public String getTemplate()
	{
		return template;
	}
	public void setTemplate(String template)
	{
	    jec.setTemplateId(template);
		this.template = template;
	}

    public String getType()
    {
        return type;
    }
    public void setType(String type)
    {
        jec.setParamFrom(type);
        this.type = type;
    }

    public static class Pkg
    {
    	private String pkgName = "";
        /*
    	private String pkgPrefix = "com.easycode";
        private String pkgPrj = "prj";
        private String pkgDu = "x";
        private String pkgPu = "xx";
        private String pkgCom = "xxx";
        */
        private String[] pkgArray = null;
        private String pkgSource = null;
        
	 
		public String[] getPkgArray()
		{
			return pkgArray;
		}
		public void setPkgArray(String[] pkgArray)
		{
			this.pkgArray = pkgArray;
		}
 
		public String getPkgName()
		{
			return pkgName;
		}
		public Pkg()
		{
		    
		}
	     public Pkg(String pkgName)
	    {
	           this.pkgName = pkgName;
	           this.pkgArray = pkgName.split("\\.");
	    }
	     /*
		public void setPkgName(String pkgName)
		{
			this.pkgName = pkgName;
			this.pkgArray = pkgName.split("\\.");
		}
		*/

    }

	public  Pkg getPkg()
	{
		return pkg;
	}
	public void setPkg(Pkg pkg)
	{
		this.pkg = pkg;
	}
	public List<JavaAn> getJavaAnList() {
		return javaAnList;
	}
	public void setJavaAnList(List<JavaAn> javaAnList) {
		this.javaAnList = javaAnList;
	}
    public JECInfoModel getJec()
    {
        return jec;
    }
    public void setJec(JECInfoModel jec)
    {
        this.jec = jec;
    }
 
 
 
}

