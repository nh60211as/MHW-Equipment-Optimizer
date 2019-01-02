package equipmentOptimizer;

import java.util.ArrayList;
import java.util.List;

public class EquipmentSkillList {
	private List<String> _skillName;
	private List<Integer> _skillLevel;

	public EquipmentSkillList(){
		_skillName = new ArrayList<String>();
		_skillLevel = new ArrayList<Integer>();
	}

	public void add(String skillName, int skillLevel) {
		_skillName.add(skillName);
		_skillLevel.add(skillLevel);
	}

	public void plus(String skillName, int skillLevel) {
		int indexOfSkill = indexOf(skillName);
		if(indexOfSkill==-1) {
			_skillName.add(skillName);
			_skillLevel.add(skillLevel);
		}
		else {
			_skillLevel.set(indexOfSkill, _skillLevel.get(indexOfSkill)+skillLevel);
		}
	}
	
	public void set(String skillName, int skillLevel) {
		int indexOfSkill = indexOf(skillName);
		if(indexOfSkill==-1) {
			_skillName.add(skillName);
			_skillLevel.add(skillLevel);
		}
		else {
			_skillLevel.set(indexOfSkill,skillLevel);
		}
	}
	
	public void setSkillLevel(int index, int skillLevel) {
		_skillLevel.set(index,skillLevel);
	}

	public boolean contains(String skillName) {
		return _skillName.contains(skillName);
	}

	public boolean contains(SkillList decorationList) {
		for(Skill skillNow:decorationList) {
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
	
	public int getSkillLevel(int index) {
		return _skillLevel.get(index);
	}

	public List<String> skillName(){
		return _skillName;
	}
	
	public int size() {
		return _skillName.size();
	}
	
	public void print() {
		for(int i=0;i<=_skillName.size()-2;i++) {
			System.out.print(_skillName.get(i)+"="+_skillLevel.get(i)+", ");
		}
		System.out.println(_skillName.get(_skillName.size()-1)+"="+_skillLevel.get(_skillName.size()-1));
	}
}
