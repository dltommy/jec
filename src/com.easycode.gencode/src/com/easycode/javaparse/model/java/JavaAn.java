package com.easycode.javaparse.model.java;

import java.util.HashMap;
import java.util.Map;

public class JavaAn 
{
   private String name = null;
   private Map<String,Object> valueMap = new HashMap<String,Object>();
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}

public Map<String, Object> getValueMap() {
	return valueMap;
}
public void setValueMap(Map<String, Object> valueMap) {
	this.valueMap = valueMap;
}
   
}
