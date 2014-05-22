package com.example.wheellight.network;

public interface IOrderCompleteListener
{
	public void onForwardComplete();
	public void onLeftComplete();
	public void onRightComplete();
	public void onWinComplete();
	public void onLoseComplete();
}
