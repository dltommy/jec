/**
 * 作 者: 欧阳超
 * 日 期: 2011-12-5
 * 描 叙:
 */
package com.easycode.gencode.ui.elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.CheckboxTreeViewer;

import net.sf.json.JSONObject;
 

/**
 * 功能描叙: 编 码:  完成时间: 2011-12-5 下午08:52:01
 */
public class SelectCheckBox
{
	private SelectCheckBox parent = null;
	private String key = null;
	private List<SelectCheckBox> childList = new ArrayList<SelectCheckBox>();
	private boolean isLeaf = true;
	private String value = null;

	private List<String> groupList = null;
	public SelectCheckBox(String key, String value)
	{
		this.key = key;
		this.value = value;
	}
	public void addChild(SelectCheckBox child)
	{
		this.childList.add(child);
	}
    public void setChildList(List<SelectCheckBox> childList)
    {
    	this.childList = childList;
    }
	public boolean isLeaf()
	{
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf)
	{
		this.isLeaf = isLeaf;
	}

	public String getKey()
	{
		return key;
	}

	public void setKey(String key)
	{
		this.key = key;
	}

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public void buildNode(SelectCheckBox node, String src,
			HashMap<String, SelectCheckBox> map) throws Exception
	{
		try
		{
			if (src.startsWith("{"))
			{
				if("{}".equals(src))
				{
					node.setValue(node.getKey());
					map.put(node.getKey(), node);
				}
				net.sf.json.JSONObject jsonObj = net.sf.json.JSONObject
						.fromObject(src);
				for (Iterator iter = jsonObj.keys(); iter.hasNext();)
				{
					
					String key = (String) iter.next();
				 
					String checkKey = key.trim();
					List<String> grops = null;
					if(checkKey.indexOf("/") > -1)
					{
						checkKey = checkKey.substring(0,key.lastIndexOf("/"));
						grops = new ArrayList<String>();
						String gropStr = key.substring(key.lastIndexOf("/")+1);
						String grpInfo[] = gropStr.split("\\)");
						for(String t:grpInfo)
						{ 
							grops.add(t.trim()+")");
						}
					}
					checkKey = checkKey.trim(); 
					
					//设置默认
					if(node.getValue() == null)
					{
						node.setValue(node.getKey());
					}

					if (key.equalsIgnoreCase("node_name"))
					{
						map.remove(map.get(node.getKey()));
						node.setValue(jsonObj.get(key).toString().trim()); 
						continue;
					}
					else
					{
						 
					    SelectCheckBox b = new SelectCheckBox();
					    b.setGroupList(grops);
					    b.setKey(checkKey);
					    b.setParent(node);
					
					    map.put(checkKey, b);
					
					    buildNode(b, jsonObj.get(key).toString().trim(), map);
					    node.addChild(b);
					}
				}
			}

			else
			{
				 
				node.setValue(src);
				node.setLeaf(true);
				// node.setValue(src);

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}

	}

	public SelectCheckBox()
		{

		}

	public HashMap<String, SelectCheckBox> getThreadNode(String src) throws Exception
	{
		src = src.replaceAll("[\t\n\r\f\\s]", " ");
		HashMap<String, SelectCheckBox> retMap = new HashMap<String, SelectCheckBox>();
		SelectCheckBox root = new SelectCheckBox();
		root.setKey("root");
		root.setValue("root");
		retMap.put("root", root);

		SelectCheckBox son = new SelectCheckBox();
		son.setParent(root);
		son.setKey("top");
		root.childList.add(son);
		retMap.put("top", son);
		try
		{
			new SelectCheckBox().buildNode(son, src, retMap);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}

		return retMap;
	}

	/**
	 * 查询子节点 功 能: 实现流程:
	 * 
	 * @param parent
	 * @param retList
	 */
	public static void getSubList(SelectCheckBox parent,
			List<SelectCheckBox> retList)
	{

		if(parent != null)
		{
		List<SelectCheckBox> subList = parent.getChildList();
		if (subList != null && subList.size() > 0)
		{
			for (SelectCheckBox c : subList)
			{
				retList.add(c);
				getSubList(c, retList);
			}
		}
		}
	}

	public static void getParentList(SelectCheckBox box,
			List<SelectCheckBox> retList)
	{
		if(box != null && box.getParent() != null)
		{
			retList.add(box.getParent());
			getParentList(box.getParent(),retList);
		}
	}
 
	public SelectCheckBox getParent()
	{
		return parent;
	}

	public void setParent(SelectCheckBox parent)
	{
		this.parent = parent;
	}

	public List<SelectCheckBox> getChildList()
	{
		return childList;
	}
	//取消时，同一类的取消，但一类的不变
	public static void syncCheckStatus(String ctrlkKey, SelectCheckBox selectBox, boolean checked, HashMap<String, SelectCheckBox> boxMap,CheckboxTreeViewer checkTree)
	{
		if (selectBox == null)
		{
			return;
		}
		

        List<String> gropList = selectBox.getGroupList();
        String value = selectBox.getKey();
        
        if("root".equals(value))
        {
        	
        	return;
        }
        
    	if(checked)
    	{
			if(ctrlkKey.equals(selectBox.getKey()))
			{
	    		syncParentOrChildNode("PC",checked,value,boxMap,checkTree);
			}
			else
			{
	    		syncParentOrChildNode("P",checked,value,boxMap,checkTree);
			}

    	}
    	else
    	{
    		syncParentOrChildNode("C",checked,value,boxMap,checkTree);
    	}
         
        if(gropList != null && gropList.size() > 0)
        {
        	for(String group:gropList)
        	{
        		//单选
        		if(group.endsWith("(r)"))
        		{
        			Iterator<Entry<String,SelectCheckBox>> itStr = boxMap.entrySet().iterator();
        			if(checked)
        			{
        				while (itStr.hasNext())
        				{
        					Entry<String,SelectCheckBox> t = itStr.next();
        					if(value.equals(t.getKey()))
        					{
        						continue;
        					}
        					List<String> tmpList = t.getValue().getGroupList();
        					if(tmpList != null && tmpList.contains(group))
        					{
        						checkTree.setChecked(t.getValue(), !checked);
        				  
        					}
        				}
        			}
        		}
        		else if(group.endsWith("(c)"))
        		{
    				if(!ctrlkKey.equals(selectBox.getKey()))
    				{
    					return;
    				}
        			if(checked)
        			{
            			Iterator<Entry<String,SelectCheckBox>> itStr = boxMap.entrySet().iterator();
        				while (itStr.hasNext())
        				{
        					Entry<String,SelectCheckBox> t = itStr.next();
        					if(value.equals(t.getKey()))
        					{
        						continue;
        					}
        					List<String> tmpList = t.getValue().getGroupList();
         
        					if(tmpList != null)
        					{
        						for(String grp:tmpList)
        						{
        							String reg="_*"+group.substring(0,group.length()-3)+"\\(c\\)";
        							
        							if(grp.matches(reg))
        							{
        							
                						checkTree.setChecked(t.getValue(), checked);
        							}
        						}

        					}
        					
        				}
        			}
        			else
        			{

            			Iterator<Entry<String,SelectCheckBox>> itStr = boxMap.entrySet().iterator();
        				while (itStr.hasNext())
        				{
        					Entry<String,SelectCheckBox> t = itStr.next();
        					if(value.equals(t.getKey()))
        					{
        						continue;
        					}
        					List<String> tmpList = t.getValue().getGroupList(); 
        					if(tmpList != null)
        					{
        						for(String grp:tmpList)
        						{
        							String reg="_+"+group.substring(0,group.length()-3)+"\\(c\\)"; 
        							if(grp.matches(reg))
        							{ 
                						checkTree.setChecked(t.getValue(), checked);
        							}
        						}

        					}
        					
        				}
        			
        			}
        		}
        	}
        }


	}
	private static void syncParentOrChildNode(String type, boolean checked,String key,HashMap<String, SelectCheckBox> srcMap,CheckboxTreeViewer checkTree)
	{
		//自动选中父节点
		java.util.List<SelectCheckBox> nodeList = new ArrayList<SelectCheckBox>();
		if("C".equals(type))
		{
		    SelectCheckBox.getSubList(srcMap.get(key), nodeList);
		}
		else if("P".equals(type))
		{
			SelectCheckBox.getParentList(srcMap.get(key), nodeList);
		}
		else if("PC".equals(type))
		{
		    SelectCheckBox.getParentList(srcMap.get(key), nodeList);
		    
			SelectCheckBox.getSubList(srcMap.get(key), nodeList);
		}
		if (nodeList != null)
		{
			for (SelectCheckBox b : nodeList)
			{
				checkTree.setChecked(b, checked);

			}
		}
	}

	public List<String> getGroupList()
	{
		return groupList;
	}
	public void setGroupList(List<String> groupList)
	{
		this.groupList = groupList;
	}
}
