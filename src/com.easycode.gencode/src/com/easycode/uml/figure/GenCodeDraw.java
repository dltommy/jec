/**
 * 作 者: dltommy
 * 日 期: 2012-6-1
 * 描 叙:
 */
package com.easycode.uml.figure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionEndpointLocator;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.ScrollPane;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;

import com.easycode.templatemgr.FileGenModel.FileModel;
 

/**
 * 功能描叙: 编 码: dltommy 完成时间: 2012-6-1 下午10:19:16
 */
public class GenCodeDraw extends Figure
{
	//private  static int x = 10;
	//private static int y = 20;
	public static void createUMLClass(Map map, Canvas cas,
			LightweightSystem lws)
	{
		//Figure contents = new Figure();
		 ScrollPane contents = new ScrollPane();   
		 //contents.setScrollBarVisibility(ScrollPane.ALWAYS);
		XYLayout contentsLayout = new XYLayout();
		contents.setLayoutManager(contentsLayout);
		Font classFont = new Font(null, "Arial", 10, SWT.BOLD);
		// 添加表的显示文本及显示的图标
		String clzName = (String) map.get("clzName");

		Label classLabel = new Label(clzName, new Image(cas.getDisplay(),
				UMLClassFigure.class.getResourceAsStream("class_obj.gif")));
		classLabel.setFont(classFont);
		classLabel.setToolTip(new Label(clzName));
		UMLClassFigure classFigure = new UMLClassFigure(classLabel);
		
		Image privateImage = new Image(cas.getDisplay(), GenCodeDraw.class
				.getResourceAsStream("private.gif"));
		Image protectedImage = new Image(cas.getDisplay(), GenCodeDraw.class
				.getResourceAsStream("protected.gif"));
		Image publicImage = new Image(cas.getDisplay(), GenCodeDraw.class
				.getResourceAsStream("public.gif"));
		java.util.List<HashMap> fieldList = (java.util.List) map
				.get("propList");
		//HashMap<String, Label> propLabelMap = new HashMap<String, Label>();
		//HashMap<String, String> referMap = new HashMap<String, String>();
 
		 if(fieldList != null)
		 {
	          classFigure.setBounds(new Rectangle(5, 10, 130, 20+fieldList.size()*18+10)); 
		 }
		 else
		 {
	           classFigure.setBounds(new Rectangle(5, 10, 130, 20+10)); 
		 }
			contents.add(classFigure);
		if (fieldList != null)
		{
		    int i =0;
			for (Map m : fieldList)
			{
				String refer = (String)m.get("refer");


				Map type = (Map) m.get("propType");
				
				String limit = (String) type.get("limitRange");
				Label attr = null;
				String propName = (String) m.get("propName");
			 
				String titleName = (String)m.get("title");
				if ("private".equals(limit))
				{
					if(propName.equals(titleName))
					{
						attr = new Label(propName, privateImage);
					}
					else
					{
						attr = new Label(propName+" "+titleName, privateImage);
					}

                    
				}
				else if ("protected".equals(limit))
				{
					if(propName.equals(titleName))
					{
						attr = new Label(propName, protectedImage);
					}
					else
					{
						attr = new Label(propName+" "+titleName, protectedImage);
					}
					
					//attr = new Label((String) m.get("propName"), protectedImage);
				}
				else
				{

					if(propName.equals(titleName))
					{
						attr = new Label(propName, publicImage);
						
					}
					else
					{
						attr = new Label(propName+" "+titleName, publicImage);
					}

				}
				attr.setToolTip(new Label(attr.getText()));
				String isObj = String.valueOf(type.get("isObject"));

				classFigure.getAttributesCompartment().add(attr);
				String referClzName = null;
				referClzName = (String) type.get("clzName");


				//String refer = "";
		
					if ("true".equals(isObj))
					{
						if("auto".equals(refer))
						{
						referClzName = (String) type.get("clzName");
	 
						refer = "one-to-one";
					
						
						
					    if("true".equals((String)type.get("array")))
					    {
					    	String tmpClzName = (String) type.get("clzName");
					    	if(tmpClzName.indexOf("<")>-1)
					    	{
					    		referClzName = tmpClzName.substring(tmpClzName.indexOf("<")+1,tmpClzName.indexOf(">"));
					    	}
	                        if(tmpClzName.indexOf("[")>-1)
	                        {
	                        	referClzName = tmpClzName.substring(0,tmpClzName.indexOf("["));
	                        	 
	                        }
						     
							refer = "one-to-more";
					    }
						}
					}
					else
					{
					}

				
				
				
				

					if(refer == null || "".equals(refer.trim()))
					{
						continue;
					}
					 
				UMLClassFigure target = drawReferFigue(referClzName,map,classFont,cas,privateImage,protectedImage,publicImage,i);

				if(target == null)
				{
					continue;
				}
				else
				{
				    i++;
					contents.add(target);
					new Dragger(target);
					//画线

					// 新建连线
					PolylineConnection conn = new PolylineConnection();
					// 添加图形的锚点
					ChopboxAnchor sourceAnchor = new ChopboxAnchor(attr);
					ChopboxAnchor targetAnchor = new ChopboxAnchor(target);
					conn.setSourceAnchor(sourceAnchor);
					conn.setTargetAnchor(targetAnchor);

					PolygonDecoration decoration = new PolygonDecoration();
					PointList decorationPointList = new PointList();
					decorationPointList.addPoint(0, 0);
					decorationPointList.addPoint(-1, 1);
					// decorationPointList.addPoint(-4,0);
					decorationPointList.addPoint(-1, -1);
					decoration.setTemplate(decorationPointList);
					conn.setTargetDecoration(decoration);

					// 添加连线的Locator
					/*
					ConnectionEndpointLocator sourceEndpointLocator = new ConnectionEndpointLocator(
							conn, true);
					sourceEndpointLocator.setVDistance(0);
					Label targetMultiplicityLabel = new Label("one-one");
					conn.add(targetMultiplicityLabel, sourceEndpointLocator);
                    */
					if(refer != null && !"".equals(refer.trim()))
					{
					    Label label = new Label(refer);
					    label.setOpaque(true);
					    label.setBackgroundColor(ColorConstants.buttonLightest);
					    label.setBorder(new LineBorder());
					    conn.add(label, new MidpointLocator(conn, 0));
					}
					contents.add(conn);
					
				
				}
				
			}

			classFigure.setToolTip(new Label(clzName));
			//contents.add(classFigure,new Rectangle(x, y, -1, -1));
			new Dragger(classFigure);
		}


		// 画线
		lws.setContents(contents);

	}
public static UMLClassFigure drawReferFigue(String clzName, Map map,
		Font classFont,
		Canvas cas,
		Image privateImage ,
		Image protectedImage ,
		Image publicImage,int index )
	{
 

   
		UMLClassFigure tmpFigure = null;

		Map refer = (Map) map.get("zzzReferObj");

		HashMap<String, UMLClassFigure> figureMap = new HashMap<String, UMLClassFigure>();
		// int y = 100;
		if (refer != null && refer.size() > 0)
		{
		     int x = (130-30)*(index+1);
		     if(index==0)
		     {
		         x = 150;
		     }
		    
		     int y = 20*(index+1);
		     
			Iterator itKeys = refer.keySet().iterator();
			 
			while (itKeys.hasNext())
			{

				String prop = (String) itKeys.next();
				HashMap tempCls = (HashMap) refer.get(prop);
				String tempClsName = (String) tempCls.get("clzName");
				if (tempClsName.equals(clzName))
				{
				     
					Label tempLabel = new Label(tempClsName, new Image(cas
							.getDisplay(), UMLClassFigure.class
							.getResourceAsStream("class_obj.gif")));
					tempLabel.setFont(classFont);
					tempLabel.setToolTip(new Label(tempClsName));
					tmpFigure = new UMLClassFigure(tempLabel);

					java.util.List<HashMap> fieldList = (java.util.List) tempCls
							.get("propList");
					int height = 0;
					if (fieldList != null)
					{
						for (HashMap m : fieldList)
						{
							height = height + 50;
							HashMap type = (HashMap) m.get("propType");
							String limit = (String) type.get("limitRange");
							Label attr = null;
							String propName = (String) m.get("propName");
							String titleName = (String)m.get("title");
							if ("private".equals(limit))
							{
								//attr = new Label((String) m.get("propName"),
								//		privateImage);
								
								if(propName.equals(titleName))
								{
									attr = new Label(propName, privateImage);
								}
								else
								{
									attr = new Label(propName+" "+titleName, privateImage);
								}
							}
							else if ("protected".equals(limit))
							{
								//attr = new Label((String) m.get("propName"),
								//		protectedImage);

								if(propName.equals(titleName))
								{
									attr = new Label(propName, protectedImage);
								}
								else
								{
									attr = new Label(propName+" "+titleName, protectedImage);
								}
							}
							else
							{
								//attr = new Label((String) m.get("propName"),
								//		publicImage);
								if(propName.equals(titleName))
								{
									attr = new Label(propName, publicImage);
								}
								else
								{
									attr = new Label(propName+" "+titleName, publicImage);
								}
							}
							attr.setToolTip(new Label(attr.getText()));
							tmpFigure.getAttributesCompartment().add(attr);
						}

					}
					// y = y + 50;
					// x = x + 150 ;
					  
					// contentsLayout.setConstraint(tmpFigure, new Rectangle(x,
					// y,
					// 100, -1));
					 int fieldCount = 0;
					 if(fieldList != null)
					 {
					     fieldCount = fieldList.size();
					 }
					  
					tmpFigure.setBounds(new Rectangle(x, y, 130, 20 + fieldCount* 18 + 10));
					tmpFigure.setToolTip(new Label(tempClsName));
					break;
				}
			}
		}
		return tmpFigure;
}
public static void createGenFileFigure(List<FileModel> fileList,
			Canvas cas, LightweightSystem lws,int disHight,int disWidth)
	{
		//Figure contents = new Figure();
		 ScrollPane scrollPan = new ScrollPane();   
		 //scrollPan.setSize(1000, 500);


		 //scrollPan.setScrollBarVisibility(ScrollPane.ALWAYS);



		 Panel contents = new Panel();   
		XYLayout contentsLayout = new XYLayout();
		//contents.setLayoutManager(contentsLayout);
		contents.setLayoutManager(contentsLayout);
		Font classFont = new Font(null, "Arial", 10, SWT.BOLD);

		HashMap<Integer, UMLClassFigure> figureIndexMap = new HashMap<Integer, UMLClassFigure>();
		HashMap<String, UMLClassFigure> figureNameMap = new HashMap<String, UMLClassFigure>();
		HashMap<String, Label> labelNameMap = new HashMap<String, Label>();
		HashMap<String, Label> labelIndexMap = new HashMap<String, Label>();
		// HashMap<String, Label> labelIndexMap = new HashMap<String, Label>();

		// 添加表的显示文本及显示的图标
		if (fileList != null)
		{
			if (fileList.size() == 0)
			{
				return;
			}
			Image jspImage = new Image(cas.getDisplay(), UMLClassFigure.class
					.getResourceAsStream("jsp.gif"));

			Image txtImage = new Image(cas.getDisplay(), UMLClassFigure.class
					.getResourceAsStream("txt.gif"));
			Image xmlImage = new Image(cas.getDisplay(), UMLClassFigure.class
					.getResourceAsStream("xml.gif"));

			Image javaImage = new Image(cas.getDisplay(), UMLClassFigure.class
					.getResourceAsStream("java.gif"));
			Image docImage = new Image(cas.getDisplay(), UMLClassFigure.class
					.getResourceAsStream("document.png"));
			
			Image memoImage = new Image(cas.getDisplay(), UMLClassFigure.class
					.getResourceAsStream("memo.gif"));


			int y = 0;//20;
			int x = 0;//10;
			int index = 0;
			int size = fileList.size();

			int eachLine = 4;
			int rows = 1;
			if (size % eachLine != 0)
			{
				rows = size / eachLine + 1;
			}
			else
			{
				rows = size / eachLine;
			}
			
			int eachHeight = 0;
			if(size <= 8)
			{
				eachHeight = (disHight - y) / size;//eachHeight = (420 - y) / size;
			}
			else
			{
				eachHeight = (disHight - y) / size;//(720 - y) / size;
			}
			int eachWidth = (disWidth - x) / eachLine;//int eachWidth = (810 - x)

			for (FileModel fm : fileList)
			{
				if("N".equals(fm.getDisplay()))
				{
					continue;
				}
				index++;
				fm.setIndex(index);
				String fileName = "Unknow";
				if (fm.getFilePath() != null)
				{
					String names[] = fm.getFilePath().split("[\\|/]");
					for (int i = names.length - 1; i >= 0; i--)
					{
						fileName = names[i];
						
						if (fileName != null && !"".equals(fileName.trim()))
						{
							break;
						}
					}

					Label classLabel = null;
					if (fileName.toLowerCase().endsWith(".xml"))
					{
						classLabel = new Label(fileName, xmlImage);
					}
					else if (fileName.toLowerCase().endsWith(".jsp"))
					{
						classLabel = new Label(fileName, jspImage);
					}
					else if(fileName.toLowerCase().endsWith(".java"))
					{
						classLabel = new Label(fileName, javaImage);
					}
					else
					{
						classLabel = new Label(fileName, docImage);
					}
					classLabel.setFont(classFont);
					classLabel.setToolTip(new Label(fileName));
					UMLClassFigure fileFigure = new UMLClassFigure(classLabel);
					//fileFigure.setToolTip(new Label(fileName));
					
					String memo = fm.getMemo();
					String name = fm.getName();
					int height = 20;
					int memoSize = 0;
					if (memo != null)
					{
						String memos[] = memo.split(";");
						//int i = 0;
						for (String m : memos)
						{
							memoSize++;
							String[] memoParse = m.split("\\$");
							String nodeName = "";
							if(memoParse.length>0)
							{
								nodeName = memoParse[0].trim();
								
							}
							
							if (m.lastIndexOf("$") > 0)
							{
								m = m.substring(0, m.lastIndexOf("$"));
							}
							else
							{
								//m="";
							}
							Label tempLabel = new Label(nodeName, memoImage);
							tempLabel.setToolTip(new Label(nodeName));
							fileFigure.getAttributesCompartment()
									.add(tempLabel);
							if (name != null && !"".equals(name))
							{
								labelNameMap.put(name + "." +  memoSize, tempLabel);
								labelIndexMap.put(index + "." + memoSize, tempLabel);

							}
							if(memoParse.length == 3)
							{
								labelNameMap.put(name + "." +  memoParse[2], tempLabel);
							}

						}

					}
					else
					{
						fileFigure.getAttributesCompartment().add(
								new Label("\n"));
					}

					if (name != null && !"".equals(name.trim()))
					{
						figureNameMap.put(name, fileFigure);
					}
					figureIndexMap.put(index, fileFigure);

					//contentsLayout.setConstraint(fileFigure, new Rectangle(x,
					//		y, 100, -1));
					//contentsLayout.setConstraint(fileFigure, new Rectangle(x,
					//		y, -1, -1));
					fileFigure.setBounds( new Rectangle(x,
							y, 150, (memoSize)*height+18));
					new Dragger(fileFigure);
					contents.add(fileFigure);
					if (index % eachLine == 0)
					{

						int curLine = index / eachLine;
						if(curLine %2 == 0)
						{
							x = 10+(curLine+1)*10;//x-curLine*100;
						}
						else
						{
							x = 10 + curLine * 20;
						}
						/*
                        if(curLine %4 == 0)
                        {
                        	x = 10 + curLine*10;
                        }
                        else
                        {
    						x = 10 + curLine * 100;
                        }
                        */

					}
					else
					{
						x = x + eachWidth;
						y = y + eachHeight;
						/*
						if (index % 2 == 1)
						{
							y = y + eachHeight - 20;

						}
						else
						{
							y = y + eachHeight + 20;


						}
						*/
					}
					

				}
			}
			// 画线
			if (figureNameMap != null && figureNameMap.size() > 0)
			{
				for (FileModel fm : fileList)
				{
					// 对象到备注或对象
					if (fm.getAnchor() != null && !"".equals(fm.getAnchor()))
					{
						UMLClassFigure src = figureIndexMap.get(fm.getIndex());
						String anStr = fm.getAnchor();
						ChopboxAnchor targetAnchor = null;// new
						// ChopboxAnchor(target);

						if ("".equals(anStr.trim()))
						{
							continue;
						}

						//String ans[] = anStr.split("\\$");
						String ans[] = anStr.split(";");
						for (String tan : ans)
						{
							String tip = null;
							String an = tan;
							int checkTip = an.indexOf("(");
							if(checkTip > -1)
							{
								//tip = an.substring(checkTip);
								tip = an.substring(checkTip+1,an.length()-1);
								an=an.substring(0,checkTip);
							}
							
							Color color = null;
							int lineStyle = 0;
							if (an.indexOf(".") > -1)
							{
								Label target = labelNameMap.get(an.trim());
								if (target == null)
								{
									continue;
								}
								targetAnchor = new ChopboxAnchor(target);
								color = cas.getDisplay().getSystemColor(SWT.COLOR_BLUE);
								//lineStyle = Graphics.LINE_DASHDOTDOT;
							}
							else
							{
								UMLClassFigure target = figureNameMap.get(an);
								if (target == null)
								{
									continue;
								}
								targetAnchor = new ChopboxAnchor(target);
								color = cas.getDisplay().getSystemColor(SWT.COLOR_BLACK);
								lineStyle = Graphics.LINE_CUSTOM;
							}

							// 新建连线
							PolylineConnection conn = new PolylineConnection();
							conn.setForegroundColor(color);
							//conn.setLineStyle(lineStyle);
							// 添加图形的锚点
							ChopboxAnchor sourceAnchor = new ChopboxAnchor(src);

							conn.setSourceAnchor(sourceAnchor);
							conn.setTargetAnchor(targetAnchor);

							PolygonDecoration decoration = new PolygonDecoration();
							PointList decorationPointList = new PointList();
							decorationPointList.addPoint(0, 0);
							decorationPointList.addPoint(-1, 1);
							// decorationPointList.addPoint(-4,0);
							decorationPointList.addPoint(-1, -1);
							decoration.setTemplate(decorationPointList);
							conn.setTargetDecoration(decoration);

							// 添加连线的Locator
							/*
							ConnectionEndpointLocator sourceEndpointLocator = new ConnectionEndpointLocator(
									conn, true);
							sourceEndpointLocator.setVDistance(0);
							if (tip != null)
							{
								Label targetMultiplicityLabel = new Label(
										tip);
								conn.add(targetMultiplicityLabel,
										sourceEndpointLocator);
							}
							*/
							if (tip != null)
							{
								Label label = new Label(tip);
								label.setOpaque(true);
								label
										.setBackgroundColor(ColorConstants.buttonLightest);
								label.setBorder(new LineBorder());
								conn.add(label, new MidpointLocator(conn, 0));
							}
							contents.add(conn);
						}

					}
					// 备注到备注或对象
					String memo = fm.getMemo();
					if (memo != null && !"".equals(memo.trim()))
					{
						String memos[] = memo.split(";");
						int i = 0;
						for (String m : memos)
						{

							i++;
							Label srcLabel = labelIndexMap.get(fm.getIndex()
									+ "." + i);
							if (srcLabel == null)
							{
								continue;
							}
							ChopboxAnchor targetAnchor = null;// new
																
							String memoParse[] = m.split("\\$");
							//int pos = m.indexOf("$");
							//if (pos > -1)
							if(memoParse.length>=2)
							{

								//String achorStr = m.substring(pos + 1);
								
								//String achors[] = achorStr.split("\\$");
								
								//for(String achor:achors)
								//for(String item:memoParse)
								{
									Color color = null;
									int lineStyle = 0;
									if ("".equals(memoParse[1].trim()))
									{
										continue;
									}

									String tip = null;
									int checkTip = memoParse[1].indexOf("(");
									String achor = null;
									if(checkTip > -1)
									{
										tip = memoParse[1].substring(checkTip+1,memoParse[1].length()-1);
										achor =memoParse[1].substring(0,checkTip);
									}
									else
									{
										achor =memoParse[1];
									}
									if (achor.indexOf(".") > -1)
									{
										Label targetLabel = labelNameMap
												.get(achor);
										if (targetLabel == null)
										{
											
											continue;
										}
										
										targetAnchor = new ChopboxAnchor(
												targetLabel);
										color = cas.getDisplay().getSystemColor(SWT.COLOR_BLUE);
										lineStyle = Graphics.LINE_DASHDOTDOT;
									}
									else
									{
										UMLClassFigure targetFig = figureNameMap
												.get(achor);
										if (targetFig == null)
										{
											continue;
										}
										targetAnchor = new ChopboxAnchor(
												targetFig);
										color = cas.getDisplay().getSystemColor(SWT.COLOR_BLACK);
										lineStyle = Graphics.LINE_CUSTOM;
									}

									PolylineConnection conn = new PolylineConnection();
									ChopboxAnchor sourceAnchor = new ChopboxAnchor(
											srcLabel);
                                   // c.setLineStyle(PolylineConnection.);
									
									conn.setForegroundColor(color);
									//conn.setLineStyle(lineStyle);
									conn.setSourceAnchor(sourceAnchor);
									conn.setTargetAnchor(targetAnchor);

									PolygonDecoration decoration = new PolygonDecoration();
									PointList decorationPointList = new PointList();
									decorationPointList.addPoint(0, 0);
									decorationPointList.addPoint(-1, 1);
									// decorationPointList.addPoint(-4,0);
									decorationPointList.addPoint(-1, -1);
									decoration.setTemplate(decorationPointList);
									conn.setTargetDecoration(decoration);
									
									
									// 添加连线的Locator
									/*
									if(tip != null)
									{
									    ConnectionEndpointLocator sourceEndpointLocator = new ConnectionEndpointLocator(
											conn, true);
									     sourceEndpointLocator.setVDistance(0);
									     //sourceEndpointLocator.
									     Label targetMultiplicityLabel = new Label(tip);
									    conn.add(targetMultiplicityLabel, sourceEndpointLocator);
									}
									*/
									if (tip != null)
									{
										Label label = new Label(tip);
										label.setOpaque(true);
										label
												.setBackgroundColor(ColorConstants.buttonLightest);
										label.setBorder(new LineBorder());
										conn.add(label, new MidpointLocator(conn, 0));
									}
									contents.add(conn);
								}
							}
						}
					}
				}
			}
		}
		
		scrollPan.setContents(contents);
		lws.setContents(scrollPan);
	}
}   
