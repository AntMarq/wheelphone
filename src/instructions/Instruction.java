package instructions;

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
	
	public Instruction()
	{
		
	}
	
	public Instruction(EInstructionType _type)
	{
		type = _type;
	}
	
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
}
