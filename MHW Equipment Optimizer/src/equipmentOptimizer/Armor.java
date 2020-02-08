package equipmentOptimizer;

class Armor extends Equipment {
	// 用於比較防具
	static final int BETTER = 0;
	static final int SAME = 1;
	static final int WORSE = 2;
	static final int MAYBE = 3;
	final int[] elementalResistance;
	final String setBonus;

	Armor(final String input, final SkillHashMap skillHashMap) {
		// 巨蜂頭盔α;76;-2,+1,+1,+1,+2;0,0,0,0;(無);收刀術,1,麻痺屬性強化,1
		String[] stringBlock = input.split(";", -1);

		// 巨蜂頭盔α
		name = stringBlock[0];

		// 76
		defense = Integer.parseInt(stringBlock[1]);

		// -2,+1,+1,+1,+2
		String[] elementalDefBlock = stringBlock[2].split(",");
		elementalResistance = new int[5];
		for (int i = 0; i <= elementalResistance.length - 1; i++)
			elementalResistance[i] = Integer.parseInt(elementalDefBlock[i]);

		// 0,0,0,0
		String[] decorBlock = stringBlock[3].split(",");
		setDecor(decorBlock);

		// (無);收刀術,1,麻痺屬性強化,1
		String[] skillBlock = stringBlock[4].split(",");
		// (無)
		if (skillBlock[0].contentEquals("(無)"))
			setBonus = "";
		else
			setBonus = skillBlock[0];
		// 收刀術,1,麻痺屬性強化,1
		skills = new ItemSkillList(stringBlock[5], skillHashMap);
	}

	static int isBetterDecorationSlot(Armor e1, Armor e2) {
		int e1Score = e1.decor4 * 1000 + e1.decor3 * 100 + e1.decor2 * 10 + e1.decor1;
		int e2Score = e2.decor4 * 1000 + e2.decor3 * 100 + e2.decor2 * 10 + e2.decor1;

		int decorationLevelScore = decorationScore(e1Score, e2Score);
		int decorationNumberScore = decorationScore(e1.totalDecor, e2.totalDecor);

		return compare(decorationLevelScore, decorationNumberScore);
	}

	private static int isBetterCombinedDecorationSlot(Armor e1, Armor e2) {
		int e1Score = e1.combinedDecor4 * 1000 + e1.combinedDecor3 * 100 + e1.combinedDecor2 * 10 + e1.combinedDecor1;
		int e2Score = e2.combinedDecor4 * 1000 + e2.combinedDecor3 * 100 + e2.combinedDecor2 * 10 + e2.combinedDecor1;

		int decorationLevelScore = decorationScore(e1Score, e2Score);
		int decorationNumberScore = decorationScore(e1.totalCombinedDecor, e2.totalCombinedDecor);

		return compare(decorationLevelScore, decorationNumberScore);
	}

	private static int decorationScore(int e1Score, int e2Score) {
		if (e1Score > e2Score)
			return BETTER;
		else if (e1Score == e2Score)
			return SAME;
		else if (e1Score < e2Score)
			return WORSE;

		return MAYBE;
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
			case 1: // {1,1,0} , {1,1,0}
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

//	void setCombinedDecoration(ItemSkillList includedSkill) {
//		for (Integer skillIndex : this.skills.keySet()) {
//			if (includedSkill.containsSkillIndex(skillIndex)) {
//				int skillLevel = skills.getSkillLevel(skillIndex);
//				switch (includedSkill.) {
//					case 1:
//						combinedDecor1 += skillLevel;
//						break;
//					case 2:
//						combinedDecor2 += skillLevel;
//						break;
//					case 3:
//						combinedDecor3 += skillLevel;
//						break;
//					case 4:
//						combinedDecor4 += skillLevel;
//						break;
//					default:
//						break;
//				}
//			}
//		}
//
//		totalCombinedDecor = combinedDecor3 + combinedDecor2 + combinedDecor1;
//	}

	boolean containsSetBonus(SetBonusList setBonusList) {
		return setBonusList.contains(setBonus);
	}

	int isBetter(final Armor anotherArmor, final ItemSkillList includedSkill, final SkillHashMap skillHashMap) {
		// Compare the valid skills (skills required to optimize the equipment) of with another armor
		int BETTERHERE = BETTER;
		int SAMEHERE = SAME;
		int WORSEHERE = WORSE;

		boolean thisIsIrreplaceable = false;
		boolean anotherIsIrreplaceable = false;
		for (Integer skillIndex : this.skills.keySet())
			if (includedSkill.containsSkillIndex(skillIndex) && !skillHashMap.get(skillIndex).isReplaceable)
				thisIsIrreplaceable = true;
		for (Integer skillIndex : anotherArmor.skills.keySet())
			if (includedSkill.containsSkillIndex(skillIndex) && !skillHashMap.get(skillIndex).isReplaceable)
				anotherIsIrreplaceable = true;

		if (thisIsIrreplaceable && anotherIsIrreplaceable)
			return MAYBE;
		if (thisIsIrreplaceable && !anotherIsIrreplaceable) {
			WORSEHERE = MAYBE;
			SAMEHERE = MAYBE;
		}
		if (!thisIsIrreplaceable && anotherIsIrreplaceable) {
			BETTERHERE = MAYBE;
			SAMEHERE = MAYBE;
		}

		int[] score = {MAYBE, MAYBE}; // 防禦, 裝飾珠數量
		// 比較防禦力
		int isBetterDefense = MAYBE;
		if (this.defense > anotherArmor.defense)
			isBetterDefense = BETTER;
		else if (this.defense == anotherArmor.defense)
			isBetterDefense = SAME;
		else if (this.defense < anotherArmor.defense)
			isBetterDefense = WORSE;

		score[0] = isBetterDefense;
		score[1] = isBetterDecorationSlot(this, anotherArmor);

		int[] scoreCount = {0, 0, 0, 0};
		for (int i = 0; i < score.length; i++)
			scoreCount[score[i]]++;

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
	}

	int isBetterWithNoSkill(final Armor anotherArmor) {
		int[] score = {MAYBE, MAYBE}; // 防禦, 裝飾珠數量
		// 比較防禦力
		int isBetterDefense = MAYBE;
		if (this.defense > anotherArmor.defense)
			isBetterDefense = BETTER;
		else if (this.defense == anotherArmor.defense)
			isBetterDefense = SAME;
		else if (this.defense < anotherArmor.defense)
			isBetterDefense = WORSE;

		score[0] = isBetterDefense;
		score[1] = isBetterDecorationSlot(this, anotherArmor);

		int[] scoreCount = {0, 0, 0, 0};
		for (int i = 0; i < score.length; i++)
			scoreCount[score[i]]++;

		if (scoreCount[MAYBE] >= 1) {
			return MAYBE;
		}
		if (scoreCount[SAME] == score.length) {
			return SAME;
		}
		if (scoreCount[BETTER] >= 1) {
			if (scoreCount[WORSE] >= 1) {
				return MAYBE;
			} else {
				return BETTER;
			}
		} else {
			return WORSE;
		}
	}

	public String toString() {
		return this.name;
	}
}
