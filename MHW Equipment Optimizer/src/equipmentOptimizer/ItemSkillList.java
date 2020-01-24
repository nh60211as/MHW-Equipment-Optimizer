package equipmentOptimizer;

import java.util.HashMap;

// ItemSkillList:
// Key: skillIndex
// Value: level of skill provided by the item
public class ItemSkillList extends HashMap<Integer, Integer> {
	public ItemSkillList(String input, SkillHashMap skillHashMap) {
		String[] skillBlock = input.split(",");
		if (skillBlock[0].isEmpty())
			return;

		for (int i = 0; i < skillBlock.length; i += 2) {
			int skillIndex = skillHashMap.keyOfSkillName(skillBlock[i]);
			int skillLevel = Integer.parseInt(skillBlock[i + 1]);
			this.put(skillIndex, skillLevel);
		}
	}

	public ItemSkillList() {

	}
}
