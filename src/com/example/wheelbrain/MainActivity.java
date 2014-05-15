package com.example.wheelbrain;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.wheelphone.wheelphonelibrary.WheelphoneRobot;
import com.wheelphone.wheelphonelibrary.WheelphoneRobot.WheelPhoneRobotListener;

public class MainActivity extends Activity implements WheelPhoneRobotListener
{
	private Context context;
	WheelphoneRobot wheelphone;
	
	private int firmwareVersion=0;
	boolean getFirmwareFlag = true;

	private String TAG = "wheelbrain";
	private String logString;
	private boolean debugUsbComm = false;
	
	//UI
	private TextView connectionState;
	
	//move 
	private boolean isConnect = false;
	private int moveTimeCounter = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		context = this.getApplicationContext();
		
		connectionState = (TextView)findViewById(R.id.connectionState);
		
        wheelphone = new WheelphoneRobot(getApplicationContext(), getIntent());
        wheelphone.enableSpeedControl();
        wheelphone.enableSoftAcceleration();
        //wheelphone.enableObstacleAvoidance();
        //wheelphone.enableCliffAvoidance();
	}

	@Override
	public void onStart() {
		if(debugUsbComm) {
			logString = TAG + ": onStart";
			Log.d(TAG, logString);
			appendLog("debugUsbComm.txt", logString, false);
		}		
		super.onStart();	
	}
	
    @Override
    public void onResume() {
		if(debugUsbComm) {
			logString = TAG + ": onResume";
			Log.d(TAG, logString);
			appendLog("debugUsbComm.txt", logString, false);
		}    	
    	super.onResume();
    	wheelphone.startUSBCommunication();
    	wheelphone.setWheelPhoneRobotListener(this);
    }	
    
    @Override
    public void onStop() {
		if(debugUsbComm) {
			logString = TAG + ": onStop";
			Log.d(TAG, logString);
			appendLog("debugUsbComm.txt", logString, false);
		}    	
    	super.onStop();    	
    	android.os.Process.killProcess(android.os.Process.myPid());
    }
    
    @Override
    public void onPause() {
		if(debugUsbComm) {
			logString = TAG + ": onPause";
			Log.d(TAG, logString);
			appendLog("debugUsbComm.txt", logString, false);
		}    	
    	super.onPause();
    	wheelphone.closeUSBCommunication();
    	wheelphone.setWheelPhoneRobotListener(null);
    }
	
	@Override
	public void onWheelphoneUpdate() {
		Log.v(TAG, "onWheelphoneUpdate");
		
		if(getFirmwareFlag) {
			firmwareVersion=wheelphone.getFirmwareVersion();
			if(firmwareVersion>0) {	// wait for the first USB transaction to be accomplished
				getFirmwareFlag = false;
				if(firmwareVersion >= 3) {
					Toast.makeText(MainActivity.this, "Firmware version "+firmwareVersion+".0, fully compatible.", Toast.LENGTH_SHORT).show();
				} else {
					msgbox("Firmware version "+firmwareVersion+".0", "Firmware is NOT fully compatible. Update robot firmware.");
				}
			}
		}
		
		if(wheelphone.isRobotConnected()) {
			connectionState.setText("Connected");
			connectionState.setTextColor(getResources().getColor(R.color.green));
			isConnect = true;
			wheelphone.setRawSpeed(-60, 60);
		} else {
			connectionState.setText("Disconnected");
			connectionState.setTextColor(getResources().getColor(R.color.red));
			isConnect = false;
		}
		
		moveTimeCounter++;
		
		if(moveTimeCounter == 20) {
			wheelphone.setRawSpeed(0, 0);
		}
	}
	
	public void msgbox(String title,String msg) {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);                      
        dlgAlert.setTitle(title); 
        dlgAlert.setMessage(msg); 
        dlgAlert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                 //finish(); 
            }
       });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
		
	void appendLog(String fileName, String text, boolean clearFile)
	{       
	   File logFile = new File("sdcard/" + fileName);
	   if (!logFile.exists()) {
	      try
	      {
	         logFile.createNewFile();
	      } 
	      catch (IOException e)
	      {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      }
	   } else {
		   if(clearFile) {
			   logFile.delete();
			   try {
				   logFile.createNewFile();
			   } catch (IOException e) {
				   // TODO Auto-generated catch block
				   e.printStackTrace();
			   }
		   }
	   }
	   try
	   {
	      //BufferedWriter for performance, true to set append to file flag
	      BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true)); 
	      buf.append(text);
	      buf.newLine(); 
	      buf.close();
	   }
	   catch (IOException e)
	   {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	   }
	}

}
