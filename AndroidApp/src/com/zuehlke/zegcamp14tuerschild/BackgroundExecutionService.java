package com.zuehlke.zegcamp14tuerschild;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BackgroundExecutionService extends Service {

	private CommunicationWorker commWorker;
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		commWorker = new CommunicationWorker();
		commWorker.start();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		System.out.println("Background service destroyed.");
		super.onDestroy();
	}
	
	
	

}
