package com.zuehlke.zegcamp14tuerschild;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zuehlke.zegcamp14tuerschild.RESTManager.RESTCallbacks;

import android.util.SparseArray;


public class UpdateManager {

	private static UpdateManager instance;
	private SparseArray<JSONObject> dataStore;
		
	public static UpdateManager getInstance() {
		if(instance == null) {
			instance = new UpdateManager();
		}
		return instance;
	}
	
	public interface RequestUpdateDataCallback {
		public void onSuccess(JSONObject object);
	}
	
	public void getUpdateDataForPlate(final int plateId, final RequestUpdateDataCallback successCallback) {
		if(dataStore == null) {
			RESTManager.getInstance().requestGetUpdatesPending(new RESTCallbacks() {
				@Override
				public void onSuccess(JSONArray response) {
					successCallback.onSuccess((dataStore.get(plateId)));
				}
			});
		}
		else {
			successCallback.onSuccess((dataStore.get(plateId)));
		}
	}
	
	public void handleUpdate(JSONArray update) {
		if(dataStore == null) {
			dataStore = new SparseArray<JSONObject>();
		}
		for(int i=0;i<update.length();i++) {
			try {
				dataStore.put(update.getJSONObject(i).getInt("plateId"), update.getJSONObject(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
}
