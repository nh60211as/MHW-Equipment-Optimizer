package equipmentOptimizer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

class EquipmentOptimizer {
	// 檔案路徑
	private static final String equipmentFileDirectory = "裝備檔案/";
	private static final String decorationFileName = "_擁有裝飾珠.txt";
	private static final String[] weaponFileNames = {"_武器.txt"};
	private static final String[] armorFileNames = {"_頭.txt", "_身.txt", "_腕.txt", "_腰.txt", "_腳.txt", "_護石.txt"};

	// 基本資料
	private final SkillList decorationList;
	private final WeaponList weaponList;
	private final ArmorList armorList;

	// 需求技能的資料
	private SetBonusList setBonusList;
	private SkillList includedSkill;
	private SkillList excludedSkill;
	// 包含裝備的資料
	private WeaponList includedWeaponList;
	private ArmorList includedArmorList;
	private WeaponList excludedWeaponList; // TODO

	private String requirementFileName;
	private final JTextArea textArea;
	private final JLabel eventLabel;

	EquipmentOptimizer(JTextArea textArea, JLabel eventLabel) {
		// 初始化函數
		this.textArea = textArea;
		this.eventLabel = eventLabel;

		// 所有裝飾珠的資料
		decorationList = ReadFile.readDecorationFile(equipmentFileDirectory, decorationFileName, eventLabel);
		// 所有武器的資料
		weaponList = ReadFile.readWeaponFile(equipmentFileDirectory, weaponFileNames, eventLabel);
		// 所有裝備的資料
		armorList = ReadFile.readArmorFile(equipmentFileDirectory, armorFileNames, eventLabel);

		// 結束初始化函數
	}

	void readRequirement(String requirementFileName) throws CloneNotSupportedException {
		this.requirementFileName = requirementFileName;

		setBonusList = new SetBonusList();
		includedSkill = new SkillList();
		excludedSkill = new SkillList();

		includedWeaponList = new WeaponList();
		includedArmorList = new ArmorList();

		// 讀取技能、裝備需求檔案
		ReadFile.readRequirementFile(requirementFileName, textArea, eventLabel,
				decorationList, weaponList, armorList,
				setBonusList, includedSkill, excludedSkill,
				includedWeaponList, includedArmorList);
	}

	void generateIncludedEquipmentList() {
		if (includedWeaponList.isEmptyWeaponList()) {
			PrintMessage.warning(textArea, "未指定武器");
			includedWeaponList.add(WeaponList.GREATSWORD, new Weapon());
		}

		//System.out.println("搜索合適裝備中...");
		for (int currentBodyPart = ArmorList.HEAD; currentBodyPart <= ArmorList.CHARM; currentBodyPart++) {
			List<Armor> currentIncludedArmorList = includedArmorList.get(currentBodyPart);
			if (currentIncludedArmorList.size() == 0) {
				if (currentBodyPart == ArmorList.CHARM) { //若裝備為護石
					for (Armor charmNow : armorList.get(currentBodyPart)) {
						for (Skill currentIncludedSkill : includedSkill) {
							if (charmNow.skillList.contains(currentIncludedSkill.skillName)) {
								currentIncludedArmorList.add(charmNow);
								break;
							}
						}
					}
				} else {
					for (Armor currentAddedArmor : armorList.get(currentBodyPart)) {
						//includedArmorList.printBodyPart(currentBodyPart);
						if (currentAddedArmor.skillList.contains(excludedSkill))
							continue;

						currentAddedArmor.setCombinedDecoration(includedSkill);

						if (currentAddedArmor.containsSetBonus(setBonusList) || currentIncludedArmorList.size() == 0) {
							currentIncludedArmorList.add(currentAddedArmor);
							continue;
						}

						List<Integer> marked2remove = new ArrayList<>();
						boolean marked2add = false;
						for (int includedArmorIndex = 0; includedArmorIndex <= currentIncludedArmorList.size() - 1; includedArmorIndex++) {
							Armor armorNow = currentIncludedArmorList.get(includedArmorIndex);

							if (armorNow.containsSetBonus(setBonusList)) {
								continue;
							}

							int isCurrentArmorBetter = armorNow.isBetter(currentAddedArmor, includedSkill);
//							System.out.println(armorNow.equipmentName + " " +
//									isCurrentArmorBetter + " " +
//									currentAddedArmor.equipmentName);
							if (isCurrentArmorBetter == Armor.BETTER) {
								break;
							} else if (isCurrentArmorBetter == Armor.SAME) {
								marked2add = true;
							} else if (isCurrentArmorBetter == Armor.WORSE) {
								marked2remove.add(includedArmorIndex);
								marked2add = true;
							} else if (isCurrentArmorBetter == Armor.MAYBE) {
								marked2add = true;
							}
						}
						//includedEquipmentList.printBodyPart(bodyPartNow);
						//System.out.print("remove: ");
						//System.out.println(marked2remove);
						for (int i = marked2remove.size() - 1; i >= 0; i--) {
							currentIncludedArmorList.remove((int) marked2remove.get(i));
						}
						if (marked2add) {
							//System.out.println("add: " + currentEquipment.equipmentName);
							currentIncludedArmorList.add(currentAddedArmor);
						}
						//includedArmorList.printBodyPart(currentBodyPart);
						//System.out.println();
					}
				}
			}
		}
		//System.out.println("搜索完成");
	}

	void findAndPrintMatchingEquipmentList() {
		PrintMessage.print(textArea, "符合 " + requirementFileName + " 條件的裝備：\n\n");

		int weaponSize = includedWeaponList.totalSize();
		int armorSize = includedArmorList.iterationSize();

		double lastPrint = 0;
		int searchCount = 0;
		for (List<Weapon> weaponType : includedWeaponList)
			for (Weapon weapon : weaponType)
				for (Armor head : includedArmorList.get(ArmorList.HEAD)) {
					double currentPrint = searchCount * 100.0 / (weaponSize * armorSize);
					if (lastPrint <= currentPrint - 10) {
						lastPrint = currentPrint;
						PrintMessage.updateEventLabel(eventLabel, String.format("已搜尋:%.2f%%\n\n", currentPrint));
						//System.out.format("已搜尋:%.2f%%\n", currentPrint);
						//System.out.println();
					}
					for (Armor body : includedArmorList.get(ArmorList.BODY))
						for (Armor hands : includedArmorList.get(ArmorList.HANDS))
							for (Armor belt : includedArmorList.get(ArmorList.BELT))
								for (Armor feet : includedArmorList.get(ArmorList.FEET))
									for (Armor charm : includedArmorList.get(ArmorList.CHARM)) {
										searchCount++;

//										EquipmentList currentEquipmentList =
//												new EquipmentList(weapon,
//														includedArmorList.get(ArmorList.HEAD).get(1),
//														includedArmorList.get(ArmorList.BODY).get(8),
//														includedArmorList.get(ArmorList.HANDS).get(0),
//														includedArmorList.get(ArmorList.BELT).get(7),
//														includedArmorList.get(ArmorList.FEET).get(5),
//														includedArmorList.get(ArmorList.CHARM).get(5));

										EquipmentList currentEquipmentList =
												new EquipmentList(weapon, head, body, hands, belt, feet, charm);

										//檢查是否為套裝
										if (!setBonusList.checkSetBonus(currentEquipmentList.setBonusList))
											continue;

										int[] numberOfHoleHave = new int[4];
										int[] numberOfHoleNeed = new int[4];
										int[] skillHave = new int[includedSkill.size()]; // 目前裝備擁有的技能
										int[] skillNeed = new int[includedSkill.size()]; // 目前裝備需要的技能
										for (int i = 0; i <= skillNeed.length - 1; i++)
											skillNeed[i] = includedSkill.get(i).required;

										// 計算目前裝備的鑲嵌槽數量
										currentEquipmentList.setDecorationSlot();
										numberOfHoleHave[3] = currentEquipmentList.decor3;
										numberOfHoleHave[2] = currentEquipmentList.decor2;
										numberOfHoleHave[1] = currentEquipmentList.decor1;
										numberOfHoleHave[0] = 0;

										// 計算目前裝備的技能列表
										for (Armor currentArmor : currentEquipmentList.armors) {
											List<String> currentArmorSkillList = currentArmor.skillList.skillName();
											for (int currentArmorSkillIndex = 0; currentArmorSkillIndex <= currentArmor.skillList.size() - 1; currentArmorSkillIndex++) {
												int indexOfIncludedSkill = includedSkill.indexOf(currentArmorSkillList.get(currentArmorSkillIndex));
												if (indexOfIncludedSkill != -1)
													skillHave[indexOfIncludedSkill] += currentArmor.skillList.getSkillLevel(currentArmorSkillIndex);
											}
										}

										// 計算仍然需要的技能數量
										boolean success = true;
										for (int i = 0; i <= skillNeed.length - 1; i++) {
											int decorationNeed = skillNeed[i] - skillHave[i];
											if (decorationNeed >= 1) {
												if (decorationNeed > includedSkill.get(i).owned) {
													success = false;
													break;
												}
												numberOfHoleNeed[includedSkill.get(i).levelOfDecor] += decorationNeed;
											}
										}
										if (!success)
											continue;

										boolean finished = true;
										for (int i = 0; i <= numberOfHoleHave.length - 1; i++) {
											numberOfHoleHave[i] -= numberOfHoleNeed[i];
											if (numberOfHoleHave[i] < 0) {
												finished = false;
											}
										}

										while (!finished) {
											finished = true;
											if (numberOfHoleHave[numberOfHoleHave.length - 1] < 0) {
												success = false;
												break;
											}

											for (int i = numberOfHoleHave.length - 2; i >= 1; i--) {
												if (numberOfHoleHave[i] < 0) {
													numberOfHoleHave[i + 1] += numberOfHoleHave[i];
													numberOfHoleHave[i] += -numberOfHoleHave[i];
												}
											}

											for (int i = 0; i <= numberOfHoleHave.length - 1; i++) {
												if (numberOfHoleHave[i] < 0) {
													finished = false;
												}
											}
										}

										if (!success)
											continue;

										printEquipment(currentEquipmentList, numberOfHoleHave);
									}
				}
		PrintMessage.updateEventLabel(eventLabel, String.format("已搜尋:%.2f%%\n\n", 100.0));
	}

	private void printEquipment(EquipmentList currentEquipmentList, int[] remainDecorSlot) {
		currentEquipmentList.setAdditionalInformation();
		// 印出裝備名稱
		PrintMessage.print(textArea, currentEquipmentList.toString() + "\n");

		// 印出裝備技能名稱和等級
		EquipmentSkillList skillListWithDecoration = currentEquipmentList.equipmentSkillList;
		for (Skill currentIncludedSkill : includedSkill) {
			skillListWithDecoration.set(currentIncludedSkill.skillName,
					Math.max(currentIncludedSkill.required, skillListWithDecoration.getSkillLevel(currentIncludedSkill.skillName)));
		}
		List<String> skillNameList = skillListWithDecoration.skillName();
		for (int i = 0; i <= skillListWithDecoration.size() - 1; i++) {
			String skillNow = skillNameList.get(i);
			int indexOfDecorationList = decorationList.indexOf(skillNow);
			skillListWithDecoration.setSkillLevel(i,
					Math.min(skillListWithDecoration.getSkillLevel(i), decorationList.get(indexOfDecorationList).max));
		}
		currentEquipmentList.setEquipmentSkillList(skillListWithDecoration);
		PrintMessage.print(textArea, skillListWithDecoration.toAdditionalString(decorationList) + "\n");

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(String.format("防禦力： %d,", currentEquipmentList.defense));

		stringBuilder.append("屬性抗性：");
		for (int i = 0; i <= currentEquipmentList.elementalResistance.length - 1; i++) {
			stringBuilder.append(String.format("%+3d,", currentEquipmentList.elementalResistance[i]));
		}

		stringBuilder.append(" 剩餘鑲嵌槽：");
		for (int i = 1; i <= remainDecorSlot.length - 2; i++) {
			stringBuilder.append(String.format("%2d,", remainDecorSlot[i]));
		}
		stringBuilder.append(String.format("%2d", remainDecorSlot[remainDecorSlot.length - 1]));

		stringBuilder.append("\n\n");
		PrintMessage.print(textArea, stringBuilder.toString());
	}
}
