package com.example.wheellight;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class DisplayFullImage_Fragment extends Fragment 
{

	private Bundle b;
	private static String tag = "FragmentFullImage";
	private int image;
	private ImageView imageView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(getResources().getLayout(R.layout.full_image), container, false);
		b = this.getArguments();
		if(b != null)
		{
			image =  b.getInt("image");
		}		
		imageView = (ImageView)view.findViewById(R.id.full_image_view);
		
		if(image == 1)
		{
			imageView.setImageResource(R.drawable.etape1);
		}
		else if(image == 2)
		{
			imageView.setImageResource(R.drawable.etape2);
		}
		else
		{
			imageView.setImageResource(R.drawable.etape3_legende);
		}
		return view;
	}
}