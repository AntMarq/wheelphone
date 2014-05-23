package com.example.wheellight;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class TutorielActivity extends FragmentActivity {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutoriel_activity);
		WheelRobotActivity.idbuttonselect = R.id.tutofragment;
		TutorielFragment tutoFragment = new TutorielFragment();       
        getSupportFragmentManager().beginTransaction()
                .replace(WheelRobotActivity.idbuttonselect, tutoFragment)
                .addToBackStack(null)
                .commit();
	
	
	}
	
	/**
	 * Quit application when the user press back button in MainFragment
	 */
		@Override
		public void onBackPressed()
		{
			super.onBackPressed();
			FragmentManager fm = getSupportFragmentManager();
			int count = fm.getBackStackEntryCount();	
		    if(count == 0)
		    {
		    	finish();
		    }		
		}	
		

}
