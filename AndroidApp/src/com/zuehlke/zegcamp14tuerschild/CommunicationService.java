package com.zuehlke.zegcamp14tuerschild;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class CommunicationService extends Service {

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mBluetoothDevice;
    private Handler mHandler = new Handler();
    private boolean mScanning = false;
    private BluetoothGattCharacteristic configCharacteristic;
    private BluetoothGattCharacteristic revisionNumberCharacteristic;
    private BluetoothGattCharacteristic personCharacteristic;

    //private String mBluetoothDeviceAddress = "6c00e78a-1b9c-4330-8731-54eb46af7c9f";
    private String doorPlateServiceUUID = "13370000-4200-1000-8000-00805f9b34fb";
    private String configCharacteristicUUID = "13370000-4201-1000-8000-00805f9b34fb";
    private String revisionNumberCharacteristicUUID = "13370000-4202-1000-8000-00805f9b34fb";
    private String personCharacteristicUUID = "13370000-4203-1000-8000-00805f9b34fb";
    
    private short doorPlateID;

    //private String mBluetoothDeviceAddress = "ffffffff-ffff-ffff-ffff-fffffffffff0";
    
    //UUID=13370000-4201-1000-8000-00805f9b34fb

    
    private BluetoothGatt mBluetoothGatt;
    //private int mConnectionState = STATE_DISCONNECTED;
    private final static String TAG = CommunicationService.class.getSimpleName();

    
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	private List<UUID> parseUUIDs(final byte[] advertisedData) {
		//Log.i(TAG, "adv: " + Arrays.toString(advertisedData));
	    List<UUID> uuids = new ArrayList<UUID>();

	    int offset = 0;
	    while (offset < (advertisedData.length - 2)) {
	        int len = advertisedData[offset++];
	        if (len == 0)
	            break;

	        int type = advertisedData[offset++];
	        switch (type) {
	        case 0x02: // Partial list of 16-bit UUIDs
	        case 0x03: // Complete list of 16-bit UUIDs
	            while (len > 1) {
	                int uuid16 = advertisedData[offset++];
	                uuid16 += (advertisedData[offset++] << 8);
	                len -= 2;
	                uuids.add(UUID.fromString(String.format(
	                        "%08x-0000-1000-8000-00805f9b34fb", uuid16)));
	            }
	            break;
	        case 0x06:// Partial list of 128-bit UUIDs
	        case 0x07:// Complete list of 128-bit UUIDs
	            // Loop through the advertised 128-bit UUID's.
	            while (len >= 16) {
	                try {
	                    // Wrap the advertised bits and order them.
	                    ByteBuffer buffer = ByteBuffer.wrap(advertisedData,
	                            offset++, 16).order(ByteOrder.LITTLE_ENDIAN);
	                    long mostSignificantBit = buffer.getLong();
	                    long leastSignificantBit = buffer.getLong();
	                    uuids.add(new UUID(leastSignificantBit,
	                            mostSignificantBit));
	                } catch (IndexOutOfBoundsException e) {
	                    // Defensive programming.
	                    Log.e(TAG, e.toString());
	                    continue;
	                } finally {
	                    // Move the offset to read the next uuid.
	                    offset += 15;
	                    len -= 16;
	                }
	            }
	            break;
	        default:
	            offset += (len - 1);
	            break;
	        }
	    }

	    return uuids;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return startId;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return startId;
        }
        
        Log.i(TAG, "starting scan.");
        
        scanLeDevice();
        
		return startId;
	}
	

    private void scanLeDevice() {
    	    	
        // Stops scanning after a pre-defined scan period.
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                
                if(mScanning) {
                    mBluetoothAdapter.stopLeScan(bleScanCallback);
                    mScanning = false;
                }
                
                new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						scanLeDevice();
					}
                }, 5000);
            }
        }, 10000);

        if(mBluetoothDevice == null) {
            mScanning = true;
        	mBluetoothAdapter.startLeScan(bleScanCallback);
        }
    }
	
	private final BluetoothAdapter.LeScanCallback bleScanCallback = new BluetoothAdapter.LeScanCallback() {
		@Override
		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
			List<UUID> scannedUUIDs = CommunicationService.this.parseUUIDs(scanRecord);
			//Log.i(TAG, "Devices found: "+scannedUUIDs);
			for (UUID uuid : scannedUUIDs) {
				if (uuid.toString().equals(doorPlateServiceUUID)) {
					updateRoomNameDisplay(device.getName());

					Log.i(TAG, "Device selected: "+uuid+" ("+device.getName()+") with RSSI "+rssi);
					mBluetoothDevice = device;
					
					mBluetoothAdapter.stopLeScan(bleScanCallback);
					mScanning = false;
					
					Handler handler = new Handler(Looper.getMainLooper());
					handler.post(new Runnable() {

						@Override
						public void run() {
							Log.i(TAG, "run");
			                mBluetoothAdapter.cancelDiscovery();
							mBluetoothGatt = mBluetoothDevice.connectGatt(CommunicationService.this, true, mGattCallback);							
						}
						
					});

				}
			}
		}
	
    };
    
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
    	
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                //intentAction = ACTION_GATT_CONNECTED;
                //mConnectionState = STATE_CONNECTED;
                //broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                //Log.i(TAG, "Attempting to start service discovery:" +
                mBluetoothGatt.discoverServices();

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                //intentAction = ACTION_GATT_DISCONNECTED;
                //mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                mBluetoothDevice = null;
                //scanLeDevice(true);
                
                
                /*Handler handler = new Handler(Looper.getMainLooper());
				handler.post(new Runnable() {

					@Override
					public void run() {
		                mBluetoothGatt.close();

		                mBluetoothAdapter = mBluetoothManager.getAdapter();
						mBluetoothAdapter.startLeScan(bleScanCallback);
					}
					
				});*/
				



                //broadcastUpdate(intentAction);
            }
            
        }

        @Override
        // New services discovered
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(TAG, "onServicesDiscovered received: " + status + "#services: " + gatt.getServices().size());
                for (BluetoothGattService service : gatt.getServices()) {
                	if (service.getUuid().toString().equals(doorPlateServiceUUID)) {
                		// Save characteristics
                    	for (BluetoothGattCharacteristic characteristic :service.getCharacteristics()) {
                    		if (characteristic.getUuid().toString().equals(configCharacteristicUUID)) {
                    			configCharacteristic = characteristic;
                    		}
                    		else if (characteristic.getUuid().toString().equals(revisionNumberCharacteristicUUID)) {
                    			revisionNumberCharacteristic = characteristic;
                    		}
                    		else if (characteristic.getUuid().toString().equals(personCharacteristicUUID)) {
                    			personCharacteristic = characteristic;
                    		}
                    	}
                    	// Read revision number
                    	gatt.readCharacteristic(configCharacteristic);
                	}
                	
                			
                			/*ByteBuffer buffer = ByteBuffer.allocate(2);
                			buffer.putShort(Short.reverseBytes((short)1));
                			
                			byte[] id = buffer.array();
                			byte[] roomName = "Super Raum".getBytes();
                			
                			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
                			try {
								outputStream.write(id);
		             			outputStream.write(roomName);
							} catch (IOException e) {
								e.printStackTrace();
							}   

                			byte payload[] = outputStream.toByteArray();
                			
                			characteristic.setValue(payload);
                			gatt.writeCharacteristic(characteristic);*/

                }
                //broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } 
            else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        // Result of a characteristic read operation
        public void onCharacteristicRead(BluetoothGatt gatt,
                BluetoothGattCharacteristic characteristic,
                int status) {
        	
        	if (status == BluetoothGatt.GATT_SUCCESS) {
        		if (characteristic == configCharacteristic) {
                	Log.i(TAG, "config read: " + characteristic.getStringValue(0));
                	// TODO: parse data
                	// doorPlateID = ...
                	gatt.readCharacteristic(revisionNumberCharacteristic);
            	}
            	if (characteristic == revisionNumberCharacteristic) {
            		long doorPlateRevisionNumber = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT32, 0);
                	Log.i(TAG, "revision number read: " + doorPlateRevisionNumber);
                	// TODO: if doorPlateRevisionNumber < my currently saved revision number for that room
                	// write update to door plate
            	}
            }
        }

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
        	Log.i(TAG, "characteristic write status: "+status);

			if (status == BluetoothGatt.GATT_SUCCESS) {
        		if (characteristic == configCharacteristic) {
                	Log.i(TAG, "config write successful");
            	}
        		else if (characteristic == revisionNumberCharacteristic) {
                	Log.i(TAG, "revision number write successful");
                	// TODO: PUT /updates/xxxx/status
            	}
        		else if (characteristic == personCharacteristic) {
                	Log.i(TAG, "person write successful");
            	} 
			}
			else {
        		if (characteristic == configCharacteristic) {
                	Log.i(TAG, "config write unsuccessful");
                	// TODO: close connection?
            	}
        		else if (characteristic == revisionNumberCharacteristic) {
                	Log.i(TAG, "revision number write unsuccessful");
                	// TODO: close connection?
                	// TODO: repeat later
            	}
        		else if (characteristic == personCharacteristic) {
                	Log.i(TAG, "person write unsuccessful");
                	// TODO: close connection?
            	} 
			}
		}  
    };

	@Override
	public void onDestroy() {
        Log.i(TAG, "destroyed.");
		super.onDestroy();
	}
	
	private void updateRoomNameDisplay(String roomName) {
		Intent intent = new Intent(MainActivity.UPDATE_ROOM_NAME);
	    intent.putExtra(MainActivity.EXTRA_ROOM_NAME, roomName);
	    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}
	
	private long getRevisionNumberFromDoorPlate() {
		return 0;
	}
}
