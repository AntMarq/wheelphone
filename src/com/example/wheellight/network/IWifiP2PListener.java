package com.example.wheellight.network;

public interface IWifiP2PListener
{
	public void onWifiP2PEnable();
	public void onWifiP2PDisable();
	
	public void onPeersChanged();
}
