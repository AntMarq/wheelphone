package com.example.wheelbrain.network;

import instructions.Instruction;

import java.util.ArrayList;

public interface IClientManagerListener
{
	public void onConnectionOpened();
	public void onConnectionClosed();
	public void onInstructionReceived(ArrayList<Instruction> _instrus);
}
