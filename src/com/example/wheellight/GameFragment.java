package com.example.wheellight;

import instructions.Instruction;
import instructions.Instruction.EInstructionType;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.MapGridViewAdapter;
import com.example.enumClass.TypeOfCell;
import com.example.model.GameMap;
import com.example.model.WheelDatabase;
import com.example.utility.ColorPickerDialog;
import com.example.utility.ColorPickerDialog.OnColorChangedListener;
import com.example.utility.Utility;

public class GameFragment extends Fragment{
	
	private TextView userInfo;
	private Button startGame, choosemap;
	private MapGridViewAdapter adapter; 
	private ImageButton imageButtonColor;
	private ImageButton left,up,right;
	private ImageView selectColor, crossStartDelete, crossBlueDelete, crossGreenDelete, crossBlackDelete, crossBeigeDelete;
	private int size = 125;
	private SharedPreferences sh_Pref;
	private int idMapSelect;
	private GameMap mapSelect;
	private static String tag = "GameFragment";
	private GridView gridview;
	private WheelDatabase db;
	private int setColorInChild;
	private LinearLayout blueLinear, greenLinear, beigeLinear, blackLinear,startLinear;
	private ImageView image;

	HashMap<String, ArrayList<Instruction>> colorsInstructions = new HashMap<String, ArrayList<Instruction>>();
	ArrayList<TypeOfCell> structureMap;
	ArrayList<Instruction> sendStructure;
	
	int gridIndex = 0;
	//curDirection  1 = right | 2 = down | 3 = up | 4 = left
	int curDirection = 1;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Log.v(tag, "onCreateView");
		
		View view = inflater.inflate(getResources().getLayout(R.layout.game_fragment), container, false);
		setHasOptionsMenu(true);
		db = WheelDatabase.getInstance(getActivity().getApplicationContext());
		sh_Pref = getActivity().getSharedPreferences("WheelLight", 0);		
		if(sh_Pref!= null)
		{
			Editor toEdit = sh_Pref.edit();
			LoadMapInSharePreferences();
		}

		userInfo = (TextView)view.findViewById(R.id.notice);
		startGame = (Button)view.findViewById(R.id.startbutton);
		choosemap = (Button)view.findViewById(R.id.choosemap);
		selectColor = (ImageView)view.findViewById(R.id.imageView17);
		blueLinear =  (LinearLayout)view.findViewById(R.id.LinearLayout15);
		greenLinear = (LinearLayout)view.findViewById(R.id.linearLayout25);
		beigeLinear = (LinearLayout)view.findViewById(R.id.linearLayout35);
		blackLinear = (LinearLayout)view.findViewById(R.id.linearLayout45);
		startLinear = (LinearLayout)view.findViewById(R.id.linearLayout95);
		imageButtonColor = (ImageButton)view.findViewById(R.id.imageButtonColor);
		crossBlueDelete = (ImageView)view.findViewById(R.id.imageView7);
		crossBlackDelete = (ImageView)view.findViewById(R.id.imageView11);
		crossGreenDelete = (ImageView)view.findViewById(R.id.imageView9);
		crossStartDelete = (ImageView)view.findViewById(R.id.imageView8);
		crossBeigeDelete = (ImageView)view.findViewById(R.id.imageView10);
		
		left = (ImageButton)view.findViewById(R.id.imageButtonLeft);
		up = (ImageButton)view.findViewById(R.id.imageButtonUp);
		right = (ImageButton)view.findViewById(R.id.imageButtonRight);

		userInfo.setText(Html.fromHtml("Selectionner une couleur en cliquant sur le pot de peinture <br /><br /> Remplisser la grille avec la couleur de votre choix <br /><br /> &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp et/ou <br /><br /> Saisisser la squence de mouvement en fonction de la couleur selectionne"));
		userInfo.setBackgroundColor(getResources().getColor(R.color.white));
		
		if(selectColor.getBackground() != null)
		{
			selectColor.setImageResource(R.drawable.no_color);
			setColorInChild = 0;
		}
		structureMap = mapSelect.getMapStructure();
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
/////////////////////Reset Instruction in Layout //////////////////////////
///////////////////////////////////////////////////////////////////////////			
		crossBlueDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				blueLinear.removeAllViews();
				blueLinear.refreshDrawableState();
			}
		});
		
		crossBlackDelete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					blackLinear.removeAllViews();
					blackLinear.refreshDrawableState();
				}
			});
	
		crossStartDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startLinear.removeAllViews();
				startLinear.refreshDrawableState();
			}
		});
	
		crossBeigeDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				beigeLinear.removeAllViews();
				beigeLinear.refreshDrawableState();
			}
		});
	
		crossGreenDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				greenLinear.removeAllViews();
				greenLinear.refreshDrawableState();
			}
		});
		
///////////////////////////////////////////////////////////////////////////
/////////////////////Select Color to change gridView item color////////////
///////////////////////////////////////////////////////////////////////////		
		final OnColorChangedListener listener=new OnColorChangedListener() {  
		       @Override  
		       public void colorChanged(int color) {  
		        setColorInChild = color;
				selectColor.setImageResource(setColorInChild);
		       }  
		     };
		     
		imageButtonColor.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Dialog dialog = new ColorPickerDialog(getActivity(),listener);			
				dialog.show();
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
				}
				else
				{
					
					switch(setColorInChild) 
					{
				    case R.color.dark_blue:
				    	structureMap.set(position,TypeOfCell.Blue);
				        break;
				    case R.color.beige:
				    	structureMap.set(position,TypeOfCell.Beige);
				        break;
				    case R.color.noir:
				    	structureMap.set(position,TypeOfCell.Black);
				        break;
				    case R.color.green:
				    	structureMap.set(position,TypeOfCell.Green);
				        break;
				    case R.color.grisclair:
				    	structureMap.set(position,TypeOfCell.None);
				        break;
				    case R.color.color_red:
				    	structureMap.set(position,TypeOfCell.Red);
				        break;
					}
				}
				adapter.notifyDataSetChanged();
			}
		});
		
///////////////////////////////////////////////////////////////////////////
/////////////////////Send Instruction to Connectivity Fragment/////////////
///////////////////////////////////////////////////////////////////////////
		
		startGame.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				sendInstructions();
					
				Log.v(tag, "sendStructure = " + sendStructure.toString());
				
				Connectivity_Fragment gameFragment = new Connectivity_Fragment();  
				Bundle bdl = new Bundle();
				bdl.putSerializable("instruction", sendStructure);
					
		        getFragmentManager().beginTransaction()
		                .replace(R.id.mainfragment, gameFragment)
		                .addToBackStack(null)
		                .commit();	
			}
		});
		
		choosemap.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) {
				saveMapInGridView();
				ChooseMapFragment chooseMapFragment = new ChooseMapFragment();       
		        getFragmentManager().beginTransaction()
		                .replace(R.id.mainfragment, chooseMapFragment)
		                .addToBackStack("connection")
		                .commit();					
			}
		});		
		return view;
	}
	

	private void LoadMapInSharePreferences()
	{
		idMapSelect = sh_Pref.getInt("id", 0); // getting int
		if(idMapSelect != 0)
		{		
			mapSelect = db.getSelectMap(idMapSelect);		
		}
		else
		{
			mapSelect = db.getSelectMap(1);
			Toast.makeText(getActivity().getApplicationContext(), "map par defaut charge", Toast.LENGTH_SHORT).show();
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
	}
	
	public void identifyLayout(ImageButton imgBtn)
	{
		if(setColorInChild != 0 )
		{			
			if(setColorInChild == R.color.dark_blue)
			{
				if(blueLinear.getChildCount() < 3 )
				{
					drawImageArrowInLayout(blueLinear,imgBtn);
				}else
				{
					displayToast();
				}
			}
			else if(setColorInChild == R.color.beige)
			{
				if(beigeLinear.getChildCount() < 3 )
				{
					drawImageArrowInLayout(beigeLinear, imgBtn);
				}
				else
				{
					displayToast();
				}
			}
			else if(setColorInChild == R.color.green)
			{
				if(greenLinear.getChildCount() < 3 )
				{
					drawImageArrowInLayout(greenLinear, imgBtn);
				}
				else
				{
					displayToast();
				}
			}
			else if(setColorInChild == R.color.noir)
			{
				if(blackLinear.getChildCount() < 3 )
				{
					drawImageArrowInLayout(blackLinear, imgBtn);
				}
				else
				{
					displayToast();
				}
			}
			else if(setColorInChild == R.color.white)
			{
				if(startLinear.getChildCount() < 3 )
				{
					drawImageArrowInLayout(startLinear, imgBtn);
				}
				else
				{
					displayToast();
				}
			}
			else if(setColorInChild == R.color.grisclair  || setColorInChild == R.color.color_red)
			{
				Toast.makeText(getActivity(), "Aucune instruction disponible pour cette couleur", Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			Toast.makeText(getActivity(), "Slectionner une couleur et cliquer sur une flche", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void drawImageArrowInLayout(LinearLayout selectLinear, ImageButton btn)
	{
		image = new ImageView(getActivity());
		image.setImageDrawable(btn.getDrawable());
		image.setLayoutParams(new LayoutParams(48, 48) {});
		image.setPadding(20, 10, 0, 10);					
		selectLinear.addView(image);
	}
	
	public void displayToast()
	{
		Toast.makeText(getActivity(), "3 mouvements max par couleur", Toast.LENGTH_SHORT).show();
	}
	
	public void sendInstructions() 
	{
		gridIndex = 0;
		curDirection = 1;
		boolean lose = false;
		ArrayList<Instruction> tmp;
		ArrayList<String> responseArray = new ArrayList<String>();
		
		sendStructure = new ArrayList<Instruction>();
		
		ArrayList<Integer> startTabMove = getColorInstructions(startLinear,"Start");
		ArrayList<Integer> blueTabMove = getColorInstructions(blueLinear,"Blue");
		ArrayList<Integer> greenTabMove = getColorInstructions(greenLinear,"Green");
		ArrayList<Integer> blackTabMove = getColorInstructions(blackLinear,"Black");
		ArrayList<Integer> beigeTabMove = getColorInstructions(beigeLinear,"Beige");
		
		//Start
		sendStructure.add(new Instruction(EInstructionType.Start));		
		tmp = colorsInstructions.get("Start");
		responseArray = checkMove(startTabMove);
		
		if(tmp.size() <= 0) {			
			Log.v(tag, "LOSE at start !");
			sendStructure.add(new Instruction(EInstructionType.Lose));
			lose = true;
			
		} else {
			for(int i = 0 ; i < tmp.size() ; i++)
			{			
				if(responseArray.get(i).equals("OK")) {
					sendStructure.add(tmp.get(i));
				} else {
					Log.v(tag, "LOSE during start move !");
					sendStructure.add(new Instruction(EInstructionType.Lose));
					lose = true;
					break;
				}
			}
			
			//Path		
			while(gridIndex != 24 && lose == false) {
				Log.d(tag, "gridIndex = " + gridIndex);
				switch(structureMap.get(gridIndex))
				{
					case Blue:
						tmp = colorsInstructions.get("Blue");
						responseArray = checkMove(blueTabMove);
				        break;
				    case Beige:
				    	tmp = colorsInstructions.get("Beige");
				    	responseArray = checkMove(beigeTabMove);
				        break;
				    case Black:
				    	tmp = colorsInstructions.get("Black");
				    	responseArray = checkMove(blackTabMove);
				        break;
				    case Green:
				    	tmp = colorsInstructions.get("Green");
				    	responseArray = checkMove(greenTabMove);
				        break;
				    default:
				    	Log.v(tag, "LOSE in game !");
				    	sendStructure.add(new Instruction(EInstructionType.Lose));
				    	lose = true;
				    	break;
				}
								
				if(lose == false) {
					if(tmp.size() == 0) {
						Log.v(tag, "LOSE no instruction set !");
						sendStructure.add(new Instruction(EInstructionType.Lose));
						lose = true;
					} else {
						for(int i = 0 ; i < tmp.size() ; i++)
						{	
							Log.v(tag, "for");
							if(responseArray.get(i).equals("OK")) {
								sendStructure.add(tmp.get(i));
							} else {
								Log.v(tag, "LOSE during move !");
								sendStructure.add(new Instruction(EInstructionType.Lose));
								lose = true;
								break;
							}						
						}
					}
				} else {
					Log.v(tag, "Sortie du while");
					break;
				}
				
				if(gridIndex == 24) {
					Log.v(tag, "WIN !");
					sendStructure.add(new Instruction(EInstructionType.Win));
				}
			}
		}
	}
	
	private ArrayList<Integer> getColorInstructions(LinearLayout ll,String key)
	{
		ArrayList<Instruction> colorInstructions = new ArrayList<Instruction>();
		int childCount = ll.getChildCount();
		// 0 = forward | 1 = left | 2 = right
		ArrayList<Integer> moveSequence = new ArrayList<Integer>();
		if(childCount > 0)
		{	
			for(int i = 0 ; i<childCount; i++)
			{
				ImageView d = (ImageView) ll.getChildAt(i);
				Drawable drw = d.getDrawable();			
				if(up.getDrawable() == drw)
				{
					colorInstructions.add(new Instruction(EInstructionType.Forward));
					moveSequence.add(0);
				}
				else if(left.getDrawable() == drw)
				{
					colorInstructions.add(new Instruction(EInstructionType.Left));
					moveSequence.add(1);
				}
				else
				{
					colorInstructions.add(new Instruction(EInstructionType.Right));
					moveSequence.add(2);
				}	
			}
		}
		colorsInstructions.put(key, colorInstructions);
		return moveSequence;
		
	}
	
	public ArrayList<String> checkMove (ArrayList<Integer> moveSequence)
	{
		ArrayList<String> responseArray = new ArrayList<String>();
		int x = gridIndex % 5;
		int y = gridIndex / 5;
		
		Log.d(tag, "x = " + x + " | y = " + y);
		
		if(moveSequence.size() == 0) {
			responseArray.add("no instruction");
		} else {
			for(int i = 0 ; i < moveSequence.size() ; i++) {
				if(moveSequence.get(i) == 0) { // forward
					Log.v(tag, "forward");
					
					switch(curDirection) {
					case 1 :
						x++;
						break;
					case 2 :
						y++;
						break;
					case 3 :
						y--;
						break;
					case 4 :
						x--;
						break;
					}
					
					if(x > 4 || x < 0 || y > 4 || y < 0) {
						Log.v(tag, "out of map");
						responseArray.add("out of map");
						break;
					} else {
						gridIndex = x + (y * 5);
						Log.v(tag, "gridIndex after move : " + gridIndex);
						
						if(structureMap.get(gridIndex) == TypeOfCell.Red) {
							Log.d(tag, "isRed : " + (structureMap.get(gridIndex) == TypeOfCell.Red));
							responseArray.add("trap");
							break;
						} else {
							Log.v(tag, "OK");
							responseArray.add("OK");
						}
					}
				} else {
					Log.v(tag, "turn");
					responseArray.add("OK");
					curDirection = getCurDirection(moveSequence.get(i));
				}
			}
		}
	
		return responseArray;		
	}
	
	public int getCurDirection(int side) {
		int _curDirection = 0;
		
		if(side == 1) { // left
			switch(curDirection) {
			case 1 :
				_curDirection = 3;
				break;
			case 2 :
				_curDirection = 1;
				break;
			case 3 :
				_curDirection = 4;
				break;
			case 4 :
				_curDirection = 2;
				break;
			}
		} else { // rigth
			switch(curDirection) {
			case 1 :
				_curDirection = 2;
				break;
			case 2 :
				_curDirection = 4;
				break;
			case 3 :
				_curDirection = 1;
				break;
			case 4 :
				_curDirection = 3;
				break;
			}
		}
		
		return _curDirection;
	}
}
