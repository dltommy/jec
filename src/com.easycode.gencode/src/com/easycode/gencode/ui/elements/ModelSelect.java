package com.easycode.gencode.ui.elements;

import org.eclipse.jdt.core.ICompilationUnit;

public class ModelSelect 
{
    private String src=null;
    private String temp= null;
    private String node = null;
    private ICompilationUnit unit = null;
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}
	public ICompilationUnit getUnit() {
		return unit;
	}
	public void setUnit(ICompilationUnit unit) {
		this.unit = unit;
	}
 
    
}
