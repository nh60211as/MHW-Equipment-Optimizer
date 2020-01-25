package equipmentOptimizer;

import java.util.HashMap;

public class Skill {
	String skillName;
	int level;
	public boolean isReplaceable;
	HashMap<Integer, Jewel> jewelsContains;

	public Skill(String skillName, int level, boolean isReplaceable) {
		this.skillName = skillName;
		this.level = level;
		this.isReplaceable = isReplaceable;

		jewelsContains = new HashMap<>();
	}

	public void setRequired(int readSkillRequirement) {
	}
}
