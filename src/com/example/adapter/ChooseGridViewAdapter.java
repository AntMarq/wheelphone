package com.example.adapter;


import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.model.GameMap;
import com.example.wheellight.R;

public class ChooseGridViewAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater inflater;
	private ArrayList<GameMap> listMap;
	
	public ChooseGridViewAdapter(Context c, ArrayList<GameMap> _listMap){
	    mContext = c;
	    inflater = LayoutInflater.from(c);
	    listMap = _listMap;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listMap.size();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		//Log.v("choose adapter", "getView");
		ViewHolder holder;
		GameMap objData = ((GameMap)(listMap.get (position)));
		
		
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        	
        	 holder = new ViewHolder();	 
        	 convertView = inflater.inflate(R.layout.gridview_cell, null);
        	 holder.title = (TextView) convertView.findViewById(R.id.title);
        	 holder.imageView = (ImageView) convertView.findViewById(R.id.imageView1);
        	// holder.imageView.setAdapter(new MapGridViewAdapter(mContext,40, objData));
        	 holder.imageView.setImageBitmap(objData.getImage());
        	 convertView.setTag(holder);
           
        } else {
        	holder = (ViewHolder) convertView.getTag();
        }
       
        holder.title.setText(objData.getName());
      //  holder.imageView.setImageBitmap(objData.getImage());
        
        return convertView;
	}
	
	private class ViewHolder 
    {
           TextView title;
           ImageView imageView ;	         
    }
	
	

}
