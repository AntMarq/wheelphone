package com.example.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.enumClass.TypeOfCell;
import com.example.model.GameMap;
import com.example.wheellight.R;

public class AddMapDialogAdapter extends BaseAdapter{
	
	private Context context;
	int colorGridColumnWidth;
	private GameMap mapDataCreate;
	
	public AddMapDialogAdapter(Context context, GameMap gridMapDialog) {
		this.context = context;
		// defines the width of each color square
		colorGridColumnWidth = 65 ;
		mapDataCreate = gridMapDialog;
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
		TypeOfCell enumColor = mapDataCreate.getMapStructure().get(position);
		
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
		else if(position == 24)
		{
			imageView.setImageResource(R.drawable.finish_flag);
			imageView.setPadding(0, 0, 0, 2);
		}
		else{
			imageView.setBackgroundColor(context.getResources().getColor(R.color.grisclair));
			imageView.setBackgroundResource(R.drawable.border);
		}
		
		//imageView.setId(position);
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
