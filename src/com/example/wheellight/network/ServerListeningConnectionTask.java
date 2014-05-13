package com.example.wheellight.network;

import java.io.IOException;
import java.net.Socket;

import android.os.AsyncTask;
import android.util.Log;

public class ServerListeningConnectionTask extends AsyncTask<Void, Void, Void>
{
    public ServerListeningConnectionTask()
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
        	Socket client = ClientManager.getInstance().getServer().accept();
            ClientManager.getInstance().setClient(client);
        }
        catch (IOException e)
        {
            Log.e("ServerListeningTask", e.getMessage());
        }
        return null;
    }


    @Override
    protected void onPostExecute(Void arg)
    {
    	
    }
}
