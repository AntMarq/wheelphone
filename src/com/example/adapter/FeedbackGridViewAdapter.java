package com.example.adapter;


import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.enumClass.TypeOfCell;
import com.example.model.GameMap;
import com.example.wheellight.FeedbackFragment.BotDirection;
import com.example.wheellight.R;

public class FeedbackGridViewAdapter extends BaseAdapter{

	private Context mContext;
	private int size;
	private ArrayList<TypeOfCell> mapData;
	private BotDirection botDirection = BotDirection.Right;
	
	public FeedbackGridViewAdapter(Context c, int _size, ArrayList<TypeOfCell> _mapData){
	    mContext = c;
	    size = _size;
	    mapData = _mapData;
	}
	
	@Override
	public int getCount() {
		
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
		TypeOfCell enumColor = mapData.get(position);

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
			 switch(botDirection)
			 {
				 case Right:
					 imageView.setRotation(0f);
					 imageView.setBackgroundResource(R.drawable.search);
			     break;
					 
				 case Left:
					 imageView.setRotation(0f);
					 imageView.setBackgroundResource(R.drawable.search_mirror);
					 break;
					 
				 case Up:
					 imageView.setRotation(270f);
					 imageView.setBackgroundResource(R.drawable.search);
					 break;
					 
				 case Down:
					 imageView.setRotation(90f);
					 imageView.setBackgroundResource(R.drawable.search);
					break;
			 }
			 
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
	
	public void setData(ArrayList<TypeOfCell> _mapData, BotDirection _direction)
	{
		mapData = _mapData;
		botDirection = _direction;
		notifyDataSetChanged();
	}

}
