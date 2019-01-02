package equipmentOptimizer;

import java.util.List;
import java.util.ArrayList;

public class EquipmentOptimizer {
	// 基本資料
	private SkillList decorationList;
	private WeaponList weaponList;
	private ArmorList armorList;

	// 需求技能的資料
	private SetBonusList setBonusList;
	private SkillList includedSkill;
	private SkillList excludedSkill;
	// 包含裝備的資料
	private WeaponList includedWeaponList;
	private ArmorList includedArmorList;
	private WeaponList excludedWeaponList; // 尚未實做

	public EquipmentOptimizer() {
		// 初始化函數

		// 讀取裝備資訊和技能資訊
		final String equipmentFileDirectory = "裝備檔案/";
		final String decorationFileName = "_擁有裝飾珠.txt";
		final String[] weaponFileNames = {"_武器.txt"};
		final String[] armorFileNames = {"_頭.txt", "_身.txt", "_腕.txt", "_腰.txt", "_腳.txt", "_護石.txt"};

		// 所有裝飾珠的資料
		decorationList = ReadFile.readDecorationFile(equipmentFileDirectory,decorationFileName);
		// 所有武器的資料
		weaponList = ReadFile.readWeaponFile(equipmentFileDirectory,weaponFileNames);
		// 所有裝備的資料
		armorList = ReadFile.readArmorFile(equipmentFileDirectory,armorFileNames);

		// 結束初始化函數
	}

	public boolean readAndFindMatchingEquipmentList(String requirementFileName) {
		setBonusList = new SetBonusList();
		includedSkill = new SkillList();
		excludedSkill = new SkillList();

		includedWeaponList = new WeaponList();
		includedArmorList = new ArmorList();

		// 讀取技能、裝備需求檔案
		ReadFile.readRequirementFile(requirementFileName,
				decorationList,weaponList,armorList,
				setBonusList,includedSkill,excludedSkill,
				includedWeaponList,includedArmorList);

		if(includedWeaponList.isEmptyWeaponList()) {
			PrintMessage.warning("未指定武器");
			includedWeaponList.add(WeaponList.GREATSWORD, new Weapon());
		}

		System.out.println("搜索合適裝備中...");
		for(int currentBodyPart=ArmorList.HEAD;currentBodyPart<=ArmorList.CHARM;currentBodyPart++) {
			List<Armor> currentInculdedArmorList = includedArmorList.get(currentBodyPart);
			if(currentInculdedArmorList.size()==0) {
				if(currentBodyPart==ArmorList.CHARM) { //若裝備為護石
					for(Armor charmNow:armorList.get(currentBodyPart)) {
						for(Skill currentIncludedSkill:includedSkill) {
							if(charmNow.skillList.contains(currentIncludedSkill.skillName)) {
								currentInculdedArmorList.add(charmNow);
							}
						}
					}
				}
				else {
					for(Armor currentAddedArmor:armorList.get(currentBodyPart)) {
						if(currentAddedArmor.skillList.contains(excludedSkill))
							continue;

						currentAddedArmor.setIsReplaceable(includedSkill,setBonusList);
						if(!currentAddedArmor.isReplaceable() || currentInculdedArmorList.size()==0)
							currentInculdedArmorList.add(currentAddedArmor);
						else {
							List<Integer> marked2remove = new ArrayList<Integer>();
							boolean marked2add = false;
							for(int includedArmorIndex=0;includedArmorIndex<=currentInculdedArmorList.size()-1;includedArmorIndex++) {
								if(currentInculdedArmorList.get(includedArmorIndex).isReplaceable()) {
									Armor armorNow = (Armor) currentInculdedArmorList.get(includedArmorIndex);
									int isEquipmentNowBetter = armorNow.isBetter(currentAddedArmor);
									//System.out.println(includedEquipmentList.get(bodyPartNow).get(includedEquipmentNow).equipmentName
									//		+" "+isEquipmentNowBetter+" "+currentEquipment.equipmentName);
									switch(isEquipmentNowBetter) {
									case 1:
										break;
									case 0:
										marked2add = true;
										break;
									case -1:
										marked2remove.add(includedArmorIndex);
										marked2add = true;
										break;
									default:
										break;
									}
								}
							}
							//includedEquipmentList.printBodyPart(bodyPartNow);
							//System.out.print("remove: ");
							//System.out.println(marked2remove);
							for(int i=marked2remove.size()-1;i>=0;i--) {
								currentInculdedArmorList.remove((int)marked2remove.get(i));
							}
							if(marked2add) {
								//System.out.println("add: " + currentEquipment.equipmentName);
								currentInculdedArmorList.add(currentAddedArmor);
							}
						}
					}
				}
			}
			else
				continue;
		}
		System.out.println("搜索完成");
		//		System.out.println(requirements.size());
		//		for(int i=0;i<=equipmentLists.size()-1;i++)
		//			System.out.println(equipmentLists.get(i).size());

		System.out.println("符合 " + requirementFileName + " 條件的裝備：");
		//開始配對裝備
		findAndPrintMatchingEquipmentList();
		return true;
	}

	private void findAndPrintMatchingEquipmentList() {
		for(List<Weapon> weaponType:includedWeaponList)
			for(Equipment e1:weaponType)
				for(Equipment e2:includedArmorList.get(0))
					for(Equipment e3:includedArmorList.get(1))
						for(Equipment e4:includedArmorList.get(2))
							for(Equipment e5:includedArmorList.get(3))
								for(Equipment e6:includedArmorList.get(4))
									for(Equipment e7:includedArmorList.get(5)){
										List<Equipment> currentEquipment = new ArrayList<Equipment>();
										//									currentEquipment.add(0, includedEquipmentList.get(0).get(0));
										//									currentEquipment.add(1, includedEquipmentList.get(1).get(5));
										//									currentEquipment.add(2, includedEquipmentList.get(2).get(1));
										//									currentEquipment.add(3, includedEquipmentList.get(3).get(8));
										//									currentEquipment.add(4, includedEquipmentList.get(4).get(7));
										//									currentEquipment.add(5, includedEquipmentList.get(5).get(0));
										//									currentEquipment.add(6, includedEquipmentList.get(6).get(7));

										currentEquipment.add(0, e1); // 武器
										currentEquipment.add(ArmorList.HEAD+1, e2);
										currentEquipment.add(ArmorList.BODY+1, e3);
										currentEquipment.add(ArmorList.HANDS+1, e4);
										currentEquipment.add(ArmorList.BELT+1, e5);
										currentEquipment.add(ArmorList.FEET+1, e6);
										currentEquipment.add(ArmorList.CHARM+1, e7);

										SetBonusList currentSetBonusList = new SetBonusList();
										int numberOfHole[] = new int[4];
										int skillHave[] = new int[includedSkill.size()];
										int skillNeed[] = new int[includedSkill.size()];
										for(int i=0;i<=skillNeed.length-1;i++)
											skillNeed[i] = includedSkill.get(i).required;

										for(int i=ArmorList.HEAD+1;i<=ArmorList.CHARM+1;i++){
											Armor currentBodyPart = (Armor) currentEquipment.get(i);
											currentSetBonusList.plus1(currentBodyPart.setBonus);
										}
										//檢查是否為套裝
										if(!setBonusList.checkSetBonus(currentSetBonusList))
											continue;

										for(int i=0;i<=currentEquipment.size()-1;i++){
											numberOfHole[3] += currentEquipment.get(i).decor3;
											numberOfHole[2] += currentEquipment.get(i).decor2;
											numberOfHole[1] += currentEquipment.get(i).decor1;

											EquipmentSkillList skill = currentEquipment.get(i).skillList;
											for(int j=0;j<=includedSkill.size()-1;j++){
												String currentSkillName = includedSkill.get(j).skillName;
												if(skill.contains(currentSkillName))
													skillHave[j] += skill.getSkillLevel(currentSkillName);
											}
										}

										boolean success = true;

										for(int i=0;i<=skillNeed.length-1;i++){
											int gemNeed = skillNeed[i]-skillHave[i];
											if(gemNeed>=1)
												if(gemNeed>includedSkill.get(i).owned) {
													success = false;
													break;
												}
										}

										int numberOfHoleNeed[] = new int[4];
										for(int i=0;i<=skillNeed.length-1;i++){
											int temp = skillNeed[i]-skillHave[i];
											if(temp>=1)
												numberOfHoleNeed[includedSkill.get(i).levelOfDecor] += temp;
										}

										for(int i=3;i>=1;i--){
											if(numberOfHole[i]<numberOfHoleNeed[i]){
												success = false;
												break;
											}
											numberOfHole[i-1] += numberOfHole[i]-numberOfHoleNeed[i];
										}

										if(!success)
											continue;

										int defense = 1;
										int elementalDef[] = new int[5];
										for(int i=0;i<=currentEquipment.size()-1;i++){
											Equipment equipmentNow = currentEquipment.get(i);

											defense += equipmentNow.defense;
											numberOfHole[3] += currentEquipment.get(i).decor3;
											numberOfHole[2] += currentEquipment.get(i).decor2;
											numberOfHole[1] += currentEquipment.get(i).decor1;

											if(equipmentNow.getClass().getName().contentEquals("Armor")) {
												Armor armorNow = (Armor) equipmentNow;
												elementalDef[0] += armorNow.fireDef;
												elementalDef[1] += armorNow.waterDef;
												elementalDef[2] += armorNow.thunderDef;
												elementalDef[3] += armorNow.iceDef;
												elementalDef[4] += armorNow.dragonDef;
											}
										}
										printEquipment(currentEquipment, defense, elementalDef, numberOfHole[0]);
									}
	}

	private void printEquipment(List<Equipment> currentEquipment, int defense, int elementalDef[], int remainDecroSlot) {
		for(int i=0;i<=currentEquipment.size()-2;i++)
			System.out.print(currentEquipment.get(i).equipmentName + ", ");
		System.out.println(currentEquipment.get(currentEquipment.size()-1).equipmentName);

		EquipmentSkillList skill = new EquipmentSkillList();
		for(Equipment equipmentNow:currentEquipment) {
			for (String skillNameNow : equipmentNow.skillList.skillName()) {
				skill.plus(skillNameNow, equipmentNow.skillList.getSkillLevel(skillNameNow));
			}
		}
		for(Skill skillRequirementNow:includedSkill) {
			skill.set(skillRequirementNow.skillName, Math.max(skillRequirementNow.required, skill.getSkillLevel(skillRequirementNow.skillName)));
		}

		List<String> skillNameList = skill.skillName();
		for(int i=0;i<=skill.size()-1;i++) {
			String skillNow = skillNameList.get(i);
			int indexOfDecorationList = decorationList.indexOf(skillNow);
			skill.setSkillLevel(i, Math.min(skill.getSkillLevel(i), decorationList.get(indexOfDecorationList).max));
		}
		skill.print();

		String skillName = "防禦";
		if(skill.contains(skillName)) {
			int defBonus = skill.getSkillLevel(skillName);
			defBonus = (defBonus>=8) ? 7 : defBonus; // 強制設定防禦技能至7以下
			defense += defBonus*5;
			if(defBonus>=4)
				for(int i=0;i<=elementalDef.length-1;i++)
					elementalDef[i] += 3;
		}

		String elementalDefPrefix[] = {"火","水","雷","冰","龍"};
		for(int i=0;i<=elementalDef.length-1;i++) {
			skillName = elementalDefPrefix[i]+"耐性";
			if(skill.contains(skillName)) {
				int elementaldefBonus = skill.getSkillLevel(skillName);
				elementaldefBonus = (elementaldefBonus>=4) ? 3 : elementaldefBonus; // 強制設定耐性技能至3以下
				elementalDef[i] += elementaldefBonus*6;
				if(elementaldefBonus==3){
					elementalDef[i] += 2;
					defense += 10;
				}
			}
		}

		System.out.print("防禦力： " + defense + ", ");
		System.out.print("屬性抗性：  ");
		for(int i=0;i<=elementalDef.length-1;i++) {
			if(elementalDef[i]>=0)
				System.out.print("+" + elementalDef[i] + ",");
			else
				System.out.print(elementalDef[i] + ",");
		}
		System.out.println(" 剩餘鑲嵌槽： " + remainDecroSlot);
	}
}
