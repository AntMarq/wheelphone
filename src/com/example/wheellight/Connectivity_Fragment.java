package com.example.wheellight;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wheellight.network.ClientManager;
import com.example.wheellight.network.IClientManagerListener;
import com.example.wheellight.network.IWifiP2PListener;
import com.example.wheellight.network.WiFiDirectBroadcastReceiver;


public class Connectivity_Fragment extends Fragment implements IWifiP2PListener, PeerListListener, IClientManagerListener
{
	
	WifiP2pManager mManager;
	Channel mChannel;
	BroadcastReceiver mReceiver;	
	IntentFilter mIntentFilter;
	
	WifiP2pDevice connectingDevice;
	WheelLightApp context;
	
	ListView mListView;
	WifiP2pDeviceList peers;
	
	ProgressDialog progressDialog;
	boolean isWifiEnabled = false;
	boolean isWifiPaired = false;
	
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
	public void onWifiP2PEnable()
	{
		isWifiEnabled = true;
		if(peers == null || peers.getDeviceList().size() == 0)
		{
			if(progressDialog != null)
				progressDialog.dismiss();
			
			progressDialog = ProgressDialog.show(getActivity(), "Please wait", "Searching for local peers...", true);
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
		else
		{
			onPeersAvailable(peers);
		}
	}

	@Override
	public void onWifiP2PDisable()
	{
		isWifiEnabled = false;

		if(peers == null)
		{
			if(progressDialog != null)
				progressDialog.dismiss();
			
			progressDialog = ProgressDialog.show(getActivity(), "Wifi Direct is Disabled", "Please enable your Wifi Direct...", true);
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

	@Override
	public void onPeersChanged()
	{
		if(connectingDevice == null)
		{
			if (mManager != null)
		    {
		        mManager.requestPeers(mChannel, this);
		    }
		}
	}
	
	@Override
	public void onPeersAvailable(WifiP2pDeviceList _peers)
	{	
		if(connectingDevice == null)
		{
			peers = _peers;
	
			if(progressDialog != null)
				progressDialog.dismiss();
			
			((DeviceP2PAdapter)mListView.getAdapter()).setData(peers);
		}
	}
	
	public void openConnectionWithDevice(final WifiP2pDevice _device)
	{
		connectingDevice = _device;
		//obtain a peer from the WifiP2pDeviceList
		WifiP2pConfig config = new WifiP2pConfig();
		config.deviceAddress = _device.deviceAddress;
		
		
		if(progressDialog != null)
			progressDialog.dismiss();
		
		progressDialog = ProgressDialog.show(getActivity(), "Please wait", "Waiting for target response.", true);
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
		
		
		mManager.connect(mChannel, config, new ActionListener()
		{

		    @Override
		    public void onSuccess()
		    {
		    }

		    @Override
		    public void onFailure(int reason)
		    {
		    }
		});
	}

	@Override
	public void onWifiTunnelMade(WifiP2pInfo _info)
	{
		isWifiPaired = true;
		if(progressDialog != null)
			progressDialog.dismiss();
		
		ClientManager.getInstance().startListening(this);
		progressDialog = ProgressDialog.show(getActivity(), "Server", "Server socket awaiting connection.", true);
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
		if(isWifiPaired && connectingDevice != null)
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
	    getActivity().registerReceiver(mReceiver, mIntentFilter);
	}
	/* unregister the broadcast receiver */
	@Override
	public void onPause()
	{
	    super.onPause();
	    getActivity().unregisterReceiver(mReceiver);
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		ClientManager.getInstance().closeConnection();
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
	
	static int dialogCount = 0;
	
	public void showDialog(String _title, String _message, boolean canRetry, boolean shouldLeave)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
					peers = null;
					connectingDevice = null;
					((BaseAdapter)mListView.getAdapter()).notifyDataSetChanged();
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
	
	void onBackPressed()
	{
		
	}

	@Override
	public void onSocketOpen()
	{
		if(progressDialog!=null)
			progressDialog.dismiss();
		
		progressDialog = ProgressDialog.show(getActivity(), "Socket", "Socket is opened. Feel free to transfer data.", true);
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
	public void onSocketClose()
	{
	}

}
