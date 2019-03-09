/*
 * ====================================================================
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 * 
 */

package com.easycode.common;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.easycode.templatemgr.RpcFactory;

/**
 * Example how to use multipart/form encoded POST request.
 */
public class HttpUtil
{

    public static void main(String[] args) throws Exception
    {

        RpcFactory.httpSrv(
                "http://www.testjeasycode.org/TemplateEngine/codemgr")
                .regCodegenSeed("guest", "guest");
    }

    /**
     * 将结果自动解压缩
     * 
     * @param url
     * @param blist
     * @return
     */
    public static String httpPostWithUnzipResult(String url,
            List<FormItem> blist) throws Exception
    {
        byte b[] = httpPost(url, blist);
        return ZipUtil.unzip(b);
    }

    public static byte[] httpPost(String url, List<FormItem> blist)
            throws Exception
    {
        byte[] ret = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        MultipartEntity reqEntity = new MultipartEntity(
                HttpMultipartMode.BROWSER_COMPATIBLE, null,
                Charset.forName("UTF-8"));
        if (blist != null)
        {
            for (FormItem f : blist)
            {
                reqEntity.addPart(f.getName(), f.getCtx());
            }
        }
        httppost.setEntity(reqEntity);
        HttpResponse response = httpclient.execute(httppost);

        HttpEntity resEntity = response.getEntity();

        if (resEntity != null)
        {
            InputStream tis = resEntity.getContent();

            java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = tis.read(bytes)) > 0)
            {
                out.write(bytes, 0, len);
            }
            ret = out.toByteArray();
        }
        EntityUtils.consume(resEntity);
        try
        {
            httpclient.getConnectionManager().shutdown();
        }
        catch (Exception ignore)
        {
        }
        return ret;
    }

    public static class FormItem
    {
        public FormItem()
        {

        }

        /**
         * 构造函数
         * 
         * @param name
         * @param byteCtx
         * @param zip
         *            true 进行压缩，false,不压缩
         */
        public FormItem(String name, byte byteCtx[], boolean zip)
                throws Exception
        {
            this.name = name;

            if (zip)
            {
                ZipUtil uti = new ZipUtil();
                uti.addZip(name, byteCtx);
                uti.finish();

                this.ctx = new ByteArrayBody(uti.getCompressedByte(), name);

            }
            else
            {
                this.ctx = new ByteArrayBody(byteCtx, name);
            }

        }

        /**
         * 构造函数,strCtx为字符串，则直接创建
         * 
         * @param name
         * @param strCtx
         */
        public FormItem(String name, String strCtx)
        {
            this.name = name;
            try
            {
                this.ctx = new StringBody(strCtx, Charset.forName("UTF-8"));

            }
            catch (UnsupportedEncodingException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        private String name = "";
        private AbstractContentBody ctx = null;

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public AbstractContentBody getCtx()
        {
            return ctx;
        }

        public void setCtx(AbstractContentBody ctx)
        {
            this.ctx = ctx;
        }

    }

}
