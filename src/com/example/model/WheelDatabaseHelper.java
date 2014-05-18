package com.example.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteDatabase;
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
				"structure TEXT" +
				")");
		
		db.execSQL("INSERT INTO map (title,structure) VALUES ('map1','Start,Red,None,None,Blue,None,None,Black,Yellow,Red,Blue,None,None,None,Green,None,Black,Green,Red,Black,None,None,None,Yellow,End')");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	

}
