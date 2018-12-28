package equipmentOptimizer;

import java.util.ArrayList;
import java.util.List;

public class SkillList {
	private List<String> _skillName;
	private List<Integer> _skillLevel;
	
	public SkillList(){
		_skillName = new ArrayList<String>();
		_skillLevel = new ArrayList<Integer>();
	}
	
	public void add(String skillName, int skillLevel) {
		_skillName.add(skillName);
		_skillLevel.add(skillLevel);
	}
	
	public boolean contains(String skillName) {
		return _skillName.contains(skillName);
	}
	
	public boolean contains(DecorationList decorationList) {
		for(SkillRequirement skillNow:decorationList) {
			boolean tf = _skillName.contains(skillNow.skillName);
			if(tf)
				return true;
			else
				continue;
		}
			
		return false;
	}
	
	// return the first index of the instance. return -1 if not found
	public int indexOf(String skillName) {
		return _skillName.indexOf(skillName);
	}
	
	public int getSkillLevel(String skillName) {
		int indexOfSkill = indexOf(skillName);
		return (indexOfSkill!=-1) ? _skillLevel.get(indexOfSkill) : 0;
	}
	
	public List<String> skillName(){
		return _skillName;
	}
}
