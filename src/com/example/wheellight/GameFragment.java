package com.example.wheellight;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.adapter.MapGridViewAdapter;

public class GameFragment extends Fragment{
	
	private Button startGame, choosemap;
	private MapGridViewAdapter adapter; 
	private ImageView imageFirstLine, imageSecondLine, imageThirdLine,imageFourthLine;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(getResources().getLayout(R.layout.test), container, false);
		setHasOptionsMenu(true);
		
		startGame = (Button)view.findViewById(R.id.startbutton);
		choosemap = (Button)view.findViewById(R.id.choosemap);
		imageFirstLine = (ImageView)view.findViewById(R.id.imageView1);
		imageSecondLine = (ImageView)view.findViewById(R.id.imageView2);
		imageThirdLine = (ImageView)view.findViewById(R.id.imageView3);
		imageFourthLine = (ImageView)view.findViewById(R.id.imageView4);
				
		imageFirstLine.setBackgroundColor(Color.BLUE);
		imageSecondLine.setBackgroundColor(Color.GREEN);
		imageThirdLine.setBackgroundColor(Color.YELLOW);
		imageFourthLine.setBackgroundColor(Color.BLACK);
		
		GridView gridview = (GridView)view.findViewById(R.id.game_gridview);
		adapter = new MapGridViewAdapter(getActivity().getApplicationContext(), 125);
		gridview.setAdapter(adapter);
		
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
		
		choosemap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ChooseMapFragment chooseMapFragment = new ChooseMapFragment();       
		        getFragmentManager().beginTransaction()
		                .replace(R.id.mainfragment, chooseMapFragment)
		                .addToBackStack(null)
		                .commit();					
			}
		});
		
		
		
		return view;
	}
	
	@Override
	public void onCreateOptionsMenu (Menu menu, MenuInflater inflater)
	{
		inflater.inflate (R.menu.game, menu);		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		
		
		
		
		return false;
	}

}
