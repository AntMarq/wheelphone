package com.example.wheelbrain.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientManager
{
	public Socket socket;
	public InetAddress INetHost;
	ClientConnectionTask clientTask;
	IClientManagerListener delegate;
	SocketInputThread thread;
	
	private ClientManager()
	{
	}
	
	private static class Holder
	{
		private static ClientManager sInstance = new ClientManager();
	}
	
	public static ClientManager getInstance()
	{
		return Holder.sInstance;
	}
	
	public void tryConnection(IClientManagerListener _delegate)
	{
		delegate = _delegate;
		new ClientConnectionTask().execute();
	}
	
	public void closeConnection()
	{
		try
		{
			if(socket.isConnected())
			{
				socket.close();
			}
			if(delegate != null)
			{
				delegate.onConnectionClosed();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void setSocket(Socket client)
	{
		
		socket = client;
		thread = new SocketInputThread(socket,delegate);
		
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
