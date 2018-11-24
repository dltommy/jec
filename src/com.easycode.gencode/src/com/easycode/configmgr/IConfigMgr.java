package com.easycode.configmgr;

import com.easycode.common.XmlUtil;
import com.easycode.configmgr.model.Config;
import com.easycode.configmgr.model.Config.DB;

public interface IConfigMgr {
	
	/**
	 * 存在则直接返回，不存在则新增
	 * @return
	 * @throws Exception
	 */
    public Config readOrCreate(XmlUtil.Callback callback) throws Exception;
    
    /**
     * 
     * @param config
     * @throws Exception
     */
    public void update(Config config, XmlUtil.Callback callback) throws Exception;
    
	public void addOrUpdateDBConfig(DB dbconfig, XmlUtil.Callback callback) throws Exception;
	
	public DB readDBConfig(String dbname) throws Exception;
	
    public void delDBConfig(String id, XmlUtil.Callback callback) throws Exception;
	
}
