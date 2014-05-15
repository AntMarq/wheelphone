package com.example.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wheellight.R;

public class ChooseGridViewAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater inflater;
	
	public ChooseGridViewAdapter(Context c){
	    mContext = c;
	    inflater = LayoutInflater.from(c);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 9;
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
		
		
		ViewHolder holder;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        	
        	 holder = new ViewHolder();	 
        	 convertView = inflater.inflate(R.layout.gridview_cell, null);
        	 holder.title = (TextView) convertView.findViewById(R.id.title);
        	 holder.gridView = (GridView) convertView.findViewById(R.id.cell_gridview);
        	 holder.gridView.setAdapter(new MapGridViewAdapter(mContext, 40));
        //	 holder.gridView.setLayoutParams(new GridView.LayoutParams(250, 250));  
        	 convertView.setTag(holder);
           
        } else {
        	holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText("Map number X");
      
        return convertView;
	}
	
	private class ViewHolder 
    {
           TextView title;
           GridView gridView ;	         
    }

}
