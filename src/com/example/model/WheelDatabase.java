package com.example.model;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.wheellight.GameMap;
import com.example.wheellight.TypeOfCell;




public class WheelDatabase 
{
	private static WheelDatabase instance = null;
	private static String tag = "Database";
	private String dbname = "wheellight";
	private int dbversion = 1 ;
	private WheelDatabaseHelper helper;
	
	public static WheelDatabase getInstance(Context context)
	{
		if (instance == null) 
		{
			Log.v(tag, "Database instance null");
			instance = new WheelDatabase(context);
		}
		
		return instance;
	}
	
	private WheelDatabase(Context context) 
	{
		Log.v(tag, "Database constructor");		
		helper = new WheelDatabaseHelper(context, dbname, null, dbversion);
	}
	
	public WheelDatabaseHelper getHelper() {
		return helper;
	}

	public void setHelper(WheelDatabaseHelper helper) {
		this.helper = helper;
	}
	
	public ArrayList<GameMap> getAllMapDB()
	{
		SQLiteDatabase db = helper.getReadableDatabase();
		ArrayList<GameMap> mapList = new ArrayList<GameMap>();
		ArrayList<TypeOfCell> mapStructure = new ArrayList<TypeOfCell>();
		String title = null;
		GameMap map = new GameMap(mapStructure, title);
		 // Select All Query
	    String selectQuery =  "SELECT * FROM map ";	
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    Log.v(tag, "cursor = " + cursor.getCount());
	    // looping through all rows and adding to list
	    if(cursor.getCount () !=0)
	    {
		    if (cursor.moveToFirst()) {
		        do {

		        	title = cursor.getString(cursor.getColumnIndex("title"));
		        	String structure = cursor.getString(cursor.getColumnIndex("structure"));
		        	String [] separated = structure.split(",");	
		        	
		        	for (String item : separated)
		     	    {	 	     	
		        		TypeOfCell color = TypeOfCell.valueOf(item);
		        		//ArrayList Enum
		        		mapStructure.add(color);
		        		Log.v("WheelDatabase", "structure_map.size() = " + mapStructure.size());		        		

		     	    }
		        	
		        	map.setMapStructure(mapStructure);
		        	map.setName(title);
		        	mapList.add(map);
		        }
		        while (cursor.moveToNext());
		    }
		    
	    }
	    Log.v("WheelDatabase", "mapList.size() = " + mapList.size());
		return mapList;	
	}

	public GameMap getSelectMap(int idMapSelect) {
		
		ArrayList<TypeOfCell> mapStructure = new ArrayList<TypeOfCell>();
		String title = null;
		GameMap mapGame = new GameMap(mapStructure, title);
		SQLiteDatabase db = helper.getReadableDatabase();
		String selectQuery =  "SELECT * FROM map WHERE _id =" + idMapSelect;	
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    if(cursor.getCount () !=0)
	    {
		    if (cursor.moveToFirst()) {
		        do {
		        	title = cursor.getString(cursor.getColumnIndex("title"));
		        	String structure = cursor.getString(cursor.getColumnIndex("structure"));
		        	String [] separated = structure.split(",");	
		        	
		        	for (String item : separated)
		     	    {	 	     	
		        		TypeOfCell color = TypeOfCell.valueOf(item);
		        		//ArrayList Enum
		        		mapStructure.add(color);	        		
		     	    }
		        	
		        	mapGame.setMapStructure(mapStructure);
		        	mapGame.setName(title);
		       
		        }
		        while (cursor.moveToNext());
		    }
	    }
		
		return mapGame;
	}

}
