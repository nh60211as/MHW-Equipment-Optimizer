package equipmentOptimizer;

import java.util.ArrayList;
import java.util.List;

class Armor extends Equipment {
	int[] elementalResistance;

	String setBonus;

	// 用於比較防具
	static int BETTER = 0;
	static int SAME = 1;
	static int WORSE = 2;
	static int MAYBE = 3;

	Armor(String input) {
		// 巨蜂頭盔α;76;-2,+1,+1,+1,+2;0,0,0;(無),收刀術,1,麻痺屬性強化,1
		String[] stringBlock = input.split(";");

		// 巨蜂頭盔α
		equipmentName = stringBlock[0];

		// 76
		defense = Integer.parseInt(stringBlock[1]);

		// -2,+1,+1,+1,+2
		String[] elementalDefBlock = stringBlock[2].split(",");
		elementalResistance = new int[5];
		for (int i = 0; i <= elementalResistance.length - 1; i++)
			elementalResistance[i] = Integer.parseInt(elementalDefBlock[i]);

		// 0,0,0
		String[] decorBlock = stringBlock[3].split(",");
		decor3 = Integer.parseInt(decorBlock[0]);
		decor2 = Integer.parseInt(decorBlock[1]);
		decor1 = Integer.parseInt(decorBlock[2]);
		totalDecor = decor3 + decor2 + decor1;

		combinedDecor3 = decor3;
		combinedDecor2 = decor2;
		combinedDecor1 = decor1;
		totalCombinedDecor = combinedDecor3 + combinedDecor2 + combinedDecor1;

		// (無),收刀術,1,麻痺屬性強化,1
		String[] skillBlock = stringBlock[4].split(",");
		// (無)
		setBonus = skillBlock[0];

		skillList = new EquipmentSkillList();
		for (int i = 1; i <= skillBlock.length - 1; i += 2)
			skillList.add(skillBlock[i], Integer.parseInt(skillBlock[i + 1]));

		isReplaceable = true;
	}

	private static int isBetterDecorationSlot(Armor e1, Armor e2) {
		int e1Score = e1.decor3 * 100 + e1.decor2 * 10 + e1.decor1;
		int e2Score = e2.decor3 * 100 + e2.decor2 * 10 + e2.decor1;

		int decorationLevelScore = MAYBE;
		if (e1Score > e2Score)
			decorationLevelScore = BETTER;
		else if (e1Score == e2Score)
			decorationLevelScore = SAME;
		else if (e1Score < e2Score)
			decorationLevelScore = WORSE;

		int decorationNumberScore = MAYBE;
		if (e1.totalDecor > e2.totalDecor)
			decorationNumberScore = BETTER;
		else if (e1.totalDecor == e2.totalDecor)
			decorationNumberScore = SAME;
		else if (e1.totalDecor < e2.totalDecor)
			decorationNumberScore = WORSE;

		return compare(decorationLevelScore, decorationNumberScore);
	}

	private static int isBetterCombinedDecorationSlot(Armor e1, Armor e2) {
		int e1Score = e1.combinedDecor3 * 100 + e1.combinedDecor2 * 10 + e1.combinedDecor1;
		int e2Score = e2.combinedDecor3 * 100 + e2.combinedDecor2 * 10 + e2.combinedDecor1;

		int decorationLevelScore = MAYBE;
		if (e1Score > e2Score)
			decorationLevelScore = BETTER;
		else if (e1Score == e2Score)
			decorationLevelScore = SAME;
		else if (e1Score < e2Score)
			decorationLevelScore = WORSE;

		int decorationNumberScore = MAYBE;
		if (e1.totalCombinedDecor > e2.totalCombinedDecor)
			decorationNumberScore = BETTER;
		else if (e1.totalCombinedDecor == e2.totalCombinedDecor)
			decorationNumberScore = SAME;
		else if (e1.totalCombinedDecor < e2.totalCombinedDecor)
			decorationNumberScore = WORSE;

		return compare(decorationLevelScore, decorationNumberScore);
	}

	private static int compare(int decorationLevelScore, int decorationNumberScore) {
		switch (decorationLevelScore) {
			case 0:
				switch (decorationNumberScore) {
					case 0: // {1,1,0} , {0,0,1}
						return BETTER;
					case 1: // {1,0,1} , {0,2,0}
						return MAYBE;
					case 2: // {1,0,0} , {0,0,3}
						return MAYBE;
					default:
						return MAYBE;
				}
			case 1: //{1,1,0} , {1,1,0}
				return SAME;
			case 2:
				switch (decorationNumberScore) {
					case 0: // {0,0,3} , {1,0,0}
						return MAYBE;
					case 1: // {0,2,0} , {1,0,1}
						return MAYBE;
					case 2: // {0,0,1} , {1,1,0}
						return WORSE;
					default:
						return MAYBE;
				}
			default:
				return MAYBE;
		}
	}

	void setCombinedDecoration(SkillList includedSkill) {
		for (Skill includedSkillNow : includedSkill) {
			int indexOfSkill = skillList.indexOf(includedSkillNow.skillName);
			if (indexOfSkill != -1) {
				int skillLevel = skillList.getSkillLevel(includedSkillNow.skillName);
				switch (includedSkillNow.levelOfDecor) {
					case 1:
						combinedDecor1 += skillLevel;
						break;
					case 2:
						combinedDecor2 += skillLevel;
						break;
					case 3:
						combinedDecor3 += skillLevel;
						break;
					default:
						break;
				}
			}
		}

		totalCombinedDecor = combinedDecor3 + combinedDecor2 + combinedDecor1;
	}

	boolean containsSetBonus(SetBonusList setBonusList) {
		return setBonusList.contains(setBonus);
	}

	int isBetter(Armor anotherArmor, SkillList includedSkill) {
		int BETTERHERE = BETTER;
		int SAMEHERE = SAME;
		int WORSEHERE = WORSE;

		List thisIrreplaceableSkill = new ArrayList<Boolean>();
		List anotherIrreplaceableSkill = new ArrayList<Boolean>();
		for (Skill currentSkill : includedSkill) {
			if (!currentSkill.isReplaceable) {
				String currentSkillName = currentSkill.skillName;
				thisIrreplaceableSkill.add(this.skillList.contains(currentSkillName));
				anotherIrreplaceableSkill.add(anotherArmor.skillList.contains(currentSkillName));
			}
		}

		if (thisIrreplaceableSkill.contains(true) && anotherIrreplaceableSkill.contains(true)) {
			for (int i = 0; i <= thisIrreplaceableSkill.size() - 1; i++) {
				if (thisIrreplaceableSkill.get(i) != anotherIrreplaceableSkill.get(i)) {
					return MAYBE;
				}
			}
		}

		if (thisIrreplaceableSkill.contains(true) && !anotherIrreplaceableSkill.contains(true)) {
			WORSEHERE = MAYBE;
			SAMEHERE = MAYBE;
		}
		if (!thisIrreplaceableSkill.contains(true) && anotherIrreplaceableSkill.contains(true)) {
			BETTERHERE = MAYBE;
			SAMEHERE = MAYBE;
		}

		int[] score = {MAYBE, MAYBE, MAYBE}; // 防禦 實際裝飾珠, 總和裝飾珠

		int temp = MAYBE;
		if (this.defense > anotherArmor.defense)
			temp = BETTER;
		else if (this.defense == anotherArmor.defense)
			temp = SAME;
		else if (this.defense < anotherArmor.defense)
			temp = WORSE;

		score[0] = temp;
		score[1] = isBetterDecorationSlot(this, anotherArmor);
		score[2] = isBetterCombinedDecorationSlot(this, anotherArmor);

		int[] scoreCount = {0, 0, 0, 0};
		for (int i = 0; i <= score.length - 1; i++) {
			scoreCount[score[i]]++;
		}

		if (scoreCount[MAYBE] >= 1) {
			return MAYBE;
		}
		if (scoreCount[SAME] == score.length) {
			return SAMEHERE;
		}
		if (scoreCount[BETTER] >= 1) {
			if (scoreCount[WORSE] >= 1) {
				return MAYBE;
			} else {
				return BETTERHERE;
			}
		} else {
			return WORSEHERE;
		}
		//return MAYBE;
	}
}
