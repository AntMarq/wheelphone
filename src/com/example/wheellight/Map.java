package com.example.wheellight;

import java.util.HashMap;

public class Map 
{
	HashMap<Integer,TypeOfCell> mapStructure;
	String name;
	
	public Map (HashMap<Integer,TypeOfCell> _mapStructure, String _name)
	{
		mapStructure = _mapStructure;
		name = _name ;
	}
		
}
