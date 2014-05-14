package com.example.wheellight;

import com.example.wheellight.network.IWifiP2PListener;
import com.example.wheellight.network.WiFiDirectBroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class Connectivity_Fragment extends Fragment implements IWifiP2PListener, PeerListListener{
	
	WifiP2pManager mManager;
	Channel mChannel;
	BroadcastReceiver mReceiver;	
	IntentFilter mIntentFilter;
	WifiP2pDeviceList peers;	
	WifiP2pDevice connectingDevice;
	WheelLightApp context;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(getResources().getLayout(R.layout.connectivity_fragment), container, false);
		context = (WheelLightApp) getActivity().getApplicationContext();
		
		mManager = (WifiP2pManager)getActivity().getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mManager.initialize(context,getActivity().getMainLooper(), null);
		mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
		 
	    mIntentFilter = new IntentFilter();
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
	    mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
	    
	    return view;
	}

	@Override
	public void onPeersAvailable(WifiP2pDeviceList _peers) {
		peers = _peers;
		openConnectionWithDevice(peers.getDeviceList().iterator().next());
		for(WifiP2pDevice device : _peers.getDeviceList())
		{
			Log.w("Main Activity", device.deviceName);
		}	
	}

	@Override
	public void onWifiP2PEnable() {
		Toast.makeText(context, "Wifi P2P ON", Toast.LENGTH_SHORT).show();
		
		mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
		    @Override
		    public void onSuccess()
		    {
		    	Toast.makeText(context, "Discovered succeded", Toast.LENGTH_SHORT).show();
		    }

		    @Override
		    public void onFailure(int reasonCode)
		    {
		    	Toast.makeText(context, "Discovered failed.", Toast.LENGTH_SHORT).show();
		    }
		});
		
	}

	@Override
	public void onWifiP2PDisable() {
		Toast.makeText(context, "Wifi P2P OFF", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onPeersChanged() {
		 if (mManager != null)
		    {
		        mManager.requestPeers(mChannel, this);
		    }
	}
	
	public void openConnectionWithDevice(final WifiP2pDevice _device)
	{
		if(connectingDevice == null || !_device.deviceName.equals(connectingDevice.deviceName))
		{
			connectingDevice = _device;
			//obtain a peer from the WifiP2pDeviceList
			WifiP2pConfig config = new WifiP2pConfig();
			config.deviceAddress = _device.deviceAddress;
			mManager.connect(mChannel, config, new ActionListener()
			{
	
			    @Override
			    public void onSuccess()
			    {
			    	
			        Toast.makeText(context, "Connection succeded", Toast.LENGTH_SHORT).show();
			    }
	
			    @Override
			    public void onFailure(int reason)
			    {
			    	Toast.makeText(context, "Connection failed", Toast.LENGTH_SHORT).show();
			    }
			});
		}
	}
	/* register the broadcast receiver with the intent values to be matched */
	@Override
	public void onResume()
	{
	    super.onResume();
	    getActivity().registerReceiver(mReceiver, mIntentFilter);
	}
	/* unregister the broadcast receiver */
	@Override
	public void onPause()
	{
	    super.onPause();
	    getActivity().unregisterReceiver(mReceiver);
	}

}
