/**
 * 作 者: ouyangchao
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
 * 编   码: ouyangchao
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
		//System.err.println("拖动......");
		Point p = e.getLocation();
		Dimension delta = p.getDifference(last);
		last = p;
		Figure f = (Figure) e.getSource();
		//System.err.println("---width-"+delta.width);
		//设置拖动的Figure的位置   
		
		//System.err.println("dddd.....");
		f.setBounds(f.getBounds().getTranslated(delta.width, delta.height));
		//contentsLayout.setConstraint(f, new Rectangle(delta.width, delta.height, 100, 100));
		//f.repaint(p.x, p.y, delta.width, delta.height);
		//System.err.println("repaint:");
		//f.repaint();
	}
}

