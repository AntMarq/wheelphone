package com.example.wheellight;

import instructions.Instruction;

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
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
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

import com.example.wheellight.network.ConnectionManager;
import com.example.wheellight.network.IConnectionManagerListener;
import com.example.wheellight.network.IRequestListener;
import com.example.wheellight.network.IWifiP2PListener;
import com.example.wheellight.network.RequestManager;
import com.example.wheellight.network.WiFiDirectBroadcastReceiver;


public class Connectivity_Fragment extends Fragment implements 
IWifiP2PListener, PeerListListener, IConnectionManagerListener, ConnectionInfoListener, IRequestListener
{
	
	WifiP2pManager mManager;
	Channel mChannel;
	BroadcastReceiver mReceiver;	
	IntentFilter mIntentFilter;
	
	WheelLightApp context;
	
	ListView mListView;
	ArrayList<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
	
	ProgressDialog progressDialog;
	boolean isWifiEnabled = false;
	boolean isWifiPaired = false;
	boolean isConnecting = false;
	
	WifiP2pInfo info;
	
	ArrayList<Instruction> instrus;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
	    if(FeedbackFragment.isQuitting)
	    {
	    	FeedbackFragment.isQuitting = false;
	    	getFragmentManager().popBackStackImmediate();
	    }
	    else
	    {
			Bundle b = getArguments();
			instrus = (ArrayList<Instruction>)b.getSerializable("instruction");
			
			if(RequestManager.getInstance().isInit())
			{
				RequestManager.getInstance().sendInstructions(instrus);
				
				FeedbackFragment feedbackFragment = new FeedbackFragment();
		        getFragmentManager().beginTransaction()
		                .replace(R.id.mainfragment, feedbackFragment)
		                .addToBackStack(null)
		                .commit();	
			}
	    }
			
		RequestManager.getInstance().delegate = this;
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
					Iterator<WifiP2pDevice> it = peers.iterator();
					WifiP2pDevice target = null;
					if(peers.size() > _pos)
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
	public void setWifiP2pEnabled(boolean _state)
	{
		isWifiEnabled = _state;
		if(isWifiEnabled)
			onWifiP2PEnable();
		else
			onWifiP2PDisable();
		
	}

	public void onWifiP2PEnable()
	{
		if(RequestManager.getInstance().isInit())
			return;
		
		if(info != null && info.groupFormed)
			return;
		
		if(progressDialog != null && progressDialog.isShowing())
			progressDialog.dismiss();
		
		progressDialog = ProgressDialog.show(getActivity(), "Please wait", "Looking for peers...", true);
		progressDialog.setCancelable(true);
		progressDialog.setOnCancelListener(new OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface _dialog)
			{
				_dialog.dismiss();
				if(dialogCount == 0)
					requestQuit();
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
		
		progressDialog = ProgressDialog.show(getActivity(), "Wifi Direct is Disabled", "Please enable your Wifi Direct...", true);
		progressDialog.setCancelable(true);
		progressDialog.setOnCancelListener(new OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface _dialog)
			{
				_dialog.dismiss();
				if(dialogCount == 0)
					requestQuit();
			}
		});
	}

	@Override
	public void onPeersAvailable(WifiP2pDeviceList _peers)
	{	
		if(RequestManager.getInstance().isInit())
			return;
		
		if(info != null && info.groupFormed || isConnecting)
			return;
		
	    if (progressDialog != null && progressDialog.isShowing())
	        progressDialog.dismiss();
	    
        peers.clear();
        peers.addAll(_peers.getDeviceList());
        ((DeviceP2PAdapter)mListView.getAdapter()).setData(peers);
        if (peers.size() == 0)
        {
            showDialog("No peer found", "Couldn't find any local peer.", true, true);
            return;
        }
    }

    public void clearPeers()
    {
        peers.clear();
        ((DeviceP2PAdapter)mListView.getAdapter()).setData(peers);
    }
	
	public void openConnectionWithDevice(final WifiP2pDevice _device)
	{
		if(RequestManager.getInstance().isInit())
			return;
		
		isConnecting = true;
		//obtain a peer from the WifiP2pDeviceList
		WifiP2pConfig config = new WifiP2pConfig();
		config.deviceAddress = _device.deviceAddress;
		
		
		if(progressDialog != null && progressDialog.isShowing())
			progressDialog.dismiss();
		
		progressDialog = ProgressDialog.show(getActivity(), "Please wait", "Waiting for target response...", true);
		progressDialog.setCancelable(true);
		progressDialog.setOnCancelListener(new OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface _dialog)
			{
				_dialog.dismiss();
				if(dialogCount == 0)
					requestQuit();
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
		    	isConnecting = false;
		    }
		});
	}
	

	@Override
	public void onConnectionInfoAvailable(final WifiP2pInfo _info)
	{
		if(RequestManager.getInstance().isInit())
			return;
		
		if(isConnecting || info == null)
		{
			
			isConnecting = false;
			
		    if (progressDialog != null && progressDialog.isShowing())
		        progressDialog.dismiss();
		    
		    info = _info;
	
			// InetAddress from WifiP2pInfo struct.
			// info.groupOwnerAddress.getHostAddress();
			
			// After the group negotiation, we assign the group owner as the file
			// server. The file server is single threaded, single connection server
			// socket.
			if (info.groupFormed && info.isGroupOwner)
			{
				
				if(progressDialog != null)
					progressDialog.dismiss();
				
				ConnectionManager.getInstance().startListening(this);
				progressDialog = ProgressDialog.show(getActivity(), "Please wait", "Server socket awaiting connection...", true);
				progressDialog.setCancelable(true);
				progressDialog.setOnCancelListener(new OnCancelListener()
				{
					@Override
					public void onCancel(DialogInterface _dialog)
					{
						_dialog.dismiss();
						if(dialogCount == 0)
							requestQuit();
					}
				});
			}
			
			clearPeers();
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
		RequestManager.getInstance().delegate = null;
		super.onDestroy();
		isWifiEnabled = false;
		isWifiPaired = false;
		isConnecting = false;
	}

	private class DeviceP2PAdapter extends BaseAdapter
	{
		public ArrayList<String> data;
		
		public DeviceP2PAdapter()
		{
			data = new ArrayList<String>();

		}
		
		public void setData(ArrayList<WifiP2pDevice> _peers)
		{
			data.clear();
			for(WifiP2pDevice device : _peers)
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
						requestQuit();
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
					clearPeers();
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
		
		progressDialog = ProgressDialog.show(getActivity(), "Please wait", "Sending informations to client...", true);
		progressDialog.setCancelable(true);
		progressDialog.setOnCancelListener(new OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface _dialog)
			{
				_dialog.dismiss();
				if(dialogCount == 0)
					requestQuit();
			}
		});
		RequestManager.getInstance().sendWelcome();
	}

	@Override
	public void onConnectionClosed()
	{
		if(progressDialog != null && progressDialog.isShowing())
			progressDialog.dismiss();
	}

	@Override
	public void resetP2pState()
	{
		clearPeers();
	}
	
	public void requestQuit()
	{
		if(RequestManager.getInstance().isInit())
		{
			progressDialog = ProgressDialog.show(getActivity(), "Closing", "Closing connection...", true);
			progressDialog.setCancelable(true);
			progressDialog.setOnCancelListener(new OnCancelListener()
			{
				@Override
				public void onCancel(DialogInterface _dialog)
				{
					goBack();
				}
			});
			RequestManager.getInstance().disconnect();
		}
		else
			goBack();
	}
	
	private void goBack()
	{
		if(progressDialog != null && progressDialog.isShowing())
			progressDialog.dismiss();
		
		getFragmentManager().popBackStackImmediate();
	}

	@Override
	public void onFarewellSent()
	{
		getActivity().runOnUiThread(new Runnable()
		{
		    public void run()
		    {
		        goBack();
		    }
		});
	}

	@Override
	public void onWelcomeReceived()
	{
		getActivity().runOnUiThread(new Runnable()
		{
		    public void run()
		    {
		    	/*if(progressDialog != null && progressDialog.isShowing())
					progressDialog.dismiss();
				
				progressDialog = ProgressDialog.show(getActivity(), "Playing", "Now playing.", true);
				progressDialog.setCancelable(true);
				progressDialog.setOnCancelListener(new OnCancelListener()
				{
					@Override
					public void onCancel(DialogInterface _dialog)
					{
						_dialog.dismiss();
						if(dialogCount == 0)
							requestQuit();
					}
				});*/
		    	progressDialog.dismiss();
				RequestManager.getInstance().sendInstructions(instrus);
				FeedbackFragment feedbackFragment = new FeedbackFragment();
		        getFragmentManager().beginTransaction()
		                .replace(R.id.mainfragment, feedbackFragment)
		                .addToBackStack(null)
		                .commit();	
		    }
		});
	
	}

	@Override
	public void onFarewellReceived()
	{
		getActivity().runOnUiThread(new Runnable()
		{
		    public void run()
		    {
		        goBack();
		    }
		});
	}
}
