package com.example.wheellight;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.wheellight.network.ClientManager;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class Connectivity_Fragment extends Fragment implements IWifiP2PListener, PeerListListener{
	
	WifiP2pManager mManager;
	Channel mChannel;
	BroadcastReceiver mReceiver;	
	IntentFilter mIntentFilter;
	
	WifiP2pDevice connectingDevice;
	WheelLightApp context;
	
	ListView mListView;
	WifiP2pDeviceList peers;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(getResources().getLayout(R.layout.connectivity_fragment), container, false);
		context = (WheelLightApp) getActivity().getApplicationContext();
		
		mListView = (ListView)view.findViewById(R.id.connection_list);
		mListView.setAdapter(new DeviceP2PAdapter());
		mListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> _arg0, View _arg1, int _pos, long _arg3)
			{
				if(peers != null)
				{
					Iterator<WifiP2pDevice> it = peers.getDeviceList().iterator();
					WifiP2pDevice target = null;
					if(peers.getDeviceList().size() > _pos)
					{
						for(int i = 0; i <= _pos ; i++)
						{
							target = it.next();
						}
						
						if(target != null)
						{
							openConnectionWithDevice(target);
						}
					}
				}
			}
		});
		
		
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
	public void onPeersAvailable(WifiP2pDeviceList _peers)
	{
		Toast.makeText(getActivity(), "Found some peers.", Toast.LENGTH_SHORT).show();
		peers = _peers;
		((DeviceP2PAdapter)mListView.getAdapter()).setData(peers);
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
	public void onPeersChanged()
	{
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
			        ClientManager.getInstance().startListening();
			        Toast.makeText(context, "Waiting for client to connect to socket.", Toast.LENGTH_SHORT).show();
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

	private class DeviceP2PAdapter extends BaseAdapter
	{
		public ArrayList<String> data;
		
		public DeviceP2PAdapter()
		{
			data = new ArrayList<String>();

		}
		
		public void setData(WifiP2pDeviceList _deviceList)
		{
			data.clear();
			for(WifiP2pDevice device : _deviceList.getDeviceList())
			{
				data.add(device.deviceName);
			}
			notifyDataSetChanged();
		}
		
		@Override
		public int getCount()
		{
			return data.size();
		}

		@Override
		public Object getItem(int _position)
		{
			return data.get(_position);
		}

		@Override
		public long getItemId(int _position)
		{
			return _position;
		}

		@Override
		public View getView(int _position, View _convertView, ViewGroup _parent)
		{
			if(_convertView == null)
			{
				_convertView = new TextView(getActivity());
				
			}
			((TextView)_convertView).setText(data.get(_position));
			return _convertView;
		}

	}
}
