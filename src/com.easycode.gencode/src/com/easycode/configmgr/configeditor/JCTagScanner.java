package com.easycode.configmgr.configeditor;

import org.eclipse.jface.text.*;
import org.eclipse.jface.text.rules.*;

public class JCTagScanner extends RuleBasedScanner {

	public JCTagScanner(ColorManager manager) {
		IToken string =
			new Token(
				new TextAttribute(manager.getColor(JCColorConstants.STRING)));

		IRule[] rules = new IRule[3];

		// Add rule for double quotes
		rules[0] = new SingleLineRule("\"", "\"", string, '\\');
		// Add a rule for single quotes
		rules[1] = new SingleLineRule("'", "'", string, '\\');
		// Add generic whitespace rule.
		rules[2] = new WhitespaceRule(new JCWhitespaceDetector());

		setRules(rules);
	}
}
