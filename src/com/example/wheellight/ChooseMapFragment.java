package com.example.wheellight;

import java.util.ArrayList;

import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.example.adapter.ChooseGridViewAdapter;
import com.example.adapter.IRefreshAdapter;
import com.example.enumClass.TypeOfCell;
import com.example.model.GameMap;
import com.example.model.WheelDatabase;
import com.example.utility.AddMapDialog;
import com.example.utility.ColorPickerDialog;
import com.example.utility.Utility;

public class ChooseMapFragment extends Fragment{
	
	private ChooseGridViewAdapter adapter; 
	private static ArrayList<GameMap> listMap;
	private Editor toEdit;
	private static String tag = "ChooseMapFragment";
	GridView gridview;
	private SharedPreferences sh_Pref;
	private Bundle b;
	private WheelDatabase db ;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(getResources().getLayout(R.layout.choosemap_fragment), container, false);
		setHasOptionsMenu(true);
		b = getArguments();
		db = WheelDatabase.getInstance(getActivity().getApplicationContext());

		listMap = new ArrayList<GameMap>();		
		loadListMap();	
		gridview = (GridView)view.findViewById(R.id.choose_gridview);
		adapter = new ChooseGridViewAdapter(getActivity().getApplicationContext(),listMap);
		gridview.setAdapter(adapter);
		
		gridview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				
				if(b.getString("typeofgame").equalsIgnoreCase("play"))
				{
					sh_Pref = getActivity().getSharedPreferences("WheelLight", 0);		
					toEdit = sh_Pref.edit();
					sharedPreferencesPlay(listMap.get(position).getIdmap());
					Log.v(tag, "send id " + listMap.get(position).getIdmap());
					GameFragment gameFragment = new GameFragment(); 
					gameFragment.setArguments(b);
			        getFragmentManager().beginTransaction()
			                .replace(R.id.playfragment, gameFragment)
			                .addToBackStack(null)
			                .commit();
				}
				else
				{
					Log.v(tag, "onItemClick = " + position + "/" + listMap.get(position).getIdmap());
					sh_Pref = getActivity().getSharedPreferences("WheelLight", 0);		
					toEdit = sh_Pref.edit();
					sharedPreferences(listMap.get(position).getIdmap());
					
					FragmentManager fm = getFragmentManager();
					fm.popBackStackImmediate();
				}
			}
		});
		
		return view;
	}
	
	final IRefreshAdapter listener = new IRefreshAdapter() {
		
		@Override
		public void updateAdapter() {
			Log.v(tag, "updateAdapter ()");
			
			loadListMap();		
			adapter.notifyDataSetChanged();
		
		}
	};
	
	private void sharedPreferences(int idMap) {		 
        toEdit.putInt("id", idMap);   
        toEdit.commit();
    }
	
	private void sharedPreferencesPlay(int idMap) {		 
        toEdit.putInt("idplay", idMap);   
        toEdit.commit();
    }

	private void loadListMap() {
		Log.v(tag, "loadListMap");
		if(listMap.size() == 0)
		{
			if(b.getString("typeofgame").equalsIgnoreCase("play"))
			{			
				Log.v(tag, "play !");
				if(db.CheckPlayGameMapexist())
				{
					Log.v(tag, "play with DB !");
					listMap = db.getAllMapGameDB();
				}
				else
				{
					loadGamPlayModel();
				}	
			}
			else
			{		
				listMap = db.getAllMapDB();

				Log.v(tag, "demo with DB !" + listMap.size());
			}

		}
		
	}
//Only use at the first application launch
	private void loadGamPlayModel()
	{
		Log.v(tag, "loadGamPlayModel");
		ArrayList<TypeOfCell> structure = new ArrayList<TypeOfCell>();
		//map lvl 1
		structure.add(TypeOfCell.Start);
		for(int j = 1; j<24;j++)
		{
			
			if(j == 4)
			{
				structure.add(TypeOfCell.Blue);
			}
			else if (j == 14)
			{
				structure.add(TypeOfCell.Green);
			}
			else{
				structure.add(TypeOfCell.None);
			}
		}
		structure.add(TypeOfCell.End);
		String idMap1 = "1";
		GameMap map1 = new GameMap(structure, "lvl" + idMap1);
		Bitmap b1 = BitmapFactory.decodeResource(getResources(), R.drawable.lvl_1_2);
		map1.setImage(b1);
		db.insertMapGameInDB(map1, Utility.getBytes(b1));
		listMap.add(map1);
		//Clear structure
		structure.clear();
		//map lvl 2
		structure.add(TypeOfCell.Start);
		for(int j = 1; j<24;j++)
		{
			
			if(j == 6)
			{
				structure.add(TypeOfCell.Beige);
			}
			else if (j == 8)
			{
				structure.add(TypeOfCell.Blue);
			}
			else if (j == 18)
			{
				structure.add(TypeOfCell.Green);
			}
			else{
				structure.add(TypeOfCell.None);
			}
		}
		structure.add(TypeOfCell.End);
		String idMap2 = "2";
		GameMap map2 = new GameMap(structure, "lvl" + idMap2);
		Bitmap b2 = BitmapFactory.decodeResource(getResources(), R.drawable.lvl_1_3);
		map2.setImage(b2);
		db.insertMapGameInDB(map2, Utility.getBytes(b2));
		listMap.add(map2);
		//Clear structure
		structure.clear();
		//map lvl 3
		structure.add(TypeOfCell.Start);
		for(int j = 1; j<24;j++)
		{
		
			if(j == 10)
			{
				structure.add(TypeOfCell.Black);
			}
			else if (j == 8)
			{
				structure.add(TypeOfCell.Blue);
			}
			else if (j == 14)
			{
				structure.add(TypeOfCell.Green);
			}
			else if (j == 12)
			{
				structure.add(TypeOfCell.Beige);
			}
			else if (j == 13)
			{
				structure.add(TypeOfCell.Red);
			}
			else{
				structure.add(TypeOfCell.None);
			}
		}
		structure.add(TypeOfCell.End);
		String idMap3 = "3";
		GameMap map3 = new GameMap(structure, "lvl" + idMap3);
		Bitmap b3 = BitmapFactory.decodeResource(getResources(), R.drawable.lvl_1_4);
		map3.setImage(b3);
		db.insertMapGameInDB(map3, Utility.getBytes(b3));
		listMap.add(map3);
		
	}
	
	@Override
	public void onCreateOptionsMenu (Menu menu, MenuInflater inflater)
	{
		inflater.inflate (R.menu.game, menu);		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		if(WheelRobotActivity.idbuttonselect == R.id.mainfragment)
		{
			
			
			if(item.getItemId() == R.id.addmap)
			{
				Dialog addDialog = new AddMapDialog(getActivity(),listener);
				addDialog.setTitle("Ajout d'une carte");
				
				addDialog.show();
				
			}
		}
		else
		{
			Toast.makeText(getActivity(),"Fonctionnalité non présente dans le mode jeu", Toast.LENGTH_SHORT).show();
		}
		
		return false;
	}

	
	public ChooseGridViewAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(ChooseGridViewAdapter adapter) {
		this.adapter = adapter;
	}

	public static ArrayList<GameMap> getListMap() {
		return listMap;
	}

	public void setListMap(ArrayList<GameMap> listMap) {
		this.listMap = listMap;
	}
	
	
	
	
}
