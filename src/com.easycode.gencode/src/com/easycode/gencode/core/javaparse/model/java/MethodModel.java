/**
 * 作 者: 
 * 日 期: 2011-8-12
 * 描 叙:
 */
package com.easycode.gencode.core.javaparse.model.java;

import java.util.ArrayList;
import java.util.List;

import com.easycode.common.BaseObject;

/**
 * 功能描叙:
 * 编   码: dltommy
 * 完成时间: 2011-8-12 下午11:01:33
 */
public class MethodModel extends BaseObject
{
	private String accessLimit = null;//"public"; //访问类型修饰,public,private,protected

	//方法名
	private String mothdName = null;
	//是否为静态方法
	private Boolean isStatic = null;//false; //
    //返回值
	private JavaTypeModel retType = null;

	//参数
	private List<PropModel> paramList = new ArrayList<PropModel>();
	
	private String digest = null;
    public String getDigest()
	{
		return digest;
	}
	public void setDigest(String digest)
	{
		this.digest = digest;
	}
	public String getMothdName()
	{
		return mothdName;
	}
	public void setMothdName(String mothdName)
	{
		this.mothdName = mothdName;
	}
	
	public MethodModel()
    {
    	
    }
	public MethodModel(String mothdName)
    {
    	this.mothdName = mothdName;
    }
	public List<PropModel> getParamList()
	{
		return paramList;
	}

	public void setParamList(List<PropModel> paramList)
	{
		this.paramList = paramList;
	}

	public JavaTypeModel getRetType()
	{
		return retType;
	}

	public void setRetType(JavaTypeModel retType)
	{
		this.retType = retType;
	}

	public void addPara(PropModel p)
	{
		this.paramList.add(p);
	}
	

	public String digest()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("public");
		if(this.retType == null)
		{
			sb.append("void");
		}
		else
		{
			sb.append(this.retType.getClzName());
		}
		sb.append(this.mothdName);
		sb.append("(");
		//参数
		if(this.paramList == null || this.paramList.size() == 0)
		{
			sb.append("");
		}
		else
		{
			for(PropModel p:this.paramList)
			{
				sb.append(p.getPropType().getClzName());
				sb.append(",");
			}
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append(")");
		return sb.toString();
	}

	public static void main(String arg[])
	{ 
	    
	}
	public String getAccessLimit()
	{
		return accessLimit;
	}
	public void setAccessLimit(String accessLimit)
	{
		this.accessLimit = accessLimit;
	}
	public Boolean isStatic()
	{
		return isStatic;
	}
	public void setStatic(Boolean isStatic)
	{
		this.isStatic = isStatic;
	}
}

