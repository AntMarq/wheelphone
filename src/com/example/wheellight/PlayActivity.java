package com.example.wheellight;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class PlayActivity extends FragmentActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_activity);
	
		ChooseMapFragment chooseFragment = new ChooseMapFragment(); 
		Bundle bundle = new Bundle();
		bundle.putString("typeofgame", "play");
		chooseFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.playfragment, chooseFragment)
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
		
		public void onResume()
		{
			super.onResume();
			
		}

}
