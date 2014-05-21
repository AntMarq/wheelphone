package com.example.wheelbrain.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.os.AsyncTask;

public class ConnectionTask extends AsyncTask<Void, Void, Void>
{
	int error_count = 0;
	Socket socket;
	boolean success = false;
    public ConnectionTask()
    {
    	 
    }

    @Override
	protected Void doInBackground(Void ...params)
	{
    	while(!success)
    	{
			try
			{
				socket = new Socket();
				socket.bind(null);
				socket.connect((new InetSocketAddress(ConnectionManager.getInstance().INetHost, 8765)), 500);
				success = true;
			} 
			catch (IOException e)
			{
				try
				{
					Thread.sleep(500 * error_count);
					error_count++;
				} catch (InterruptedException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				e.printStackTrace();
			}
    	}

        return null;
    }


    @Override
    protected void onPostExecute(Void arg)
    {
    	if(success)
    		ConnectionManager.getInstance().setSocket(socket);
    }
}
