package com.easycode.jspeditor.editor;
 
 
import org.eclipse.jface.action.*;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;

import com.easycode.jspeditor.action.AddMultLangAction;
import com.easycode.resource.MultLang;


/**
 * Manages the installation/deinstallation of global actions for multi-page editors.
 * Responsible for the redirection of global actions to the active editor.
 * Multi-page contributor replaces the contributors for the individual editors in the multi-page editor.
 */
public class JSPMultiPageEditorContributor extends MultiPageEditorActionBarContributor {
	private IEditorPart activeEditorPart;
	private Action sampleAction;
	//private Action autoTagAction;
	private Action autoTurnAciton;
	/**
	 * Creates a multi-page contributor.
	 */
	public JSPMultiPageEditorContributor() {

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
	    
	    
	    
	        
	      //  JSPMultiPageEditor multPage = (JSPMultiPageEditor)part;
	 //       multPage.initAction((AddMultLangAction)sampleAction);
	    
	    
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
	private void createActions() 
	{
		sampleAction = new AddMultLangAction();
		sampleAction.setText(MultLang.getMultLang("code.068")+"(Alt+Q)");//生成多语言
		sampleAction.setToolTipText("Add MultLang");
		sampleAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(IDE.SharedImages.IMG_OBJS_TASK_TSK));
 
	}
	public void contributeToMenu(IMenuManager manager) 
	{
		IMenuManager menu = new MenuManager(MultLang.getMultLang("code.071"));//多语言工具
		manager.prependToGroup(IWorkbenchActionConstants.MB_ADDITIONS, menu);
		menu.add(sampleAction);
		//menu.add(autoTagAction);
		//menu.add(autoTurnAciton);
	}
	public void contributeToToolBar(IToolBarManager manager) {
		manager.add(new Separator());
		manager.add(sampleAction);

	}
	@Override  
	 public void setActiveEditor(IEditorPart part)
	{   
	    super.setActiveEditor(part);   
	     
	    JSPMultiPageEditor multPage = (JSPMultiPageEditor)part;
	    multPage.initAction((AddMultLangAction)sampleAction);
	    IActionBars actionBars= getActionBars();   
	    
	    actionBars.setGlobalActionHandler("client.editors.gencode.jsp.id", sampleAction);   
	}   
	public class PosObject
	{
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
		
	}
}
