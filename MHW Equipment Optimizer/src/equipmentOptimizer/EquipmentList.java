package equipmentOptimizer;

import java.util.ArrayList;

// TODO
// This part need a code clean up
class EquipmentList {
	private final Weapon weapon;
	final SetBonusList setBonusList; // 這項會在建構式出現
	private final ArrayList<Armor> armors;
	private final Charm charm;
	ItemSkillList equipmentSkillList;
	int defense;
	int[] decorSlots = new int[5];
	int[] combinedDecorSlots = new int[5];
	int totalCombinedDecor;
	int[] elementalResistance;

	EquipmentList(Weapon weapon, Armor armorSet, Charm charm) {
		this.weapon = weapon;

		armors = new ArrayList<>();
		armors.add(armorSet);

		this.charm = charm;

		setBonusList = new SetBonusList();
		for (Armor currentArmor : armors) {
			setBonusList.plus1(currentArmor.setBonus);
		}
	}

	EquipmentList(Weapon weapon, Armor head, Armor body, Armor hands, Armor belt, Armor feet, Charm charm) {
		this.weapon = weapon;

		armors = new ArrayList<>();
		armors.add(head);
		armors.add(body);
		armors.add(hands);
		armors.add(belt);
		armors.add(feet);

		this.charm = charm;

		setBonusList = new SetBonusList();
		for (Armor currentArmor : armors) {
			setBonusList.plus1(currentArmor.setBonus);
		}
	}

	int[] getDecorationSlot() {
		decorSlots[0] = 0;

		decorSlots[1] = this.weapon.decor1;
		decorSlots[2] = this.weapon.decor2;
		decorSlots[3] = this.weapon.decor3;
		decorSlots[4] = this.weapon.decor4;

		for (Armor currentArmor : armors) {
			decorSlots[1] += currentArmor.decor1;
			decorSlots[2] += currentArmor.decor2;
			decorSlots[3] += currentArmor.decor3;
			decorSlots[4] += currentArmor.decor4;
		}

		decorSlots[1] += charm.decor1;
		decorSlots[2] += charm.decor2;
		decorSlots[3] += charm.decor3;
		decorSlots[4] += charm.decor4;

		return decorSlots;
	}

	ItemSkillList getEquipmentSkillList() {
		equipmentSkillList = new ItemSkillList();

		equipmentSkillList.addSkill(weapon.skills);
		for (Armor currentArmor : armors)
			equipmentSkillList.addSkill(currentArmor.skills);
		equipmentSkillList.addSkill(charm.skills);

		return equipmentSkillList;
	}

	void setAdditionalInformation() {
//		equipmentSkillList.plus(weapon.skillList);
//		for (Armor currentArmor : armors)
//			equipmentSkillList.plus(currentArmor.skillList);
//		equipmentSkillList.plus(charm.skillList);
		updateDefenseAndElementalResistance();
	}

	void setEquipmentSkillList(ItemSkillList equipmentSkillList) {
		this.equipmentSkillList = new ItemSkillList(equipmentSkillList);
		updateDefenseAndElementalResistance();
	}

	private void updateDefenseAndElementalResistance() {
		defense = this.weapon.defense + 1;
		elementalResistance = new int[5];

		for (int i = 0; i <= elementalResistance.length - 1; i++)
			elementalResistance[i] = 0;

		for (Armor currentArmor : armors) {
			defense += currentArmor.defense;

			for (int i = 0; i <= elementalResistance.length - 1; i++)
				elementalResistance[i] += currentArmor.elementalResistance[i];
		}
		defense += charm.defense;
		for (int i = 0; i <= elementalResistance.length - 1; i++)
			elementalResistance[i] += charm.elementalResistance[i];
//
//		String skillName = "防禦";
//		if (equipmentSkillList.contains(skillName)) {
//			int defBonus = equipmentSkillList.getSkillLevel(skillName);
//			defense += defBonus * 5;
//			if (defBonus >= 4) {
//				for (int i = 0; i <= elementalResistance.length - 1; i++)
//					elementalResistance[i] += 3;
//			}
//		}
//
//		String[] elementalDefPrefix = {"火", "水", "雷", "冰", "龍"};
//		for (int i = 0; i <= elementalDefPrefix.length - 1; i++) {
//			skillName = elementalDefPrefix[i] + "耐性";
//			if (equipmentSkillList.contains(skillName)) {
//				int elementalDefBonus = equipmentSkillList.getSkillLevel(skillName);
//				elementalResistance[i] += elementalDefBonus * 6;
//				if (elementalDefBonus == 3) {
//					elementalResistance[i] += 2;
//					defense += 10;
//				}
//			}
//		}
	}

	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append(weapon.name);
		output.append(",");
		for (int currentArmorIndex = 0; currentArmorIndex <= armors.size() - 1; currentArmorIndex++) {
			output.append(armors.get(currentArmorIndex).name);
			output.append(",");
		}
		output.append(charm.name);
		return output.toString();
	}

}
