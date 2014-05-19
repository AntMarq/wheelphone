package com.example.wheellight;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class GameMap 
{
	ArrayList<TypeOfCell> mapStructure;
	String name;
	Bitmap image;
	int idmap;
	
	public GameMap (int _idmap,ArrayList<TypeOfCell> _mapStructure, String _name, Bitmap _image)
	{
		idmap = _idmap;
		mapStructure = _mapStructure;
		name = _name ;
		image = _image;
	}
	
	public GameMap (ArrayList<TypeOfCell> _mapStructure, String _name)
	{
		mapStructure = _mapStructure;
		name = _name ;
	}

	public GameMap (int _idmap,ArrayList<TypeOfCell> _mapStructure, String _name)
	{
		idmap = _idmap;
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

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public int getIdmap() {
		return idmap;
	}

	public void setIdmap(int idmap) {
		this.idmap = idmap;
	}
	
	
		
}
