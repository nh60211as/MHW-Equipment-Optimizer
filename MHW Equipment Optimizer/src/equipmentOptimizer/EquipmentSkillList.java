package equipmentOptimizer;

import java.util.ArrayList;
import java.util.List;

public class EquipmentSkillList {
	private List<String> _skillName;
	private List<Integer> _skillLevel;

	EquipmentSkillList() {
		_skillName = new ArrayList<>();
		_skillLevel = new ArrayList<>();
	}

	public void add(String skillName, int skillLevel) {
		_skillName.add(skillName);
		_skillLevel.add(skillLevel);
	}

	public void plus(String skillName, int skillLevel) {
		int indexOfSkill = indexOf(skillName);
		if (indexOfSkill == -1) {
			_skillName.add(skillName);
			_skillLevel.add(skillLevel);
		} else {
			_skillLevel.set(indexOfSkill, _skillLevel.get(indexOfSkill) + skillLevel);
		}
	}

	void plus(EquipmentSkillList addedSkillList) {
		for (String currentSkillName : addedSkillList.skillName()) {
			int indexOfSkill = indexOf(currentSkillName);
			if (indexOfSkill == -1) {
				_skillName.add(currentSkillName);
				_skillLevel.add(addedSkillList.getSkillLevel(currentSkillName));
			} else {
				_skillLevel.set(indexOfSkill, addedSkillList.getSkillLevel(currentSkillName));
			}
		}
	}

	void set(String skillName, int skillLevel) {
		int indexOfSkill = indexOf(skillName);
		if (indexOfSkill == -1) {
			_skillName.add(skillName);
			_skillLevel.add(skillLevel);
		} else {
			_skillLevel.set(indexOfSkill, skillLevel);
		}
	}

	void setSkillLevel(int index, int skillLevel) {
		_skillLevel.set(index, skillLevel);
	}

	boolean contains(String skillName) {
		return _skillName.contains(skillName);
	}

	boolean contains(SkillList decorationList) {
		for (Skill skillNow : decorationList) {
			if (_skillName.contains(skillNow.skillName))
				return true;
		}

		return false;
	}

	// return the first index of the instance. return -1 if not found
	int indexOf(String skillName) {
		return _skillName.indexOf(skillName);
	}

	int getSkillLevel(String skillName) {
		int indexOfSkill = indexOf(skillName);
		return (indexOfSkill != -1) ? _skillLevel.get(indexOfSkill) : 0;
	}

	int getSkillLevel(int index) {
		return _skillLevel.get(index);
	}

	List<String> skillName() {
		return _skillName;
	}

	int size() {
		return _skillName.size();
	}

	public String toString() {
		if (_skillName.size() == 0)
			return "(無)";

		StringBuilder output = new StringBuilder();
		for (int i = 0; i <= _skillName.size() - 2; i++) {
			output.append(_skillName.get(i));
			output.append("=");
			output.append(_skillLevel.get(i));
			output.append(", ");
		}
		output.append(_skillName.get(_skillName.size() - 1));
		output.append("=");
		output.append(_skillLevel.get(_skillName.size() - 1));
		return output.toString();
	}

	String toAdditionalString(SkillList input) {
		EquipmentSkillList output = new EquipmentSkillList();
		for (int i = 0; i <= this.size() - 1; i++) {
			int inputIndex = input.indexOf(this._skillName.get(i));
			if (inputIndex != -1) {
				int difference = this._skillLevel.get(i) - input.get(inputIndex).required;
				if (difference >= 1) {
					output.add(this._skillName.get(i), difference);
				}
			}
		}

		return output.toString();
	}

}
