package com.example.wheellight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class WheelRobotActivity extends FragmentActivity{
	
	private Button play, demo, tuto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
	
		tuto = (Button)findViewById(R.id.tuto);
		play = (Button)findViewById(R.id.play);
		demo = (Button)findViewById(R.id.demo);
		
		tuto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.play_msg), Toast.LENGTH_SHORT).show();
			}
		});
		
		demo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {		
				Intent i = new Intent(WheelRobotActivity.this, DemoActivity.class);
				startActivity(i); 
			}
		});
		
		
	}

}
