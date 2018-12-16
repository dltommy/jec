/**
 * 作 者:  
 * 日 期: 2010-8-31
 * 描 叙:
 */
package com.easycode.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


/**
 * 功能描叙: 编 码:   完成时间: 2010-8-31 下午10:17:06
 */
public class FileUtil
{
	/**
	 * 
	 * 功 能: 实现流程:
	 * 
	 * @param path
	 */
	public static void createFilePath(String path)
	{
		File f = new File(path);

		if (f.exists())
		{
			return;
		}
		else
		{
			String files[] = path.split("\\\\+|/+");
			File newFile = null;
			String npath = "";
			for (String s : files)
			{
				if ("".equals(s.trim()))
				{
					continue;
				}
				npath = npath + "/" + s;
				newFile = new File(npath);
				if (newFile.exists())
				{
					continue;
				}
				else
				{
					newFile.mkdir();

				}

			}

			f.mkdir();
		}
	}

	/**
	 * 
	 * 功 能: 实现流程:
	 * 
	 * @param filePath
	 * @param fileCtx
	 * @param checkPath
	 * @throws IOException
	 */
	public static void createFile(String filePath, String fileCtx,
			boolean checkPath, boolean replace) throws IOException
	{

		if (checkPath)
		{
			int filePos1 = filePath.lastIndexOf("\\");
			int filePos2 = filePath.lastIndexOf("/");
			int pos = 0;
			if (filePos1 > filePos2)
			{
				pos = filePos1;
			}
			else
			{
				pos = filePos2;
			}
			createFilePath(filePath.substring(0, pos));
		}
		File f = new File(filePath);
		if (!replace)
		{
			if (f.exists())
			{
				return;
			}
		}

		FileOutputStream fout = new FileOutputStream(f);

		java.io.OutputStreamWriter os = new java.io.OutputStreamWriter(fout,"utf-8");
		os.write(fileCtx);
		os.close();

	}

	


    public static String readFileCtx(String filepath)
    {
    	if(filepath == null )
    	{
    		return null;
    	}
		StringBuffer bs=new StringBuffer("");
		BufferedReader br = null;
		try
		{
			String fPath = filepath.replaceAll("\\\\/", "/");
			fPath = fPath.replaceAll("\\\\+", "/");
			File f = new File(fPath);
			if(f.exists() == false)
			{
				return null;
			}
			InputStreamReader isr=new InputStreamReader( new FileInputStream(f),"UTF-8");
		    br=new BufferedReader(isr);
		    while(true)
		    {
		    	String tempStr = br.readLine();
		    	if(tempStr != null)
		    	{
			        bs.append(tempStr+"\n");
		    	}
		    	else
		    	{
		    		break;
		    	}
		    }
		    br.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			if(br != null)
			{
				try
				{
					br.close();
				} 
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return bs.toString();
    }

    public static String readFileFromStream(InputStream input)
    { 
    	if(input == null )
    	{
    		return null;
    	}
		StringBuffer bs=new StringBuffer("");
		BufferedReader br = null;
		try
		{
			 
			InputStreamReader isr=new InputStreamReader(input);
		    br=new BufferedReader(isr);
		    while(true)
		    {
		    	String tempStr = br.readLine();
		    	if(tempStr != null)
		    	{
			        bs.append(tempStr+"\n");
		    	}
		    	else
		    	{
		    		break;
		    	}
		    }
		    br.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			if(br != null)
			{
				try
				{
					br.close();
				} 
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return bs.toString();
    }
    public static String getEditCtx(InputStream input,   HashMap<String,String>  appendCtxtMap,String encode) throws Exception
    {

        List<String> keyList = new ArrayList<String>();
 
        StringBuffer bs = new StringBuffer("");
        BufferedReader br = null;

        try
        {
            InputStreamReader isr = new InputStreamReader(input,encode);//,"utf-8");
            br = new BufferedReader(isr);
            boolean flagExists = false;
            while (true)
            {
                String tempStr = br.readLine();

                if (tempStr != null)
                {

                    Iterator<String> keyIt = appendCtxtMap.keySet().iterator();
                    while (keyIt.hasNext())
                    {

                        String key = keyIt.next();
                        if (tempStr.indexOf(key) > -1)
                        {
                            bs.append("\n" + appendCtxtMap.get(key) + "\n");
                            flagExists = true;
                        }
                    }
                    bs.append(tempStr + "\n");
                }
                else
                {
                    break;
                }
            }
           
            br.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        return bs.toString();

    }


}
