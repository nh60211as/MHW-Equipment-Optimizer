package equipmentOptimizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

// ArrayList<JewelList>(0)：1級裝飾珠
// ArrayList<JewelList>(1)：1級裝飾珠
// ArrayList<JewelList>(2)：1級裝飾珠
// ArrayList<JewelList>(3)：1級裝飾珠
public class IncludedJewelTable extends TreeMap<Integer, ArrayList<JewelList>> {
	public IncludedJewelTable() {

	}

	public void add(Integer skillIndex, JewelList jewelList) {
		if (!this.containsKey(skillIndex)) {
			ArrayList<JewelList> addedSkillJewelList = new ArrayList<>();
			for (int i = 1; i <= 4; i++) {
				addedSkillJewelList.add(new JewelList());
			}

			for (Jewel currentJewel : jewelList) {
				int slotLevelMinus1 = currentJewel.slotLevel - 1;
				JewelList currentJewelList = addedSkillJewelList.get(slotLevelMinus1);
				currentJewelList.add(currentJewel);
				addedSkillJewelList.set(slotLevelMinus1, currentJewelList);
			}

			this.put(skillIndex, addedSkillJewelList);
		}

		boolean hasDominantJewel = false;
		int lowestSlotLevel = Integer.MAX_VALUE;
		for (JewelList currentJewelList : this.get(skillIndex)) {
			ArrayList<Integer> removeList = new ArrayList<>();
			for (int currentJewelIndex = 0; currentJewelIndex < currentJewelList.size(); currentJewelIndex++) {
				Jewel currentJewel = currentJewelList.get(currentJewelIndex);
				if (currentJewel.owned == currentJewel.maxRequired) {
					hasDominantJewel = true;
					lowestSlotLevel = Math.min(lowestSlotLevel, currentJewel.slotLevel);
				} else {
					if (hasDominantJewel && currentJewel.validSkills.size() == 1 && currentJewel.slotLevel >= lowestSlotLevel) {
						Collection<Integer> skillLevel = currentJewel.validSkills.values();
						if (skillLevel.iterator().next() == 1)
							removeList.add(currentJewelIndex);
					}
				}
			}
			for (int i = removeList.size() - 1; i >= 0; i--) {
				currentJewelList.remove((int) removeList.get(i));
			}
		}
	}

	public boolean contains(Jewel jewel) {
		for (ArrayList<JewelList> currentSkillOfJewelList : this.values()) {
			for (JewelList currentJewelList : currentSkillOfJewelList) {
				for (Jewel currentJewel : currentJewelList) {
					if (currentJewel.equals(jewel)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public Jewel getJewelWithUniqueSkill(Integer skillIndex) {
		int jewelCount = 0;
		Jewel ans = null;
		for (JewelList currentJewelList : this.get(skillIndex)) {
			jewelCount += currentJewelList.size();
			for (Jewel currentJewel : currentJewelList)
				ans = currentJewel;
		}

		if (jewelCount == 1)
			return ans;
		else
			return null;
	}
}
