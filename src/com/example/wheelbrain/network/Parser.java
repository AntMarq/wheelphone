package com.example.wheelbrain.network;

import instructions.Instruction;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public class Parser
{
	public static ArrayList<Instruction> parse(String _line)
	{
		ArrayList<Instruction> instructions = new ArrayList<Instruction>();
		try
		{
			JSONArray json = new JSONArray(_line);
			
			for(int i = 0 ; i < json.length() ; i++)
			{
				instructions.add(Instruction.newFromJson(json.getJSONObject(i)));
			}
		}
		catch(JSONException e)
		{
			Log.e("JSON","JSON Exception");
		}
		return instructions;
	}
}
