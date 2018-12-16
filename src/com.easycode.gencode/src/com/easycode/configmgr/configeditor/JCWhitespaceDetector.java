package com.easycode.configmgr.configeditor;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

public class JCWhitespaceDetector implements IWhitespaceDetector {

	public boolean isWhitespace(char c) {
		return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
	}
}
