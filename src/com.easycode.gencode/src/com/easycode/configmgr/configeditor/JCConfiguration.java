package com.easycode.configmgr.configeditor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class JCConfiguration extends SourceViewerConfiguration {
	private JCDoubleClickStrategy doubleClickStrategy;
	private JCTagScanner tagScanner;
	private JCScanner scanner;
	private ColorManager colorManager;

	public JCConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE,
			JCPartitionScanner.XML_COMMENT,
			JCPartitionScanner.XML_TAG };
	}
	public ITextDoubleClickStrategy getDoubleClickStrategy(
		ISourceViewer sourceViewer,
		String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new JCDoubleClickStrategy();
		return doubleClickStrategy;
	}

	protected JCScanner getXMLScanner() {
		if (scanner == null) {
			scanner = new JCScanner(colorManager);
			scanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(JCColorConstants.DEFAULT))));
		}
		return scanner;
	}
	protected JCTagScanner getXMLTagScanner() {
		if (tagScanner == null) {
			tagScanner = new JCTagScanner(colorManager);
			tagScanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(JCColorConstants.TAG))));
		}
		return tagScanner;
	}

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr =
			new DefaultDamagerRepairer(getXMLTagScanner());
		reconciler.setDamager(dr, JCPartitionScanner.XML_TAG);
		reconciler.setRepairer(dr, JCPartitionScanner.XML_TAG);

		dr = new DefaultDamagerRepairer(getXMLScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
/*
		NonRuleBasedDamagerRepairer ndr =
			new NonRuleBasedDamagerRepairer(
				new TextAttribute(
					colorManager.getColor(JCColorConstants.XML_COMMENT)));
		reconciler.setDamager(ndr, JCPartitionScanner.XML_COMMENT);
		reconciler.setRepairer(ndr, JCPartitionScanner.XML_COMMENT);
        */
		return reconciler;
	}

}