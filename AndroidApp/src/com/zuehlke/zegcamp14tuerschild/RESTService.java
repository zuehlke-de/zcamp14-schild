package com.zuehlke.zegcamp14tuerschild;

import org.apache.http.Header;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

public class RESTService extends Service {

	private final IBinder restBinder = new RESTBinder();
	private final AsyncHttpClient httpClient = new AsyncHttpClient();

	public class RESTBinder extends Binder {
		public RESTService getService() {
			return RESTService.this;
		}
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return restBinder;
	}

	public void testGet() {
		Header[] headers = {};
		//headers.add(new BasicHeader("key", "value"));
		RequestHandle handle = httpClient.get(this, "https://public.opencpu.org/ocpu/library/", headers, null, new AsyncHttpResponseHandler() {

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
