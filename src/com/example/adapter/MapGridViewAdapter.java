package com.example.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class MapGridViewAdapter extends BaseAdapter{

	private Context mContext;
	private int r,g,b;
	private int size;
	
	public MapGridViewAdapter(Context c, int _size){
	    mContext = c;
	    size = _size;
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
		r= (int) (Math.random()*255);
		g=(int) (Math.random()*255);
		b=(int) (Math.random()*255);
		
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(size, size));
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
      
            imageView.setPadding(0, 0, 0, 0);
           
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setBackgroundColor(Color.rgb(r, g, b));
        return imageView;

	}
	
	

}
