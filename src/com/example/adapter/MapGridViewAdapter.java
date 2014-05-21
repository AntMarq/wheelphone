package com.example.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.enumClass.TypeOfCell;
import com.example.model.GameMap;
import com.example.wheellight.R;

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
			 imageView.setBackgroundResource(R.color.grisclair); 		 
			 break;
		 case Green:
			 imageView.setBackgroundResource(R.color.green);
			 break;
		 case Black:
			 imageView.setBackgroundResource(R.color.noir);
			 break;
		 case Blue:
			 imageView.setBackgroundResource(R.color.dark_blue);
			 break;
		 case Start:
			 imageView.setBackgroundResource(R.drawable.search);
			 break;
		 case End:
			 imageView.setBackgroundResource(R.drawable.finish_flag);
			 break;
		 case Beige:
			 imageView.setBackgroundResource(R.color.beige);
			 break;
		 case Red:
			 imageView.setBackgroundResource(R.color.color_red);
			 break;
			 
		}
    
        return imageView;

	}
	
	

}
