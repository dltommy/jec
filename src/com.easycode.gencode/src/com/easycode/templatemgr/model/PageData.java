package com.easycode.templatemgr.model;

import java.util.ArrayList;
import java.util.List;

public class PageData<T> {
int rows = 0;
List<T> dataList = new ArrayList<T>();
public int getRows() {
	return rows;
}
public void setRows(int rows) {
	this.rows = rows;
}
public List<T> getDataList() {
	return dataList;
}
public void setDataList(List<T> dataList) {
	this.dataList = dataList;
}


}
