package com.easycode.templatemgr.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

	private ByteArrayOutputStream out = null;
	private ZipOutputStream zout = null;
	public ZipUtil()
	{
		  out = new ByteArrayOutputStream();
          zout = new ZipOutputStream(out);
	}
	public ZipUtil addZip(String name,byte src[]) throws Exception
	{
        zout.putNextEntry(new ZipEntry(name));
        zout.write(src);
		return this;
	}
	public byte[] getCompressedByte()
	{
		return out.toByteArray();
	}
	public void finish()
	{
		
		if (zout != null)
        {
            try
            {
            	zout.flush();
            	zout.closeEntry();
                zout.close();
            } catch (Exception e)
            {
            }
        }
        if (out != null)
        {
            try
            {
                out.close();
            } catch (Exception e)
            {
            }
        }
	}

 
	public static final String unzip(byte[] compressedByte) throws Exception{

		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		ZipInputStream zin = null;
		String decompressed = null;
		try {

			out = new ByteArrayOutputStream();
			in = new ByteArrayInputStream(compressedByte);
			zin = new ZipInputStream(in);
			ZipEntry entry = zin.getNextEntry(); 
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = zin.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = new String(out.toByteArray(),"utf-8");
		} catch (Exception e) {
			decompressed = null;
		} finally {
			if (zin != null) {
				try {
					zin.close();
				} catch (Exception e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}
		return decompressed;
	}
	public static final String unzip(byte[] compressedByte, String name) throws Exception{

		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		ZipInputStream zin = null;
		String decompressed = null;
		try {

			out = new ByteArrayOutputStream();
			in = new ByteArrayInputStream(compressedByte);
			zin = new ZipInputStream(in);
			ZipEntry entry = zin.getNextEntry(); 
			while(!name.equalsIgnoreCase(entry.getName()))
			{
				entry = zin.getNextEntry(); 
			}
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = zin.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = new String(out.toByteArray(),"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			decompressed = null;
		} finally {
			if (zin != null) {
				try {
					zin.close();
				} catch (Exception e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}
		return decompressed;
	}
	public static final Map<String,String> unzipItems(byte[] compressedByte) throws Exception{

		//System.err.println("length:->"+compressedByte.length);
		Map<String,String> retMap = new HashMap<String,String>();
		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		ZipInputStream zin = null;
		 
		try {

			
			in = new ByteArrayInputStream(compressedByte);
			zin = new ZipInputStream(in);
		 
			while(true)
           {
				ZipEntry entry = zin.getNextEntry();
				if (entry == null) {
					break;
				}

				out = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int offset = -1;
				while ((offset = zin.read(buffer)) != -1) {
					out.write(buffer, 0, offset);
				}
				String  decompressed = new String(out.toByteArray(), "utf-8");
				retMap.put(entry.getName(), decompressed);
			}
		} catch (Exception e) {
		 
			e.printStackTrace();
		} finally {
			if (zin != null) {
				try {
					zin.close();
				} catch (Exception e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}
		return retMap;
	}
	public static void main(String arg[]) throws Exception
	{
		String src = "测试";
		String src2 = "测试个ggg";
		ZipUtil u = new ZipUtil();
		u.addZip("a", src.getBytes());
		u.addZip("b", src2.getBytes());
		u.finish();
		byte[] compred = u.getCompressedByte();
		System.err.println(ZipUtil.unzip(compred,"b"));
	}
}
