package equipmentOptimizer;

import java.util.List;
import java.util.ArrayList;

public class EquipmentOptimizer {
	static DecorationList decorationList;
	// 需求技能的資料
	static SetBonusList setBonusList;
	static DecorationList includedSkill;
	static DecorationList excludedSkill;
	// 包含裝備的資料
	static EquipmentList includedEquipmentList;
	static EquipmentList excludedEquipmentList; // 尚未實做

	public static void main(String[] args) {

		// 初始化函數

		// 讀取裝備資訊和技能資訊
		String equipmentFileDirectory = "裝備檔案/";
		String decorationFileName = "_擁有裝飾珠.txt";
		String[] equipmentFileName = {"_武器.txt", "_頭.txt", "_身.txt", "_腕.txt", "_腰.txt", "_腳.txt", "_護石.txt"};

		// 所有裝飾珠的資料
		decorationList = ReadFile.readDecorationFile(equipmentFileDirectory,decorationFileName);
		// 所有裝備的資料
		EquipmentList equipmentList = ReadFile.readEquipmentFile(equipmentFileDirectory,equipmentFileName);

		setBonusList = new SetBonusList();
		includedSkill = new DecorationList();
		excludedSkill = new DecorationList();

		includedEquipmentList = new EquipmentList();

		// 結束初始化函數

		// 讀取技能需求檔案
		String requirementFileName = args[0];
		ReadFile.readRequirementFile(requirementFileName,
				decorationList,equipmentList,
				setBonusList,includedSkill,excludedSkill,
				includedEquipmentList);

		for(int bodyPartNow=0;bodyPartNow<=includedEquipmentList.size()-1;bodyPartNow++) {
			if(includedEquipmentList.get(bodyPartNow).size()==0) {
				if(bodyPartNow==EquipmentList.WEAPON) { //若裝備為武器
					System.out.println("未指定武器，結束程式");
					return;
				}
				else if(bodyPartNow==EquipmentList.CHARM) { //若裝備為護石
					for(Equipment equipmentNow:equipmentList.get(bodyPartNow)) {
						for(Decoration skillNow:includedSkill) {
							if(equipmentNow.skillList.contains(skillNow.skillName)) {
								includedEquipmentList.add(bodyPartNow, equipmentNow);
							}
						}
					}
				}
				else { //若為頭、身、腕、腰、腳
					for(int equipmentNow=0;equipmentNow<=equipmentList.get(bodyPartNow).size()-1;equipmentNow++) {
						Equipment currentEquipment = equipmentList.get(bodyPartNow).get(equipmentNow);
						currentEquipment.setIsReplaceable(includedSkill,setBonusList);
						if(currentEquipment.skillList.contains(excludedSkill))
							continue;
						if(!currentEquipment.isReplaceable() || includedEquipmentList.get(bodyPartNow).size()==0)
							includedEquipmentList.get(bodyPartNow).add(currentEquipment);
						else {
							List<Integer> marked2remove = new ArrayList<Integer>();
							boolean marked2add = false;
							for(int includedEquipmentNow=0;includedEquipmentNow<=includedEquipmentList.get(bodyPartNow).size()-1;includedEquipmentNow++) {
								if(includedEquipmentList.get(bodyPartNow).get(includedEquipmentNow).isReplaceable()) {
									int isEquipmentNowBetter = includedEquipmentList.get(bodyPartNow).get(includedEquipmentNow).isBetter(equipmentList.get(bodyPartNow).get(equipmentNow));
									switch(isEquipmentNowBetter) {
									case 1:
										break;
									case 0:
										marked2add = true;
										break;
									case -1:
										marked2remove.add(includedEquipmentNow);
										marked2add = true;
										break;
									default:
										break;
									}
								}
							}
							for(int i=marked2remove.size()-1;i>=0;i--)
								includedEquipmentList.get(bodyPartNow).remove((int)marked2remove.get(i));
							if(marked2add)
								includedEquipmentList.get(bodyPartNow).add(equipmentList.get(bodyPartNow).get(equipmentNow));
						}
					}
				}
			}
			else
				continue;
		}
		//		System.out.println(requirements.size());
		//		for(int i=0;i<=equipmentLists.size()-1;i++)
		//			System.out.println(equipmentLists.get(i).size());

		System.out.println("符合條件的裝備：");
		//開始配對裝備
		findAndPrintMatchingEquipmentList();
	}

	private static void findAndPrintMatchingEquipmentList() {
		for(Equipment e1:includedEquipmentList.get(0))
			for(Equipment e2:includedEquipmentList.get(1))
				for(Equipment e3:includedEquipmentList.get(2))
					for(Equipment e4:includedEquipmentList.get(3))
						for(Equipment e5:includedEquipmentList.get(4))
							for(Equipment e6:includedEquipmentList.get(5))
								for(Equipment e7:includedEquipmentList.get(6)){
									List<Equipment> currentEquipment = new ArrayList<Equipment>();
									//									currentEquipment.add(0, includedEquipmentList.get(0).get(0));
									//									currentEquipment.add(1, includedEquipmentList.get(1).get(5));
									//									currentEquipment.add(2, includedEquipmentList.get(2).get(1));
									//									currentEquipment.add(3, includedEquipmentList.get(3).get(8));
									//									currentEquipment.add(4, includedEquipmentList.get(4).get(7));
									//									currentEquipment.add(5, includedEquipmentList.get(5).get(0));
									//									currentEquipment.add(6, includedEquipmentList.get(6).get(7));

									currentEquipment.add(0, e1);
									currentEquipment.add(1, e2);
									currentEquipment.add(2, e3);
									currentEquipment.add(3, e4);
									currentEquipment.add(4, e5);
									currentEquipment.add(5, e6);
									currentEquipment.add(6, e7);

									int defense = 1;
									int elementalDef[] = new int[5];
									SetBonusList currentSetBonusList = new SetBonusList();
									int numberOfHole[] = new int[4];
									int skillHave[] = new int[includedSkill.size()];
									int skillNeed[] = new int[includedSkill.size()];
									for(int i=0;i<=skillNeed.length-1;i++)
										skillNeed[i] = includedSkill.get(i).required;

									for(int i=0;i<=currentEquipment.size()-1;i++){
										currentSetBonusList.plus1(currentEquipment.get(i).setBonus);
									}
									//檢查是否為套裝
									if(!setBonusList.checkSetBonus(currentSetBonusList))
										continue;

									for(int i=0;i<=currentEquipment.size()-1;i++){
										defense += currentEquipment.get(i).defense;
										elementalDef[0] += currentEquipment.get(i).fireDef;
										elementalDef[1] += currentEquipment.get(i).waterDef;
										elementalDef[2] += currentEquipment.get(i).thunderDef;
										elementalDef[3] += currentEquipment.get(i).iceDef;
										elementalDef[4] += currentEquipment.get(i).dragonDef;

										numberOfHole[3] += currentEquipment.get(i).decor3;
										numberOfHole[2] += currentEquipment.get(i).decor2;
										numberOfHole[1] += currentEquipment.get(i).decor1;

										SkillList skill = currentEquipment.get(i).skillList;
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
									printEquipment(currentEquipment, defense, elementalDef, numberOfHole[0]);
								}
	}

	private static void printEquipment(List<Equipment> currentEquipment, int defense, int elementalDef[], int remainDecroSlot) {
		for(int i=0;i<=currentEquipment.size()-2;i++)
			System.out.print(currentEquipment.get(i).equipmentName + ", ");
		System.out.println(currentEquipment.get(currentEquipment.size()-1).equipmentName);

		SkillList skill = new SkillList();
		for(Equipment equipmentNow:currentEquipment) {
			for (String skillNameNow : equipmentNow.skillList.skillName()) {
				skill.plus(skillNameNow, equipmentNow.skillList.getSkillLevel(skillNameNow));
			}
		}
		for(Decoration skillRequirementNow:includedSkill) {
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
