package com.example.wheellight.network;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.json.JSONArray;

public class SocketOutputThread extends Thread
{
	Socket socket;
	BufferedWriter writer;
	JSONArray json;
	
	public SocketOutputThread(Socket _socket , JSONArray json)
	{
		socket = _socket;
		try
		{
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		if(socket.isConnected())
		{
			try
			{
				writer.write(json.toString());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
