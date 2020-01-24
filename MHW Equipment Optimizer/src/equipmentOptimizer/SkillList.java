package equipmentOptimizer;

import java.util.ArrayList;

class SkillList extends ArrayList<Skill> {
	int indexOf(String skillName) {
		for (int i = 0; i <= this.size() - 1; i++) {
			if (this.get(i).skillName.contentEquals(skillName)) {
				return i;
			}
		}
		return -1;
	}

}

