package com.example.wheelbrain.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.os.AsyncTask;

public class ClientConnectionTask extends AsyncTask<Void, Void, Void>
{
	Socket socket;
	boolean success = false;
    public ClientConnectionTask()
    {
    	 socket = new Socket();
    }

    @Override
	protected Void doInBackground(Void ...params)
	{


		try
		{
		    socket.bind(null);
			socket.connect((new InetSocketAddress(ClientManager.getInstance().INetHost, 8765)), 500);
			success = true;
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return null;
    }


    @Override
    protected void onPostExecute(Void arg)
    {
    	if(success)
    		ClientManager.getInstance().setSocket(socket);
    }
}
