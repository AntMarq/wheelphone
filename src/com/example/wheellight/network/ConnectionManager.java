package com.example.wheellight.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;

public class ConnectionManager
{
	Socket clientSocket;
	ServerSocket serverSocket;
	ConnectionTask serverTask;
	IConnectionManagerListener delegate;
	
	
	private ConnectionManager()
	{
		try
		{
			serverSocket = new ServerSocket(8765);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private static class Holder
	{
		private static ConnectionManager sInstance = new ConnectionManager();
	}
	
	public static ConnectionManager getInstance()
	{
		return Holder.sInstance;
	}
	
	public void startListening(IConnectionManagerListener _delegate)
	{
		delegate = _delegate;
		
		if(serverTask != null)
			serverTask.cancel(true);

		
		serverTask = new ConnectionTask();
		serverTask.execute();
	}
	
	public void closeConnection()
	{
		if(clientSocket != null && clientSocket.isConnected())
		{
			RequestManager.getInstance().close();
			if(delegate != null)
			{
				delegate.onConnectionClosed();
			}
		}
	}
	
	public void setClient(Socket client)
	{
		clientSocket = client;
		RequestManager.getInstance().initConnection();

		if(delegate != null)
		{
			delegate.onConnectionOpened();
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
}
