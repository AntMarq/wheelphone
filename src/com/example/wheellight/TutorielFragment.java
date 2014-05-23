package com.example.wheellight;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TutorielFragment extends Fragment{
	
	private ImageView etape1, etape2, etape3;
	private Bundle b;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(getResources().getLayout(R.layout.tutoruel_fragment), container, false);
		b = new Bundle();
		etape1 = (ImageView)view.findViewById(R.id.imageView2);
		etape2 = (ImageView)view.findViewById(R.id.imageView3);
		etape3 = (ImageView)view.findViewById(R.id.imageView4);
		
		etape1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendBitmapToFullFragment(1);
			}
		});
		
		etape2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				sendBitmapToFullFragment(2);
			}
		});
		

		etape3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				sendBitmapToFullFragment(3);
			}
		});
		
		return view;

	
	}
	
	public void sendBitmapToFullFragment(int i)
	{
		DisplayFullImage_Fragment frag =  new DisplayFullImage_Fragment();
		b.putInt("image", i);
		frag.setArguments(b);	
		FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
		ft.replace(WheelRobotActivity.idbuttonselect,frag,"DisplayFullImage_Fragment");
		ft.addToBackStack(null).commit();
	}
}
