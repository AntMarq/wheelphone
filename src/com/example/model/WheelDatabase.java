package com.example.model;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.wheellight.GameMap;
import com.example.wheellight.TypeOfCell;
import com.example.wheellight.Utility;




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
		
	
		
		 // Select All Query
	    String selectQuery =  "SELECT * FROM map ";	
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    Log.v(tag, "cursor = " + cursor.getCount());
	    // looping through all rows and adding to list
	    if(cursor.getCount () !=0)
	    {
		    if (cursor.moveToFirst()) 
		    {
		        do {
		        	ArrayList<TypeOfCell> mapStructure = new ArrayList<TypeOfCell>();
		        	int idmap = cursor.getInt(cursor.getColumnIndex("_id"));
		        	
		        	String title = cursor.getString(cursor.getColumnIndex("title"));
		        	byte [] image = cursor.getBlob(cursor.getColumnIndex("image"));
		        	String structure = cursor.getString(cursor.getColumnIndex("structure"));
		        	String [] separated = structure.split(",");	
		        	
		        	for (String item : separated)
		     	    {	 	     			        		
		        		TypeOfCell color = TypeOfCell.valueOf(item);
		        		mapStructure.add(color);	        		
		     	    }
		        	
		        	GameMap map = new GameMap(idmap,mapStructure,title);
		        	if(image != null)
		        	{
		        		map.setImage(Utility.getPhoto(image));
		        	}
		        	mapList.add(map);
		        }
		        while (cursor.moveToNext());
		    }
		    
	    }
		return mapList;	
	}

	public GameMap getSelectMap(int idMapSelect) {
		
		GameMap mapGame = null;
		SQLiteDatabase db = helper.getReadableDatabase();
		String selectQuery =  "SELECT * FROM map WHERE _id =" + idMapSelect;	
	    Cursor cursor = db.rawQuery(selectQuery, null);
	    if(cursor.getCount () !=0)
	    {
		    if (cursor.moveToFirst()) {
		        do {
		    		ArrayList<TypeOfCell> mapStructure = new ArrayList<TypeOfCell>();
		        	int idmap = cursor.getInt(cursor.getColumnIndex("_id"));
		        	String title = cursor.getString(cursor.getColumnIndex("title"));
		        	String structure = cursor.getString(cursor.getColumnIndex("structure"));
		        	String [] separated = structure.split(",");	
		        	
		        	for (String item : separated)
		     	    {	 	     	
		        		TypeOfCell color = TypeOfCell.valueOf(item);
		        		//ArrayList Enum
		        		mapStructure.add(color);	        		
		     	    }
		        	mapGame = new GameMap(idmap,mapStructure, title);
		        	
		       
		        }
		        while (cursor.moveToNext());
		    }
	    }
		
		return mapGame;
	}
	
	public void saveImageinDB(GameMap map, byte[] photo)
	{
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		//remove [ ] && space
    	String cleanStructure = map.getMapStructure().toString().replaceAll("\\[", "").replaceAll("\\]","").replaceAll(" ", "");
		values.put("structure", cleanStructure);
        values.put("image",photo);     
        db.update("map", values, "_id =" + map.getIdmap(), null); 
	}

}
