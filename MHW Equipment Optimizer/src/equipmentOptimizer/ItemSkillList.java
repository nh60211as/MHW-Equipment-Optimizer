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

	public ItemSkillList(ItemSkillList includedSkill) {
		this.putAll(includedSkill);
	}

	public static ItemSkillList removeSkill(final ItemSkillList skillRequired, final ItemSkillList skillHave) {
		ItemSkillList ans = new ItemSkillList(skillRequired);
		for (Integer skillIndex : skillRequired.keySet())
			if (skillHave.containsSkillIndex(skillIndex)) {
				Integer remainingSkillLevel = skillRequired.getSkillLevel(skillIndex) - skillHave.getSkillLevel(skillIndex);
				if (remainingSkillLevel > 0)
					ans.put(skillIndex, remainingSkillLevel);
				else
					ans.remove(skillIndex);
			}

		return ans;
	}

	public static ItemSkillList maxSkill(final ItemSkillList skillList1, final ItemSkillList skillList2) {
		ItemSkillList ans = new ItemSkillList(skillList1);
		for (Integer skillIndex : skillList2.keySet()) {
			Integer skillLevel = 0;
			if (ans.containsSkillIndex(skillIndex))
				skillLevel = Math.max(ans.getSkillLevel(skillIndex), skillList2.getSkillLevel(skillIndex));
			else
				skillLevel = skillList2.getSkillLevel(skillIndex);
			ans.put(skillIndex, skillLevel);
		}

		return ans;
	}

	public boolean containsSkill(ItemSkillList inputItemSkillList) {
		for (Integer skillIndex : inputItemSkillList.keySet())
			if (this.containsKey(skillIndex))
				return true;

		return false;
	}

	public boolean containsSkillIndex(Integer inputSkillIndex) {
		return this.containsKey(inputSkillIndex);
	}

	public Integer getSkillLevel(Integer inputSkillIndex) {
		return this.get(inputSkillIndex);
	}

	public void addSkill(Integer skillIndex, Integer skillLevel) {
		int inputSkillLevel = skillLevel;
		if (this.containsSkillIndex(skillIndex))
			inputSkillLevel += this.getSkillLevel(skillIndex);
		this.put(skillIndex, inputSkillLevel);
	}

	public void addSkill(ItemSkillList inputItemSkillList) {
		for (Integer skillIndex : inputItemSkillList.keySet()) {
			Integer skillLevel = 0;
			if (this.containsSkillIndex(skillIndex))
				skillLevel = this.getSkillLevel(skillIndex) + inputItemSkillList.getSkillLevel(skillIndex);
			else
				skillLevel = inputItemSkillList.getSkillLevel(skillIndex);
			this.put(skillIndex, skillLevel);
		}
	}

	public void addSkill(ItemSkillList inputItemSkillList, int times) {
		for (Integer skillIndex : inputItemSkillList.keySet()) {
			Integer skillLevel = inputItemSkillList.getSkillLevel(skillIndex) * times;
			if (this.containsSkillIndex(skillIndex))
				skillLevel += this.getSkillLevel(skillIndex);
			this.put(skillIndex, skillLevel);
		}
	}

	public String toString(SkillHashMap skillHashMap) {
		if (this.size() == 0)
			return "(無)";

		StringBuilder output = new StringBuilder();
		for (Integer skillIndex : this.keySet()) {
			output.append(skillHashMap.get(skillIndex).skillName);
			output.append("=");
			output.append(this.getSkillLevel(skillIndex));
			output.append(", ");
		}
		return output.substring(0, output.length() - 2);
	}
}
