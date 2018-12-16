/**
 * 作 者: dltommy
 * 日 期: 2011-8-28
 * 描 叙:
 */
package com.easycode.templatemgr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
 
import com.easycode.common.HttpUtil;
import com.easycode.common.HttpUtil.FormItem;
import com.easycode.templatemgr.model.CodegenMudls;
import com.easycode.templatemgr.model.CodegenTemplate;
import com.easycode.templatemgr.util.ZipUtil;
import com.easycode.templatemgr.intf.IRpcDS;

/**
 * 功能描叙: 编 码: dltommy 完成时间: 2011-8-28 下午11:28:10
 */
public class HttpRpcDS implements IRpcDS
{  
    String NAME_SPACE = "";
    private String url = null; 
 
   protected HttpRpcDS(String url)
   { 
	   this.url = url;
	   
   }
   public String checkCodegenSeed(String userName, String password) throws Exception
   { 
       List<FormItem> blist = new ArrayList<FormItem>();
       blist.add(new FormItem( "method","checkCodegenSeed"));
       blist.add(new FormItem( "userName",userName));
       blist.add(new FormItem( "password", password));
       return new String(HttpUtil.httpPost(url, blist));
   }
   
	/**
	 * 
	 * 功   能:
	 * 实现流程:
	 * @param id
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	public String regCodegenSeed(String userName, String password) throws Exception
	{ 
		List<FormItem> blist = new ArrayList<FormItem>();
		blist.add(new FormItem( "method","regCodegenSeed"));
		blist.add(new FormItem( "userName",userName));
		blist.add(new FormItem( "password", password));
        return new String(HttpUtil.httpPost(url, blist));
	}
	
	public CodegenTemplate getCodegenMudlsByMudId(String opter, String mudId) throws Exception
	{ 
		 
		List<FormItem> blist = new ArrayList<FormItem>();
		                                   
		blist.add(new FormItem( "method","getCodegenMudlsByMudId"));
		blist.add(new FormItem( "templateId",mudId));
		blist.add(new FormItem( "opter",opter));
		
		byte[] ret = HttpUtil.httpPost(url, blist);
		Map<String,String> resMap = ZipUtil.unzipItems(ret);
		String ctx = resMap.get("templateCtx");
		String jsonStr = resMap.get("template");
		JSONObject js = JSONObject.fromObject(jsonStr);
		
		CodegenTemplate retObj = (CodegenTemplate)JSONObject.toBean(js,CodegenTemplate.class);
		retObj.setTemplateCtx(ctx);
	 
		return retObj;
	}
	
	public String deleteCodegenMudls(String author, String pwd, String templateId)
			throws Exception
	{ 
		List<FormItem> blist = new ArrayList<FormItem>();
        
		blist.add(new FormItem( "method","deleteCodegenMudls"));
		blist.add(new FormItem( "templateId",templateId));
		blist.add(new FormItem( "author",author));
		blist.add(new FormItem( "password",pwd));
		byte[] ret = HttpUtil.httpPost(url, blist);
		return new String(ret);
	}

	public String addCodegenMudls(String author, String pwd,String templateId, String title,
			String templateCtx, String paramDesc, String codeType, String referId, String templateAnnoText) throws Exception
	{ 
		
		CodegenTemplate t = new CodegenTemplate();
		
		t.setAuthor(author);
		t.setId(templateId);
		t.setTitle(title);
		t.setParamDesc(paramDesc);
		t.setCodeType(codeType);
		t.setTemplateAnno(templateAnnoText);
		 
		JSONObject obj = JSONObject.fromObject(t);
		
		
		List<FormItem> blist = new ArrayList<FormItem>();
		blist.add(new FormItem( "method","addCodegenMudls")); 
		blist.add(new FormItem( "password",pwd));
		
	    ZipUtil zip = new ZipUtil();
	    
	    zip.addZip("info", obj.toString().getBytes("utf-8"));
	    zip.addZip("ctx", templateCtx.getBytes("utf-8"));
	    zip.finish();
	    
		blist.add(new FormItem( "template",zip.getCompressedByte(),false));
        return new String(HttpUtil.httpPost(url, blist));
		
		
		
		 
	}

	public String updateCodegenMudls(String author, String pwd,
			String templateId, String title, String templateCtx, String paramDesc,
			String codeType, String templateAnnoText) throws Exception
	{ 
 
		
		CodegenTemplate t = new CodegenTemplate();
		
		t.setAuthor(author);
		t.setId(templateId);
		t.setTitle(title);
		t.setParamDesc(paramDesc);
		t.setCodeType(codeType);
		t.setTemplateAnno(templateAnnoText);
		 
		JSONObject obj = JSONObject.fromObject(t);
		
		
		List<FormItem> blist = new ArrayList<FormItem>();
		blist.add(new FormItem( "method","updateCodegenMudls")); 
		blist.add(new FormItem( "password",pwd));
		
	    ZipUtil zip = new ZipUtil();
	    
	    zip.addZip("info", obj.toString().getBytes("utf-8"));
	    zip.addZip("ctx", templateCtx.getBytes("utf-8"));
	    zip.finish();
	    
		blist.add(new FormItem( "template",zip.getCompressedByte(),false));
        return new String(HttpUtil.httpPost(url, blist));

	}

	public static void main(String arg[])
	{
		// RpcDS.getInstance().test();
	}

	public List<String> queryCodegenMudlsPageList2(String opter, String author, String templateId,String title,String templateCtx, 
			String codeType, int beginPos, int eachPageSize) throws Exception
	{ 
		JSONObject obj = new JSONObject();
		obj.put("opter", opter);
		obj.put("author", author);
		obj.put("templateId", templateId);
		obj.put("title", title);
		obj.put("codeType", codeType);
		obj.put("beginPos", beginPos);
		obj.put("eachPageSize", eachPageSize);
		
		List<FormItem> blist = new ArrayList<FormItem>();
		blist.add(new FormItem("method","queryCodegenMudlsPageList")); 
	    ZipUtil zip = new ZipUtil();
	    zip.addZip("info", obj.toString().getBytes("utf-8"));
	    zip.finish();
		blist.add(new FormItem( "queryParams",zip.getCompressedByte(),false));
        String ret = new String(HttpUtil.httpPost(url, blist),"utf-8");
        JSONArray pageData = JSONArray.fromObject(JSONArray.fromObject(ret));
        return pageData;
	}
 
}
