package com.easycode.gencode.javaeditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.PatternMatcherInput;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
 

import com.easycode.gencode.service.ConfigParseUtil;
import com.easycode.gencode.service.ConfigParseUtil.DefaultAnno;
import com.easycode.javaparse.JavaSrcParse;
import com.easycode.javaparse.model.AnnoMethodModel;
import com.easycode.javaparse.model.AnnoPropModel;
import com.easycode.javaparse.model.IAnnoModel;
import com.easycode.resource.MultLang;

 

/**
 * Manages the installation/deinstallation of global actions for multi-page editors.
 * Responsible for the redirection of global actions to the active editor.
 * Multi-page contributor replaces the contributors for the individual editors in the multi-page editor.
 */
public class GenJavaCodeEditorContributor extends MultiPageEditorActionBarContributor {
	private IEditorPart activeEditorPart;
	private Action sampleAction;
	
	//private Action addClsAnnoAction;
	
	/**
	 * Creates a multi-page contributor.
	 */
	public GenJavaCodeEditorContributor() {
		super();
		createActions();
	}
	/**
	 * Returns the action registed with the given text editor.
	 * @return IAction or null if editor is null.
	 */
	protected IAction getAction(ITextEditor editor, String actionID) {
		return (editor == null ? null : editor.getAction(actionID));
	}
	/* (non-JavaDoc)
	 * Method declared in AbstractMultiPageEditorActionBarContributor.
	 */

	public void setActivePage(IEditorPart part) {
		
		if (activeEditorPart == part)
			return;

		activeEditorPart = part;

		IActionBars actionBars = getActionBars();
		if (actionBars != null) {

			ITextEditor editor = (part instanceof ITextEditor) ? (ITextEditor) part : null;

			actionBars.setGlobalActionHandler(
				ActionFactory.DELETE.getId(),
				getAction(editor, ITextEditorActionConstants.DELETE));
			actionBars.setGlobalActionHandler(
				ActionFactory.UNDO.getId(),
				getAction(editor, ITextEditorActionConstants.UNDO));
			actionBars.setGlobalActionHandler(
				ActionFactory.REDO.getId(),
				getAction(editor, ITextEditorActionConstants.REDO));
			actionBars.setGlobalActionHandler(
				ActionFactory.CUT.getId(),
				getAction(editor, ITextEditorActionConstants.CUT));
			actionBars.setGlobalActionHandler(
				ActionFactory.COPY.getId(),
				getAction(editor, ITextEditorActionConstants.COPY));
			actionBars.setGlobalActionHandler(
				ActionFactory.PASTE.getId(),
				getAction(editor, ITextEditorActionConstants.PASTE));
			actionBars.setGlobalActionHandler(
				ActionFactory.SELECT_ALL.getId(),
				getAction(editor, ITextEditorActionConstants.SELECT_ALL));
			actionBars.setGlobalActionHandler(
				ActionFactory.FIND.getId(),
				getAction(editor, ITextEditorActionConstants.FIND));
			actionBars.setGlobalActionHandler(
				IDEActionFactory.BOOKMARK.getId(),
				getAction(editor, IDEActionFactory.BOOKMARK.getId()));
			actionBars.updateActionBars();
		}
	}
	
	
	private void createActions() {
		sampleAction = new Action() {
			public void run() {
				
				IWorkbench workbench = PlatformUI.getWorkbench();
				// 取得工作台窗口
				IWorkbenchWindow wind = workbench.getActiveWorkbenchWindow();
				// 取得工作台页面
				IWorkbenchPage page = wind.getActivePage();
				// 取得当前处于活动状态的编辑器窗口
				IEditorPart part = page.getActiveEditor();
			
				GenJavaCodeEditor javaEditor = (GenJavaCodeEditor)part;
				String annoCtxt = javaEditor.getCurAnnoText();
				String template = javaEditor.getCurMdlId();
				
				if(template == null || "".equals(template))
				{
					MessageBox box = new MessageBox( new Shell() ,SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
				     //设置对话框的标题
				     box.setText("Error");
				     //设置对话框显示的消息
				     box.setMessage(MultLang.getMultLang("code.086"));//请先选择一个模板"
				     box.open();
					return;
				}
				ITextSelection it = (TextSelection) (part.getSite()
						.getSelectionProvider().getSelection());
				//int begLine = it.getEndLine();
				String txt = it.getText();
                int selectBegin = it.getOffset();
				int selectEnd = selectBegin+it.getLength();
				//解析txt中包含的属性
				PatternCompiler pc = new Perl5Compiler();
				PatternMatcher pm = new Perl5Matcher();

			
				
		 
				
				if(part instanceof GenJavaCodeEditor)
				{
					
					GenJavaCodeEditor e = (GenJavaCodeEditor)part;
					//JCEditor edit = e.editor;
					CompilationUnitEditor edit = e.editor;
				
					
					
					
					 
					FileEditorInput f = (FileEditorInput) edit.getEditorInput();
                    ICompilationUnit compUnit = JavaCore.createCompilationUnitFrom(f.getFile());
                    JavaSrcParse parse = new JavaSrcParse(compUnit);
                    //System.err.println("鼠标选择:"+selectBegin+","+selectEnd);
                    List<IAnnoModel> modelList = parse.getAnnoModelByRange(selectBegin, selectEnd);
                    
                    if(modelList != null && modelList.size()>0)
                    {
                        //System.err.println("选择对象:"+modelList.size());
                    }
                    else
                    {
                        //System.err.println("未选中任何对象");
                    }
                    

                    
					List<DefaultAnno> annoList = ConfigParseUtil.parse(annoCtxt,javaEditor.getSrcJson());

					if(template != null)
					{
						annoList.add(new DefaultAnno(DefaultAnno.PROP_ANNO,"template=\""+template+"\"",",.*,"));
					}
					
					IDocument t = edit.getDocumentProvider().getDocument(e.getEditorInput());
					
					PatternMatcherInput fileInput = new PatternMatcherInput(t.get());

					//list存放添加注释的位置
					List<Integer> list = new ArrayList<Integer>();
					//Map存放添加注释的内容
	                HashMap<Integer,String> keepStrMap = new HashMap<Integer,String>();
					int begin = it.getOffset();
					int end = it.getOffset()+it.getText().length();
 
					fileInput.setBeginOffset(begin);
					fileInput.setCurrentOffset(begin);
					
					fileInput.setEndOffset(end);
					 
					//String isPk = "N";
					//String notAdd = "N";

					String blankCode = "    ";
                    HashMap<String, String> annoMap = new HashMap<String,String>();
					for (IAnnoModel m:modelList)
                    {
                        if (m.getType().equals(IAnnoModel.PROP))
                        {
                            AnnoPropModel anno = (AnnoPropModel) m;
                            annoMap = ConfigParseUtil.parsePropAnno(annoList,
                                    anno.getPropModel());
                        }
                         
                        else if (m.getType().equals(IAnnoModel.METHOD))
                        {
                            AnnoMethodModel anno = (AnnoMethodModel) m;
                            annoMap = ConfigParseUtil.parseMethodAnno(annoList,
                                    anno.getMethod());
                            
                        }
                        
                        else if (m.getType().equals(IAnnoModel.CLS))
                        {
                            blankCode = "";
                            annoMap = ConfigParseUtil.parseClsAnno(annoList);

                        }
                        //System.err.println("注释:"+annoMap);
                        StringBuffer sb = new StringBuffer();
                        Iterator<Entry<String, String>> enIt = annoMap
                                .entrySet().iterator();

                        StringBuffer anSb = new StringBuffer();



                        while (enIt.hasNext())
                        {
                            String value = enIt.next().getValue().trim();
                            if (value.endsWith(","))
                            {
                                sb.append("\n" + blankCode + " * " + value);
                            }
                            else
                            {
                                sb.append("\n" + blankCode + " * " + value + ",");
                            }
                        }

                        int pos = m.getCodeRange().getBegin();
                        if (m.getAnnoRange() == null)
                        {
                            anSb.append("\n" + blankCode + "/**\n" + blankCode
                                    + " * <EasyCode>{");
                        }
                        else
                        {
                            anSb.append("\n" + blankCode + " * <EasyCode>{");
                            pos = m.getAnnoRange().getBegin() + 3;
                        }

                        if (sb.length() > 0)
                        {
                            if (sb.toString().endsWith(","))
                            {
                                anSb.append(sb.toString(), 0, sb.length() - 1);
                            }
                            else
                            {
                                anSb.append(sb.toString());
                            }
                        }
                        if (m.getAnnoRange() == null)
                        {
                            anSb.append("\n" + blankCode + " * }</EasyCode>\n"
                                    + blankCode + " */\n" + blankCode);
                        }
                        else
                        {
                            anSb.append("\n" + blankCode + " * }</EasyCode>\n"
                                    + blankCode + " *" + blankCode);
                        }

 
                        list.add(pos); 
                        keepStrMap.put(pos, anSb.toString()); 

                    }
					List<Integer> newList = new ArrayList<Integer>();
					//将list排序
					while(list.size()>0)
                    {

                        int tmp = 0;
                        for (int i = 0; i < list.size(); i++)
                        {
                            tmp = list.get(i);
                            for (int j = 0; j < list.size(); j++)
                            {
                                if (list.get(j) < tmp)
                                {
                                    tmp = list.get(j);
                                }
                            }
                        }
                        list.remove((Integer)tmp);
                        newList.add(tmp);
                    }
					int addSize = 0;
					for (Integer i : newList)
					{
						try
						{
							t.replace(i + addSize, 0, keepStrMap.get(i));
							addSize += keepStrMap.get(i).length();
						}
						catch (BadLocationException e1)
						{
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

				}
				
			}
		};
		sampleAction.setText("添加注释");//添加属性注释"Add Prop Annotation");
		sampleAction.setToolTipText("添加注释");
		sampleAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(IDE.SharedImages.IMG_OBJS_TASK_TSK));
		
 
		 
		//addClsAnnoAction.setText(MultLang.getMultLang("code.030"));//添加类注释"Add Class Annotation");
		//addClsAnnoAction.setToolTipText(MultLang.getMultLang("code.030"));
		//addClsAnnoAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
		//		getImageDescriptor(IDE.SharedImages.IMG_OBJS_TASK_TSK));
	}
	public void contributeToMenu(IMenuManager manager) {
		IMenuManager menu = new MenuManager(MultLang.getMultLang("code.031"));//编码工具
		manager.prependToGroup(IWorkbenchActionConstants.MB_ADDITIONS, menu);
		menu.add(sampleAction);
		//menu.add(addClsAnnoAction);
	}
	public void contributeToToolBar(IToolBarManager manager) {
		//manager.add(new Separator());
		//manager.add(sampleAction);
	}
}
