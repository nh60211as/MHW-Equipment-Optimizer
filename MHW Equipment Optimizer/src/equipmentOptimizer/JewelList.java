package equipmentOptimizer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class JewelList extends ArrayList<Jewel> {
	public JewelList(Jewel jewel) {
		this.add(jewel);
	}

	public JewelList(JewelList includedJewel) {
		this.addAll(includedJewel);
	}

	public JewelList(HashMap<Integer, ArrayList<JewelList>> singleSkillUniqueJewelHashMap) {
		for (ArrayList<JewelList> currentSeparatedJewelList : singleSkillUniqueJewelHashMap.values())
			for (JewelList currentJewelList : currentSeparatedJewelList)
				this.addAll(currentJewelList);
	}

	public JewelList(HashMap<Integer, JewelList> singleSkillJewelHashMap, int a) {
		for (JewelList currentJewelList : singleSkillJewelHashMap.values())
			this.addAll(currentJewelList);
	}

	public JewelList() {
	}

	public JewelList(ArrayList<JewelList> separatedSkillJewelList) {
		for (JewelList currentJewelList : separatedSkillJewelList)
			this.addAll(currentJewelList);
	}

	public JewelList getJewelsContainsValidSkill(int skillIndex) {
		JewelList ans = new JewelList();

		for (Jewel currentJewel : this) {
			if (currentJewel.validSkills.containsSkillIndex(skillIndex))
				ans.add(currentJewel);
		}

		return ans;
	}

	public long getIterationSizeAndRemoveUnnecessaryJewels(ItemSkillList skillNeed) {
		long totalIter = 1;
		for (int i = this.size() - 1; i >= 0; i--) {
			if (!this.get(i).validSkills.containsSkill(skillNeed))
				this.remove(i);
			else
				totalIter *= (this.get(i).owned + 1); // calculate the iteration needed for all the jewels
		}
		return totalIter;
	}

	public static class CustomComparator implements Comparator<Jewel> {
		@Override
		public int compare(Jewel o1, Jewel o2) {
			if (o1.slotLevel < o2.slotLevel)
				return -1;
			if (o1.slotLevel > o2.slotLevel)
				return 1;

			if (o1.singleValidSkillLevel > o2.singleValidSkillLevel)
				return -1;
			if (o1.singleValidSkillLevel == o2.singleValidSkillLevel)
				return 0;
			if (o1.singleValidSkillLevel < o2.singleValidSkillLevel)
				return 1;

			return 0;
		}
	}
}
