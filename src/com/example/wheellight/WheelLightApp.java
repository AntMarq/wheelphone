package com.example.wheellight;

import android.app.Application;

import com.example.model.WheelDatabase;

public class WheelLightApp extends Application
{
	@Override
	public void onCreate() 
	{
		super.onCreate();
		WheelDatabase.getInstance(getApplicationContext());
	
	}
}
