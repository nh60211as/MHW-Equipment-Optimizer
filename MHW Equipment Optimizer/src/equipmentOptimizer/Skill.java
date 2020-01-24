package equipmentOptimizer;

public class Skill {
	public int levelOfDecor; // TODO: dummy field
	public boolean isReplaceable; // TODO: dummy field
	public int required; // TODO: dummy field
	public int owned; // TODO: dummy field
	String skillName;
	int level;

	public Skill(String skillName, int level) {
		this.skillName = skillName;
		this.level = level;
	}

	public void setRequired(int readSkillRequirement) {
	}
}
