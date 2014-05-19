package com.example.wheelbrain.network;

import android.net.wifi.p2p.WifiP2pInfo;

public interface IWifiP2PListener
{
	public void onWifiP2PEnable();
	public void onWifiP2PDisable();
	
	public void onWifiTunnelMade(WifiP2pInfo _info);
	public void onWifiTunnelLost(WifiP2pInfo _info);
}
