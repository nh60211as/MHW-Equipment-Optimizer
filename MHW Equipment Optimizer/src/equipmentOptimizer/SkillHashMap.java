package equipmentOptimizer;

import java.util.HashMap;

class SkillHashMap extends HashMap<Integer, Skill> {

	SkillHashMap() {
	}

	int keyOfSkillName(String skillName) {
		for (Integer skillIndex : this.keySet()) {
			if (this.get(skillIndex).skillName.contentEquals(skillName)) {
				return skillIndex;
			}
		}
		return -1;
	}

	int indexOf(String skillName) {
		for (int i = 0; i <= this.size() - 1; i++) {
			if (this.get(i).skillName.contentEquals(skillName)) {
				return i;
			}
		}
		return -1;
	}

}
