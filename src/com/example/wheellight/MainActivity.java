package com.example.wheellight;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.example.wheellight.network.IWifiP2PListener;
import com.example.wheellight.network.WiFiDirectBroadcastReceiver;

public class MainActivity extends Activity implements IWifiP2PListener, PeerListListener
{
	WifiP2pManager mManager;
	Channel mChannel;
	BroadcastReceiver mReceiver;
	
	IntentFilter mIntentFilter;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mManager.initialize(this, getMainLooper(), null);
		mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
		 
	    mIntentFilter = new IntentFilter();
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);


	}
	
	/* register the broadcast receiver with the intent values to be matched */
	@Override
	protected void onResume()
	{
	    super.onResume();
	    registerReceiver(mReceiver, mIntentFilter);
	}
	/* unregister the broadcast receiver */
	@Override
	protected void onPause()
	{
	    super.onPause();
	    unregisterReceiver(mReceiver);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onWifiP2PEnable()
	{
		Toast.makeText(this, "Wifi P2P ON", Toast.LENGTH_SHORT).show();
		
		mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
		    @Override
		    public void onSuccess()
		    {
		    	Toast.makeText(getApplicationContext(), "Found some peers", Toast.LENGTH_SHORT).show();
		    }

		    @Override
		    public void onFailure(int reasonCode)
		    {
		    	Toast.makeText(getApplicationContext(), "didn't find any peers.", Toast.LENGTH_SHORT).show();
		    }
		});

	}

	@Override
	public void onWifiP2PDisable()
	{
		Toast.makeText(this, "Wifi P2P OFF", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onPeersChanged()
	{
	    // request available peers from the wifi p2p manager. This is an
	    // asynchronous call and the calling activity is notified with a
	    // callback on PeerListListener.onPeersAvailable()
	    if (mManager != null) {
	        mManager.requestPeers(mChannel, this);
	    }
	}

	@Override
	public void onPeersAvailable(WifiP2pDeviceList _peers)
	{
		for(WifiP2pDevice device : _peers.getDeviceList())
		{
			Log.w("Main Activity", device.deviceName);
		}
	}

}
