package com.easycode.templatemgr;


import com.easycode.templatemgr.model.CodegenTemplate;
import com.easycode.templatemgr.model.PageData;


public interface ITemplateMgr {
	

	/**
	 * 依照模板ID删除一个模板
	 * @param templateid 模板ID
	 * @throws Exception
	 */
	public void deleteTemplate(String templateid) throws Exception;
	
	/**
	 * 更新模板
	 * @param templateid
	 * @param author
	 * @param mudCtx
	 * @param title
	 * @param paras
	 * @param codeType
	 * @param anno
	 * @return
	 * @throws Exception
	 */
	public String updateTemplate(String templateid,  String mudCtx, String title,
			String paras, String codeType, String anno) throws Exception ;
	
	/**
	 * 新增加一个模板 
	 * @param author
	 * @param mudCtx
	 * @param title
	 * @param paras
	 * @param codeType
	 * @param anno
	 * @return
	 * @throws Exception
	 */
	public String addTemplate(String author, String mudCtx, String title,
			String paras, String codeType, String anno) throws Exception;
	
	/**
	 * 将服务器上的模板保存到本地
	 * @param serverTemplateId
	 * @param author
	 * @param templateCtx
	 * @param title
	 * @param paras
	 * @param codeType
	 * @param version
	 * @param referId
	 * @param anno
	 * @return
	 * @throws Exception
	 */
	public String serverToLocal(String serverTemplateId, String author, String templateCtx,
			String title, String paras, String codeType, String version,
			  String anno) throws Exception;
	
	/**
	 * 通过templateId查询一个指定的模板 
	 * @param templateId
	 * @param withDetail
	 * @return
	 * @throws Exception
	 */
	public CodegenTemplate getCodegenTemplate(String templateId, boolean withDetail) throws Exception;

	/**
	 * 
	 * @param author
	 * @param templateId
	 * @param title
	 * @param mudCtx
	 * @param codeType
	 * @param beginPos
	 * @param eachPageSize
	 * @return
	 * @throws Exception
	 */
	public PageData<CodegenTemplate> queryPageList(String author,
			String templateId, String title, String mudCtx, String codeType,
			int beginPos, int eachPageSize) throws Exception;
	 
	 
}
