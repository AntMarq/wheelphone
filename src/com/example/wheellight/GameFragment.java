package com.example.wheellight;

import instructions.Instruction;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.adapter.MapGridViewAdapter;
import com.example.enumClass.Move;
import com.example.enumClass.TypeOfCell;
import com.example.model.GameMap;
import com.example.model.WheelDatabase;
import com.example.utility.Utility;

public class GameFragment extends Fragment{
	
	private Button startGame, choosemap;
	private MapGridViewAdapter adapter; 
	private ImageButton imageFirstLine, imageSecondLine, imageThirdLine,imageFourthLine;
	private ImageButton left,up,right;
	private ImageView selectColor,greyImg,redImg;
	private int size = 125;
	private SharedPreferences sh_Pref;
	private int idMapSelect;
	private GameMap mapSelect;
	private static String tag = "GameFragment";
	private GridView gridview;
	private WheelDatabase db;
	private int setColorInChild;
	private LinearLayout blueLinear, greenLinear, yellowLinear, blackLinear;
	private ImageView image;
	private HashMap<String, ArrayList<String>> moveSequences;
	private ArrayList<Instruction> moveArray;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(getResources().getLayout(R.layout.test), container, false);
		setHasOptionsMenu(true);
		db = WheelDatabase.getInstance(getActivity().getApplicationContext());
		sh_Pref = getActivity().getSharedPreferences("WheelLight", 0);		
		if(sh_Pref!= null)
		{
			Editor toEdit = sh_Pref.edit();
			LoadMapInSharePreferences();
		}

		startGame = (Button)view.findViewById(R.id.startbutton);
		choosemap = (Button)view.findViewById(R.id.choosemap);
		imageFirstLine = (ImageButton)view.findViewById(R.id.imageView1);
		imageSecondLine = (ImageButton)view.findViewById(R.id.imageView2);
		imageThirdLine = (ImageButton)view.findViewById(R.id.imageView3);
		imageFourthLine = (ImageButton)view.findViewById(R.id.imageView4);
		selectColor = (ImageView)view.findViewById(R.id.imageView17);
		blueLinear =  (LinearLayout)view.findViewById(R.id.LinearLayout15);
		greenLinear = (LinearLayout)view.findViewById(R.id.linearLayout25);
		greyImg = (ImageView)view.findViewById(R.id.imageView6);
		redImg = (ImageView)view.findViewById(R.id.imageView5);
		yellowLinear = (LinearLayout)view.findViewById(R.id.linearLayout35);
		blackLinear = (LinearLayout)view.findViewById(R.id.linearLayout45);

		
		left = (ImageButton)view.findViewById(R.id.imageButtonLeft);
		up = (ImageButton)view.findViewById(R.id.imageButtonUp);
		right = (ImageButton)view.findViewById(R.id.imageButtonRight);

///////////////////////////////////////////////////////////////////////////
///////////////////////// Insert Arrow in layout ///////////////////////////
///////////////////////////////////////////////////////////////////////////

		
		
		left.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				identifyLayout(left);			
			}
		});
		
		right.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				identifyLayout(right);
			}
		});
		
		up.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				identifyLayout(up);
			}
		});
		
///////////////////////////////////////////////////////////////////////////
/////////////////////Select Color to change gridView item color////////////
///////////////////////////////////////////////////////////////////////////		
		
		
		imageFirstLine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				setColorInChild = Color.BLUE;
				selectColor.setBackgroundColor(setColorInChild);
				
			}
		});
		imageSecondLine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setColorInChild = Color.GREEN;
				selectColor.setBackgroundColor(setColorInChild);
			}
		});

		imageThirdLine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setColorInChild = Color.YELLOW;
				selectColor.setBackgroundColor(setColorInChild);
			}
		});
		
		imageFourthLine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setColorInChild = Color.BLACK;
				selectColor.setBackgroundColor(setColorInChild);
			}
		});
		
		redImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setColorInChild = Color.RED;
				selectColor.setBackgroundColor(setColorInChild);			
			}
		});
		
		greyImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setColorInChild = Color.LTGRAY;
				selectColor.setBackgroundColor(setColorInChild);
			}
		});
		
		
		gridview = (GridView)view.findViewById(R.id.game_gridview);
		adapter = new MapGridViewAdapter(getActivity().getApplicationContext(), size, mapSelect);
		gridview.setDrawingCacheEnabled(true);
		gridview.setAdapter(adapter);
		
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				
				if(position == 0 || position == 24)
				{
					//nothing
					Log.v(tag, "click on item 0 or 24");
				}
				else
				{
					ArrayList<TypeOfCell> structureMap = mapSelect.getMapStructure();
					switch(setColorInChild) 
					{
				    case Color.BLUE:
				    	structureMap.set(position,TypeOfCell.Blue);
				        break;
				    case Color.YELLOW:
				    	structureMap.set(position,TypeOfCell.Yellow);
				        break;
				    case Color.BLACK:
				    	structureMap.set(position,TypeOfCell.Black);
				        break;
				    case Color.GREEN:
				    	structureMap.set(position,TypeOfCell.Green);
				        break;
				    case Color.LTGRAY:
				    	structureMap.set(position,TypeOfCell.None);
				        break;
				    case Color.RED:
				    	structureMap.set(position,TypeOfCell.Red);
				        break;
				   /* default:
				    	structureMap.set(position,TypeOfCell.None);*/
				   //     Log.v(tag, "structureMapModified = " + structureMap);
						
					}
				}
				adapter.notifyDataSetChanged();
			}
		});
		
		startGame.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				moveArray = new ArrayList<Instruction>();
				move
				
				
				Connectivity_Fragment gameFragment = new Connectivity_Fragment();  
				Bundle bdl = new Bundle();
				bdl.putStringArrayList("blue", moveArray);		
		        getFragmentManager().beginTransaction()
		                .replace(R.id.mainfragment, gameFragment)
		                .addToBackStack(null)
		                .commit();			
			}
		});
		
		choosemap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ChooseMapFragment chooseMapFragment = new ChooseMapFragment();       
		        getFragmentManager().beginTransaction()
		                .replace(R.id.mainfragment, chooseMapFragment)
		                .addToBackStack(null)
		                .commit();					
			}
		});
		
		return view;
	}
	

	private void LoadMapInSharePreferences()
	{
		Log.v(tag, "LoadMapInSharePreferences " );
		idMapSelect = sh_Pref.getInt("id", 0); // getting int
		Log.v(tag, "idMapSelect = " + idMapSelect);
		if(idMapSelect != 0)
		{
			
			mapSelect = db.getSelectMap(idMapSelect);
		
		}
		else
		{
			mapSelect = db.getSelectMap(1);
			Toast.makeText(getActivity().getApplicationContext(), "map par defaut chargée", Toast.LENGTH_SHORT).show();
		}
	}
	

	@Override
	public void onCreateOptionsMenu (Menu menu, MenuInflater inflater)
	{
		inflater.inflate (R.menu.game, menu);		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		if(item.getItemId() == R.id.savemap)
		{
			saveMapInGridView();
		}
		return false;
	}


	private void saveMapInGridView() {
		
		Bitmap bm = gridview.getDrawingCache();
		if(bm != null)
		{
			db.saveImageinDB(mapSelect,Utility.getBytes(bm));
		}
		else
		{
			Toast.makeText(getActivity(), "la map actuelle n'est pas sauvegardée", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	public void identifyLayout(ImageButton imgBtn)
	{
		if(setColorInChild != 0 )
		{
			if(setColorInChild == Color.BLUE)
			{
				if(blueLinear.getChildCount() < 3 )
				{
					drawImageArrowInLayout(blueLinear,imgBtn);
				}else
				{
					Toast.makeText(getActivity(), "3 mouvements max par couleur", Toast.LENGTH_SHORT).show();
				}
			}
			else if(setColorInChild == Color.YELLOW)
			{
				if(yellowLinear.getChildCount() < 3 )
				{
					drawImageArrowInLayout(yellowLinear, imgBtn);
				}
				else
				{
					Toast.makeText(getActivity(), "3 mouvements max par couleur", Toast.LENGTH_SHORT).show();
				}
			}
			else if(setColorInChild == Color.GREEN)
			{
				if(greenLinear.getChildCount() < 3 )
				{
					drawImageArrowInLayout(greenLinear, imgBtn);
				}
				else
				{
					Toast.makeText(getActivity(), "3 mouvements max par couleur", Toast.LENGTH_SHORT).show();
				}
			}
			else if(setColorInChild == Color.BLACK)
			{
				if(blackLinear.getChildCount() < 3 )
				{
					drawImageArrowInLayout(blackLinear, imgBtn);
				}
				else
				{
					Toast.makeText(getActivity(), "3 mouvements max par couleur", Toast.LENGTH_SHORT).show();
				}
			}
		}
		else
		{
			Toast.makeText(getActivity(), "Sélectionner une couleur et cliquer sur une flèche", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void drawImageArrowInLayout(LinearLayout selectLinear, ImageButton btn)
	{
		image = new ImageView(getActivity());
		image.setImageDrawable(btn.getDrawable());
		image.setLayoutParams(new LayoutParams(64, 64) {});
		image.setPadding(25, 0, 5, 12);					
		selectLinear.addView(image);
	}

}
