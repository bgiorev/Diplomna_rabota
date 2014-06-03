package wordpress.bgiorev.healthdevice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.wordpress.bgiorev.healthDevice.models.BloodPressureModel;
import com.wordpress.bgiorev.healthDevice.models.BloodSaturationModel;
import com.wordpress.bgiorev.healthDevice.models.BodyTemperatureModel;
import com.wordpress.bgiorev.healthDevice.models.HeartRateModel;
import com.wordpress.bgiorev.healthDevice.services.*;
import com.wordpress.bgiorev.healthDevice.services.AsyncService.AsyncServiceCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class MainActivity extends Activity implements OnTouchListener{

	private static final int REQUEST_ENABLE_BT = 1;
	private static final String BT_DEVICE_ADDRESS = "4C:80:93:56:9D:79";
	
	private float downXValue;
	private BluetoothAdapter btAdapter;
	private DisasterChecker disasterChecker;
	
	public static UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	public static final int CONNECTED = 1;
	public static final int CONNECTING = 0;
	public static final int READING_FINISHED_SUCCESSFUL = 2;
	
	private ConnectThread connectThread;
	private ConnectedThread connectedThread;
	private DatabaseHandler db;
	
	private AsyncService service;
	
	private TextView temperatureTextView;
	private TextView bloodPressureTopBorderTextView;
	private TextView bloodPressureBottomBorderTextView;
	private TextView heartRateTextView;
	private TextView bloodSaturationTextView;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case CONNECTING:
				connectedThread = new ConnectedThread((BluetoothSocket) msg.obj);
				connectedThread.start();
				String string = "Success finally!";
				connectedThread.write(string.getBytes());
				break;
			case READING_FINISHED_SUCCESSFUL:
				Toast.makeText(getApplicationContext(), "Reading finished successfully", Toast.LENGTH_SHORT).show();
				connectThread.cancel();
				connectedThread.cancel();
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        init();
	}
	
	private void init() {
		LinearLayout layMain = (LinearLayout) findViewById(R.id.layout_main);
		temperatureTextView = (TextView) findViewById(R.id.bodyTemperature);
		bloodPressureTopBorderTextView = (TextView) findViewById(R.id.bloodPressureTopBorder);
		bloodPressureBottomBorderTextView = (TextView) findViewById(R.id.bloodPressureBottomBorder);
		heartRateTextView = (TextView) findViewById(R.id.heartRate);
		bloodSaturationTextView = (TextView) findViewById(R.id.bloodSaturation);
		disasterChecker = new DisasterChecker(getApplicationContext());
        layMain.setOnTouchListener((OnTouchListener)this);
        db = new DatabaseHandler(this);
        makeConnection();
      
    }

	private void changeValues(int id, String value) {
		switch(id){
       		case 1: temperatureTextView.setText(value);
       				BodyTemperatureModel bodyTemperature = new BodyTemperatureModel(1, Double.parseDouble(value));
       				db.updateBodyTemperature(bodyTemperature);
       				sendToServer(bodyTemperature.getId(), bodyTemperature.getBodyTemperature());
       				break;
       		case 2:	String[] values = value.split("\\s+");
       				bloodPressureTopBorderTextView.setText(values[0]);
       				bloodPressureBottomBorderTextView.setText(values[1]);
       				BloodPressureModel bloodPressure = new BloodPressureModel(2,Integer.parseInt(values[0]), Integer.parseInt(values[1]));
       				db.updateBloodPressure(bloodPressure);
       				sendToServer(bloodPressure.getId(), bloodPressure.getTopBorder(), bloodPressure.getBottomBorder());
       				break;
       		case 3: heartRateTextView.setText(value);
       				HeartRateModel heartRate = new HeartRateModel(3,Integer.parseInt(value));
       				db.updateHeartRate(heartRate);
       				sendToServer(heartRate.getId(), heartRate.getHeartRate());
       				break;
       		case 4: bloodSaturationTextView.setText(value);
       				BloodSaturationModel bloodSaturation = new BloodSaturationModel(4,Double.parseDouble(value));
       				db.updateBloodSaturation(bloodSaturation);
       				sendToServer(bloodSaturation.getId(), bloodSaturation.getSaturation());
       				break;
   		}
		disasterChecker.checkForDisaster(Integer.parseInt((String) heartRateTextView.getText()), 
				Double.parseDouble((String) temperatureTextView.getText()), 
				Integer.parseInt((String) bloodPressureTopBorderTextView.getText()), 
				Integer.parseInt((String) bloodPressureBottomBorderTextView.getText()), 
				Integer.parseInt((String) bloodSaturationTextView.getText()));
		if(disasterChecker.isDisaster()) {
			Toast.makeText(getApplicationContext(), "Disaster!!!", Toast.LENGTH_SHORT).show();
		}
	}

	private void makeConnection() {
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		if(!btAdapter.isEnabled()) {
			turnOnBt();
		}else{
			BluetoothDevice device = btAdapter.getRemoteDevice(BT_DEVICE_ADDRESS);
			connectThread = new ConnectThread(device);
			connectThread.start();
		}
	}
	
	private void turnOnBt() {
		Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_ENABLE_BT){
			if(resultCode == RESULT_OK){
				makeConnection();
			}
			if(resultCode == RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(), "Your bluetooth is turned off, please turn it on", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	private void sendToServer(int id, double value) {
		service = new AsyncService(getApplicationContext());
		Map<String, String> requestMap = new HashMap<String,String>();
		requestMap.put("id", Integer.toString(id));
		requestMap.put("value", Double.toString(value));
		
		Gson gson = new GsonBuilder().create();
		String params = gson.toJson(requestMap);
		service.doRequest(params, new AsyncServiceCallback() {

			@Override
			public void onResult(String content) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onError(String message) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Something went wrong " + message, Toast.LENGTH_LONG).show();
			}
			
		});

	}
	
	private void sendToServer(int id, int value, int value2) {
		service = new AsyncService(getApplicationContext());
		Map<String, String> requestMap = new HashMap<String,String>();
		requestMap.put("id", Integer.toString(id));
		requestMap.put("value", Integer.toString(value));
		requestMap.put("value2", Integer.toString(value2));
		Gson gson = new GsonBuilder().create();
		String params = gson.toJson(requestMap);
		service.doRequest(params, new AsyncServiceCallback() {
			
			@Override
			public void onResult(String content) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onError(String message) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Something went wrong " + message, Toast.LENGTH_LONG).show();
			}
			
		});
	}
	
	
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		 // Get the action that was done on this touch event
        switch (arg1.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                // store the X value when the user's finger was pressed down
                downXValue = arg1.getX();
                break;
            }

            case MotionEvent.ACTION_UP:
            {
                // Get the X value when the user released his/her finger
                float currentX = arg1.getX();            
                ViewFlipper vf = (ViewFlipper) findViewById(R.id.views);
                // going backwards: pushing stuff to the right
                if (downXValue < currentX-30)
                {
                     // Set the animation
                      vf.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_right));
                      // Flip!
                      vf.showPrevious();
                }

                // going forwards: pushing stuff to the left
                if (downXValue > currentX + 30)
                {
                     // Set the animation
                     vf.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_left));
                      // Flip!
                     vf.showNext();
                }
                break;
            }
        }
        // if you return false, these actions will not be recorded
        return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(connectedThread !=null)
			connectedThread.cancel();
		if(connectThread != null)
			connectThread.cancel();
	}
	
	private class ConnectThread extends Thread {
	    private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;
	 
	    public ConnectThread(BluetoothDevice device) {
	        // Use a temporary object that is later assigned to mmSocket,
	        // because mmSocket is final
	        BluetoothSocket tmp = null;
	        	mmDevice = device;
	 
	        // Get a BluetoothSocket to connect with the given BluetoothDevice
	        try {
	            // MY_UUID is the app's UUID string, also used by the server code
	            tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
	        } catch (IOException e) { }
	        mmSocket = tmp;
	    }
	 
	    public void run() {
	        try {
	            // Connect the device through the socket. This will block
	            // until it succeeds or throws an exception
	            mmSocket.connect();
	        } catch (IOException connectException) {
	            // Unable to connect; close the socket and get out
	            try {
	                mmSocket.close();
	            } catch (IOException closeException) { }
	            return;
	        }
	 
	        // Do work to manage the connection (in a separate thread)
	        mHandler.obtainMessage(CONNECTING, mmSocket).sendToTarget();
	    }
	 
	    /** Will cancel an in-progress connection, and close the socket */
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
	    }
	}
	
	
	private class ConnectedThread extends Thread {
	    private final BluetoothSocket mmSocket;
	    private final InputStream mmInStream;
	    private final OutputStream mmOutStream;
	 
	    public ConnectedThread(BluetoothSocket socket) {
	        mmSocket = socket;
	        InputStream tmpIn = null;
	        OutputStream tmpOut = null;
	 
	        // Get the input and output streams, using temp objects because
	        // member streams are final
	        try {
	            tmpIn = socket.getInputStream();
	            tmpOut = socket.getOutputStream();
	        } catch (IOException e) { }
	 
	        mmInStream = tmpIn;
	        mmOutStream = tmpOut;
	    }
	 
	    public void run() {
	    	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(mmInStream));
	    	String line = "";
	    	try {
				while(!((line = bufferedReader.readLine()).equals("close"))) {
					final int id = Integer.parseInt(line);
					line = bufferedReader.readLine();
					final String value = line;
					runOnUiThread(new Runnable() {
						public void run() {
							changeValues(id, value);	
						}
						
					});
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	//mHandler.obtainMessage(READING_FINISHED_SUCCESSFUL).sendToTarget();
	    }
	 
	    /* Call this from the main activity to send data to the remote device */
	    public void write(byte[] bytes) {
	        try {
	            mmOutStream.write(bytes);
	        } 
	        catch (IOException e) { }
	    }
	    
	 
	    /* Call this from the main activity to shutdown the connection */
	    public void cancel() {
	    	try {
	            mmSocket.close();
	        } catch (IOException e) { }
	    }
	}

}
