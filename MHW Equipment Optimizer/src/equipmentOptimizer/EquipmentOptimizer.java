package equipmentOptimizer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

class EquipmentOptimizer {
	// 檔案路徑
	private static final String equipmentFileDirectory = "裝備檔案/";
	private static final String skillFileName = "_擁有裝飾珠.txt";
	private static final String[] weaponFileNames = {"_武器.txt"};
	private static final String[] armorFileNames = {"_頭.txt", "_身.txt", "_腕.txt", "_腰.txt", "_腳.txt"};
	private static final String[] armorSetsFileNames = {"_整套防具.txt"}; // TODO
	private static final String[] charmFileNames = {"_護石.txt"};
	private static final String jewelFileName = "_擁有裝飾珠.txt";

	// 基本資料
	private final SkillHashMap skillHashMap;
	private final WeaponList weaponList;
	private final ArmorList armorList;
	private final ArrayList<Armor> armorSetList;
	private final CharmList charmList;
	private final JewelList jewelList;

	// 需求技能的資料
	private SetBonusList setBonusList;
	private ItemSkillList includedSkill;
	private ItemSkillList excludedSkill;
	// 包含裝備的資料
	private WeaponList includedWeaponList;
	private ArmorList includedArmorList;
	private WeaponList excludedWeaponList; // TODO
	private List<Armor> includedArmorSetList; // TODO
	private CharmList includedCharmList;
	private JewelList includedJewelList;

	private String requirementFileName;
	private final JTextArea textArea;
	private final JLabel eventLabel;

	EquipmentOptimizer(JTextArea textArea, JLabel eventLabel) {
		// 初始化變數
		this.textArea = textArea;
		this.eventLabel = eventLabel;

		// 所有技能的資料
		skillHashMap = ReadFile.readSkillFile(equipmentFileDirectory, skillFileName, eventLabel);

		// 所有武器的資料
		weaponList = ReadFile.readWeaponFile(equipmentFileDirectory, weaponFileNames, skillHashMap, eventLabel);
		// 所有防具的資料
		armorList = ReadFile.readArmorFile(equipmentFileDirectory, armorFileNames, skillHashMap, eventLabel);
		// 所有整套防具的資料
		armorSetList = ReadFile.readArmorSetFile(equipmentFileDirectory, armorSetsFileNames, skillHashMap, eventLabel);
		includedArmorSetList = armorSetList;
		// 所有護石的資料
		charmList = ReadFile.readCharmFile(equipmentFileDirectory, charmFileNames, skillHashMap, eventLabel);
		// 所有裝飾珠的資料
		jewelList = ReadFile.readJewelFile(equipmentFileDirectory, jewelFileName, skillHashMap, eventLabel);
		// 結束初始化函數
	}

	void readRequirement(String requirementFileName) throws CloneNotSupportedException {
		this.requirementFileName = requirementFileName;

		setBonusList = new SetBonusList();
		includedSkill = new ItemSkillList();
		excludedSkill = new ItemSkillList();

		includedWeaponList = new WeaponList();
		includedArmorList = new ArmorList();
		includedCharmList = new CharmList();
		includedJewelList = new JewelList();

		// 讀取技能、裝備需求檔案
		ReadFile.readRequirementFile(requirementFileName, skillHashMap, weaponList, armorList, setBonusList, includedSkill, excludedSkill, includedWeaponList, includedArmorList, textArea, eventLabel);

		// 若未指定武器則加入空武器
		if (includedWeaponList.isEmptyWeaponList()) {
			PrintMessage.warning(textArea, "未指定武器");
			includedWeaponList.add(WeaponList.GREATSWORD, new Weapon());
		}

		//System.out.println("搜索合適裝備中...");
		// 加入要搜尋的防具
		for (int currentBodyPart = ArmorList.HEAD; currentBodyPart <= ArmorList.FEET; currentBodyPart++) {
			ArrayList<Armor> currentIncludedArmorList = includedArmorList.get(currentBodyPart);
			if (currentIncludedArmorList.size() == 0) {
				for (Armor currentArmor : armorList.get(currentBodyPart)) {
					//includedArmorList.printBodyPart(currentBodyPart);
					if (currentArmor.skills.containsSkill(excludedSkill))
						continue;
					if (currentArmor.containsSetBonus(setBonusList) || currentIncludedArmorList.size() == 0) {
						currentIncludedArmorList.add(currentArmor);
						continue;
					}

					ArrayList<Integer> markedToRemove = new ArrayList<>();
					boolean markedToAdd = false;
					for (int includedArmorIndex = 0; includedArmorIndex <= currentIncludedArmorList.size() - 1; includedArmorIndex++) {
						Armor armorNow = currentIncludedArmorList.get(includedArmorIndex);

						if (armorNow.containsSetBonus(setBonusList)) {
							continue;
						}

						int isCurrentArmorBetter = armorNow.isBetter(currentArmor, includedSkill, skillHashMap);
//						System.out.println(armorNow.name + " " +
////								isCurrentArmorBetter + " " +
////								currentArmor.name);
						if (isCurrentArmorBetter == Armor.BETTER) {
							break;
						} else if (isCurrentArmorBetter == Armor.SAME) {
							markedToAdd = true;
						} else if (isCurrentArmorBetter == Armor.WORSE) {
							markedToRemove.add(includedArmorIndex);
							markedToAdd = true;
						} else if (isCurrentArmorBetter == Armor.MAYBE) {
							markedToAdd = true;
						}
					}
					//includedEquipmentList.printBodyPart(bodyPartNow);
					//System.out.print("remove: ");
					//System.out.println(markedToRemove);
					for (int i = markedToRemove.size() - 1; i >= 0; i--) {
						currentIncludedArmorList.remove((int) markedToRemove.get(i));
					}
					if (markedToAdd) {
						//System.out.println("add: " + currentEquipment.equipmentName);
						currentIncludedArmorList.add(currentArmor);
					}
					//includedArmorList.printBodyPart(currentBodyPart);
					//System.out.println();
				}
			}
		}

		// 加入要搜尋的護石
		for (Charm currentCharm : charmList) {
			boolean toAdd = false;
			if (includedSkill.containsSkill(currentCharm.skills))
				toAdd = true;
			if (excludedSkill.containsSkill(currentCharm.skills))
				toAdd = false;
			if (toAdd)
				includedCharmList.add(currentCharm);
		}

		// 加入要搜尋的裝飾珠
		for (Jewel currentJewel : jewelList) {
			if (currentJewel.skills.containsSkill(excludedSkill))
				continue;
			if (currentJewel.owned == 0)
				continue;
			if (currentJewel.skills.containsSkill(includedSkill))
				includedJewelList.add(currentJewel);
		}
		//System.out.println("搜索完成");
	}

	void findAndPrintMatchingEquipmentList() {
		PrintMessage.print(textArea, "符合 " + requirementFileName + " 條件的裝備：\n\n");

		long weaponSize = includedWeaponList.totalSize();
		long armorSize = includedArmorList.iterationSize();
		long armorSetSize = includedArmorSetList.size();
		long charmSize = includedCharmList.size();
		long totalSize = weaponSize * armorSize * charmSize;
		//long totalSize = weaponSize * (armorSize + armorSetSize) * charmSize;

		double lastPrint = 0;
		int searchCount = 0;
		for (ArrayList<Weapon> weaponType : includedWeaponList)
			for (Weapon weapon : weaponType) {
				for (Charm charm : includedCharmList) {
					// 搜尋整套防具
//					for (Armor armorSet : includedArmorSetList) {
//						searchCount++;
//						double currentPrint = searchCount * 100.0 / (totalSize);
//						if (lastPrint <= currentPrint - 10) {
//							lastPrint = currentPrint;
//							PrintMessage.updateEventLabel(eventLabel, String.format("已搜尋:%.2f%%\n\n", currentPrint));
//							//System.out.format("已搜尋:%.2f%%\n", currentPrint);
//							//System.out.println();
//						}
//
//						EquipmentList currentEquipmentList = new EquipmentList(weapon, armorSet, charm);
//
//						//檢查是否為套裝
//						if (!setBonusList.checkSetBonus(currentEquipmentList.setBonusList))
//							continue;
//
//						int[] numberOfHoleHave = new int[4];
//						int[] numberOfHoleNeed = new int[4];
//						int[] skillHave = new int[includedSkill.size()]; // 目前裝備擁有的技能
//						int[] skillNeed = new int[includedSkill.size()]; // 目前裝備需要的技能
//						for (int i = 0; i <= skillNeed.length - 1; i++)
//							skillNeed[i] = includedSkill.get(i).required;
//
//						// 計算目前裝備的鑲嵌槽數量
//						currentEquipmentList.setDecorationSlot();
//						numberOfHoleHave[3] = currentEquipmentList.decor3;
//						numberOfHoleHave[2] = currentEquipmentList.decor2;
//						numberOfHoleHave[1] = currentEquipmentList.decor1;
//						numberOfHoleHave[0] = 0;
//
//						// 計算目前裝備的技能列表
//						for (Armor currentArmor : currentEquipmentList.armors) {
//							ArrayList<String> currentArmorSkillList = new ArrayList<>();
//							for (int currentArmorSkillIndex = 0; currentArmorSkillIndex <= currentArmor.skills.size() - 1; currentArmorSkillIndex++) {
//								int indexOfIncludedSkill = includedSkill.indexOf(currentArmorSkillList.get(currentArmorSkillIndex));
//								if (indexOfIncludedSkill != -1)
//									skillHave[indexOfIncludedSkill] += currentArmor.skills.size();
//							}
//						}
//						for (int currentCharmSkillIndex = 0; currentCharmSkillIndex <= charm.skills.size() - 1; currentCharmSkillIndex++) {
//							int indexOfIncludedSkill = includedSkill.indexOf(charm.skills.size());
//							if (indexOfIncludedSkill != -1)
//								skillHave[indexOfIncludedSkill] += charm.skills.size();
//						}
//
//						// 計算仍然需要的技能數量
//						boolean success = true;
//						for (int i = 0; i <= skillNeed.length - 1; i++) {
//							int decorationNeed = skillNeed[i] - skillHave[i];
//							if (decorationNeed >= 1) {
//								if (decorationNeed > includedSkill.get(i).owned) {
//									success = false;
//									break;
//								}
//								numberOfHoleNeed[includedSkill.get(i).levelOfDecor] += decorationNeed;
//							}
//						}
//						if (!success)
//							continue;
//
//						boolean finished = true;
//						for (int i = 0; i <= numberOfHoleHave.length - 1; i++) {
//							numberOfHoleHave[i] -= numberOfHoleNeed[i];
//							if (numberOfHoleHave[i] < 0) {
//								finished = false;
//							}
//						}
//
//						while (!finished) {
//							finished = true;
//							if (numberOfHoleHave[numberOfHoleHave.length - 1] < 0) {
//								success = false;
//								break;
//							}
//
//							for (int i = numberOfHoleHave.length - 2; i >= 1; i--) {
//								if (numberOfHoleHave[i] < 0) {
//									numberOfHoleHave[i + 1] += numberOfHoleHave[i];
//									numberOfHoleHave[i] += -numberOfHoleHave[i];
//								}
//							}
//
//							for (int i = 0; i <= numberOfHoleHave.length - 1; i++) {
//								if (numberOfHoleHave[i] < 0) {
//									finished = false;
//								}
//							}
//						}
//
//						if (!success)
//							continue;
//
//						printEquipment(currentEquipmentList, numberOfHoleHave);
//					}
					// 搜尋個別防具
					for (Armor head : includedArmorList.get(ArmorList.HEAD)) {
						double currentPrint = searchCount * 100.0 / (totalSize);
						if (lastPrint <= currentPrint - 10) {
							lastPrint = currentPrint;
							PrintMessage.updateEventLabel(eventLabel, String.format("已搜尋:%.2f%%\n\n", currentPrint));
						}
						for (Armor body : includedArmorList.get(ArmorList.BODY))
							for (Armor hands : includedArmorList.get(ArmorList.HANDS))
								for (Armor belt : includedArmorList.get(ArmorList.BELT))
									for (Armor feet : includedArmorList.get(ArmorList.FEET)) {
										searchCount++;

//										EquipmentList currentEquipmentList =
//												new EquipmentList(weapon,
//														includedArmorList.get(ArmorList.HEAD).get(14),
//														includedArmorList.get(ArmorList.BODY).get(14),
//														includedArmorList.get(ArmorList.HANDS).get(14),
//														includedArmorList.get(ArmorList.BELT).get(18),
//														includedArmorList.get(ArmorList.FEET).get(9),
//														includedCharmList.get(0));

										EquipmentList currentEquipmentList =
												new EquipmentList(weapon, head, body, hands, belt, feet, charm);

										//檢查是否為套裝
										if (!setBonusList.checkSetBonus(currentEquipmentList.setBonusList))
											continue;

										// 計算目前裝備的鑲嵌槽數量
										int[] numberOfSlotHave = currentEquipmentList.getDecorationSlot();
										int totalSlotHave = 0;
										int maxSkillPossible = 0;
										int maxSlotLevel = 0;
										for (int i = 0; i < numberOfSlotHave.length; i++) {
											if (numberOfSlotHave[i] > 0) {
												totalSlotHave += numberOfSlotHave[i];
												maxSkillPossible += numberOfSlotHave[i];
												maxSlotLevel = i;
											}
										}
										maxSkillPossible += numberOfSlotHave[4] * 2;

										// 目前需要的技能
										ItemSkillList skillRequired = new ItemSkillList(includedSkill);
										// 計算目前裝備的技能列表
										ItemSkillList skillHave = currentEquipmentList.getEquipmentSkillList();
										// 計算仍然需要的技能列表
										ItemSkillList skillNeed = ItemSkillList.removeSkill(skillRequired, skillHave);
										// 計算需要的技能等級數
										int maxSkillNeeded = 0;
										for (Integer skillLevel : skillNeed.keySet()) {
											maxSkillNeeded += skillNeed.getSkillLevel(skillLevel);
										}
										if (maxSkillNeeded > maxSkillPossible)
											continue;

										JewelList finalJewelList = new JewelList(includedJewelList);
										long totalIter = 1;
										for (int i = finalJewelList.size() - 1; i >= 0; i--) {
											if (!finalJewelList.get(i).skills.containsSkill(skillNeed))
												finalJewelList.remove(i);
											else
												totalIter *= (finalJewelList.get(i).owned + 1); // calculate the iteration needed for all the jewels
										}

										int[] jewelsUsedNow = new int[finalJewelList.size()];

										for (int iter = 0; iter < totalIter; iter++) {
											boolean skip = false;
											ItemSkillList skillNow = new ItemSkillList();
											int[] numberOfSlotNeed = new int[5];
											int totalSlotUsed = 0;
											int maxSlotLevelNow = 0;
											for (int i = 0; i < jewelsUsedNow.length; i++) {
												if (jewelsUsedNow[i] == 0)
													continue;
												skillNow.addSkill(finalJewelList.get(i).skills, jewelsUsedNow[i]);
												numberOfSlotNeed[finalJewelList.get(i).slotLevel] += jewelsUsedNow[i];
												totalSlotUsed += jewelsUsedNow[i];
												maxSlotLevelNow = Math.max(maxSlotLevelNow, finalJewelList.get(i).slotLevel);
											}
											// skip if the current set of jewels slot level is too high
											if (maxSlotLevelNow > maxSlotLevel)
												skip = true;
											// skip if the amount of jewels used are higher than the jewels available
											if (totalSlotUsed > totalSlotHave)
												skip = true;
											// skip if there are still skills unfulfilled
											ItemSkillList skillNeedNow = ItemSkillList.removeSkill(skillNeed, skillNow);
											if (!skillNeedNow.isEmpty())
												skip = true;
											//System.out.println(skip);
											boolean success = false;
											if (!skip) {
												boolean finished = true;
												int[] numberOfHoleHave = new int[numberOfSlotHave.length];
												for (int i = 0; i <= numberOfHoleHave.length - 1; i++) {
													numberOfHoleHave[i] = numberOfSlotHave[i] - numberOfSlotNeed[i];
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

													success = true;
												}
											}
											if (success) {
												printEquipment(currentEquipmentList, numberOfSlotHave);
												break;
											}

											// add one jewel every iteration and carry the overflowed jewel to the next one
											// just like basic math addition
											boolean carry = true;
											for (int i = 0; i < jewelsUsedNow.length; i++) {
												if (carry) {
													jewelsUsedNow[i]++;
													if (jewelsUsedNow[i] > finalJewelList.get(i).owned) {
														carry = true;
														jewelsUsedNow[i] = 0;
													} else {
														carry = false;
													}
												} else
													break;
											}
										}

									}
					}
				}
			}
		PrintMessage.updateEventLabel(eventLabel, String.format("已搜尋:%.2f%%\n\n", 100.0));
	}

	private void printEquipment(EquipmentList currentEquipmentList, int[] remainDecorSlot) {
		currentEquipmentList.setAdditionalInformation();
		// 印出裝備名稱
		PrintMessage.print(textArea, currentEquipmentList.toString() + "\n");

		// 印出裝備技能名稱和等級
		//ItemSkillList skillListWithDecoration = ItemSkillList.maxSkill(currentEquipmentList.equipmentSkillList,includedSkill);
		//PrintMessage.print(textArea, skillListWithDecoration.toString(skillHashMap) + "\n");
		PrintMessage.print(textArea, currentEquipmentList.equipmentSkillList.toString(skillHashMap) + "\n");

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(String.format("防禦力： %d,", currentEquipmentList.defense));

		stringBuilder.append("屬性抗性：");
		for (int i = 0; i <= currentEquipmentList.elementalResistance.length - 1; i++) {
			stringBuilder.append(String.format("%+3d,", currentEquipmentList.elementalResistance[i]));
		}
		stringBuilder.append(" 剩餘鑲嵌槽：");
		for (int i = remainDecorSlot.length - 1; i >= 2; i--) {
			stringBuilder.append(String.format("%2d,", remainDecorSlot[i]));
		}
		stringBuilder.append(String.format("%2d", remainDecorSlot[1]));

		stringBuilder.append("\n\n");
		PrintMessage.print(textArea, stringBuilder.toString());
	}
}
