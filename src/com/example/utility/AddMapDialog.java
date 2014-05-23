package com.example.utility;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.example.adapter.AddMapDialogAdapter;
import com.example.adapter.ColorPickerAdapter;
import com.example.adapter.IRefreshAdapter;
import com.example.enumClass.TypeOfCell;
import com.example.model.GameMap;
import com.example.model.WheelDatabase;
import com.example.wheellight.ChooseMapFragment;
import com.example.wheellight.R;

public class AddMapDialog extends Dialog implements android.view.View.OnClickListener {;
	
	private Context context;
	private GameMap gridMapDialog;
	private int colorPicker;
	private AddMapDialogAdapter adapter;
	ArrayList<TypeOfCell> structureMapDialog;
	private Button valid;
	private WheelDatabase db;
	private IRefreshAdapter mRefreshAdapter; 
	private GridView gridViewMap ;
	public AddMapDialog(Context _context, IRefreshAdapter _mRefreshAdapter) {
		super(_context);
		context = _context;
		mRefreshAdapter = _mRefreshAdapter;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addmap_dialog);
		db = WheelDatabase.getInstance(context);
		// for convenience and better reading, we place the colors in a two dimension array
		String colors[][] = {{"FCDFA6", "AEAEAE", "E64C66", "1BBC9B", "3090DE", "000000" }};
		//String colors[][] = {{"AEAEAE"}};
		final ArrayList<Integer>colorList = new ArrayList<Integer>();
		structureMapDialog = new ArrayList<TypeOfCell>();
		// add the color array to the list
		for (int i = 0; i < colors.length; i++) {
			for (int j = 0; j < colors[i].length; j++) {
				colorList.add(Color.parseColor("#" + colors[i][j]));
			}
		}
		valid = (Button)this.findViewById(R.id.button1);
		gridViewMap = (GridView) findViewById(R.id.gridViewaddMap);
		gridViewMap.setDrawingCacheEnabled(true);
		
		gridMapDialog =  db.getSelectMap(1);
		structureMapDialog = gridMapDialog.getMapStructure();
		adapter = new AddMapDialogAdapter(context,gridMapDialog);
		gridViewMap.setAdapter(adapter);
		gridViewMap.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			{			
				Log.v("AddmapDiaog", "grdiviewmap postition = " + position);
				if(position == 0 || position == 24){
					//nothing
				}
				else
				{	
					switch(colorPicker) 
					{
				    case R.color.dark_blue:
				    	structureMapDialog.set(position,TypeOfCell.Blue);
				        break;
				    case R.color.beige:
				    	structureMapDialog.set(position,TypeOfCell.Beige);
				        break;
				    case R.color.noir:
				    	structureMapDialog.set(position,TypeOfCell.Black);
				        break;
				    case R.color.green:
				    	structureMapDialog.set(position,TypeOfCell.Green);
				        break;
				    case R.color.grisclair:
				    	structureMapDialog.set(position,TypeOfCell.None);
				        break;
				    case R.color.color_red:
				    	structureMapDialog.set(position,TypeOfCell.Red);
				        break;
					}
				}
				adapter.notifyDataSetChanged();
			}
		});
		GridView gridViewColor = (GridView) findViewById(R.id.gridViewColor);
		gridViewColor.setAdapter(new ColorPickerAdapter(getContext(), colorList));
		// close the dialog on item click
		gridViewColor.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {	
				Log.v("ColorPicker", "position = " + position);
				findColor(position);
				
				//AddMapDialog.this.dismiss();
			}
		});
		
		
		valid.setOnClickListener(this);
	}
	
	
	
	public void onClick(View view) {
		if(view == findViewById(R.id.button1))
		{
			Bitmap bmp = gridViewMap.getDrawingCache();
			int rowid = (db.getRowCountInTableMap()+1);
			GameMap gMap = new GameMap(structureMapDialog, "Carte n° " + rowid);
			gMap.setImage(bmp);
			gMap.setIdmap(rowid);
			db.insertMapInDB(gMap, Utility.getBytes(bmp), gMap.getName());
			ChooseMapFragment.getListMap().add(gMap);
			mRefreshAdapter.updateAdapter();
			this.dismiss();
		
		}
	}
	
	public int findColor(int position) {
		switch(position)
		{
			case 0 :
				colorPicker = R.color.beige ;
			break;
			case 1 :
				colorPicker = R.color.grisclair;
			break;
			case 2 :
				colorPicker = R.color.color_red;
			break;
			case 3 :
				colorPicker = R.color.green;
			break;
			case 4 :
				colorPicker = R.color.dark_blue;
			break;
			case 5:
				colorPicker = R.color.noir;
			break;
				
		}
		return colorPicker;
	}
}
