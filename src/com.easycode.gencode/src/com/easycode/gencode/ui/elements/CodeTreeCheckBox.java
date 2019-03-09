/**
 * 作 者: dltommy
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
 
 

/**
 * 功能描叙: 编 码:  完成时间: 2011-12-5 下午08:52:01
 */
public class CodeTreeCheckBox
{
	private CodeTreeCheckBox parent = null;
	private String key = null;
	private List<CodeTreeCheckBox> childList = new ArrayList<CodeTreeCheckBox>();
	private boolean isLeaf = true;
	private String value = null;

	private List<String> groupList = null;
	public CodeTreeCheckBox(String key, String value)
	{
		this.key = key;
		this.value = value;
	}
	public void addChild(CodeTreeCheckBox child)
	{
		this.childList.add(child);
	}
    public void setChildList(List<CodeTreeCheckBox> childList)
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

	public void buildNode(CodeTreeCheckBox node, String src,
			HashMap<String, CodeTreeCheckBox> map) throws Exception
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
						 
					    CodeTreeCheckBox b = new CodeTreeCheckBox();
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

	public CodeTreeCheckBox()
		{

		}

	public HashMap<String, CodeTreeCheckBox> getThreadNode(String src) throws Exception
	{
		src = src.replaceAll("[\t\n\r\f\\s]", " ");
		HashMap<String, CodeTreeCheckBox> retMap = new HashMap<String, CodeTreeCheckBox>();
		CodeTreeCheckBox root = new CodeTreeCheckBox();
		root.setKey("root");
		root.setValue("root");
		retMap.put("root", root);

		CodeTreeCheckBox son = new CodeTreeCheckBox();
		son.setParent(root);
		son.setKey("top");
		root.childList.add(son);
		retMap.put("top", son);
		try
		{
			new CodeTreeCheckBox().buildNode(son, src, retMap);
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
	public static void getSubList(CodeTreeCheckBox parent,
			List<CodeTreeCheckBox> retList)
	{

		if(parent != null)
		{
		List<CodeTreeCheckBox> subList = parent.getChildList();
		if (subList != null && subList.size() > 0)
		{
			for (CodeTreeCheckBox c : subList)
			{
				retList.add(c);
				getSubList(c, retList);
			}
		}
		}
	}

	public static void getParentList(CodeTreeCheckBox box,
			List<CodeTreeCheckBox> retList)
	{
		if(box != null && box.getParent() != null)
		{
			retList.add(box.getParent());
			getParentList(box.getParent(),retList);
		}
	}
 
	public CodeTreeCheckBox getParent()
	{
		return parent;
	}

	public void setParent(CodeTreeCheckBox parent)
	{
		this.parent = parent;
	}

	public List<CodeTreeCheckBox> getChildList()
	{
		return childList;
	}
	//取消时，同一类的取消，但一类的不变
	public static void syncCheckStatus(String ctrlkKey, CodeTreeCheckBox selectBox, boolean checked, HashMap<String, CodeTreeCheckBox> boxMap,CheckboxTreeViewer checkTree)
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
        			Iterator<Entry<String,CodeTreeCheckBox>> itStr = boxMap.entrySet().iterator();
        			if(checked)
        			{
        				while (itStr.hasNext())
        				{
        					Entry<String,CodeTreeCheckBox> t = itStr.next();
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
            			Iterator<Entry<String,CodeTreeCheckBox>> itStr = boxMap.entrySet().iterator();
        				while (itStr.hasNext())
        				{
        					Entry<String,CodeTreeCheckBox> t = itStr.next();
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

            			Iterator<Entry<String,CodeTreeCheckBox>> itStr = boxMap.entrySet().iterator();
        				while (itStr.hasNext())
        				{
        					Entry<String,CodeTreeCheckBox> t = itStr.next();
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
	private static void syncParentOrChildNode(String type, boolean checked,String key,HashMap<String, CodeTreeCheckBox> srcMap,CheckboxTreeViewer checkTree)
	{
		//自动选中父节点
		java.util.List<CodeTreeCheckBox> nodeList = new ArrayList<CodeTreeCheckBox>();
		if("C".equals(type))
		{
		    CodeTreeCheckBox.getSubList(srcMap.get(key), nodeList);
		}
		else if("P".equals(type))
		{
			CodeTreeCheckBox.getParentList(srcMap.get(key), nodeList);
		}
		else if("PC".equals(type))
		{
		    CodeTreeCheckBox.getParentList(srcMap.get(key), nodeList);
		    
			CodeTreeCheckBox.getSubList(srcMap.get(key), nodeList);
		}
		if (nodeList != null)
		{
			for (CodeTreeCheckBox b : nodeList)
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
