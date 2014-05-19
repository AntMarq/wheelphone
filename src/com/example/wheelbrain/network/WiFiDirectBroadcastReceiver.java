package com.example.wheelbrain.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver
{
    private WifiP2pManager mManager;
    private Channel mChannel;
    private IWifiP2PListener delegate;

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel, IWifiP2PListener _delegate) 
    {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.delegate = _delegate;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action))
        {
        	int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED)
            {
            	delegate.onWifiP2PEnable();
            } else
            {
            	delegate.onWifiP2PDisable();
            }

        }
       else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action))
        {
        }
        else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action))
        {
        	WifiP2pInfo info = (WifiP2pInfo) intent.getExtras().get(WifiP2pManager.EXTRA_WIFI_P2P_INFO);
        	if(info.groupFormed)
        		delegate.onWifiTunnelMade(info);
        	else
        		delegate.onWifiTunnelLost(info);
            // Respond to new connection or disconnections
        }
        else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action))
        {
            // Respond to this device's wifi state changing
        }
    }

}
