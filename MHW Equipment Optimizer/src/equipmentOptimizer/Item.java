package equipmentOptimizer;

public class Item extends Object {
	String name;
	ItemSkillList skills;
	ItemSkillList validSkills;
	int singleValidSkillIndex;
	int singleValidSkillLevel;

	public void setValidSkills(ItemSkillList includedSkill) {
		validSkills = new ItemSkillList();

		int currentSkillIndex = -1;
		for (Integer skillIndex : this.skills.keySet()) {
			if (includedSkill.containsSkillIndex(skillIndex)) {
				currentSkillIndex = skillIndex;
				this.validSkills.put(skillIndex, this.skills.get(skillIndex));
			}
		}

		if (validSkills.size() == 1) {
			singleValidSkillIndex = currentSkillIndex;
			singleValidSkillLevel = this.validSkills.getSkillLevel(singleValidSkillIndex);
		}
	}

	@Override
	public String toString() {
		return this.name;
	}
}
