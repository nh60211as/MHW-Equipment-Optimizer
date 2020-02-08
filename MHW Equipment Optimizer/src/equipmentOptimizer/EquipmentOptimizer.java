package equipmentOptimizer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;

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
	private SetBonusList includedSetBonus;
	private ItemSkillList includedSkill;
	private ItemSkillList excludedSkill;
	// 包含裝備的資料
	private WeaponList includedWeapon;
	private WeaponList excludedWeapon; // TODO
	private ArmorList includedArmor;
	private ArrayList<Armor> includedArmorSet; // TODO
	private CharmList includedCharm;
	private JewelList includedJewel;
	private IncludedJewelTable includedJewelTable;

	private final JTextArea textArea;
	private final JLabel eventLabel;
	private String requirementFileName;

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
		includedArmorSet = armorSetList;
		// 所有護石的資料
		charmList = ReadFile.readCharmFile(equipmentFileDirectory, charmFileNames, skillHashMap, eventLabel);
		// 所有裝飾珠的資料
		jewelList = ReadFile.readJewelFile(equipmentFileDirectory, jewelFileName, skillHashMap, eventLabel);
		// 結束初始化函數
	}

	void readRequirement(String requirementFileName) throws CloneNotSupportedException {
		this.requirementFileName = requirementFileName;

		includedSetBonus = new SetBonusList();
		includedSkill = new ItemSkillList();
		excludedSkill = new ItemSkillList();

		includedWeapon = new WeaponList();
		includedArmor = new ArmorList();
		includedCharm = new CharmList();
		includedJewel = new JewelList();
		includedJewelTable = new IncludedJewelTable();

		// 讀取技能、裝備需求檔案
		ReadFile.readRequirementFile(this.requirementFileName, skillHashMap, weaponList, armorList, includedSetBonus, includedSkill, excludedSkill, includedWeapon, includedArmor, textArea, eventLabel);

		// 若未指定武器則加入空武器
		if (includedWeapon.isEmptyWeaponList()) {
			PrintMessage.warning(textArea, "未指定武器");
			includedWeapon.add(WeaponList.GREATSWORD, new Weapon());
		}

		//System.out.println("搜索合適裝備中...");
		// 加入要搜尋的防具
		for (int currentBodyPart = ArmorList.HEAD; currentBodyPart <= ArmorList.FEET; currentBodyPart++) {
			ArrayList<Armor> currentIncludedArmorList = includedArmor.get(currentBodyPart);
			if (currentIncludedArmorList.isEmpty()) {
				// add the following armors to the list
				ArrayList<Armor> includedArmorWithSetBonus = new ArrayList<>();
				ArrayList<Armor> includedArmorWithIncludedSkill = new ArrayList<>();
				ArrayList<Armor> includedArmorGeneral = new ArrayList<>();
				for (Armor currentArmor : armorList.get(currentBodyPart)) {
					currentArmor.setValidSkills(includedSkill);
					// 如果目前的防具有不能加入的技能則跳過
					if (currentArmor.skills.containsSkill(excludedSkill))
						continue;

					// 如果目前的防具有系列技能則加入
					if (currentArmor.containsSetBonus(includedSetBonus)) {
						includedArmorWithSetBonus.add(currentArmor);
						continue;
					}

					// 如果目前的防具有需求技能
					if (!currentArmor.validSkills.isEmpty()) {
						// 如果目前擁有需求技能的防具列表示空的則直接加入
						if (includedArmorWithIncludedSkill.isEmpty()) {
							includedArmorWithIncludedSkill.add(currentArmor);
							continue;
						}

						ArrayList<Integer> markedToRemove = new ArrayList<>();
						boolean markedToAdd = false;
						for (int includedArmorIndex = 0; includedArmorIndex < includedArmorWithIncludedSkill.size(); includedArmorIndex++) {
							Armor armorNow = includedArmorWithIncludedSkill.get(includedArmorIndex);

							int isArmorNowBetter = armorNow.isBetter(currentArmor, includedSkill, skillHashMap);
//						System.out.println(armorNow.name + " " +
////								isArmorNowBetter + " " +
////								currentArmor.name);
							if (isArmorNowBetter == Armor.BETTER) {
								break;
							} else if (isArmorNowBetter == Armor.SAME) {
								markedToAdd = true;
							} else if (isArmorNowBetter == Armor.WORSE) {
								markedToRemove.add(includedArmorIndex);
								markedToAdd = true;
							} else if (isArmorNowBetter == Armor.MAYBE) {
								markedToAdd = true;
							}
						}
						//includedEquipmentList.printBodyPart(bodyPartNow);
						//System.out.print("remove: ");
						//System.out.println(markedToRemove);
						for (int i = markedToRemove.size() - 1; i >= 0; i--) {
							includedArmorWithIncludedSkill.remove((int) markedToRemove.get(i));
						}
						if (markedToAdd) {
							//System.out.println("add: " + currentEquipment.equipmentName);
							includedArmorWithIncludedSkill.add(currentArmor);
						}
						//includedArmorList.printBodyPart(currentBodyPart);
						//System.out.println();
						//includedArmorWithIncludedSkill.add(currentArmor);
						continue;
					}

					// 剩下的防具
					if (includedArmorGeneral.isEmpty()) {
						includedArmorGeneral.add(currentArmor);
						continue;
					}

					ArrayList<Integer> markedToRemove = new ArrayList<>();
					boolean markedToAdd = false;
					for (int includedArmorIndex = 0; includedArmorIndex < includedArmorGeneral.size(); includedArmorIndex++) {
						Armor armorNow = includedArmorGeneral.get(includedArmorIndex);

						int isArmorNowBetter = armorNow.isBetterWithNoSkill(currentArmor);
//						System.out.println(armorNow.name + " " +
//								isArmorNowBetter + " " +
//								currentArmor.name);
						if (isArmorNowBetter == Armor.BETTER) {
							break;
						} else if (isArmorNowBetter == Armor.SAME) {
							markedToAdd = true;
						} else if (isArmorNowBetter == Armor.WORSE) {
							markedToRemove.add(includedArmorIndex);
							markedToAdd = true;
						} else if (isArmorNowBetter == Armor.MAYBE) {
							markedToAdd = true;
						}
					}
					//System.out.println(includedArmorGeneral.toString());
					//System.out.print("remove: ");
					//System.out.println(markedToRemove);
					for (int i = markedToRemove.size() - 1; i >= 0; i--) {
						includedArmorGeneral.remove((int) markedToRemove.get(i));
					}
					if (markedToAdd) {
						//System.out.println("add: " + currentEquipment.equipmentName);
						includedArmorGeneral.add(currentArmor);
					}
					//System.out.println(includedArmorGeneral.toString());
					//System.out.println();
				}
				currentIncludedArmorList.addAll(includedArmorWithSetBonus);
				currentIncludedArmorList.addAll(includedArmorWithIncludedSkill);
				currentIncludedArmorList.addAll(includedArmorGeneral);
			}
		}

		// 加入要搜尋的護石
		for (Charm currentCharm : charmList) {
			if (currentCharm.skills.containsSkill(excludedSkill))
				continue;

			currentCharm.setValidSkills(includedSkill);
			if (!currentCharm.validSkills.isEmpty())
				includedCharm.add(currentCharm);
		}

		// 加入要搜尋的裝飾珠
		for (Jewel currentJewel : jewelList) {
			if (currentJewel.skills.containsSkill(excludedSkill))
				continue;
			if (currentJewel.owned == 0)
				continue;

			currentJewel.setValidSkills(includedSkill);
			if (!currentJewel.validSkills.isEmpty()) {
				includedJewel.add(currentJewel);
			}
		}

		// 設定要包含的 Jewel Table
		for (Integer skillIndex : includedSkill.keySet()) {
			JewelList currentJewelList = includedJewel.getJewelsContainsValidSkill(skillIndex);
			currentJewelList.removeContained(includedJewelTable);

			includedJewelTable.add(skillIndex, currentJewelList);
		}
		//System.out.println("搜索完成");
	}

	void findAndPrintMatchingEquipmentList() {
		PrintMessage.print(textArea, "符合 " + requirementFileName + " 條件的裝備：\n\n");

		long weaponSize = includedWeapon.totalSize();
		long armorSize = includedArmor.iterationSize();
		long armorSetSize = includedArmorSet.size();
		long charmSize = includedCharm.size();
		long totalSize = weaponSize * armorSize * charmSize;
		//long totalSize = weaponSize * (armorSize + armorSetSize) * charmSize;

		double lastPrint = 0;
		int searchCount = 0;
		for (ArrayList<Weapon> weaponType : includedWeapon)
			for (Weapon weapon : weaponType) {
				// 搜尋整套防具
//					for(){
//
//					}
				// 搜尋個別防具
				ArrayList<Integer> armorsUsedNow = new ArrayList<Integer>(Collections.nCopies(ArmorList.size, 0));
				armorsUsedNow.set(0, -1);
				for (long armorIter = 0; armorIter < armorSize; armorIter++) {
					for (Charm charm : includedCharm) {
						double currentPrint = searchCount * 100.0 / (totalSize);
						if (lastPrint <= currentPrint - 0.01) {
							lastPrint = currentPrint;
							PrintMessage.updateEventLabel(eventLabel, String.format("已搜尋:%.2f%%\n\n", currentPrint));
						}
						searchCount++;
						bitsAddOne(armorsUsedNow, includedArmor);
//						EquipmentList currentEquipmentList =
//								new EquipmentList(weapon,
//										includedArmor.get(ArmorList.HEAD).get(2),
//										includedArmor.get(ArmorList.BODY).get(8),
//										includedArmor.get(ArmorList.HANDS).get(14),
//										includedArmor.get(ArmorList.BELT).get(18),
//										includedArmor.get(ArmorList.FEET).get(9),
//										includedCharm.get(0));

						Armor head = includedArmor.get(ArmorList.HEAD).get(armorsUsedNow.get(0));
						Armor body = includedArmor.get(ArmorList.BODY).get(armorsUsedNow.get(1));
						Armor hands = includedArmor.get(ArmorList.HANDS).get(armorsUsedNow.get(2));
						Armor belt = includedArmor.get(ArmorList.BELT).get(armorsUsedNow.get(3));
						Armor feet = includedArmor.get(ArmorList.FEET).get(armorsUsedNow.get(4));
						EquipmentList currentEquipmentList =
								new EquipmentList(weapon, head, body, hands, belt, feet, charm);

						//檢查是否為套裝
						if (!includedSetBonus.checkSetBonus(currentEquipmentList.setBonusList))
							continue;

						// 計算目前裝備的鑲嵌槽數量
						ArrayList<Integer> numberOfSlotHave = currentEquipmentList.getDecorationSlot();
						int totalSlotHave = 0;
						int maxSkillPossible = 0;
						int maxSlotLevel = 0;
						for (int i = 0; i < numberOfSlotHave.size(); i++) {
							if (numberOfSlotHave.get(i) > 0) {
								totalSlotHave += numberOfSlotHave.get(i);
								maxSkillPossible += numberOfSlotHave.get(i);
								maxSlotLevel = i;
							}
						}
						maxSkillPossible += numberOfSlotHave.get(4) * 2;

						// 目前需要的技能
						ItemSkillList skillRequired = new ItemSkillList(includedSkill);
						// 計算目前裝備的技能列表
						ItemSkillList skillHave = currentEquipmentList.getEquipmentSkillList();
						// 計算仍然需要的技能列表
						ItemSkillList skillNeed = ItemSkillList.removeSkill(skillRequired, skillHave);

						// 計算仍然需要的獨特裝飾珠
						//HashMap<Integer, Integer> uniqueSkillJewelNeed = new HashMap<>();
						//JewelList finalJewelList = new JewelList();
						// 計算需要的技能等級數
						int maxSkillNeeded = 0;
						for (Integer skillIndex : skillNeed.keySet()) {
							//uniqueSkillJewelNeed.put(skillIndex, uniqueSkillJewelList.get(skillIndex));
							//finalJewelList.add(includedJewelList.get(uniqueSkillJewelList.get(skillIndex)));
							maxSkillNeeded += skillNeed.getSkillLevel(skillIndex);
						}
						if (maxSkillNeeded > maxSkillPossible)
							continue;

						// 開始使用 includedJewelTable
						boolean impossible = false;
						for (Integer skillIndex : includedJewelTable.keySet()) {

							if (skillNeed.containsKey(skillIndex)) {
								Jewel jewel = includedJewelTable.getJewelWithUniqueSkill(skillIndex);
								if (jewel != null) {
									float levelProvidedByOneJewel = jewel.validSkills.getSkillLevel(skillIndex);
									float levelNeeded = skillNeed.getSkillLevel(skillIndex);
									int jewelNeed = (int) Math.ceil(levelNeeded / levelProvidedByOneJewel);
									int jewelOwned = jewel.owned;
									if (jewelNeed > jewelOwned) {
										impossible = true;
										break;
									}


									int[] numberOfSlotNeed = new int[5];
									int jewelSlotLevel = jewel.slotLevel;
									numberOfSlotNeed[jewelSlotLevel] = jewelNeed;
									if (!isCurrentSlotPossible(numberOfSlotHave, numberOfSlotNeed)) {
										impossible = true;
										break;
									} else {
										skillNeed.remove(skillIndex);
									}
								}
							}

						}
						if (impossible)
							continue;

						if (skillNeed.isEmpty()) {
							printEquipment(currentEquipmentList, numberOfSlotHave);
							continue;
						}

						JewelList finalJewelList = new JewelList(includedJewel);
						long totalIter = 1;
						for (int i = finalJewelList.size() - 1; i >= 0; i--) {
							if (!finalJewelList.get(i).skills.containsSkill(skillNeed))
								finalJewelList.remove(i);
							else
								totalIter *= (finalJewelList.get(i).owned + 1); // calculate the iteration needed for all the jewels
						}

						ArrayList<Integer> jewelsUsedNow = new ArrayList<>(Collections.nCopies(finalJewelList.size(), 0));
						for (int iter = 0; iter < totalIter; iter++) {
							boolean skip = false;
							ItemSkillList skillNow = new ItemSkillList();
							int[] numberOfSlotNeed = new int[5];
							int totalSlotUsed = 0;
							int maxSlotLevelNow = 0;
							for (int i = 0; i < jewelsUsedNow.size(); i++) {
								if (jewelsUsedNow.get(i) == 0)
									continue;
								skillNow.addSkill(finalJewelList.get(i).skills, jewelsUsedNow.get(i));
								numberOfSlotNeed[finalJewelList.get(i).slotLevel] += jewelsUsedNow.get(i);
								totalSlotUsed += jewelsUsedNow.get(i);
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
							int[] numberOfHoleHave = new int[0];
							boolean success = false;
							if (!skip) {
								boolean finished = true;
								numberOfHoleHave = new int[numberOfSlotHave.size()];
								for (int i = 0; i <= numberOfHoleHave.length - 1; i++) {
									numberOfHoleHave[i] = numberOfSlotHave.get(i) - numberOfSlotNeed[i];
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
								printEquipment(currentEquipmentList, numberOfHoleHave);
								break;
							}

							// add one jewel every iteration and carry the overflowed jewel to the next one
							// just like basic math addition
							bitsAddOne(jewelsUsedNow, finalJewelList);
						} // end of finding suitable jewels

					}
				}
			}
		PrintMessage.updateEventLabel(eventLabel, String.format("已搜尋:%.2f%%\n\n", 100.0));
	}

	// numberOfSlotHave = [0,1,1,0,0]
	// numberOfSlotNeed = [0,1,1,0,0]
	private boolean isCurrentSlotPossible(ArrayList<Integer> numberOfSlotHave, int[] numberOfSlotNeed) {
		boolean finished = true;

		for (int i = 0; i < numberOfSlotHave.size(); i++) {
			numberOfSlotHave.set(i, numberOfSlotHave.get(i) - numberOfSlotNeed[i]);
			if (numberOfSlotHave.get(i) < 0) {
				finished = false;
			}
		}

		// 如果是 finished 狀態則回傳 true，因為不缺任何鑲嵌槽
		// 如果不是的話則進入 while 迴圈
		while (!finished) {
			finished = true;
			// 如果最高級的鑲嵌槽數量小於 0 則回傳 false，因為無法再向更高級的鑲嵌槽借位
			if (numberOfSlotHave.get(numberOfSlotHave.size() - 1) < 0)
				return false;

			for (int i = numberOfSlotHave.size() - 2; i >= 1; i--) {
				// 如果目前的鑲嵌槽數量小於 0 則向上一級的鑲嵌槽借位
				if (numberOfSlotHave.get(i) < 0) {
					numberOfSlotHave.set(i + 1, numberOfSlotHave.get(i + 1) + numberOfSlotHave.get(i));
					numberOfSlotHave.set(i, 0);
				}
			}

			// 完成後檢查目前的 numberOfSlotHave 數字是不是都大於等於 0
			for (Integer currentSlot : numberOfSlotHave) {
				if (currentSlot < 0) {
					finished = false;
					break;
				}
			}
		}

		return true;
	}

	// idea from leetcode 78. Subsets
	private void bitsAddOne(ArrayList<Integer> toAddList, ArmorList includedArmorList) {
		boolean carry = true;
		for (int i = 0; i < toAddList.size(); i++) {
			if (carry) {
				toAddList.set(i, toAddList.get(i) + 1);
				if (toAddList.get(i) > includedArmorList.get(i).size() - 1) {
					carry = true;
					toAddList.set(i, 0);
				} else {
					carry = false;
				}
			} else
				break;
		}
	}

	private void bitsAddOne(ArrayList<Integer> toAddList, JewelList jewelList) {
		boolean carry = true;
		for (int i = 0; i < toAddList.size(); i++) {
			if (carry) {
				toAddList.set(i, toAddList.get(i) + 1);
				if (toAddList.get(i) > jewelList.get(i).owned) {
					carry = true;
					toAddList.set(i, 0);
				} else {
					carry = false;
				}
			} else
				break;
		}
	}

	private void printEquipment(EquipmentList currentEquipmentList, ArrayList<Integer> remainDecorSlot) {
		currentEquipmentList.setAdditionalInformation();
		// 印出裝備名稱
		PrintMessage.print(textArea, currentEquipmentList.toString() + "\n");

		// 印出裝備技能名稱和等級
		ItemSkillList skillListWithDecoration = ItemSkillList.maxSkill(currentEquipmentList.equipmentSkillList, includedSkill);
		PrintMessage.print(textArea, skillListWithDecoration.toString(skillHashMap) + "\n");
		//PrintMessage.print(textArea, currentEquipmentList.equipmentSkillList.toString(skillHashMap) + "\n");

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(String.format("防禦力： %d,", currentEquipmentList.defense));

		stringBuilder.append("屬性抗性：");
		for (int i = 0; i <= currentEquipmentList.elementalResistance.length - 1; i++) {
			stringBuilder.append(String.format("%+3d,", currentEquipmentList.elementalResistance[i]));
		}
		stringBuilder.append(" 剩餘鑲嵌槽：");
		for (int i = remainDecorSlot.size() - 1; i >= 2; i--) {
			stringBuilder.append(String.format("%2d,", remainDecorSlot.get(i)));
		}
		stringBuilder.append(String.format("%2d", remainDecorSlot.get(1)));

		stringBuilder.append("\n\n");
		PrintMessage.print(textArea, stringBuilder.toString());
	}

	private void printEquipment(EquipmentList currentEquipmentList, int[] remainDecorSlot) {
		currentEquipmentList.setAdditionalInformation();
		// 印出裝備名稱
		PrintMessage.print(textArea, currentEquipmentList.toString() + "\n");

		// 印出裝備技能名稱和等級
		ItemSkillList skillListWithDecoration = ItemSkillList.maxSkill(currentEquipmentList.equipmentSkillList, includedSkill);
		PrintMessage.print(textArea, skillListWithDecoration.toString(skillHashMap) + "\n");
		//PrintMessage.print(textArea, currentEquipmentList.equipmentSkillList.toString(skillHashMap) + "\n");

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
