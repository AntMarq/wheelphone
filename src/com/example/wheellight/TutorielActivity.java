package com.example.wheellight;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class TutorielActivity extends FragmentActivity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutoriel_activity);
		
		TutorielFragment tutoFragment = new TutorielFragment();       
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.tutofragment, tutoFragment)
                .addToBackStack(null)
                .commit();
	
	
	}
	

}
