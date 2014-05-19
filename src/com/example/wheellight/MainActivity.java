package com.example.wheellight;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;

public class MainActivity extends FragmentActivity 
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		GameFragment gameFragment = new GameFragment();       
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainfragment, gameFragment)
                .addToBackStack(null)
                .commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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