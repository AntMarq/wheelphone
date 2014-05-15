package com.example.wheellight.network;

import android.net.wifi.p2p.WifiP2pInfo;

public interface IWifiP2PListener
{
	public void onWifiP2PEnable();
	public void onWifiP2PDisable();
	
	public void onPeersChanged();
	public void onConnectionOn(WifiP2pInfo _info);
	public void onConnectionOff(WifiP2pInfo _info);
}
