package com.example.wheellight;

import java.util.ArrayList;

public class GameMap 
{
	ArrayList<TypeOfCell> mapStructure;
	String name;
	
	public GameMap (ArrayList<TypeOfCell> _mapStructure, String _name)
	{
		mapStructure = _mapStructure;
		name = _name ;
	}

	public ArrayList<TypeOfCell> getMapStructure() {
		return mapStructure;
	}

	public void setMapStructure(ArrayList<TypeOfCell> mapStructure) {
		this.mapStructure = mapStructure;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
		
}
