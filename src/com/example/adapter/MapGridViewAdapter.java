package com.example.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.wheellight.GameMap;
import com.example.wheellight.TypeOfCell;

public class MapGridViewAdapter extends BaseAdapter{

	private Context mContext;
	private int size;
	private GameMap mapData;
	
	public MapGridViewAdapter(Context c, int _size, GameMap _mapData){
	    mContext = c;
	    size = _size;
	    mapData = _mapData;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 25;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ImageView imageView;	
		TypeOfCell enumColor = mapData.getMapStructure().get(position);

        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(size, size));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
      
            imageView.setPadding(0, 0, 0, 0);
           
        } else {
            imageView = (ImageView) convertView;
        }

        switch(enumColor)
		{
		 case None:
			 imageView.setBackgroundColor(Color.GRAY); 		 
			 break;
		 case Green:
			 imageView.setBackgroundColor(Color.GREEN);
			 break;
		 case Black:
			 imageView.setBackgroundColor(Color.BLACK);
			 break;
		 case Blue:
			 imageView.setBackgroundColor(Color.BLUE);
			 break;
		 case Start:
			// imageView.setBackgroundResource(R.drawable.ic);
			 break;
		 case End:
			 imageView.setBackgroundColor(Color.CYAN);
			 break;
		 case Yellow:
			 imageView.setBackgroundColor(Color.YELLOW);
			 break;
		 case Red:
			 imageView.setBackgroundColor(Color.RED);
			 break;
			 
		}
    
        return imageView;

	}
	
	

}
