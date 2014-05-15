package com.example.wheellight;

import java.util.ArrayList;

import com.example.adapter.ChooseGridViewAdapter;
import com.example.adapter.MapGridViewAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class ChooseMapFragment extends Fragment{
	
	private ChooseGridViewAdapter adapter; 
	private ArrayList<Map> listMap;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(getResources().getLayout(R.layout.choosemap_fragment), container, false);
		
		GridView gridview = (GridView)view.findViewById(R.id.choose_gridview);
		adapter = new ChooseGridViewAdapter(getActivity().getApplicationContext());
		gridview.setAdapter(adapter);		
		listMap = new ArrayList<Map>();		
		loadListMap();
		
		
		return view;
	}

	private void loadListMap() {
		// TODO Auto-generated method stub
		// Get DBMap
	}
}
