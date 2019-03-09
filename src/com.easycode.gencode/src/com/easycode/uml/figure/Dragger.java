/**
 * 作 者: dltommy
 * 日 期: 2012-6-30
 * 描 叙:
 */
package com.easycode.uml.figure;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;//.Stub;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * 功能描叙:
 * 编   码: dltommy
 * 完成时间: 2012-6-30 下午12:24:13
 */
public class Dragger extends MouseMotionListener.Stub implements MouseListener
{
	private Point last;
	public Dragger(IFigure figure)
		{
			figure.addMouseMotionListener(this);
			figure.addMouseListener(this);
		}

	public void mouseDoubleClicked(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
		last = e.getLocation();

	}

	public void mouseReleased(MouseEvent e)
	{
	}

	public void mouseDragged(MouseEvent e)
	{ 
		Point p = e.getLocation();
		Dimension delta = p.getDifference(last);
		last = p;
		Figure f = (Figure) e.getSource();
 
		
		 
		f.setBounds(f.getBounds().getTranslated(delta.width, delta.height));
 
	}
}

