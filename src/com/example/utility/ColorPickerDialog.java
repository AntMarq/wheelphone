package com.example.utility;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.example.adapter.ColorPickerAdapter;
import com.example.wheellight.R;

public class ColorPickerDialog extends Dialog {
	
	
	
	public interface OnColorChangedListener {  
	     void colorChanged(int color);  
	   }  
	
	private OnColorChangedListener mListener; 

	public ColorPickerDialog(Context context, OnColorChangedListener listener) {
		super(context);
		this.setTitle("ColorPickerDialog");
		mListener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.color_picker);
		
		// for convenience and better reading, we place the colors in a two dimension array
		String colors[][] = {{ "FFFFFF" , "FCDFA6", "AEAEAE", "E64C66", "1BBC9B", "3090DE", "000000" }};

		final ArrayList<Integer>colorList = new ArrayList<Integer>();

		// add the color array to the list
		for (int i = 0; i < colors.length; i++) {
			for (int j = 0; j < colors[i].length; j++) {
				colorList.add(Color.parseColor("#" + colors[i][j]));
			}
		}
		
		GridView gridViewColors = (GridView) findViewById(R.id.gridViewColors);
		gridViewColors.setAdapter(new ColorPickerAdapter(getContext(),colorList));
		
		// close the dialog on item click
		gridViewColors.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {	
				Log.v("ColorPicker", "position = " + position);
				findColor(position);
				ColorPickerDialog.this.dismiss();
			}
		});
	}
	
	public void findColor(int position) {
		switch(position)
		{		
			case 0 :
				mListener.colorChanged(R.color.white);
			break;
			case 1 :
				mListener.colorChanged(R.color.beige);
			break;
			case 2 :
				mListener.colorChanged(R.color.grisclair);
			break;
			case 3 :
				mListener.colorChanged(R.color.color_red);
			break;
			case 4 :
				mListener.colorChanged(R.color.green);
			break;
			case 5 :
				mListener.colorChanged(R.color.dark_blue);
			break;
			case 6:
				mListener.colorChanged(R.color.noir);
			break;
				
		}
		
		
	}

}
