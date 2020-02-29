package equipmentOptimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class IncludedJewelTable {
	JewelList multiSkillUniqueJewelList;
	JewelList singleSkillUniqueJewelList;
	JewelList multiSkillNonUniqueJewelList;
	HashMap<Integer, ArrayList<JewelList>> singleSkillNonUniqueJewelList;

	public IncludedJewelTable(final JewelList includedJewel, final SkillHashMap skillHashMap) {
		JewelList multiSkillJewelList = new JewelList();
		HashMap<Integer, JewelList> skillJewelList = new HashMap<>();
		for (Jewel currentJewel : includedJewel) {
			if (currentJewel.validSkills.size() > 1)
				multiSkillJewelList.add(currentJewel);

			for (Integer currentSkillIndex : currentJewel.validSkills.keySet()) {
				if (!skillJewelList.containsKey(currentSkillIndex)) {
					skillJewelList.put(currentSkillIndex, new JewelList(currentJewel));
				} else {
					JewelList currentJewelList = skillJewelList.get(currentSkillIndex);
					currentJewelList.add(currentJewel);
				}
			}
		}

		multiSkillUniqueJewelList = new JewelList();
		multiSkillNonUniqueJewelList = new JewelList();
		HashSet<Integer> skillIndexRecord = new HashSet<>();
		// 將 提供獨特多重技能的裝飾珠 和
		// 　 提供非獨特多重技能的裝飾珠 分開
		// 並記錄非獨特多重技能的裝飾珠提供什麼技能
		for (Jewel currentJewel : multiSkillJewelList) {
			if (isUniqueJewel(currentJewel, skillJewelList))
				multiSkillUniqueJewelList.add(currentJewel);
			else {
				multiSkillNonUniqueJewelList.add(currentJewel);
				skillIndexRecord.addAll(currentJewel.validSkills.keySet());
			}
			removeJewel(currentJewel, skillJewelList);
		}

		singleSkillUniqueJewelList = new JewelList();
		singleSkillNonUniqueJewelList = new HashMap<>();
		for (Map.Entry<Integer, JewelList> entry : skillJewelList.entrySet()) {
			Integer currentSkillIndex = entry.getKey();
			JewelList currentJewelList = entry.getValue();

			int maxSkillLevel = skillHashMap.get(currentSkillIndex).level;

			ArrayList<JewelList> separatedSkillJewelList = separateJewelListBySkillLevel(currentJewelList);
			JewelList multiLevelJewelList = separatedSkillJewelList.get(0);
			JewelList singleLevelJewelList = separatedSkillJewelList.get(1);

			if (!multiLevelJewelList.isEmpty()) {
				int maxSkillLevelNeed = maxSkillLevel;
				JewelList tempJewelList = new JewelList(multiLevelJewelList.get(0));
				maxSkillLevelNeed -= multiLevelJewelList.get(0).owned * multiLevelJewelList.get(0).singleValidSkillLevel;
				for (int i = 1; i < multiLevelJewelList.size(); i++) {
					Jewel currentJewel = multiLevelJewelList.get(i);
					if (maxSkillLevelNeed <= 0)
						break;
					else {
						tempJewelList.add(currentJewel);
						maxSkillLevelNeed -= currentJewel.owned * currentJewel.singleValidSkillLevel;
					}
				}
				multiLevelJewelList = tempJewelList;
			}

			if (!singleLevelJewelList.isEmpty()) {
				int maxSkillLevelNeed = maxSkillLevel;
				JewelList tempJewelList = new JewelList(singleLevelJewelList.get(0));
				maxSkillLevelNeed -= singleLevelJewelList.get(0).owned * singleLevelJewelList.get(0).singleValidSkillLevel;
				for (int i = 1; i < singleLevelJewelList.size(); i++) {
					Jewel currentJewel = singleLevelJewelList.get(i);
					if (maxSkillLevelNeed <= 0)
						break;
					else {
						tempJewelList.add(currentJewel);
						maxSkillLevelNeed -= currentJewel.owned * currentJewel.singleValidSkillLevel;
					}
				}
				singleLevelJewelList = tempJewelList;
			}

			int multiLevelJewelListSize = multiLevelJewelList.size();
			int singleLevelJewelListSize = singleLevelJewelList.size();
			int totalSize = multiLevelJewelListSize + singleLevelJewelListSize;

			// 目前的技能包含的裝飾珠是唯一的
			if (totalSize == 1 && !skillIndexRecord.contains(currentSkillIndex)) {
				if (multiLevelJewelListSize == 1)
					singleSkillUniqueJewelList.add(multiLevelJewelList.get(0));
				if (singleLevelJewelListSize == 1)
					singleSkillUniqueJewelList.add(singleLevelJewelList.get(0));
			} else {
				separatedSkillJewelList.set(0, multiLevelJewelList);
				separatedSkillJewelList.set(1, singleLevelJewelList);
				singleSkillNonUniqueJewelList.put(currentSkillIndex, separatedSkillJewelList);

			}
		}
	}

	private boolean isUniqueJewel(final Jewel jewel, final HashMap<Integer, JewelList> skillJewelList) {
		for (Integer skillIndex : jewel.validSkills.keySet()) {
			if (skillJewelList.get(skillIndex).size() == 1)
				return true;
		}
		return false;
	}

	private void removeJewel(final Jewel jewel, final HashMap<Integer, JewelList> skillJewelList) {
		for (JewelList currentJewelList : skillJewelList.values()) {
			currentJewelList.remove(jewel);
		}
	}

	private ArrayList<JewelList> separateJewelListBySkillLevel(JewelList jewelList) {
		ArrayList<JewelList> sortedJewelList = new ArrayList<>();
		sortedJewelList.add(new JewelList());
		sortedJewelList.add(new JewelList());

		// 如果裝飾珠提供的技能等級大於 1 則加入第一個 ArrayList
		// 否則加入第二個 ArrayList
		for (Jewel currentJewel : jewelList) {
			if (currentJewel.singleValidSkillLevel > 1)
				sortedJewelList.get(0).add(currentJewel);
			else
				sortedJewelList.get(1).add(currentJewel);
		}

		for (JewelList currentJewelList : sortedJewelList)
			currentJewelList.sort(new JewelList.CustomComparator());

		return sortedJewelList;
	}
}
