package com.easycode.templatemgr;

import com.easycode.templatemgr.intf.IRpcDS;

public class RpcFactory {
    public static IRpcDS httpSrv(String url)
    {
    	return new HttpRpcDS(url);
    }
}
