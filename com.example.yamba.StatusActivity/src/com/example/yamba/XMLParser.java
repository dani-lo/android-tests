package com.example.yamba;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.xml.parsers.*;
import javax.xml.xpath.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.content.Context;
import android.util.Log;

public class XMLParser {
	
	public XMLParser()
	{
		//context = getApplicationContext();
	}
	
	public String getXmlFromUrl(String url) {
        String xml = null;
 
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
 
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity);
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return XML
        return xml;
    }
	public String getXmlFromRawFile(String filepath, Context context) {
		StringBuilder sbTxt = new StringBuilder();
        int resId = context.getResources().getIdentifier(filepath, "raw", "com.example.yamba");
        InputStream is = context.getResources().openRawResource(resId);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr, 8192); 
        
        try {
            String test;	
            while (true){			
            	
                test = br.readLine();   
                // readLine() returns null if no more lines in the file
                if(test == null) break;
                sbTxt.append(test);
            }
            isr.close();
            is.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return XML
        return sbTxt.toString();
    }
	
	public Document getDomElement(String xml){
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
 
            DocumentBuilder db = dbf.newDocumentBuilder();
 
            InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(xml));
                doc = db.parse(is); 
 
            } catch (ParserConfigurationException e) {
                Log.e("Error: ", e.getMessage());
                return null;
            } catch (SAXException e) {
                Log.e("Error: ", e.getMessage());
                return null;
            } catch (IOException e) {
                Log.e("Error: ", e.getMessage());
                return null;
            }
                // return DOM
            return doc;
    }
	
	public ArrayList<String> xmlToArrayList(Document doc, String xpathExpString) throws XPathExpressionException{
		ArrayList<String> outList = new ArrayList<String>();
		
		XPathFactory factory = XPathFactory.newInstance();
	    XPath xpath = factory.newXPath();
	    XPathExpression expr = xpath.compile(xpathExpString);
	
	    Object result = expr.evaluate(doc, XPathConstants.NODESET);
	    NodeList nodes = (NodeList) result;
	    for (int i = 0; i < nodes.getLength(); i++) {
	        outList.add(nodes.item(i).getNodeValue()); 
	    }
		
		return outList;
	}
    
    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }
     
    public final String getElementValue( Node elem ) {
             Node child;
             if( elem != null){
                 if (elem.hasChildNodes()){
                     for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
                         if( child.getNodeType() == Node.TEXT_NODE  ){
                             return child.getNodeValue();
                         }
                     }
                 }
             }
             return "";
    }
}
