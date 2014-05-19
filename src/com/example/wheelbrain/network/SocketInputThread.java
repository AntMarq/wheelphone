package com.example.wheelbrain.network;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketInputThread extends Thread
{
	Socket socket;
	BufferedReader reader;
	IClientManagerListener delegate;
	
	public SocketInputThread(Socket _socket , IClientManagerListener _delegate)
	{
		socket = _socket;
		delegate = _delegate;
		try
		{
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		while(socket.isConnected())
		{
			try
			{
				String line = reader.readLine();
				if(!line.isEmpty())
				{
					delegate.onInstructionReceived(Parser.parse(line));
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
