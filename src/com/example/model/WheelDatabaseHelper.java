package com.example.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WheelDatabaseHelper extends SQLiteOpenHelper  {

	public WheelDatabaseHelper(Context context, String name, CursorFactory factory,int version) 
	{
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		Log.v("DatabaseHelper", "onCreate");	
		/**
		 * Map Table
		 */
		db.execSQL("CREATE TABLE IF NOT EXISTS map (" +
				"_id INTEGER PRIMARY KEY," +
				"title TEXT," +
				"structure TEXT," +
				"image BLOB" +
				")");
		
	
		
		db.execSQL("INSERT INTO map (title,structure) VALUES ('map1_Demo','Start,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,None,End')");
	/*	db.execSQL("INSERT INTO map (title,structure) VALUES ('map2_Demo','Start,Blue,Blue,None,None,"
																	+ "None,None,Blue,Blue,None,"
																	+ "None,None,None,Blue,None,"
																	+ "None,None,None,Blue,Blue,"
																	+ "None,None,None,None,End')"); */

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	

}
