package com.easycode.gencode.ui.elements;

import java.util.Iterator;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import com.easycode.gencode.ui.elements.PreparedParamTreeContentProvider.JSONKV;

public class PreparedParamTreeLabelProvider implements ILabelProvider
{

    public void addListener(ILabelProviderListener arg0)
    {
        // TODO Auto-generated method stub
        // CellLabelProvider r;
    }

    public void dispose()
    {
        // TODO Auto-generated method stub

    }

    public boolean isLabelProperty(Object arg0, String arg1)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void removeListener(ILabelProviderListener arg0)
    {
        // TODO Auto-generated method stub

    }

    public Image getImage(Object arg0)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getText(Object arg0)
    {
        // TODO Auto-generated method stub

        if (arg0 != null)
        {

            JSONKV e = (JSONKV) arg0;
            String keyInfo = "";// "\""+e.getKey()+"\"";
            if (!"".equals(e.getKey()))
            {
                keyInfo = "\"" + e.getKey() + "\"";
            }

            if (e.isArrayElement)
            {
                keyInfo = "[" + e.getKey() + "]";
                // return "["+e.getKey()+"]";

            }
             
            if (e.getValue() instanceof String)
            {
                if (e.isArrayElement)
                {
                    return "\"" + (String) e.getValue() + "\"";
                }
                if (!"".equals(e.getKey()))
                {
                    if (e.isArrayElement)
                    {
                        return keyInfo;
                    }
                    else
                    {
                        return keyInfo + ":\"" + e.getValue() + "\"";
                    }

                }
                else
                {
                    return "\"" + (String) e.getValue() + "\"";
                }

            }

            // else if (e.getValue() instanceof Boolean )
            // {
            // return keyInfo + ":"+e.getValue();
            // }
            else if (e.getValue() instanceof JSONArray)
            {
                JSONArray js = (JSONArray) e.getValue();
                return keyInfo + ":[" + js.size() + "]";
            }
            else if (e.getValue() instanceof JSONObject)
            {
                JSONObject js = (JSONObject) e.getValue();
                if (js.size() > 0)
                {
                    return keyInfo + ":{...}";
                }
                else
                {
                    return keyInfo + ":{}";
                }

            }
            else
            {
                return keyInfo + ":" + e.getValue();
            }
            // return keyInfo;// "\""+e.getKey()+"\"";

        }

        return "";

    }

}
