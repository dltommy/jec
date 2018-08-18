/**
 * 作 者: dltommy
 * 日 期: 2011-9-4
 * 描 叙:
 */
package com.easycode.templatemgr.intf;

import java.util.List;

import com.easycode.templatemgr.model.CodegenMudls;
import com.easycode.templatemgr.model.CodegenTemplate;

/**
 * 功能描叙:
 * 编   码: dltommy
 * 完成时间: 2011-9-4 下午05:36:09
 */
public interface IRpcDS
{

	/**
	 * 
	 * 功   能:
	 * 实现流程:
	 * @param id
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	public String regCodegenSeed(String id, String pwd) throws Exception;
	
	/**
	 * 查询CodegenMudls对象
	 * 功   能:
	 * 实现流程:
	 * @param mudId
	 * @return
	 */
	public CodegenTemplate getCodegenMudlsByMudId(String mudId)  throws Exception;
	
	/**
	 * 
	 * 功   能:
	 * 实现流程:
	 * @param seedId
	 * @param bookmark
	 * @param codeType
	 * @param beginPos
	 * @param eachPageSize
	 * @return
	 */
	public List<String> queryCodegenMudlsPageList2(String seedId,String mdlId, String bookmark,String mdlCtx, String codeType, int beginPos, int eachPageSize)  throws Exception;
    /**
     * 
     * 功   能:
     * 实现流程:
     * @return
     * @throws Exception
     */
	//public String[] newCodegenSeed() throws Exception;
	/**
	 * 保存模板内容
	 * 功   能:
	 * 实现流程:
	 * @param version
	 * @param ctx
	 * @param seed
	 * @return
	 */
   public String addCodegenMudls(String seed,String pwd, String mdlId, String bookMark, String mdlCtx, String paramDesc, String codeType, String mdId, String annoText)  throws Exception;
   
   /**
    * 删除模板
    * 功   能:
    * 实现流程:
    * @param seed
    * @param pwd
    * @param bookMark
    * @param mdlCtx
    * @return
    */
   public String deleteCodegenMudls(String seed,String pwd, String mdlId)  throws Exception;
   
   /**
    * 更新模板
    * 功   能:
    * 实现流程:
    * @param seed
    * @param pwd
    * @param mdlId
    * @return
    */
   public String updateCodegenMudls(String myseedId, String myPwd, String mdId, String bookMark, String mdlCtx, String paramDesc, String codeType, String annoText)  throws Exception;
   
   /**
    * 
    * 功   能:
    * 实现流程:
    * @param version
    * @param ctx
    * @param seed
    * @return
    */
  // public List<String> getBookmarkListBySeed(String seed)  throws Exception;
   
   /**
    * 
    * 功   能:
    * 实现流程:
    * @param bookmarkId
    * @return
    */
   //public String getBookmarkById(String bookmarkId)  throws Exception;
   
   /**
    * 
    * 功   能:
    * 实现流程:
    * @param seed
    * @param pwd
    * @return
    */
   //public String deleteCodegenSeed(String seed,String pwd)  throws Exception;

}

