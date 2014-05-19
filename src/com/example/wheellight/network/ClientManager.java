package com.example.wheellight.network;

import instructions.Instruction;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ClientManager
{
	Socket clientSocket;
	ServerSocket serverSocket;
	ServerConnectionTask serverTask;
	IClientManagerListener delegate;
	
	
	private ClientManager()
	{
		try
		{
			serverSocket = new ServerSocket(8765);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private static class Holder
	{
		private static ClientManager sInstance = new ClientManager();
	}
	
	public static ClientManager getInstance()
	{
		return Holder.sInstance;
	}
	
	public void startListening(IClientManagerListener _delegate)
	{
		delegate = _delegate;
		if(serverTask == null)
		{
			serverTask = new ServerConnectionTask();
			serverTask.execute();
		}
		else
		{
			Log.w("ClientManager" , "A server task is already running");
		}
	}
	
	public void closeConnection()
	{
		try
		{
			if(clientSocket.isConnected())
			{
				clientSocket.close();
			}
			if(delegate != null)
			{
				delegate.onSocketClose();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void setClient(Socket client)
	{
		clientSocket = client;
		if(delegate != null)
		{
			delegate.onSocketOpen();
		}
	}
	
	public Socket getClient()
	{
		return clientSocket;
	}
	
	public ServerSocket getServer()
	{
		return serverSocket;
	}
	
	public void sendInstructions(ArrayList<Instruction> _instrus)
	{
		JSONArray json = new JSONArray();
		for(Instruction inst : _instrus)
		{
			try
			{
				JSONObject obj = new JSONObject();
				obj.put("type", inst.type.toString());
				json.put(obj);
			} catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		SocketOutputThread output = new SocketOutputThread(clientSocket, json);
		output.start();
	}
}
