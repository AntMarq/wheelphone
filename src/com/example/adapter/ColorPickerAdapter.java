package com.example.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.wheellight.R;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


public class ColorPickerAdapter extends BaseAdapter {

	private Context context;
	// list which holds the colors to be displayed
	private List<Integer> colorList = new ArrayList<Integer>();
	// width of grid column
	int colorGridColumnWidth;

	public ColorPickerAdapter(Context context,ArrayList<Integer> _colorList) {
		this.context = context;
		this.colorList = _colorList;
		// defines the width of each color square
		colorGridColumnWidth = 65 ;

		
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;

		// can we reuse a view?
		if (convertView == null) {
			imageView = new ImageView(context);
			// set the width of each color square
			imageView.setLayoutParams(new GridView.LayoutParams(colorGridColumnWidth, colorGridColumnWidth));
			imageView.setPadding(0, 20, 0, 0);
		} else {
			imageView = (ImageView) convertView;
		}
		if(position == 0)
		{
			imageView.setImageResource(R.drawable.search_noarrow);
			imageView.setPadding(0, 0, 0, 0);
		}
		imageView.setBackgroundColor(colorList.get(position));
		imageView.setId(position);

		return imageView;
	}

	public int getCount() {
		return colorList.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}
}