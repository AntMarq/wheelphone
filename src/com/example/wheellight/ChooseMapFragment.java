package com.example.wheellight;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.example.adapter.ChooseGridViewAdapter;
import com.example.enumClass.TypeOfCell;
import com.example.model.GameMap;
import com.example.model.WheelDatabase;

public class ChooseMapFragment extends Fragment{
	
	private ChooseGridViewAdapter adapter; 
	private ArrayList<GameMap> listMap;
	private Editor toEdit;
	private static String tag = "ChooseMapFragment";
	GridView gridview;
	private SharedPreferences sh_Pref;
	private Bundle b;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(getResources().getLayout(R.layout.choosemap_fragment), container, false);
		b = getArguments();
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
					sharedPreferences(listMap.get(position).getIdmap());
					
					GameFragment gameFragment = new GameFragment();       
			        getFragmentManager().beginTransaction()
			                .replace(R.id.playfragment, gameFragment)
			                .addToBackStack(null)
			                .commit();
				}
				else
				{
					Log.v(tag, "onItemClick = " + position);
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
	
	private void sharedPreferences(int idMap) {		 
        toEdit.putInt("id", idMap);   
        toEdit.commit();
    }

	private void loadListMap() {
		// TODO Auto-generated method stub
		if(b.getString("typeofgame").equalsIgnoreCase("play"))
		{
			loadGamPlayModel();
		}
		else
		{
			WheelDatabase db = WheelDatabase.getInstance(getActivity().getApplicationContext());
			listMap = db.getAllMapDB();
		}
		
	}

	private void loadGamPlayModel()
	{
		ArrayList<TypeOfCell> structure = new ArrayList<TypeOfCell>();
		//map lvl 1
		structure.add(TypeOfCell.Start);
		for(int j = 22; j<23;j++)
		{
			structure.add(TypeOfCell.None);
			if(j == 4)
			{
				structure.add(TypeOfCell.Blue);
			}
			else if (j == 14)
			{
				structure.add(TypeOfCell.Green);
			}
		}
		structure.add(TypeOfCell.End);
		String idMap1 = "1";
		GameMap map1 = new GameMap(structure, "lvl" + idMap1);
		Bitmap b1 = BitmapFactory.decodeResource(getResources(), R.drawable.lvl_1_2);
		map1.setImage(b1);
		listMap.add(map1);
		//Clear structure
		structure.clear();
		//map lvl 2
		structure.add(TypeOfCell.Start);
		for(int j = 22; j<23;j++)
		{
			structure.add(TypeOfCell.None);
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
		}
		structure.add(TypeOfCell.End);
		String idMap2 = "2";
		GameMap map2 = new GameMap(structure, "lvl" + idMap2);
		Bitmap b2 = BitmapFactory.decodeResource(getResources(), R.drawable.lvl_1_3);
		map2.setImage(b2);
		listMap.add(map2);
		//Clear structure
		structure.clear();
		//map lvl 3
		structure.add(TypeOfCell.Start);
		for(int j = 22; j<23;j++)
		{
			structure.add(TypeOfCell.None);
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
		}
		structure.add(TypeOfCell.End);
		String idMap3 = "3";
		GameMap map3 = new GameMap(structure, "lvl" + idMap3);
		Bitmap b3 = BitmapFactory.decodeResource(getResources(), R.drawable.lvl_1_4);
		map3.setImage(b3);
		listMap.add(map3);
		
	}
	
	
	public ChooseGridViewAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(ChooseGridViewAdapter adapter) {
		this.adapter = adapter;
	}

	public ArrayList<GameMap> getListMap() {
		return listMap;
	}

	public void setListMap(ArrayList<GameMap> listMap) {
		this.listMap = listMap;
	}
	
	
}
