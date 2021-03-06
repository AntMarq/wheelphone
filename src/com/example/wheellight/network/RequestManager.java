package com.example.wheellight.network;

import instructions.Instruction;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.wheellight.network.Request.ERequestType;

public class RequestManager
{
	public IRequestListener delegate;
	public IOrderCompleteListener orderCompleteDeletage;
	
	/*******************************
	 * InputThread
	 */
	
	private class InputThread extends Thread{
		private InputStream is;
		private DataInputStream dis;
		private boolean running;
		
		public InputThread(Socket newSocket)
		{
			running = true;
			try
			{
				is = newSocket.getInputStream();
				dis = new DataInputStream(is);
			}
			catch (IOException e)
			{
				Log.e("Request Error", "Request Service Error - IO Exception while getting the InputStream.");
				e.printStackTrace();
			}
		}
		
		public void read(String readBuffer)
		{
			Request rq = Request.readRequestFromJson(readBuffer);
			Log.e("received",rq.mType.toString());

			switch(rq.mType)
			{
				case Farewell : readFarewell(rq);
					break;
					
				case Welcome : readWelcome(rq);
					break;
					
				case Complete : readComplete(rq);
					break;
					
				default:
					break;
			}
		}
		
		private void readComplete(Request _rq)
		{
			if(orderCompleteDeletage != null)
			{
				JSONObject content;
				try
				{
					content = new JSONObject(_rq.mContent);
					Instruction readInstruction = Instruction.newFromJson(content);
					
					switch(readInstruction.type)
					{
						case Forward: orderCompleteDeletage.onForwardComplete();
							break;
							
						case Right: orderCompleteDeletage.onRightComplete();
							break;
							
						case Left: orderCompleteDeletage.onLeftComplete();
							break;
							
						case Win: orderCompleteDeletage.onWinComplete();
							break;
							
						case Lose: orderCompleteDeletage.onLoseComplete();
							break;
							
						default:
							break;
					}
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		public void readFarewell(Request rq)
		{
			if(delegate != null)
				delegate.onFarewellReceived();
		}
		
		public void readWelcome(Request rq)
		{
			if(delegate != null)
				delegate.onWelcomeReceived();
		}
		
		
		@Override
		public void run()
		{
			String readBuffer = "";
			while(running)
			{
				try
				{
					if(dis.available() != 0)
					{
						readBuffer = dis.readUTF();
						read(readBuffer);
					}
					else
					{
						try
						{
							Thread.sleep(20);
						}
						catch (InterruptedException e)
						{
							Log.e("Request Error", "InputThread was interrupted while sleeping.");
							e.printStackTrace();
						}
					}
				} catch (IOException e)
				{
					Log.e("Request Error","Request Service Error - IOException while reading the buffer of the InputStream.\nRead : "+ readBuffer);
					e.printStackTrace();
				}
			}
		}
		
		public void turnOff()
		{
			running = false;
		}
	}
	
	/*******************************
	 * OutputThread
	 */
	
	private class OutputThread extends Thread
	{
		private OutputStream os;
		private DataOutputStream dos;
		private LinkedList<Request> stack;
		private boolean running;
		private boolean closing;
		
		public OutputThread(Socket newSocket){
			try {
				os = newSocket.getOutputStream();
			} catch (IOException e) {
				Log.e("Request Error", "IO Exception Exception while getting the OutputStream.");
				e.printStackTrace();
			}
			dos = new DataOutputStream(os);
			stack = new LinkedList<Request>();
			running = true;
			closing = false;
		}
		
		public boolean isClosing(){
			return closing;
		}
		
		public boolean isStacked(){
			return !stack.isEmpty();
		}
		
		@Override
		public void run(){
			while(running){
				if(stack.isEmpty())
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						Log.e("Request Error", "OutputThread was interrupted while sleeping.");
						e.printStackTrace();
					}
				else{
					synchronized(stack){
						send(stack.pop());
					}
				}
			}
		}
		
		private void send(Request request){
			try{
				Log.e("sending",request.mType.toString());
				dos.writeUTF(request.toJson().toString());
				if(request.mType == ERequestType.Farewell){
					closing = false;
					turnOff();
				}
			} catch (IOException e) {
				Log.e("Request Error", "IO Exception while sending request.");
				e.printStackTrace();
			}
		}
		
		public void stackRequest(Request request){
			if(request.mType == ERequestType.Farewell)
				closing = true;
			synchronized(stack){
				stack.add(request);
			}
		}
		
		public void turnOff()
		{
			running = false;
			delegate.onFarewellSent();
		}
	}
	
	/*******************************
	 * MANAGER
	 */
	
	
	private static RequestManager manager;
	public static RequestManager getInstance(){
		if(manager != null)
			return manager;
		else{
			manager = new RequestManager();
			return manager;
		}
	}
	private InputThread inputThread;
	private OutputThread outputThread;
	
	public RequestManager(){
		System.out.println("Request Service - Initialized.");
	}
	
	public void close(){
		if(!isInit())
			return;
		
		outputThread.turnOff();
		inputThread.turnOff();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		outputThread = null;
		inputThread = null;
		try
		{
			ConnectionManager.getInstance().clientSocket.close();
			ConnectionManager.getInstance().serverSocket.close();
		}
		catch (IOException e)
		{
			Log.e("Request Service", "Couldn't close the socket.");
			e.printStackTrace();
		}
	}
	
	public void disconnect(){
		if(!isInit())
			return;
		
		Request request = new Request(ERequestType.Farewell , "");
		outputThread.stackRequest(request);
		inputThread.turnOff();
		while(outputThread.isClosing()){
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		inputThread.interrupt();
		outputThread.interrupt();
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		inputThread = null;
		outputThread = null;
		try
		{
			ConnectionManager.getInstance().clientSocket.close();
			ConnectionManager.getInstance().serverSocket.close();
			if(ConnectionManager.getInstance().delegate != null)
			{
				ConnectionManager.getInstance().delegate.onConnectionClosed();
			}
		}
		catch (IOException e)
		{
			Log.e("Request Service", "Couldn't close the socket.");
			e.printStackTrace();
		}
	}
	
	public void initConnection(){
		inputThread = new InputThread(ConnectionManager.getInstance().clientSocket);
		outputThread = new OutputThread(ConnectionManager.getInstance().clientSocket);
		inputThread.start();
		outputThread.start();
	}
	
	public boolean isInit(){
		return (inputThread != null && outputThread != null);
	}
	
	public boolean isStacked(){
		return outputThread.isStacked();
	}
	
	public void sendRequest(Request request){
		outputThread.stackRequest(request);
	}
	
	public void sendInstructions(ArrayList<Instruction> _instrus)
	{
		Request request = new Request(ERequestType.Instructions, Instruction.instructionsToJson(_instrus).toString());
		sendRequest(request);
	}
	
	public void sendWelcome()
	{
		Request rq = new Request(ERequestType.Welcome, "");
		sendRequest(rq);
	}
	
	public void sendFarewell()
	{
		Request rq = new Request(ERequestType.Farewell , "");
		sendRequest(rq);
	}
}
