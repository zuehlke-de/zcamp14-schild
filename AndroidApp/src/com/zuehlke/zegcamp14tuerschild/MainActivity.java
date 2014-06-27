package com.zuehlke.zegcamp14tuerschild;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	public static final String EXTRA_ROOM_NAME = "com.zuehlke.zegcamp14tuerschild.ROOM_NAME";
	public static final String UPDATE_ROOM_NAME = "com.zuehlke.zegcamp14tuerschild.UPDATE_ROOM_NAME";
	public static String longRoomName;
	public static String userName;

	private BroadcastReceiver bReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        if (intent.getAction().equals(UPDATE_ROOM_NAME)) {
	            String roomName = intent.getStringExtra(EXTRA_ROOM_NAME);
	            TextView textView = (TextView) findViewById(R.id.roomName);
	            //roomName = "Raum 43"; // TODO: remove if there
	            textView.setText(roomName);
	            longRoomName = roomName;
	        }
	    }
	};

    static BluetoothAdapter mBluetoothAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
         
        //RESTManager.getInstance().testGet(this);
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE NOT SUPPORTED", Toast.LENGTH_SHORT).show();
            finish();
        }
         
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
         
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 0);
        }
        
    	Intent intent = new Intent(this, CommunicationService.class);
    	this.startService(intent);
    	
    	LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
    	IntentFilter intentFilter = new IntentFilter();
    	intentFilter.addAction(UPDATE_ROOM_NAME);
    	bManager.registerReceiver(bReceiver, intentFilter);
    	
    }

    public void onClick(View view) {
    	Intent intent = new Intent(this, SecondActivity.class);
    	//EditText editText = (EditText) findViewById(R.id.edit_message);
    	//String message = editText.getText().toString();
    	//intent.putExtra(EXTRA_MESSAGE, message);
    	startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        @Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			getActivity().findViewById(R.id.userId).setOnKeyListener(new OnKeyListener()
        	{
        	    public boolean onKey(View v, int keyCode, KeyEvent event)
        	    {
        	        if (event.getAction() == KeyEvent.ACTION_DOWN)
        	        {
        	            switch (keyCode)
        	            {
        	                case KeyEvent.KEYCODE_DPAD_CENTER:
        	                case KeyEvent.KEYCODE_ENTER:
        	                    userName = ((EditText)getActivity().findViewById(R.id.userId)).getText().toString();
        	                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
        	                    	      Context.INPUT_METHOD_SERVICE);
        	                    	imm.hideSoftInputFromWindow(getActivity().findViewById(R.id.userId).getWindowToken(), 0);
        	                    return true;
        	                default:
        	                    break;
        	            }
        	        }
        	        return false;
        	    }
        	});
		}

		public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
          	
            return rootView;
        }
        
        
    }
    
    public void moveToCurrentRoom(View view) {
		if (longRoomName != null && longRoomName.length() > 5 && userName != null && userName.length() > 0) {
	    	RESTManager.getInstance().requestSetMoves(this, userName, getPlateId());
		}
    }
    
    public int getPlateId() {
    	if (longRoomName != null && longRoomName.length() > 5) {
         	String plateIdString = longRoomName.substring(5);
         	int plateId = 0;
        	try {
        		plateId = Integer.parseInt(plateIdString);
        	} catch (NumberFormatException e) {
        		e.printStackTrace();
        	}
         	return plateId;
    	}
    	return 0;
    }

}
