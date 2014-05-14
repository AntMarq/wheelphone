package com.example.wheellight;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

public class GameFragment extends Fragment{
	
	private Button startGame;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(getResources().getLayout(R.layout.setting_fragment), container, false);
		startGame = (Button)view.findViewById(R.id.startbutton);
		// In your oncreate (or where ever you want to create your gridview)
		GridView gridview = (GridView)view.findViewById(R.id.game_gridview);
		
		
		startGame.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Connectivity_Fragment gameFragment = new Connectivity_Fragment();       
		        getFragmentManager().beginTransaction()
		                .replace(R.id.mainfragment, gameFragment)
		                .addToBackStack(null)
		                .commit();			
			}
		});
		
		
		
		
		
		return view;
	}

}
