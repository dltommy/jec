package com.easycode.configmgr.configeditor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class JCDocumentProvider extends FileDocumentProvider {

	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document != null) {
			

			
			
			IDocumentPartitioner partitioner =
				new FastPartitioner(
					new JCPartitionScanner(),
					new String[] {
						JCPartitionScanner.XML_TAG,
						JCPartitionScanner.XML_COMMENT });
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		
		
		}
		/*
		try
		{
			document.replace(document.getLength()-1, 0, "xx");
		}
		catch (BadLocationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		return document;
	}
}