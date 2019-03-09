/**
 * 作 者: dltommy
 * 日 期: 2012-2-28
 * 描 叙:
 */
package com.easycode.multlangeditor.action;

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

 
import com.easycode.common.StringUtil;
 
import com.easycode.multlangeditor.editor.Constants;
import com.easycode.multlangeditor.editor.JSPMultiPageEditor;
import com.easycode.multlangeditor.editor.MultLangService;

import com.easycode.resource.MultLang;

/**
 * 功能描叙: 编 码: dltommy 完成时间: 2012-2-28 下午10:10:24
 */
public class AddMultLangAction extends Action
{

    // private Config config = null;
    private IProject project = null;
    // private HashMap<String, String> commMap = null;
    public int maxKey = 0;

    MultLangService multLangService = null;

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
 
        if (part instanceof JSPMultiPageEditor)
        {
            String suff = null;
            FileEditorInput f = (FileEditorInput) part.getEditorInput();
            project = f.getFile().getProject();

            JSPMultiPageEditor jsp = (JSPMultiPageEditor) part;
            HashMap<String, String> commMap = jsp.getCommMap();
            if (commMap == null || commMap.size() == 0)
            {
                jsp.initAllPropText(); 
            }
            multLangService = new MultLangService(project.getName());
            commMap = jsp.getCommMap();
             

            suff = "EC";
            try
            {
                suff = multLangService.getKeyPrefix();
            }
            catch (Exception e3)
            {
                // TODO Auto-generated catch block
                e3.printStackTrace();
            }
            ;


            String multLang = null;
            try
            {
                multLang = multLangService.getMultLangFlag();
            }
            catch (Exception e2)
            {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }

            if (multLang == null)
            {
                multLang = "<s:text name=\"[EC.#key]\"/>";
            }
            int keyB = multLang.indexOf("[");
            int keyE = multLang.indexOf("]");
            String keyMul = multLang.substring(keyB + 1, keyE);

            JSPMultiPageEditor pageEdit = (JSPMultiPageEditor) part;
            TextEditor editor = pageEdit.getEditor();

            IDocument doc = editor.getDocumentProvider().getDocument(
                    editor.getEditorInput());
            PatternMatcherInput fileInput = new PatternMatcherInput(doc.get());

            PatternCompiler pc = new Perl5Compiler();
            PatternMatcher pm = new Perl5Matcher();
            Pattern pattFile = null;
            List<PosObject> posList = new ArrayList<PosObject>();
            int begin = it.getOffset();
            int end = it.getOffset() + it.getText().length();
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
            /*
             * boolean match = false; while (pm.contains(fileInput, pattFile)) {
             * match = true; MatchResult rs = pm.getMatch();
             * 
             * if(rs.group(4) != null) { continue; } PosObject poObj = new
             * PosObject();
             * 
             * int patPos = fileInput.getMatchBeginOffset();
             * 
             * int addPos = patPos +
             * rs.group(1).length()+rs.group(2).length()+rs.group(3).length();
             * poObj.setAppendPos(addPos); poObj.setSrc(rs.group(2));
             * 
             * 
             * 
             * 
             * String mulkey = StringUtil.stringToMD5(rs.group(2).trim());
             * 
             * String key = keyMul.replace("#key", mulkey);
             * 
             * 
             * String multTemp = multLang.replace("["+keyMul+"]", key);
             * 
             * if(commMap != null && commMap.get(rs.group(2).trim()) != null) {
             * } else { }
             * 
             * poObj.setBegMtchPos(patPos); poObj.setSrcBeginPosNoTag(patPos);
             * int srcLenth =
             * rs.group(1).length()+rs.group(2).length()+rs.group(5).length();
             * if(rs.group(3)!=null) { srcLenth += rs.group(3).length();
             * 
             * } if(rs.group(4)!=null) { srcLenth += rs.group(4).length(); }
             * poObj.setSrcLengthNoTag(srcLenth);
             * 
             * poObj.setSrcBeginPos(patPos+rs.group(1).length());
             * posList.add(poObj); pageEdit.addMult(key, rs.group(2).trim()); }
             */

            String txt = it.getText().trim();
            if (!"".equals(txt))
            {
                String matchTxt = txt;
                if (txt.getBytes().length >= Constants.MAX_MD5_SWITCH)
                {
                    matchTxt = Constants.MD5_FLAG + StringUtil.stringToMD5(txt);
                }
                boolean isCommon = false;

                PosObject poObj = new PosObject();
                poObj.setAppendPos(end);
                poObj.setSrc(it.getText());
                String multTemp = null;
                String key = null;
                if (commMap != null && commMap.get(matchTxt) != null)
                {
                    key = commMap.get(matchTxt);

                    multTemp = multLang.replaceAll("\\[[\\s\\S]+?\\]", key);

                    isCommon = true;
                    String js = "{k:'" + key + "',v:'" + it.getText().trim()
                            + "'}";
                    multTemp = multTemp.replace("#value", js);
                    // poObj.setReplace(multTemp+"<%/*{k:'"+key+"',v:'"+it.getText().trim()+"'}*/%>");
                    poObj.setReplace(multTemp);
                }
                else
                {
                    if (keyB > -1 && keyE > -1 && keyE > keyB)
                    {
                        keyMul = multLang.substring(keyB + 1, keyE);
                        maxKey++;
                        String mulkey = maxKey + "";
                        if (mulkey.length() < Constants.MAX_KEY_LENGTH)
                        {
                            int lenth = Constants.MAX_KEY_LENGTH
                                    - mulkey.length();
                            for (int i = 0; i < lenth; i++)
                            {
                                mulkey = "0" + mulkey;
                            }

                        }

                        key = keyMul.replace("#key", mulkey);
                        multTemp = multLang.replace("[" + keyMul + "]", key);
                    }
                    else
                    {
                        String mulkey = StringUtil.stringToMD5(it.getText()
                                .trim());
                        key = keyMul.replace("#key", mulkey);
                        multTemp = multLang.replace("#key", key);
                        // multTemp = multTemp.replace("#prefix", suff);
                    }

                    commMap.put(matchTxt, key);
                }

                String js = "{k:'" + key + "',v:'" + it.getText().trim() + "'}";
                multTemp = multTemp.replace("#value", js);
                // poObj.setReplace(multTemp+"<%/*{k:'"+key+"',v:'"+it.getText().trim()+"'}*/%>");
                poObj.setReplace(multTemp);

                poObj.setBegMtchPos(begin);
                poObj.setSrcBeginPos(begin);

                poObj.setSrcBeginPosNoTag(begin);
                poObj.setSrcLengthNoTag(it.getText().length());

                posList.add(poObj);
                try
                {
                    if (!isCommon)
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
                MessageBox box = new MessageBox(new Shell(),
                        SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
                // 设置对话框的标题
                box.setText("Error");
                // 设置对话框显示的消息
                box.setMessage(MultLang.getMultLang("code.072"));// 请选中多语言词条"
                box.open();
            }

            int addSize = 0;
            for (PosObject p : posList)
            {
                try
                {
                    doc.replace(p.getSrcBeginPosNoTag() + addSize,
                            p.getSrcLengthNoTag(), p.getReplace());
                    addSize += (p.getReplace().length() - p.getSrcLengthNoTag());

                }
                catch (BadLocationException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
            // commMap =null;
        }

    }

}

class PosObject
{
    // 开始插入多语言代码位置，生成的多语言代码中，自动去掉<lang>标签
    private Integer srcBeginPosNoTag = null;
    private Integer srcLengthNoTag = null;
    // 开始插入多语言代码位置
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