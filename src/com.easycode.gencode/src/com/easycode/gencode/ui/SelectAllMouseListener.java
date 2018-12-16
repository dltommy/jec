package com.easycode.gencode.ui;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Text;

public class SelectAllMouseListener implements MouseListener
{

    private Text ctrlText;
    public SelectAllMouseListener(Text ctrlText)
    {
        this.ctrlText = ctrlText;
    }
    public void mouseDoubleClick(MouseEvent arg0)
    {
        // TODO Auto-generated method stub
        this.ctrlText.selectAll();
    }

    public void mouseDown(MouseEvent arg0)
    {
        // TODO Auto-generated method stub
        
    }

    public void mouseUp(MouseEvent arg0)
    {
        // TODO Auto-generated method stub
        
    }

}
