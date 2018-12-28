package equipmentOptimizer;

import java.util.ArrayList;
import java.util.List;

public class SetBonusList {
	private List<String> _setBonusName;
	private List<Integer> _setBonusRequirement;
	
	public SetBonusList(){
		_setBonusName = new ArrayList<String>();
		_setBonusRequirement = new ArrayList<Integer>();
	}
	
	public void add(String setName, int setRequirement) {
		_setBonusName.add(setName);
		_setBonusRequirement.add(setRequirement);
	}
	
	public boolean contains(String skillName) {
		return _setBonusName.contains(skillName);
	}
}
