package com.easycode.common;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.output.DOMOutputter;

public class XmlUtil
{
    public static void saveXML(Document doc, String filePath, Callback back)
    {
        FileWriter writer = null;
        FileOutputStream out = null;
        try
        {

            Transformer tf = TransformerFactory.newInstance().newTransformer();
            Properties properties = tf.getOutputProperties();
            properties.setProperty(OutputKeys.ENCODING, "UTF-8");
            properties.setProperty(OutputKeys.INDENT, "yes");
            tf.setOutputProperties(properties);
            Source xmlSource = new DOMSource(convertToDOM(doc));
            out = new FileOutputStream(filePath);
            Result result = new StreamResult(out);
            tf.transform(xmlSource, result);
            out.close();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (writer != null)
            {
                try
                {
                    writer.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (back != null)
            {
                back.doback();
            }
        }
    }

    public static org.w3c.dom.Document convertToDOM(Document jdomDoc)
            throws JDOMException
    {

        DOMOutputter outputter = new DOMOutputter();
        return outputter.output(jdomDoc);
    }

    public interface Callback
    {
        public void doback();
    }
}
