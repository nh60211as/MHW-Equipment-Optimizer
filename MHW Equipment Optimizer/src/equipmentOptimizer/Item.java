package equipmentOptimizer;

public class Item extends Object {
	String name;
	ItemSkillList skills;
	ItemSkillList validSkills;

	public void setValidSkills(ItemSkillList includedSkill) {
		validSkills = new ItemSkillList();

		for (Integer skillIndex : this.skills.keySet()) {
			if (includedSkill.containsSkillIndex(skillIndex))
				this.validSkills.put(skillIndex, this.skills.get(skillIndex));
		}
	}
}
