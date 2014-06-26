package com.zuehlke.zegcamp14tuerschild;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class RESTManager {

    private final static String TAG = RESTManager.class.getSimpleName();
	
	private final String baseURL = "http://camp14loc.apiary-mock.com";
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
	
	public interface RESTCallbacks {
		public void onSuccess(JSONArray response);
	}
	
	public void requestSetMoves(Context context, String userName, int plateId) {
		String url = baseURL+"/moves";
		JSONObject payload = new JSONObject();
		try {
			payload.put("userId", userName);
			payload.put("plateId", plateId);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		Log.d(TAG, "POST "+url+" "+payload.toString());
		try {
			httpClient.post(context, url, new StringEntity(payload.toString()), "application/json", new JsonHttpResponseHandler() {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers,	JSONObject response) {
					Log.d(TAG, statusCode+" "+response);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,	String responseString, Throwable throwable) {
					Log.d(TAG, throwable.getLocalizedMessage());
					Log.d(TAG, responseString);
				}

			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public void requestSetLocation(Context context, String userName, int plateId) {
		String url = baseURL+"/users/"+userName+"/location";
		JSONObject payload = new JSONObject();
		try {
			payload.put("plateId", plateId);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		Log.d(TAG, "PUT "+url+" "+payload.toString());

		try {
			httpClient.put(context, url, new StringEntity(payload.toString()), "application/json", new JsonHttpResponseHandler() {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers,	JSONObject response) {
					Log.d(TAG, statusCode+" "+response);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,	String responseString, Throwable throwable) {
					Log.d(TAG, throwable.getLocalizedMessage());
					Log.d(TAG, responseString);
				}

			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public void requestGetUpdatesPending(final RESTCallbacks callbacks) {
		//Header[] headers = {new BasicHeader("Content-Type", "application/json")};
		String url = baseURL+"/updates/pending";
		Log.d(TAG, "GET "+url);
		
		httpClient.get(url, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,	JSONArray response) {
				Log.d(TAG, response.toString());
				UpdateManager.getInstance().handleUpdate(response);
				callbacks.onSuccess(response);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				Log.d(TAG, throwable.getLocalizedMessage());
				Log.d(TAG, responseString);
			}	
			
			
		});
	}
	
	public void requestSetUpdateStatus(Context context, int updateId, String status) {
		try {
			String url = baseURL+"/updates/"+updateId+"/status";
			JSONObject payload = new JSONObject();
			try {
				payload.put("status", status);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			Log.d(TAG, "PUT "+url+" "+payload.toString());

			httpClient.put(context, url, new StringEntity(payload.toString()), "application/json", new JsonHttpResponseHandler() {
				
				@Override
				public void onSuccess(int statusCode, Header[] headers,	JSONObject response) {
					Log.d(TAG, statusCode+" "+response);
				}

				@Override
				public void onFailure(int statusCode, Header[] headers,	String responseString, Throwable throwable) {
					Log.d(TAG, throwable.getLocalizedMessage());
					Log.d(TAG, responseString);
				}

			});
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
