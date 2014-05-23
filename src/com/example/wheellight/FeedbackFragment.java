package com.example.wheellight;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.adapter.FeedbackGridViewAdapter;
import com.example.enumClass.TypeOfCell;
import com.example.model.GameMap;
import com.example.model.WheelDatabase;
import com.example.wheellight.network.IOrderCompleteListener;
import com.example.wheellight.network.IRequestListener;
import com.example.wheellight.network.RequestManager;

public class FeedbackFragment extends Fragment implements IOrderCompleteListener, IRequestListener
{
	static boolean isQuitting = false;
	private FeedbackGridViewAdapter adapter; 
	private int size = 125;
	private SharedPreferences sh_Pref;
	private int idMapSelect;
	private GameMap mapSelect;
	private static String tag = "FeedbackFragment";
	private GridView gridview;
	private WheelDatabase db;

	ArrayList<TypeOfCell> baseStructureMap;
	ArrayList<TypeOfCell> currentStructureMap;
	
	int botPositionIndex = 0;
	
	public enum BotDirection
	{
		Left,
		Up,
		Right,
		Down
	};
	
	BotDirection curDirection = BotDirection.Right;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		RequestManager.getInstance().orderCompleteDeletage = this;
		RequestManager.getInstance().delegate = this;
		Log.v(tag, "onCreateView");
		
		View view = inflater.inflate(getResources().getLayout(R.layout.feedback_fragment), container, false);

		db = WheelDatabase.getInstance(getActivity().getApplicationContext());
		sh_Pref = getActivity().getSharedPreferences("WheelLight", 0);		
		if(sh_Pref!= null)
		{
			Editor toEdit = sh_Pref.edit();
			LoadMapInSharePreferences();
		}
		
		/**
		 * Load map structure in DB
		 */
		baseStructureMap = mapSelect.getMapStructure();
		currentStructureMap = new ArrayList<TypeOfCell>(baseStructureMap);
	
		/**
		 * 	GridView adapter && selectColor		
		 */
		gridview = (GridView)view.findViewById(R.id.game_gridview);
		adapter = new FeedbackGridViewAdapter(getActivity().getApplicationContext(), size, baseStructureMap);
		gridview.setAdapter(adapter);		
		
		return view;
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		RequestManager.getInstance().orderCompleteDeletage = null;
		RequestManager.getInstance().delegate = null;
		isQuitting = true;
	}
	

	private void LoadMapInSharePreferences()
	{
		 // getting int
		if(WheelRobotActivity.idbuttonselect == R.id.mainfragment)
		{idMapSelect = sh_Pref.getInt("id", 0);
			if(idMapSelect != 0)
			{		
				mapSelect = db.getSelectMap(idMapSelect);		
			}
			else
			{
				mapSelect = db.getSelectMap(1);
			}
		}
		else
		{
			idMapSelect = sh_Pref.getInt("idplay", 0);
			if(idMapSelect != 0)
			{		
				mapSelect = db.getSelectMapGame(idMapSelect);		
			}
			else
			{
				mapSelect = db.getSelectMapGame(1);
			}
		}
		
	}


	@Override
	public void onForwardComplete()
	{
		getActivity().runOnUiThread(new Runnable(){

			@Override
			public void run()
			{

				currentStructureMap.set(botPositionIndex, baseStructureMap.get(botPositionIndex));

				switch(curDirection)
				{
					case Right:
						botPositionIndex += 1;
					break;
						
					case Left:
						botPositionIndex -= 1;
					break;
					
					case Up:
						botPositionIndex -= 5;
					break;
						
					case Down:
						botPositionIndex += 5;
					break;
				}
				currentStructureMap.set(botPositionIndex,TypeOfCell.Start);
				if(botPositionIndex != 0)
					currentStructureMap.set(0, TypeOfCell.None);
				adapter.setData(currentStructureMap , curDirection);
			}
			
		});

	}


	@Override
	public void onLeftComplete()
	{
		getActivity().runOnUiThread(new Runnable(){

			@Override
			public void run()
			{
				if(botPositionIndex != 0)
					currentStructureMap.set(0, TypeOfCell.None);
				switch(curDirection)
				{
					case Right:
						curDirection = BotDirection.Up;
					break;
						
					case Left:
						curDirection = BotDirection.Down;
					break;
					
					case Up:
						curDirection = BotDirection.Left;
					break;
						
					case Down:
						curDirection = BotDirection.Right;
					break;
				}
				adapter.setData(currentStructureMap , curDirection);
			}});
	}


	@Override
	public void onRightComplete()
	{
		getActivity().runOnUiThread(new Runnable(){

			@Override
			public void run()
			{
				if(botPositionIndex != 0)
					currentStructureMap.set(0, TypeOfCell.None);
				switch(curDirection)
				{
					case Right:
						curDirection = BotDirection.Down;
					break;
						
					case Left:
						curDirection = BotDirection.Up;
					break;
					
					case Up:
						curDirection = BotDirection.Right;
					break;
						
					case Down:
						curDirection = BotDirection.Left;
					break;
				}
				adapter.setData(currentStructureMap , curDirection);
			}});
	}


	@Override
	public void onWinComplete()
	{
		getActivity().runOnUiThread(new Runnable(){

			@Override
			public void run()
			{
				Toast.makeText(getActivity(), "You win", Toast.LENGTH_LONG).show();
			}});
	}


	@Override
	public void onLoseComplete()
	{
		getActivity().runOnUiThread(new Runnable(){

			@Override
			public void run()
			{
				Toast.makeText(getActivity(), "You loose", Toast.LENGTH_LONG).show();
			}});
	}

	@Override
	public void onFarewellSent()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWelcomeReceived()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFarewellReceived()
	{
		isQuitting = true;
		getActivity().onBackPressed();
		RequestManager.getInstance().close();
	}
}
