package com.easycode.gencode.ui.elements;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;

public class TypeSelect  implements SelectionListener{


	
	private String typeCode = null;

	/*
	public TypeSelect(String typeCode)
		{
			this.typeCode = typeCode;
		}
    */
	public String getTypeCode()
	{
		return typeCode;
	}

	public void setTypeCode(String typeCode)
	{
		this.typeCode = typeCode;
	}

	public void widgetDefaultSelected(SelectionEvent arg0)
	{
	}

	public void widgetSelected(SelectionEvent arg0)
	{
		
		Button b = (Button) arg0.getSource();
		if (b.getSelection())
		{
			this.typeCode = b.getText().toLowerCase();
		}

	}


}
