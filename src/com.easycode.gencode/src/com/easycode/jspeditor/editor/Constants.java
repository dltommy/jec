package com.easycode.jspeditor.editor;

public class Constants
{
    public final static int MAX_RES_SIZE = 10000;
    public final static String DEFAULT_SUFF = "EC";
    public final static String MD5_FLAG = "{MD5}";
    public final static int MAX_KEY_LENGTH = 5;
    public final static int MAX_MD5_SWITCH = 50;
    public final static int MD5_LENGTH = MD5_FLAG.length()+32;
    
    public final static String MULT_USE_MEMO="1：多语言默认生成界面标签为<s:text name=\"[EC.#key]\"/><%/*#value*/%>，#key自动生成，长度为5位。\n"
            +"2：当第一次按下CTRL+Q时，自动加载所有的资源文件到内存，词条生成时自动从内存查找该词条是否已经存在过，如果存在，则key从资源文件获取，否则自动生成。"
            +"警惕资源文件词条过多时，导致内存占用过大，所以最好不要同时开启多个页面做提取操作，应该提取完一个页面后，及时关闭掉，再打开另一个页面。";
}
