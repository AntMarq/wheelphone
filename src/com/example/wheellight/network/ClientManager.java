package com.example.wheellight.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.util.Log;

public class ClientManager
{
	Socket clientSocket;
	ServerSocket serverSocket;
	ServerListeningConnectionTask serverTask;
	IClientManagerListener delegate;
	
	
	private ClientManager()
	{
		try
		{
			serverSocket = new ServerSocket(8888);
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
	
	public void startListening()
	{
		if(serverTask == null)
		{
			serverTask = new ServerListeningConnectionTask();
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
				delegate.onConnectionClosed();
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
	
	public void setDelegate(IClientManagerListener _delegate)
	{
		delegate = _delegate;
	}
}