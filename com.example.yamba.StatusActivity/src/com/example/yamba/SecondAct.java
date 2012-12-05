package com.example.yamba;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Environment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;

public class SecondAct extends Activity 
{
	private static final String TAG = "MEDIA";
    private TextView tv;
	private XMLParser xmlParser;
	private ListView ordersListView ;
	private ArrayAdapter<String> listAdapter ;
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_act);     
        tv = (TextView) findViewById(R.id.TextViewFs);
        tvXmlTarget = (TextView) findViewById(R.id.tvXmlTarget);
        checkExternalMedia();
        writeToSDFile();
        readRaw();
        listOrders();
        
    }
    
    /** Method to check whether external media available and writable. This is adapted from
    http://developer.android.com/guide/topics/data/data-storage.html#filesExternal */
    
    private void checkExternalMedia(){
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Can't read or write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }   
        tv.append("\n\nExternal Media: readable = "
                    +mExternalStorageAvailable+" writable = "+mExternalStorageWriteable);
    }
    
    /** Method to write ascii text characters to file on SD card. Note that you must add a 
    WRITE_EXTERNAL_STORAGE permission to the manifest file or this method will throw
    a FileNotFound Exception because you won't have write permission. */
    
    private void writeToSDFile()
    {
            
        // Find the root of the external storage.
        // See http://developer.android.com/guide/topics/data/data-storage.html#filesExternal
                
        File root = android.os.Environment.getExternalStorageDirectory(); 
        tv.append("\nExternal file system root: "+root);
        
        // See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder
        
        File dir = new File (root.getAbsolutePath() + "/download");
        dir.mkdirs();
        File file = new File(dir, "myData.txt");
    
        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            pw.println("Howdy do to you.");
            pw.println("Here is a second line.");
            pw.flush();
            pw.close();
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, "******* File not found. Did you" +
                            " add a WRITE_EXTERNAL_STORAGE permission to the manifest?");
        } catch (IOException e) {
            e.printStackTrace();
        }	
        tv.append("\n\nFile written to "+file);
    }
    
    private void listOrders(){
    	XMLParser xmlParser = new XMLParser();
        Document xmlDoc = xmlParser.getDomElement(xmlParser.getXmlFromRawFile("orders",getApplicationContext()));          
        ordersListView = (ListView) findViewById( R.id.listViewOrders );
        try {         
        	ArrayList<String> ordersList = xmlParser.xmlToArrayList(xmlDoc,"//menu/item/name/text()");
        	listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, ordersList);
        	ordersListView.setAdapter( listAdapter ); 
        } catch(XPathExpressionException e) {
        	Log.v(TAG,"XPath exception thrown here");
        }
    }
    
    private void readRaw(){
        tv.append("\nData read from res/raw/textfile.txt:");
        InputStream is = this.getResources().openRawResource(R.raw.textfile);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr, 8192);    // 2nd arg is buffer size
        
        // More efficient (less readable) implementation of above is the composite expression
        /*BufferedReader br = new BufferedReader(new InputStreamReader(
                        this.getResources().openRawResource(R.raw.textfile)), 8192);*/
        
        try {
            String test;	
            while (true){				
                test = br.readLine();   
                // readLine() returns null if no more lines in the file
                if(test == null) break;
                tv.append("\n"+"    "+test);
            }
            isr.close();
            is.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tv.append("\n\nThat is all");
        
    }
}