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
		/*try
		{
		} catch (IOException e)
		{
			e.printStackTrace();
		}*/
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
		if(serverSocket == null || serverSocket.isClosed())
			try
			{
				serverSocket = new ServerSocket(8765);
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		delegate = _delegate;
		
		if(serverTask != null)
			serverTask.cancel(true);

		
		serverTask = new ConnectionTask();
		serverTask.execute();
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
