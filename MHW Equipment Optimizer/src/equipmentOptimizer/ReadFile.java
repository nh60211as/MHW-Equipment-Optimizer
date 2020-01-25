package equipmentOptimizer;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

class ReadFile {
	private static final int DEFAULT_READ_FLAG = -5;
	private static final int SET_BONUS_READ_FLAG = -4;
	private static final int SKILL_INCLUSION_READ_FLAG = -3;
	private static final int SKILL_EXCLUSION_READ_FLAG = -2;
	private static final int WEAPON_READ_FLAG = -1;


	private static int changeReadFlag(String currentLine) {
		switch (currentLine) {
			case "系列需求：":
				return SET_BONUS_READ_FLAG;
			case "需求：":
				return SKILL_INCLUSION_READ_FLAG;
			case "排除：":
				return SKILL_EXCLUSION_READ_FLAG;
			case "武器：":
				return WEAPON_READ_FLAG;
			case "大劍：":
				return WeaponList.GREATSWORD;
			case "太刀：":
				return WeaponList.LONGSWORD;
			case "單手劍：":
				return WeaponList.SWORDNSHIELD;
			case "雙劍：":
				return WeaponList.DUALBLADES;
			case "大錘：":
				return WeaponList.HAMMER;
			case "狩獵笛：":
				return WeaponList.HUNTINGHORN;
			case "長槍：":
				return WeaponList.LANCE;
			case "銃槍：":
				return WeaponList.GUNLANCE;
			case "斬擊斧：":
				return WeaponList.SWITCHAXE;
			case "充能斧：":
				return WeaponList.CHARGEBLADE;
			case "操蟲棍：":
				return WeaponList.INSECTGLAIVE;
			case "輕弩槍：":
				return WeaponList.LIGHTBOWGUN;
			case "重弩槍：":
				return WeaponList.HEAVYBOWGUN;
			case "弓：":
				return WeaponList.BOW;
			case "頭：":
				return ArmorList.HEAD;
			case "身：":
				return ArmorList.BODY;
			case "腕：":
				return ArmorList.HANDS;
			case "腰：":
				return ArmorList.BELT;
			case "腳：":
				return ArmorList.FEET;
			case "整套防具：":
				return 0;
			case "護石：":
				return CharmList.CHARM;
		}

		return DEFAULT_READ_FLAG;
	}

	static SkillHashMap readSkillFile(final String equipmentFileDirectory, final String skillFileName, final JLabel eventLabel) {
		SkillHashMap skillHashMap = new SkillHashMap();

		Reader reader = null;
		BufferedReader br = null;
		try {
			reader = new InputStreamReader(new FileInputStream(equipmentFileDirectory + skillFileName), StandardCharsets.UTF_8);
			br = new BufferedReader(reader);

			// 開始閱讀檔案
			int skillIndex = 0;
			int jewelSlotLevel = 0;
			String currentLine;

			while ((currentLine = br.readLine()) != null) {
				if (currentLine.length() == 0)
					continue;
				if (currentLine.substring(0, 1).contentEquals("#"))
					continue;

				// 鑲嵌槽等級;1
				// 攻擊珠;攻擊,1;7,5
				String[] stringBlock = currentLine.split(";");
				if (stringBlock.length == 2) {
					// 鑲嵌槽等級;1
					if (stringBlock[0].contentEquals("鑲嵌槽等級"))
						jewelSlotLevel = Integer.parseInt(stringBlock[1]);
				} else if (jewelSlotLevel <= 3 && stringBlock.length == 3) {
					// 攻擊
					String skillName = stringBlock[1].split(",")[0];
					// 7,5
					String[] requirementBlock = stringBlock[2].split(",");
					// 7
					int level = Integer.parseInt(requirementBlock[0]);
					// 5
					int owned = Integer.parseInt(requirementBlock[1]);
					boolean isReplaceable = (level - owned) <= 0;

					skillHashMap.put(skillIndex, new Skill(skillName, level, isReplaceable));
					skillIndex++;
				}
			}
		} catch (IOException e) {
			PrintMessage.updateEventLabelError(eventLabel, equipmentFileDirectory + skillFileName + "讀取錯誤");
		} finally {
			try {
				if (br != null)
					br.close();
				if (reader != null)
					reader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return skillHashMap;
	}

	static WeaponList readWeaponFile(final String equipmentFileDirectory, final String[] weaponFileNames, final SkillHashMap skillHashMap, final JLabel eventLabel) {
		WeaponList weaponList = new WeaponList();

		for (String fileName : weaponFileNames) {
			Reader reader = null;
			BufferedReader br = null;
			try {
				reader = new InputStreamReader(new FileInputStream(equipmentFileDirectory + fileName), StandardCharsets.UTF_8);
				br = new BufferedReader(reader);

				// 開始閱讀檔案
				int readFlag = DEFAULT_READ_FLAG;
				int currentFlag;
				String currentLine;

				while ((currentLine = br.readLine()) != null) {
					if (currentLine.length() == 0)
						continue;
					if (currentLine.substring(0, 1).contentEquals("#"))
						continue;

					currentFlag = changeReadFlag(currentLine); // currentFlag 只能為DEFAULT_READ_FLAG或是WeaponList的數值
					if (currentFlag != DEFAULT_READ_FLAG)
						readFlag = currentFlag;
					else {
						weaponList.get(readFlag).add(new Weapon(currentLine, skillHashMap));
					}
				}
			} catch (IOException e) {
				PrintMessage.updateEventLabelError(eventLabel, "武器相關檔案讀取錯誤");
			} finally {
				try {
					if (br != null)
						br.close();
					if (reader != null)
						reader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		return weaponList;
	}

	static ArmorList readArmorFile(final String equipmentFileDirectory, final String[] armorFileNames, final SkillHashMap skillHashMap, final JLabel eventLabel) {
		ArmorList armorList = new ArmorList();

		for (String fileName : armorFileNames) {
			Reader reader = null;
			BufferedReader br = null;
			try {
				reader = new InputStreamReader(new FileInputStream(equipmentFileDirectory + fileName), StandardCharsets.UTF_8);
				br = new BufferedReader(reader);

				//開始閱讀檔案
				int readFlag = DEFAULT_READ_FLAG;
				int currentFlag;
				String currentLine;

				while ((currentLine = br.readLine()) != null) {
					if (currentLine.length() == 0)
						continue;
					if (currentLine.substring(0, 1).contentEquals("#"))
						continue;

					currentFlag = changeReadFlag(currentLine); // currentFlag 只能為DEFAULT_READ_FLAG或是ArmorList的數值
					if (currentFlag != DEFAULT_READ_FLAG)
						readFlag = currentFlag;
					else {
						armorList.get(readFlag).add(new Armor(currentLine, skillHashMap));
					}
				}
			} catch (IOException e) {
				PrintMessage.updateEventLabelError(eventLabel, "'防具相關檔案讀取錯誤");
			} finally {
				try {
					if (br != null)
						br.close();
					if (reader != null)
						reader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		return armorList;
	}

	static ArrayList<Armor> readArmorSetFile(final String equipmentFileDirectory, final String[] armorSetFileNames, final SkillHashMap skillHashMap, final JLabel eventLabel) {
		ArrayList<Armor> armorSetsList = new ArrayList();

		for (String fileName : armorSetFileNames) {
			Reader reader = null;
			BufferedReader br = null;
			try {
				reader = new InputStreamReader(new FileInputStream(equipmentFileDirectory + fileName), StandardCharsets.UTF_8);
				br = new BufferedReader(reader);

				//開始閱讀檔案
				int readFlag = DEFAULT_READ_FLAG;
				int currentFlag;
				String currentLine;

				while ((currentLine = br.readLine()) != null) {
					if (currentLine.length() == 0)
						continue;
					if (currentLine.substring(0, 1).contentEquals("#"))
						continue;

					currentFlag = changeReadFlag(currentLine);
					if (currentFlag != DEFAULT_READ_FLAG)
						readFlag = currentFlag;
					else {
						armorSetsList.add(new Armor(currentLine, skillHashMap));
					}
				}
			} catch (IOException e) {
				PrintMessage.updateEventLabelError(eventLabel, "'防具相關檔案讀取錯誤");
			} finally {
				try {
					if (br != null)
						br.close();
					if (reader != null)
						reader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		return armorSetsList;
	}

	static CharmList readCharmFile(final String equipmentFileDirectory, final String[] charmFileNames, final SkillHashMap skillHashMap, final JLabel eventLabel) {
		CharmList charmList = new CharmList();

		for (String fileName : charmFileNames) {
			Reader reader = null;
			BufferedReader br = null;
			try {
				reader = new InputStreamReader(new FileInputStream(equipmentFileDirectory + fileName), StandardCharsets.UTF_8);
				br = new BufferedReader(reader);

				//開始閱讀檔案
				int readFlag = DEFAULT_READ_FLAG;
				int currentFlag;
				String currentLine;

				while ((currentLine = br.readLine()) != null) {
					if (currentLine.length() == 0)
						continue;
					if (currentLine.substring(0, 1).contentEquals("#"))
						continue;

					currentFlag = changeReadFlag(currentLine); // currentFlag 只能為DEFAULT_READ_FLAG或是charmList的數值
					if (currentFlag != DEFAULT_READ_FLAG)
						readFlag = currentFlag;
					else {
						charmList.add(new Charm(currentLine, skillHashMap));
					}
				}
			} catch (IOException e) {
				PrintMessage.updateEventLabelError(eventLabel, "'防具相關檔案讀取錯誤");
			} finally {
				try {
					if (br != null)
						br.close();
					if (reader != null)
						reader.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
		return charmList;
	}

	static JewelList readJewelFile(final String equipmentFileDirectory, final String jewelFileName, final SkillHashMap skillHashMap, final JLabel eventLabel) {
		JewelList jewelList = new JewelList();

		Reader reader = null;
		BufferedReader br = null;
		try {
			reader = new InputStreamReader(new FileInputStream(equipmentFileDirectory + jewelFileName), StandardCharsets.UTF_8);
			br = new BufferedReader(reader);

			// 開始閱讀檔案
			int jewelSlotLevel = 0;
			String currentLine;

			while ((currentLine = br.readLine()) != null) {
				if (currentLine.length() == 0)
					continue;
				if (currentLine.substring(0, 1).contentEquals("#"))
					continue;

				String[] stringBlock = currentLine.split(";");
				if (stringBlock.length == 2) {
					// 鑲嵌槽等級;1
					if (stringBlock[0].contentEquals("鑲嵌槽等級"))
						jewelSlotLevel = Integer.parseInt(stringBlock[1]);
				} else if (jewelSlotLevel >= 0 && stringBlock.length == 3) {
					jewelList.add(new Jewel(currentLine, jewelSlotLevel, skillHashMap));
				}
			}
		} catch (IOException e) {
			PrintMessage.updateEventLabelError(eventLabel, equipmentFileDirectory + jewelFileName + "讀取錯誤");
		} finally {
			try {
				if (br != null)
					br.close();
				if (reader != null)
					reader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return jewelList;
	}

	static void readRequirementFile(final String fileName,
									final SkillHashMap skillHashMap,
									final WeaponList weaponList,
									final ArmorList armorList,
									final SetBonusList setBonusList,
									final ItemSkillList includedSkill,
									final ItemSkillList excludedSkill,
									final WeaponList includedWeaponList,
									final ArmorList includedArmorList,
									final JTextArea textArea,
									final JLabel eventLabel) throws CloneNotSupportedException {
		Reader reader = null;
		BufferedReader br = null;
		try {
			reader = new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8);
			br = new BufferedReader(reader);

			//開始閱讀檔案
			int readFlag = DEFAULT_READ_FLAG;
			int currentFlag;
			String currentLine;

			while ((currentLine = br.readLine()) != null) {
				if (currentLine.length() == 0)
					continue;
				if (currentLine.substring(0, 1).contentEquals("#"))
					continue;

				currentFlag = changeReadFlag(currentLine);
				if (currentFlag != DEFAULT_READ_FLAG)
					readFlag = currentFlag;
				else {
					String[] stringBlock = currentLine.split(",");
					if (readFlag == SET_BONUS_READ_FLAG) {
						String setBonusName = stringBlock[0];
						int setBonusLevel = Integer.parseInt(stringBlock[1]);
						if (setBonusLevel >= 1) {
							setBonusList.add(setBonusName, setBonusLevel);
						} else {
							PrintMessage.warning(textArea, "自動忽略系列技能-" + currentLine);
						}
					} else if (readFlag == SKILL_INCLUSION_READ_FLAG) {
						String readSkill = stringBlock[0];
						int readSkillRequiredLevel = Integer.parseInt(stringBlock[1]);

						if (readSkillRequiredLevel <= 0) {
							PrintMessage.warning(textArea, "自動忽略需求技能-" + currentLine);
							continue;
						}

						int readSkillIndex = skillHashMap.keyOfSkillName(readSkill);
						if (readSkillIndex != -1) {
							includedSkill.put(readSkillIndex, readSkillRequiredLevel);
						} else {
							PrintMessage.warning(textArea, "找不到需求技能-" + stringBlock[0]);
						}
					} else if (readFlag == SKILL_EXCLUSION_READ_FLAG) {
						for (String readSkill : stringBlock) {
							int readSkillIndex = skillHashMap.keyOfSkillName(readSkill);
							if (readSkillIndex != -1) {
								excludedSkill.put(readSkillIndex, 1);
							} else {
								PrintMessage.warning(textArea, "找不到排除技能-" + readSkill);
							}
						}
					} else if (readFlag == WEAPON_READ_FLAG) {
						String readWeapon = stringBlock[0];
						int[] indexOfReadWeapon = weaponList.indexOf(readWeapon);
						if (indexOfReadWeapon[0] != -1) {
							if (stringBlock.length == 6) {
								int attackBoost = Integer.parseInt(stringBlock[1]);
								int affinityBoost = Integer.parseInt(stringBlock[2]);
								int defenseBoost = Integer.parseInt(stringBlock[3]);
								int decorationSlotBoost = Integer.parseInt(stringBlock[4]);
								int leechBoost = Integer.parseInt(stringBlock[5]);
								Weapon boostedWeapon = (Weapon) weaponList.get(readWeapon).clone();
								boostedWeapon.setBoost(attackBoost, affinityBoost, defenseBoost, decorationSlotBoost, leechBoost);
								if (!includedWeaponList.add(indexOfReadWeapon[0], boostedWeapon)) {
									PrintMessage.warning(textArea, "自動跳過重複武器-" + readWeapon);
								}
							} else {
								if (!includedWeaponList.add(indexOfReadWeapon[0], weaponList.get(readWeapon))) {
									PrintMessage.warning(textArea, "自動跳過重複武器-" + readWeapon);
								}
							}
						} else {
							PrintMessage.warning(textArea, "找不到武器-" + readWeapon);
						}
					} else {
						// stringBlock = {礦石鎧甲α,礦石鎧甲β}
						for (String readArmor : stringBlock) {
							int indexOfReadEquipment = armorList.indexOf(readFlag, readArmor);
							if (indexOfReadEquipment != -1) {
								if (!includedArmorList.add(readFlag, armorList.get(readFlag, indexOfReadEquipment))) {
									PrintMessage.warning(textArea, "自動跳過重複防具-" + readArmor);
								}
							} else {
								PrintMessage.warning(textArea, "找不到防具-" + readArmor);
							}
						}
					}
				}
			}
		} catch (IOException e) {
			PrintMessage.updateEventLabel(eventLabel, "'需求檔案讀取錯誤");
		} finally {
			try {
				if (br != null)
					br.close();
				if (reader != null)
					reader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
