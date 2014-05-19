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
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.example.wheelbrain.network.ClientManager;
import com.example.wheelbrain.network.IClientManagerListener;
import com.example.wheelbrain.network.IWifiP2PListener;
import com.example.wheelbrain.network.WiFiDirectBroadcastReceiver;

public class ConnectionActivity extends Activity implements IWifiP2PListener, IClientManagerListener
{
	WifiP2pManager mManager;
	Channel mChannel;
	BroadcastReceiver mReceiver;	
	IntentFilter mIntentFilter;
	
	ProgressDialog progressDialog;
	boolean isWifiEnabled = false;
	boolean isWifiPaired = false;
	
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

	@Override
	public void onWifiP2PEnable()
	{
		isWifiEnabled = true;
		
		if(progressDialog != null)
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
	}

	@Override
	public void onWifiP2PDisable()
	{
		isWifiEnabled = false;

		if(progressDialog != null)
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
	public void onWifiTunnelMade(WifiP2pInfo _info)
	{
		isWifiPaired = true;
		if(progressDialog != null)
			progressDialog.dismiss();
		
		ClientManager.getInstance().INetHost = _info.groupOwnerAddress;
		ClientManager.getInstance().tryConnection(this);
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

	@Override
	public void onWifiTunnelLost(WifiP2pInfo _info)
	{
		if(isWifiPaired)
		{
			isWifiPaired = false;
			if(progressDialog != null)
				progressDialog.dismiss();
			
			showDialog("Connection lost" , "The connection with the other device has been lost." , true , true);
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
		ClientManager.getInstance().closeConnection();
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
		if(progressDialog!=null)
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
	}

	@Override
	public void onInstructionReceived(ArrayList<Instruction> _instrus)
	{
		for(Instruction i : _instrus)
		{
			Log.w("Instru", i.type.toString());
		}
	}
}
