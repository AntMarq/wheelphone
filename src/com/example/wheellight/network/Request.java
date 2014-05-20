package com.example.wheellight.network;

import org.json.JSONException;
import org.json.JSONObject;

public class Request
{
	private static long ID_AUTO = 0;
	
	public enum ERequestType
	{
		Welcome,
		Instructions,
		Acknowledge,
		Complete,
		Farewell
	};
	
	public ERequestType mType;
	public String mContent;
	public long mId;
	
	public Request(){}
	
	public Request(ERequestType _type, String _jsonContent)
	{
		mType = _type;
		mContent = _jsonContent;
		mId = ID_AUTO++;
	}
	
	public Request readRequestFromJson(String _json)
	{
		Request read = new Request();
		
		try
		{
			JSONObject json = new JSONObject(_json);
			read.mType = ERequestType.valueOf(json.getString("type"));
			read.mContent = json.getString("content");
			read.mId = json.getLong("id");
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		return read;
	}
	
	public JSONObject toJson()
	{
		JSONObject obj = new JSONObject();
		try
		{
			obj.put("type", mType.toString());
			obj.put("content", mContent.toString());
			obj.put("id", mId);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return obj;
	}
}
