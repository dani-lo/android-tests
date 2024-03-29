package com.example.yamba;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;

public class StatusActivity extends Activity implements OnClickListener, TextWatcher
{
	
	private static final String TAG  = "StatusActivity";
	EditText editText;
	Button updateButton;
	Button buttonSecondAct;
	Twitter twitter;
	TextView textCount;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        
        editText = (EditText) findViewById(R.id.editText);
        
        updateButton = (Button) findViewById(R.id.buttonUpdate);
        updateButton.setOnClickListener((OnClickListener) this);
        
        buttonSecondAct = (Button) findViewById(R.id.buttonSecondAct);
        buttonSecondAct.setOnClickListener((OnClickListener) this);
        
        textCount = (TextView) findViewById(R.id.textCount);
        textCount.setText(Integer.toString(140));
        textCount.setTextColor(Color.GREEN);
        editText.addTextChangedListener(this); 
        
        //twitter = new Twitter("danielhaus00", "dan13lhaus00");
        twitter = new Twitter("student", "password");
        twitter.setAPIRootUrl("http://yamba.marakana.com/api");
    }
    
    class PostToTwitter extends AsyncTask<String, Integer, String> { //
    	// Called to initiate the background activity
    	
    	@Override
    	protected String doInBackground(String... statuses) { //
	    	try {
	    		winterwell.jtwitter.Status status = twitter.updateStatus(statuses[0]);
	    		return status.text;
	    		//return "TMP test";
	    	} catch (TwitterException e) {
	    		Log.e(TAG, e.toString());
	    		e.printStackTrace();
	    		return "Failed to post";
	    	}
    	}
    	
    	// Called when there's a status to be updated
    	@Override
    	protected void onProgressUpdate(Integer... values) { //
    		super.onProgressUpdate(values);
    	// Not used in this case
    	}
    	// Called once the background activity has completed
    	@Override
    	protected void onPostExecute(String result) { //
    		Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
    	}

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_status, menu);
        return true;
    }
    
 // Called when button is clicked
    public void onClick(View v) {
    	switch(v.getId()) {
	        case R.id.buttonUpdate: 
	        	String status = editText.getText().toString();
	        	new PostToTwitter().execute(status); //
	        	Log.d(TAG, "onClicked");
	        break;
	        case R.id.buttonSecondAct:
	        	startActivity(new Intent(this, SecondAct.class));
	        break;
    	}
    }
    
 // TextWatcher methods
    public void afterTextChanged(Editable statusText) { //
	    int count = 140 - statusText.length(); //
	    textCount.setText(Integer.toString(count));
	    textCount.setTextColor(Color.GREEN); //
	    if (count < 10)
	    textCount.setTextColor(Color.YELLOW);
	    if (count < 0)
	    textCount.setTextColor(Color.RED);
    }
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { //
    }
    public void onTextChanged(CharSequence s, int start, int before, int count) { //
    }
}