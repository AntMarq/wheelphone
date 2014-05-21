package com.example.wheelbrain;

import java.util.ArrayList;

import instructions.Instruction;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.example.wheelbrain.network.ConnectionManager;
import com.example.wheelbrain.network.IConnectionManagerListener;
import com.example.wheelbrain.network.IWifiP2PListener;
import com.example.wheelbrain.network.WiFiDirectBroadcastReceiver;

public class ConnectionActivity extends Activity implements IWifiP2PListener, IConnectionManagerListener, PeerListListener, ConnectionInfoListener 
{
	WifiP2pManager mManager;
	Channel mChannel;
	BroadcastReceiver mReceiver;	
	IntentFilter mIntentFilter;
	
	ProgressDialog progressDialog;
	boolean isWifiEnabled = false;
	boolean isWifiPaired = false;
	
	WifiP2pInfo info;
	
	int dialogCount = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection);
		
		mManager = (WifiP2pManager)getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mManager.initialize(this,getMainLooper(), null);
		mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
		 
	    mIntentFilter = new IntentFilter();
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.connection, menu);
		return true;
	}

	public void onWifiP2PEnable()
	{
		if(info != null && info.groupFormed)
			return;
		
		if(progressDialog != null && progressDialog.isShowing())
			progressDialog.dismiss();
		
		progressDialog = ProgressDialog.show(this, "Please wait", "Awaiting connection from server...", true);
		progressDialog.setCancelable(true);
		progressDialog.setOnCancelListener(new OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface _dialog)
			{
				_dialog.dismiss();
				if(dialogCount == 0)
					getFragmentManager().popBackStackImmediate();
			}
		});
		
		mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener()
		{
		    @Override
		    public void onSuccess()
		    {
		    }

		    @Override
		    public void onFailure(int reasonCode)
		    {
		    	progressDialog.dismiss();
		    	showDialog("Connection error" , "Failed to discover local peers." , true, true);
		    }
		});
	}

	public void onWifiP2PDisable()
	{
		if(progressDialog != null && progressDialog.isShowing())
			progressDialog.dismiss();
		
		progressDialog = ProgressDialog.show(this, "Wifi Direct is Disabled", "Please enable your Wifi Direct...", true);
		progressDialog.setCancelable(true);
		progressDialog.setOnCancelListener(new OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface _dialog)
			{
				_dialog.dismiss();
				if(dialogCount == 0)
					getFragmentManager().popBackStackImmediate();
			}
		});
	}
	
	@Override
	public void onConnectionInfoAvailable(final WifiP2pInfo _info)
	{
		info = _info;
		
	    if (progressDialog != null && progressDialog.isShowing())
	        progressDialog.dismiss();

		// InetAddress from WifiP2pInfo struct.
		// info.groupOwnerAddress.getHostAddress();
		
		// After the group negotiation, we assign the group owner as the file
		// server. The file server is single threaded, single connection server
		// socket.
		if (_info.groupFormed)
		{
			if( _info.isGroupOwner )
			{
				showDialog("Error" , "This device shouldn't be the group owner." , false, true);
			}
			else
			{
				if(progressDialog != null && progressDialog.isShowing())
					progressDialog.dismiss();
				
				ConnectionManager.getInstance().INetHost = _info.groupOwnerAddress;
				ConnectionManager.getInstance().tryConnection(this);
				progressDialog = ProgressDialog.show(this, "Socket", "Opening socket with the server.", true);
				progressDialog.setCancelable(true);
				progressDialog.setOnCancelListener(new OnCancelListener()
				{
					@Override
					public void onCancel(DialogInterface _dialog)
					{
						_dialog.dismiss();
						if(dialogCount == 0)
							getFragmentManager().popBackStackImmediate();
					}
				});
			}
		}
	}
	
	/* register the broadcast receiver with the intent values to be matched */
	@Override
	public void onResume()
	{
	    super.onResume();
	    registerReceiver(mReceiver, mIntentFilter);
	}
	/* unregister the broadcast receiver */
	@Override
	public void onPause()
	{
	    super.onPause();
	    unregisterReceiver(mReceiver);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		ConnectionManager.getInstance().closeConnection();
	}
	
	public void showDialog(String _title, String _message, boolean canRetry, boolean shouldLeave)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(_title);
		builder.setMessage(_message);
		builder.setCancelable(true);
		
		if(shouldLeave)
		{
			builder.setOnCancelListener(new OnCancelListener()
			{
				
				@Override
				public void onCancel(DialogInterface _dialog)
				{
					_dialog.cancel();
					dialogCount--;
					if(!progressDialog.isShowing() && dialogCount == 0)
						getFragmentManager().popBackStackImmediate();
				}
			});
		}
		else
		{
			builder.setOnCancelListener(new OnCancelListener()
			{
				
				@Override
				public void onCancel(DialogInterface _dialog)
				{
					_dialog.cancel();
					dialogCount--;
				}
			});
		}
		
		if(canRetry)
		{
			builder.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int id)
				{
					dialogCount--;
					dialog.dismiss();
					if(isWifiEnabled)
						onWifiP2PEnable();
					else
						onWifiP2PDisable();
				}
			});
		}
		
		dialogCount++;
		builder.show();
	}

	@Override
	public void onConnectionOpened()
	{
		if(progressDialog != null && progressDialog.isShowing())
			progressDialog.dismiss();
		
		progressDialog = ProgressDialog.show(this, "Socket", "Socket is opened. Feel free to transfer data.", true);
		progressDialog.setCancelable(true);
		progressDialog.setOnCancelListener(new OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface _dialog)
			{
				_dialog.dismiss();
				if(dialogCount == 0)
					getFragmentManager().popBackStackImmediate();
			}
		});
	}

	@Override
	public void onConnectionClosed()
	{
		if(progressDialog != null && progressDialog.isShowing())
			progressDialog.dismiss();
		
		showDialog("Connection closed", "The connection has been closed", true, true);
	}

	@Override
	public void setWifiP2pEnabled(boolean _state)
	{
		isWifiEnabled = _state;
		if(isWifiEnabled)
			onWifiP2PEnable();
		else
			onWifiP2PDisable();
	}

	@Override
	public void onPeersAvailable(WifiP2pDeviceList _peers)
	{
		// TODO Auto-generated method stub
		
	}
}
