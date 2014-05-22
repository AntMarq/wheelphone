package instructions;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Instruction
{
	public enum EInstructionType
	{
		Start,
		Lose,
		Win,
		Forward,
		Left,
		Right
	};
	
	public EInstructionType type;
	
	// Constructor
	
	public Instruction()
	{
		
	}
	
	public Instruction(EInstructionType _type)
	{
		type = _type;
	}

	// Method
	
	public static Instruction newFromJson(JSONObject object)
	{
		Instruction instru = new Instruction();
		try
		{
			instru.type = EInstructionType.valueOf(object.getString("type"));
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		return instru;
	}
	
	public JSONObject toJson()
	{
		JSONObject obj = new JSONObject();
		try
		{
			obj.put("type", type.toString());
		} 
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return obj;
	}
	
	public static JSONArray instructionsToJson(ArrayList<Instruction> _instrus)
	{
		JSONArray json = new JSONArray();
		for(Instruction inst : _instrus)
		{
			json.put(inst.toJson());
		}
		return json;
	}
	
	public static ArrayList<Instruction> instructionsFromJson(String _jsonString)
	{
		ArrayList<Instruction> instrus = new ArrayList<Instruction>();
		
		try
		{
			JSONArray json = new JSONArray(_jsonString);
			for(int i = 0 ; i < json.length() ; i++)
				instrus.add(newFromJson(json.getJSONObject(i)));
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		return instrus;
	}
	
	@Override
	public String toString() {
		return ""+type;
	}
}
