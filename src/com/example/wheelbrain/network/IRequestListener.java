package com.example.wheelbrain.network;

import instructions.Instruction;

import java.util.ArrayList;

public interface IRequestListener
{
	public void onInstructionReceived(ArrayList<Instruction> _instrus);
	public void onWelcomeReceived();
	public void onFarewellReceived();
	
	public void onFarewellSent();
}
