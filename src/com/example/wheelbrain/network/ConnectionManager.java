package com.example.wheelbrain.network;

import java.net.InetAddress;
import java.net.Socket;

public class ConnectionManager
{
	public Socket socket;
	public InetAddress INetHost;
	ConnectionTask clientTask;
	IConnectionManagerListener delegate;
	
	private ConnectionManager()
	{
	}
	
	private static class Holder
	{
		private static ConnectionManager sInstance = new ConnectionManager();
	}
	
	public static ConnectionManager getInstance()
	{
		return Holder.sInstance;
	}
	
	public void tryConnection(IConnectionManagerListener _delegate)
	{
		delegate = _delegate;
		new ConnectionTask().execute();
	}
	
	public void closeConnection()
	{
		if(socket != null && socket.isConnected())
		{
			RequestManager.getInstance().close();
		}
	}
	
	public void setSocket(Socket client)
	{
		socket = client;
		RequestManager.getInstance().initConnection();
		
		if(delegate != null)
		{
			delegate.onConnectionOpened();
		}
	}
	
	public Socket getSocket()
	{
		return socket;
	}
}
