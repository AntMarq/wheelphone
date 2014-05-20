package com.example.wheellight.network;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONArray;

import android.util.Log;

public class SocketOutputThread extends Thread
{
	Socket socket;
	PrintWriter writer;
	JSONArray json;
	
	public SocketOutputThread(Socket _socket , JSONArray _json)
	{
		socket = _socket;
		json = _json;
		try
		{
			writer = new PrintWriter(socket.getOutputStream(), true);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		while(true)
		{
		if(socket.isConnected())
		{
			try
			{
				Thread.sleep(3000);
				writer.println(json.toString());
				Log.e("wdfsfq","Writing message");
			}
			catch (InterruptedException e1)
			{
				e1.printStackTrace();
			}

		}
		}
	}
}
