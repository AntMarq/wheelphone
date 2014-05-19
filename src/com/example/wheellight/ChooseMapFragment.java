package com.example.wheellight;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import com.example.model.GameMap;
import com.example.model.WheelDatabase;

public class ChooseMapFragment extends Fragment{
	
	private ChooseGridViewAdapter adapter; 
	private ArrayList<GameMap> listMap;
	private Editor toEdit;
	private static String tag = "ChooseMapFragment";
	GridView gridview;
	private SharedPreferences sh_Pref;

	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(getResources().getLayout(R.layout.choosemap_fragment), container, false);
		
		listMap = new ArrayList<GameMap>();		
		loadListMap();	
		gridview = (GridView)view.findViewById(R.id.choose_gridview);
		adapter = new ChooseGridViewAdapter(getActivity().getApplicationContext(),listMap);
		gridview.setAdapter(adapter);
		
		gridview.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
				Log.v(tag, "onItemClick = " + position);
				sh_Pref = getActivity().getSharedPreferences("WheelLight", 0);		
				toEdit = sh_Pref.edit();
				sharedPreferences(listMap.get(position).getIdmap());
				
				FragmentManager fm = getFragmentManager();
				fm.popBackStackImmediate();
				
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
		WheelDatabase db = WheelDatabase.getInstance(getActivity().getApplicationContext());
		listMap = db.getAllMapDB();
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
