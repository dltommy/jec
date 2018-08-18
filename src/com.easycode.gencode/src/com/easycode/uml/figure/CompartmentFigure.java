package com.easycode.uml.figure;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Insets;

/**
 * <p>
 * Title: Eclipse Plugin Development
 * </p>
 * <p>
 * Description: Free download
 * </p>
 * <p>
 * mail: ganshm@gmail.com
 * </p>
 */

public class CompartmentFigure extends Figure
{

	public CompartmentFigure()
		{
			ToolbarLayout layout = new ToolbarLayout();
			layout.setMinorAlignment(ToolbarLayout.ALIGN_TOPLEFT);
			layout.setStretchMinorAxis(false);
			// 设置子图形的间距
			layout.setSpacing(2);
			// 设置布局管理器
			setLayoutManager(layout);
			// 设置图形的边框
			setBorder(new CompartmentFigureBorder());
		}

	// 图形的边框类
	public class CompartmentFigureBorder extends AbstractBorder
	{
		public Insets getInsets(IFigure figure)
		{
			return new Insets(1, 0, 0, 0);
		}

		// 重画图形的边框
		public void paint(IFigure figure, Graphics graphics, Insets insets)
		{
			//System.err.println("重画........" + tempRect.getTopRight());
			
			graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft(),
					tempRect.getTopRight());
		}
	}
}
