package com.easycode.configmgr.configeditor;

import org.eclipse.ui.editors.text.TextEditor;

public class JCEditor extends TextEditor {

	private ColorManager colorManager;

	public JCEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new JCConfiguration(colorManager));
		setDocumentProvider(new JCDocumentProvider());
	}
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
