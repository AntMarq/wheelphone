package com.example.wheellight;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.adapter.MapGridViewAdapter;
import com.example.model.WheelDatabase;

public class GameFragment extends Fragment{
	
	private Button startGame, choosemap;
	private MapGridViewAdapter adapter; 
	private ImageView imageFirstLine, imageSecondLine, imageThirdLine,imageFourthLine;
	private int size = 125;
	private SharedPreferences sh_Pref;
	private int idMapSelect;
	private GameMap mapSelect;
	private static String tag = "GameFragment";
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(getResources().getLayout(R.layout.test), container, false);
		setHasOptionsMenu(true);
		sh_Pref = getActivity().getSharedPreferences("WheelLight", 0);		
		if(sh_Pref!= null)
		{
			Editor toEdit = sh_Pref.edit();
			LoadMapInSharePreferences();
		}

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
		adapter = new MapGridViewAdapter(getActivity().getApplicationContext(), size, mapSelect  );
		gridview.setAdapter(adapter);
		
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
				Log.v(tag, "onItemClick = " + position);
			}
		});
		
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
	

	private void LoadMapInSharePreferences()
	{
		WheelDatabase db = WheelDatabase.getInstance(getActivity().getApplicationContext());
		idMapSelect = sh_Pref.getInt("id", 0); // getting int
		Log.v(tag, "idMapSelect = " + idMapSelect);
		if(idMapSelect != 0)
		{
			
			mapSelect = db.getSelectMap(idMapSelect);
		
		}
		else
		{
			mapSelect = db.getSelectMap(1);
			Toast.makeText(getActivity().getApplicationContext(), "Aucune map pré-selectionnée", Toast.LENGTH_SHORT).show();
		}
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
