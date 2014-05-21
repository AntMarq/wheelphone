package com.example.wheelbrain;

import instructions.Instruction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wheelbrain.network.IRequestListener;
import com.example.wheelbrain.network.RequestManager;
import com.wheelphone.wheelphonelibrary.WheelphoneRobot;
import com.wheelphone.wheelphonelibrary.WheelphoneRobot.WheelPhoneRobotListener;

public class MainActivity extends Activity implements WheelPhoneRobotListener, IRequestListener
{
	//config
	private Context context;
	WheelphoneRobot wheelphone;
	
	private int firmwareVersion=0;
	boolean getFirmwareFlag = true;

	private String TAG = "wheelbrain";
	private String logString;
	private boolean debugUsbComm = false;
	
	int lSpeed = 0; 
	int rSpeed = 0;
	
	//UI
	private TextView batteryVoltage;
	private TextView connectionState;
	private Button moveStateSwitch;
	private TextView moveAngle;
	private TextView forwardDistanceX, forwardDistanceY;
	private TextView wheelsSpeed;
	
	//move 
	private boolean isConnect   = false;
	private boolean isMoving    = false;
	private boolean moveIsEnded = true;
	//private double deltaAngle   = 0.0;
	
	public enum Move {
		FORWARD, LEFT, RIGHT;
	}
	
	//commands Array
	private Move[] commandsArray = { Move.FORWARD, Move.LEFT, Move.FORWARD, Move.LEFT, Move.FORWARD, Move.LEFT, Move.FORWARD };
	private Move curCommand;
	private int curCommandIndex	 = 0;
	
	//state methods -------------------------------------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		context = this.getApplicationContext();
		
		batteryVoltage   = (TextView)findViewById(R.id.batteryVoltage);
		connectionState  = (TextView)findViewById(R.id.connectionState);
		moveStateSwitch  = (Button)findViewById(R.id.moveStateSwitch);
		moveAngle		 = (TextView)findViewById(R.id.moveAngle);
		forwardDistanceX = (TextView)findViewById(R.id.forwardDistanceX);
		forwardDistanceY = (TextView)findViewById(R.id.forwardDistanceY);
		wheelsSpeed		 = (TextView)findViewById(R.id.wheelsSpeed);
		
        wheelphone = new WheelphoneRobot(getApplicationContext(), getIntent());
        wheelphone.enableSpeedControl();
        wheelphone.enableSoftAcceleration();
        //wheelphone.enableObstacleAvoidance();
        //wheelphone.enableCliffAvoidance();
        
        //set click listener
        moveStateSwitch.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View arg0) {
        		if(isConnect && !isMoving) {
            		isMoving = true;
            		
            		moveStateSwitch.setText("Stop");
            	} else {
            		isMoving = false;
            		
            		lSpeed = 0;
            		rSpeed = 0;
            		
            		wheelphone.setSpeed(lSpeed, rSpeed);
            		
            		moveStateSwitch.setText("Start");
            	}
        	}
        });
	}

	@Override
	public void onStart() {
		RequestManager.getInstance().delegate = this;
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
    
    // Wheelbot methods ---------------------------------------------------------------------------
    
    //Update method -----------------------------
    
	@Override
	public void onWheelphoneUpdate()
	{
		Log.v(TAG, "onWheelphoneUpdate");
		
		if(getFirmwareFlag) {
			firmwareVersion=wheelphone.getFirmwareVersion();
			if(firmwareVersion>0) {	// wait for the first USB transaction to be accomplished
				getFirmwareFlag = false;
				if(firmwareVersion >= 3) {
					//Toast.makeText(MainActivity.this, "Firmware version "+firmwareVersion+".0, fully compatible.", Toast.LENGTH_SHORT).show();
				} else {
					msgbox("Firmware version "+firmwareVersion+".0", "Firmware is NOT fully compatible. Update robot firmware.");
				}
			}
		}
		
		if(wheelphone.isRobotConnected()) {
			connectionState.setText("Connected");
			connectionState.setTextColor(getResources().getColor(R.color.green));
			isConnect = true;
		} else {
			connectionState.setText("Disconnected");
			connectionState.setTextColor(getResources().getColor(R.color.red));
			isConnect = false;
		}
		
		//show battery voltage
		batteryVoltage.setText(String.valueOf(wheelphone.getBatteryCharge())+"%");
		
		//show move infos
		forwardDistanceX.setText("x : " + wheelphone.getOdometryX() + " mm");
		forwardDistanceY.setText("y : " + wheelphone.getOdometryY() + " mm");
		moveAngle.setText("a : " + Math.toDegrees(wheelphone.getOdometryTheta()) + "°");
		
		//show wheels speeed infos
		wheelsSpeed.setText("lSpeed : " + lSpeed + "mm/s | rSpeed : " + rSpeed +"mm/s");
		
		if(isConnect && isMoving) {
			if(curCommandIndex < commandsArray.length) {
				if(moveIsEnded) {
					wheelphone.resetOdometry();
				}
				moveIsEnded = false;
				
				curCommand = commandsArray[curCommandIndex];
				
				switch(curCommand) {
				case FORWARD:
					rSpeed = 100;
					lSpeed = 100;
					
					if(Math.toDegrees(wheelphone.getOdometryTheta()) < 0) {
						rSpeed -= (int)(Math.toDegrees(wheelphone.getOdometryTheta()) * 10) / 2;
					} else if(Math.toDegrees(wheelphone.getOdometryTheta()) > 0) {
						lSpeed += (int)(Math.toDegrees(wheelphone.getOdometryTheta()) * 10) / 2;
					}
					
					wheelphone.setSpeed(lSpeed, rSpeed);
					
					if(wheelphone.getOdometryX() > 200 || wheelphone.getOdometryX() < -200 ||
					   wheelphone.getOdometryY() > 200 || wheelphone.getOdometryY() < -200) {
						
						stopWheelphone();
					}
					break;
				case LEFT:
					lSpeed = 0;
					rSpeed = 50;
					
					wheelphone.setSpeed(lSpeed, rSpeed);
					
					if(Math.toDegrees(wheelphone.getOdometryTheta()) > 89) {
						stopWheelphone();
					}
					break;
				case RIGHT:
					lSpeed = 50;
					lSpeed = 0;
					
					wheelphone.setSpeed(lSpeed, rSpeed);
					
					if(Math.toDegrees(wheelphone.getOdometryTheta()) < -89) {
						stopWheelphone();
					}
					break;
				}
				
			} else {
				isMoving = false;
				moveStateSwitch.setText("Start");
				
				curCommandIndex = 0;
			}
		}
	}
	
	//move methods
	public void stopWheelphone() {
		lSpeed = 0;
		rSpeed = 0;
		
		wheelphone.setSpeed(lSpeed, rSpeed);
		/*
		if(curCommand == Move.FORWARD) {
			deltaAngle = Math.toDegrees(wheelphone.getOdometryTheta());
		}
		*/
		moveIsEnded = true;
		curCommandIndex++;
	}
	
	// Tools --------------------------------------------------------------------------------------
	
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
	      e.printStackTrace();
	   }
	}

	@Override
	public void onInstructionReceived(ArrayList<Instruction> _instrus)
	{
		//TODO Do your stuff here with the instructions. -- bitch --
	}

	@Override
	public void onWelcomeReceived()
	{
	}

	@Override
	public void onFarewellReceived()
	{
		Intent intent = new Intent(this, ConnectionActivity.class);
		startActivity(intent);
		finish();
    	RequestManager.getInstance().close();
	}

	@Override
	public void onFarewellSent()
	{
		Intent intent = new Intent(this, ConnectionActivity.class);
		startActivity(intent);
		finish();
	}

	@Override 
	public void onBackPressed()
	{
		RequestManager.getInstance().disconnect();
	}
}
