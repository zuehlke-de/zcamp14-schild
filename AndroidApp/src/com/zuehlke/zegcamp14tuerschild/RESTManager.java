package com.zuehlke.zegcamp14tuerschild;

import org.apache.http.Header;

import android.app.Activity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

public class RESTManager {

	private static RESTManager instance;
	private AsyncHttpClient httpClient;
	
	private RESTManager() {
		httpClient = new AsyncHttpClient();
	}
	
	public static RESTManager getInstance() {
		if(instance == null) {
			instance = new RESTManager();
		}
		return instance;
	}
	
	public void testGet(Activity activity) {
		Header[] headers = {};
		//headers.add(new BasicHeader("key", "value"));
		
		for(int i=0;i<10;i++) {
			RequestHandle handle = httpClient.get(activity, "http://www.google.de", headers, null, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess(int statusCode, Header[] headers,
						byte[] responseBody) {
					// TODO Auto-generated method stub
					System.out.println(new String(responseBody));
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,
						byte[] responseBody, Throwable error) {
					// TODO Auto-generated method stub
					
				}
				
			});
		}

	}
	
}
