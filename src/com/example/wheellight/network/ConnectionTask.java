package com.example.wheellight.network;

import java.io.IOException;
import java.net.Socket;

import android.os.AsyncTask;
import android.util.Log;

public class ConnectionTask extends AsyncTask<Void, Void, Void>
{
	boolean success = false;
	Socket client;
    public ConnectionTask()
    {
    }

    @Override
	protected Void doInBackground(Void ...params)
	{
        try
        {
            /**
             * Create a server socket and wait for client connections. This
             * call blocks until a connection is accepted from a client
             */
        	client = ConnectionManager.getInstance().getServer().accept();
        	success = true;
        }
        catch (IOException e)
        {
            Log.e("ServerConnectionTask", e.getMessage());
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void arg)
    {
    	if(success)
    	{
            ConnectionManager.getInstance().setClient(client);
    	}
    }
}
