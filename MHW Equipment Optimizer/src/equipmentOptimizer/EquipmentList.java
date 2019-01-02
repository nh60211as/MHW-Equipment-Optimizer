package equipmentOptimizer;

import java.util.ArrayList;
import java.util.List;

public class EquipmentList {
	Weapon weapon;
	List<Armor> armors;

	int defense;

	int decor3; // 這項會在建構式出現
	int decor2; // 這項會在建構式出現
	int decor1; // 這項會在建構式出現

	int[] elementalResistance;

	SetBonusList setBonusList; // 這項會在建構式出現
	EquipmentSkillList equipmentSkillList;

	public EquipmentList(Weapon weapon, Armor head, Armor body, Armor hands, Armor belt, Armor feet, Armor charm) {
		this.weapon = weapon;

		armors = new ArrayList<Armor>();
		armors.add(head);
		armors.add(body);
		armors.add(hands);
		armors.add(belt);
		armors.add(feet);
		armors.add(charm);

		decor3 = this.weapon.decor3;
		decor2 = this.weapon.decor2;
		decor1 = this.weapon.decor1;
		setBonusList = new SetBonusList();
		for(Armor currentArmor:armors){
			decor3 += currentArmor.decor3;
			decor2 += currentArmor.decor2;
			decor1 += currentArmor.decor1;
			setBonusList.plus1(currentArmor.setBonus);
		}
	}
	
	public void setAdditionalInformation() {
		defense = this.weapon.defense + 1;
		elementalResistance = new int[5];
		for(int i=0;i<=elementalResistance.length-1;i++)
			elementalResistance[i] = 0;
		setBonusList = new SetBonusList();
		equipmentSkillList = new EquipmentSkillList();

		for(Armor currentArmor:armors){
			defense += currentArmor.defense;
			
			for(int i=0;i<=elementalResistance.length-1;i++)
				elementalResistance[i] += currentArmor.elementalResistance[i];
			
			equipmentSkillList.plus(currentArmor.skillList);
		}
		updateDefenseAndElementalResistance();
	}
	
	public void setEquipmentSkillList(EquipmentSkillList equipmentSkillList) {
		this.equipmentSkillList = equipmentSkillList;
		updateDefenseAndElementalResistance();
	}

	private void updateDefenseAndElementalResistance() {
		String skillName = "防禦";
		if(equipmentSkillList.contains(skillName)) {
			int defBonus = equipmentSkillList.getSkillLevel(skillName);
			defense += defBonus*5;
			if(defBonus>=4) {
				for(int i=0;i<=elementalResistance.length-1;i++)
					elementalResistance[i] += 3;
			}
		}

		String elementalDefPrefix[] = {"火","水","雷","冰","龍"};
		for(int i=0;i<=elementalDefPrefix.length-1;i++) {
			skillName = elementalDefPrefix[i]+"耐性";
			if(equipmentSkillList.contains(skillName)) {
				int elementaldefBonus = equipmentSkillList.getSkillLevel(skillName);
				elementalResistance[i] += elementaldefBonus*6;
				if(elementaldefBonus==3){
					elementalResistance[i] += 2;
					defense += 10;
				}
			}
		}
	}
	
	public String toString() {
		String output = "";
		output += weapon.equipmentName + ",";
		for(int currentArmorIndex=0;currentArmorIndex<=armors.size()-2;currentArmorIndex++)
			output += armors.get(currentArmorIndex).equipmentName + ",";
		output += armors.get(armors.size()-1).equipmentName;
		return output;
	}
	
}
