/**
 * 作 者: dltommy
 * 日 期: 2012-2-28
 * 描 叙:
 */
package com.easycode.jspeditor.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;

import com.easycode.common.EclipseConsoleUtil;
import com.easycode.common.StringUtil;
import com.easycode.configmgr.ConfigMgrFactory;
import com.easycode.configmgr.model.Config;
import com.easycode.jspeditor.editor.JSPMultiPageEditor;
 
import com.easycode.resource.MultLang;

/**
 * 功能描叙:
 * 编   码: dltommy
 * 完成时间: 2012-2-28 下午10:10:24
 */
public class AddMultLangAction extends Action
{

	private Config config = null; 
	private IProject project = null;
	private HashMap<String, String> commMap = null;
	private int maxKey = 0;
	private String suff = "EC";
	private final static int MAX_KEY_LENGTH = 5;
	private final static int MAX_MD5_SWITCH = 50;
	
	private final static int MAX_RES_SIZE = 10000;

	private final static String MD5_FLAG = "{MD5}";
	private final static int MD5_LENGTH = MD5_FLAG.length()+32;
	
	private String readKeyFromCommProp(String txt)
    {
        if (txt == null) { return null; }
        if (config.getCommonLangProp() != null
                && !"".equalsIgnoreCase(config.getCommonLangProp().trim()))
        {
            String common[] = config.getCommonLangProp()
                    .split(";");

            for (String t : common)
            {
                if (project.getFile(t).exists())
                {
                    try
                    {
                        PropertyResourceBundle p = new PropertyResourceBundle(
                                project.getFile(t).getContents());
                        Enumeration<String> enu = p.getKeys();

                        while (enu.hasMoreElements())
                        {
                            String temp = enu.nextElement();
                            if (p.getString(temp.trim()).equals(txt.trim())) { return temp; }
                        }

                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                else
                {
                    MessageBox box = new MessageBox(new Shell(),
                            SWT.APPLICATION_MODAL | SWT.ICON_WARNING);
                    // 设置对话框的标题
                    box.setText("WARNING");
                    // 设置对话框显示的消息
                    box.setMessage(MultLang.getMultLang("code.084") + t);// "找不到公用资源文件"

                    box.open();

                    // return;
                }
            }
        }
        return null;
    }
	public void initCommMap()
	{
		commMap = new HashMap<String, String>();
		if(config.getCommonLangProp() != null && !"".equalsIgnoreCase(config.getCommonLangProp().trim()))
		{
			String common[] = config.getCommonLangProp().split(";");
            String common2[] = config.getLangProp().split(";");

			for(String t:common)
			{
				if(project.getFile(t).exists())
				{
					try
					{
						PropertyResourceBundle p = new PropertyResourceBundle(project.getFile(t).getContents());
						Enumeration<String> enu = p.getKeys();

						int count =0;
						while(enu.hasMoreElements())
						{
							 //map中最多存放10000条记录
							 if(++count > MAX_RES_SIZE)
							 {
								 EclipseConsoleUtil.printToConsole("警告：资源文件记录超过最大限度，自动忽略"+MAX_RES_SIZE+"以后的记录。", false);
								 break;
							 }
							 
							 
							String key = enu.nextElement();
							if(key.matches(suff+"\\.[0-9]+"))
							{
								int tempKey = Integer.parseInt(key.replace(suff+".", ""));
								if(maxKey < tempKey)
								{
									maxKey = tempKey;
								}
							}
							String txt = p.getString(key);
							 if(txt.getBytes().length>=MAX_MD5_SWITCH)
							 {
								 commMap.put(MD5_FLAG+StringUtil.stringToMD5(txt), key);
							 }
							 else
							 {
								 commMap.put(txt, key);
							 }
						}
						
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					MessageBox box = new MessageBox( new Shell() ,SWT.APPLICATION_MODAL | SWT.ICON_WARNING);
				     //设置对话框的标题
				     box.setText("WARNING");
				     //设置对话框显示的消息
				     box.setMessage(MultLang.getMultLang("code.084")+t);//"找不到公用资源文件"
				     
				     box.open();
				     
				     //return;
				}
				
			}

            for(String t:common2)
            {
                if(project.getFile(t).exists())
                {
                    try
                    {
                        PropertyResourceBundle p = new PropertyResourceBundle(project.getFile(t).getContents());
                        Enumeration<String> enu = p.getKeys();

                        while(enu.hasMoreElements())
                        {
                            String key = enu.nextElement();
                            commMap.put(p.getString(key), key);
                        }
                         
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                else
                {
                    continue;
                }
                
            }
		}
	}
	public void run() 
	{

		IWorkbench workbench = PlatformUI.getWorkbench();
		// 取得工作台窗口
		IWorkbenchWindow wind = workbench.getActiveWorkbenchWindow();
		
		// 取得工作台页面
		IWorkbenchPage page = wind.getActivePage();
		IEditorPart part = page.getActiveEditor();
		ITextSelection it = (TextSelection) (part.getSite()
				.getSelectionProvider().getSelection());
		
		String regProp = "(<\\s*lang\\s*>)([\\s|\\S]*?)([\\s]*?)(<%/\\*[\\s|\\S]*?\\*/%>)?([\\s]*</\\s*lang\\s*>)";
		                  
		//解析txt中包含的属性

		//System.err.println(part.getClass());
		if(part instanceof JSPMultiPageEditor)
		{

			
			FileEditorInput f = (FileEditorInput) part.getEditorInput();
			project = f.getFile().getProject();
			String projectPath = f.getFile().getProject().getLocation().toFile()
			.getPath();
			  suff = "EC";
			String[] tempPath = f.getFile().getProjectRelativePath().toString().split("/");
			if(tempPath.length > 3)
			{
				suff = tempPath[2]+"";
			}
			else if(tempPath.length > 2)
			{
				suff = tempPath[1]+"";
			}
			//System.err.println("工程路径:"+projectPath);
			//prjConfig = ConfigMgr.newConfigByPrjPath(projectPath);
			try {
				config = ConfigMgrFactory.newByPrjPath(projectPath).readOrCreate(null);
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			String multLang = null;
			if(config != null)
			{
				multLang = config.getMultLangFlag();
 
			}
			else
			{
				return;
			}
			if(multLang == null)
			{
				multLang ="<s:text name=\"[#prefix.#key]\"/>";
			}
			
			if(commMap == null)
			{
				//System.err.println("--------------------------初始commLang");
				initCommMap();
			}
			
			JSPMultiPageEditor pageEdit = (JSPMultiPageEditor) part;
			TextEditor editor = pageEdit.getEditor();
			
			IDocument doc = editor.getDocumentProvider().getDocument(
					editor.getEditorInput());
			PatternMatcherInput fileInput = new PatternMatcherInput(doc.get());
			
			
			PatternCompiler pc = new Perl5Compiler();
			PatternMatcher pm = new Perl5Matcher();
			Pattern pattFile = null;
			List<PosObject> posList = new ArrayList<PosObject>();
			//System.err.println("选择开始位置:" + it.getOffset());
			int begin = it.getOffset();
			int end = it.getOffset() + it.getText().length();
			//System.err.println("选择结束位置:"
			//		+ (it.getOffset() + it.getText().length()));
			try
			{
				pattFile = pc.compile(regProp,
						Perl5Compiler.CASE_INSENSITIVE_MASK);
			}
			catch (MalformedPatternException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			fileInput.setBeginOffset(begin);
			fileInput.setCurrentOffset(begin);

			fileInput.setEndOffset(end);
			boolean match = false;
			while (pm.contains(fileInput, pattFile))
			{
				match = true;
				MatchResult rs = pm.getMatch();
				
				if(rs.group(4) != null)//说明已经多语言
				{
					continue;
				}
				PosObject poObj = new PosObject();

				int patPos = fileInput.getMatchBeginOffset();

				int addPos = patPos + rs.group(1).length()+rs.group(2).length()+rs.group(3).length();
				poObj.setAppendPos(addPos);
				poObj.setSrc(rs.group(2));
 
				int keyB = multLang.indexOf("[");
				int keyE = multLang.indexOf("]");
				String keyMul = multLang.substring(keyB+1, keyE);
				 
 
				String mulkey = StringUtil.stringToMD5(rs.group(2).trim());
				String key = keyMul.replace("#prefix", suff);
				key = key.replace("#key", mulkey);
 
 
				String multTemp = multLang.replace("["+keyMul+"]", key);
				
				if(commMap != null && commMap.get(rs.group(2).trim()) != null)
				{
					poObj.setReplace(multTemp+"<%/*{k:'"+commMap.get(rs.group(2).trim())+"',v:'"+rs.group(2).trim()+"'}*/%>");
				}
				else
				{
					poObj.setReplace(multTemp+"<%/*{k:'"+key+"',v:'"+rs.group(2).trim()+"'}*/%>");
				}

				poObj.setBegMtchPos(patPos);
				poObj.setSrcBeginPosNoTag(patPos);
				int srcLenth = rs.group(1).length()+rs.group(2).length()+rs.group(5).length();
				if(rs.group(3)!=null)
				{
					srcLenth += rs.group(3).length();
					
				}
				if(rs.group(4)!=null)
				{
					srcLenth += rs.group(4).length();
				}
				poObj.setSrcLengthNoTag(srcLenth);

				poObj.setSrcBeginPos(patPos+rs.group(1).length());
				posList.add(poObj);
				pageEdit.addMult(key, rs.group(2).trim());
			}

			if(!match)
			{
				String txt = it.getText().trim();
				if (!"".equals(txt))
				{
					String matchTxt = txt;
					if(txt.getBytes().length>=MAX_MD5_SWITCH)
					{
						System.err.println("长度超过50,通过md5匹配");
						matchTxt = MD5_FLAG+ StringUtil.stringToMD5(txt);
					}
					boolean isCommon = false;

					PosObject poObj = new PosObject();
					poObj.setAppendPos(end);
					poObj.setSrc(it.getText());
					String multTemp = null;
					String key = null;
					//System.err.println("cMap"+commMap);
					if(commMap != null && commMap.get(matchTxt) != null)
					{
						key = commMap.get(matchTxt);
					    //String mulkey = commMap.get(it.getText().trim());
					    //multTemp = multLang.replaceAll("#key", key);
					    
					    //String keyMul = "#prefix.#key";
					    multTemp = multLang.replaceAll("\\[[\\s\\S]+?\\]", key);
					    
                        //int keyB = multLang.indexOf("[");
                        //int keyE = multLang.indexOf("]");
                        //if(keyB>-1 && keyE>-1 && keyE>keyB)
                        {
                            //keyMul = multLang.substring(keyB+1, keyE);
                            //String mulkey = StringUtil.stringToMD5(it.getText().trim());
                            //key = keyMul.replace("#prefix", suff);
                            //key = key.replace("#key", mulkey);
                            //multTemp = multLang.replace("["+keyMul+"]", key);
                        }
                        //else
                        {
                            //String mulkey = StringUtil.stringToMD5(it.getText().trim());
                            //key = keyMul.replace("#prefix", suff);
                            //key = key.replace("#key", mulkey);
                            //multTemp = multLang.replace("#prefix", suff);
                            //multTemp = multTemp.replace("#key", key);
                            
                        }
                          

					    

					    
					    
					    isCommon = true;
					    String js = "{k:'"+key+"',v:'"+it.getText().trim()+"'}";
					    multTemp = multTemp.replace("#value", js);
						//poObj.setReplace(multTemp+"<%/*{k:'"+key+"',v:'"+it.getText().trim()+"'}*/%>");
					    poObj.setReplace(multTemp);
					}
					else
					{
					        String keyMul = "#prefix.#key";
			                int keyB = multLang.indexOf("[");
			                int keyE = multLang.indexOf("]");
	                        if(keyB>-1 && keyE>-1 && keyE>keyB)
	                        {
	                            keyMul = multLang.substring(keyB+1, keyE);
	                            //String mulkey = StringUtil.stringToMD5(it.getText().trim());
	                            maxKey++;
	                            String mulkey = maxKey+"";
	                            if(mulkey.length()<MAX_KEY_LENGTH)
	                            {
	                            	int lenth = MAX_KEY_LENGTH-mulkey.length();
	                                for(int i = 0;i<lenth;i++)
		                            {
	                                	mulkey ="0"+mulkey;
		                            }
	                            	 
	                            }
	                             
	                      
	                            key = keyMul.replace("#prefix", suff);
	                            key = key.replace("#key", mulkey);
	                            multTemp = multLang.replace("["+keyMul+"]", key);                             
                                //multTemp = multLang.replace("#prefix", suff);
	                        }
	                        else
	                        {
	                            String mulkey = StringUtil.stringToMD5(it.getText().trim());
	                            key = keyMul.replace("#prefix", suff);
	                            key = key.replace("#key", mulkey);
	                            multTemp = multLang.replace("#key", key);
	                            multTemp = multTemp.replace("#prefix", suff);
	                        }
			                
 
	                        commMap.put(matchTxt, key);
						//poObj.setReplace(multTemp+"<%/*{k:'"+key+"',v:'"+it.getText().trim()+"'}*/%>");
					}
					
					
					//poObj.setReplace(multTemp + "<%/*{k:'" + key + "',v:'"
					//		+ it.getText().trim() + "'}*/%>");

					
				    String js = "{k:'"+key+"',v:'"+it.getText().trim()+"'}";
				    multTemp = multTemp.replace("#value", js);
					//poObj.setReplace(multTemp+"<%/*{k:'"+key+"',v:'"+it.getText().trim()+"'}*/%>");
				    poObj.setReplace(multTemp);
				    
					
					poObj.setBegMtchPos(begin);
					poObj.setSrcBeginPos(begin);
					
					
			        poObj.setSrcBeginPosNoTag(begin);
					poObj.setSrcLengthNoTag(it.getText().length());
					
					
					posList.add(poObj);
					try
					{
						if(!isCommon)
						{
							 
						    pageEdit.addMult(key, txt);
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					MessageBox box = new MessageBox( new Shell() ,SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
				     //设置对话框的标题
				     box.setText("Error");
				     //设置对话框显示的消息
				     box.setMessage(MultLang.getMultLang("code.072"));//请选中多语言词条"
				     box.open();
				}
			}
			int addSize = 0;
			for (PosObject p : posList)
			{
				try
				{
					//doc.replace(p.getSrcBeginPos()+addSize, p.getSrc().length(), p.getReplace());
					//addSize += (p.getReplace().length()-p.getSrc().length());
					//System.err.println("替换位置: "+(p.getSrcBeginPosNoTag()+addSize)+" \n长度:"+p.getSrcLengthNoTag()+"\n内容:"+p.getReplace());
					doc.replace(p.getSrcBeginPosNoTag()+addSize, p.getSrcLengthNoTag(), p.getReplace());
					addSize += (p.getReplace().length()-p.getSrcLengthNoTag());
					
				}
				catch (BadLocationException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			//commMap =null;
		}
		
	}
	public HashMap<String, String> getCommMap()
	{
		return commMap;
	}
	public void setCommMap(HashMap<String, String> commMap)
	{
		this.commMap = commMap;
	}

}
 class PosObject
{
	 //开始插入多语言代码位置，生成的多语言代码中，自动去掉<lang>标签
	 private Integer srcBeginPosNoTag = null;
	 private Integer srcLengthNoTag = null;
	 //开始插入多语言代码位置
	private Integer srcBeginPos = null;
	private String src = null;
	private String replace = null;
	private Integer begMtchPos = 0;
	private Integer appendPos = 0;
	public Integer getAppendPos()
	{
		return appendPos;
	}
	public void setAppendPos(Integer appendPos)
	{
		this.appendPos = appendPos;
	}
	public Integer getBegMtchPos()
	{
		return begMtchPos;
	}
	public void setBegMtchPos(Integer begMtchPos)
	{
		this.begMtchPos = begMtchPos;
	}
	public String getReplace()
	{
		return replace;
	}
	public void setReplace(String replace)
	{
		this.replace = replace;
	}
	public String getSrc()
	{
		return src;
	}
	public void setSrc(String src)
	{
		this.src = src;
	}
	public Integer getSrcBeginPos()
	{
		return srcBeginPos;
	}
	public void setSrcBeginPos(Integer srcBeginPos)
	{
		this.srcBeginPos = srcBeginPos;
	}
	public Integer getSrcBeginPosNoTag()
	{
		return srcBeginPosNoTag;
	}
	public void setSrcBeginPosNoTag(Integer srcBeginPosNoTag)
	{
		this.srcBeginPosNoTag = srcBeginPosNoTag;
	}
	public Integer getSrcLengthNoTag()
	{
		return srcLengthNoTag;
	}
	public void setSrcLengthNoTag(Integer srcLengthNoTag)
	{
		this.srcLengthNoTag = srcLengthNoTag;
	}
}