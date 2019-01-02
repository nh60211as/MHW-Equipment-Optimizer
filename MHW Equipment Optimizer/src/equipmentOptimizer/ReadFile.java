package equipmentOptimizer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class ReadFile {
	private static final int DEFAULT_READ_FLAG = -5;
	private static final int SET_BONUS_READ_FLAG = -4;
	private static final int SKILL_INCLUSION_READ_FLAG = -3;
	private static final int SKILL_EXCLUSION_READ_FLAG = -2;
	private static final int WEAPON_READ_FLAG = -1;


	private static int changeReadFlag(String currentLine){
		if(currentLine.equals("系列需求：")) return SET_BONUS_READ_FLAG;
		else if(currentLine.equals("需求：")) return SKILL_INCLUSION_READ_FLAG;
		else if(currentLine.equals("排除：")) return SKILL_EXCLUSION_READ_FLAG;
		else if(currentLine.equals("武器：")) return WEAPON_READ_FLAG;

		else if(currentLine.equals("大劍：")) return WeaponList.GREATSWORD;
		else if(currentLine.equals("太刀：")) return WeaponList.LONGSWORD;
		else if(currentLine.equals("單手劍：")) return WeaponList.SWORDNSHIELD;
		else if(currentLine.equals("雙劍：")) return WeaponList.DUALBLADES;
		else if(currentLine.equals("大錘：")) return WeaponList.HAMMER;
		else if(currentLine.equals("狩獵笛：")) return WeaponList.HUNTINGHORN;
		else if(currentLine.equals("長槍：")) return WeaponList.LANCE;
		else if(currentLine.equals("銃槍：")) return WeaponList.GUNLANCE;
		else if(currentLine.equals("斬擊斧：")) return WeaponList.SWITCHAXE;
		else if(currentLine.equals("充能斧：")) return WeaponList.CHARGEBLADE;
		else if(currentLine.equals("操蟲棍：")) return WeaponList.INSECTGLAIVE;
		else if(currentLine.equals("輕弩槍：")) return WeaponList.LIGHTBOWGUN;
		else if(currentLine.equals("重弩槍：")) return WeaponList.HEAVYBOWGUN;
		else if(currentLine.equals("弓：")) return WeaponList.BOW;

		else if(currentLine.equals("頭：")) return ArmorList.HEAD;
		else if(currentLine.equals("身：")) return ArmorList.BODY;
		else if(currentLine.equals("腕：")) return ArmorList.HANDS;
		else if(currentLine.equals("腰：")) return ArmorList.BELT;
		else if(currentLine.equals("腳：")) return ArmorList.FEET;
		else if(currentLine.equals("護石：")) return ArmorList.CHARM;

		return DEFAULT_READ_FLAG;
	}

	public static SkillList readDecorationFile(String equipmentFileDirectory, String decorationFileName) {
		SkillList decorationList = new SkillList();

		Reader reader = null;
		BufferedReader br = null;
		try {
			reader = new InputStreamReader(new FileInputStream(equipmentFileDirectory+decorationFileName),"UTF-8");
			br = new BufferedReader(reader);

			//開始閱讀檔案
			int levelOfDecoration = 0;
			String currentLine = "";

			while ((currentLine = br.readLine()) != null) {
				if(currentLine.length()==0)
					continue;
				if(currentLine.substring(0, 1).contentEquals("#"))
					continue;

				String[] stringBlock = currentLine.split(",");
				if(stringBlock.length==2) {
					if(stringBlock[0].contentEquals("鑲嵌槽等級"))
						levelOfDecoration = Integer.parseInt(stringBlock[1]);
				}
				else if(stringBlock.length==3) {
					decorationList.add(new Skill(stringBlock, levelOfDecoration));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
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

		return decorationList;
	}

	public static WeaponList readWeaponFile(String equipmentFileDirectory, String[] weaponFileNames) {
		WeaponList weaponList = new WeaponList();

		for(String fileName:weaponFileNames) {
			Reader reader = null;
			BufferedReader br = null;
			try {
				reader = new InputStreamReader(new FileInputStream(equipmentFileDirectory+fileName),"UTF-8");
				br = new BufferedReader(reader);

				//開始閱讀檔案
				int readFlag = DEFAULT_READ_FLAG;
				int currentFlag = DEFAULT_READ_FLAG;
				String currentLine = "";

				while ((currentLine = br.readLine()) != null) {
					if(currentLine.length()==0)
						continue;
					if(currentLine.substring(0, 1).contentEquals("#"))
						continue;

					currentFlag = changeReadFlag(currentLine); // currentFlag 只能為DEFAULT_READ_FLAG或是WeaponList的數值
					if(currentFlag!=DEFAULT_READ_FLAG)
						readFlag = currentFlag;
					else {
						weaponList.get(readFlag).add(new Weapon(currentLine));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
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

	public static ArmorList readArmorFile(String equipmentFileDirectory, String[] armorFileNames) {
		ArmorList armorList = new ArmorList();

		for(String fileName:armorFileNames) {
			Reader reader = null;
			BufferedReader br = null;
			try {
				reader = new InputStreamReader(new FileInputStream(equipmentFileDirectory+fileName),"UTF-8");
				br = new BufferedReader(reader);

				//開始閱讀檔案
				int readFlag = DEFAULT_READ_FLAG;
				int currentFlag = DEFAULT_READ_FLAG;
				String currentLine = "";

				while ((currentLine = br.readLine()) != null) {
					if(currentLine.length()==0)
						continue;
					if(currentLine.substring(0, 1).contentEquals("#"))
						continue;

					currentFlag = changeReadFlag(currentLine); // currentFlag 只能為DEFAULT_READ_FLAG或是ArmorList的數值
					if(currentFlag!=DEFAULT_READ_FLAG)
						readFlag = currentFlag;
					else {
						armorList.get(readFlag).add(new Armor(currentLine));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
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

	public static void readRequirementFile(String fileName,
			SkillList decorationList ,WeaponList weaponList, ArmorList armorList,
			SetBonusList setBouns,SkillList includedSkill, SkillList excludedSkill,
			WeaponList includedWeaponList, ArmorList includedArmorList) {
		Reader reader = null;
		BufferedReader br = null;
		try {
			reader = new InputStreamReader(new FileInputStream(fileName),"UTF-8");
			br = new BufferedReader(reader);

			//開始閱讀檔案
			int readFlag = DEFAULT_READ_FLAG;
			int currentFlag = DEFAULT_READ_FLAG;
			String currentLine = "";

			while ((currentLine = br.readLine()) != null) {
				if(currentLine.length()==0)
					continue;
				if(currentLine.substring(0, 1).contentEquals("#"))
					continue;

				currentFlag = changeReadFlag(currentLine);
				if(currentFlag!=DEFAULT_READ_FLAG)
					readFlag = currentFlag;
				else{
					String[] stringBlock = currentLine.split(",");
					if(readFlag==SET_BONUS_READ_FLAG)
						setBouns.add(stringBlock[0], Integer.parseInt(stringBlock[1]));
					else if(readFlag==SKILL_INCLUSION_READ_FLAG){
						String readSkill = stringBlock[0];
						int readSkillRequirement = Integer.parseInt(stringBlock[1]);

						if(readSkillRequirement<=0) {
							PrintMessage.warning("自動忽略-" + currentLine);
							continue;
						}

						int indexOfReadSkill = decorationList.indexOf(readSkill);
						if(indexOfReadSkill!=-1) {
							Skill temp = decorationList.get(indexOfReadSkill);
							temp.setRequired(readSkillRequirement);
							includedSkill.add(temp);
						}
						else {
							PrintMessage.warning("找不到需求技能-" + stringBlock[0]);
						}
					}
					else if(readFlag==SKILL_EXCLUSION_READ_FLAG){
						for(String readSkill:stringBlock) {
							int indexOfReadSkill = decorationList.indexOf(readSkill);
							if(indexOfReadSkill!=-1) {
								Skill temp = decorationList.get(indexOfReadSkill);
								excludedSkill.add(temp);
							}
							else {
								PrintMessage.warning("找不到排除技能-" + stringBlock[0]);
							}
						}
					}
					else if(readFlag==WEAPON_READ_FLAG){
						for(String readWeapon:stringBlock) {
							int[] indexOfReadWeapon = weaponList.indexOf(readWeapon);
							if(indexOfReadWeapon[0]!=-1) {
								if(!includedWeaponList.add(indexOfReadWeapon[0],weaponList.get(readWeapon))) {
									PrintMessage.warning("自動捨棄重複武器-" + readWeapon);
								}
							}
							else {
								PrintMessage.warning("找不到武器-" + readWeapon);
							}
						}
					}
					else {
						// stringBlock = {礦石鎧甲α,礦石鎧甲β}
						for(String readArmor:stringBlock) {
							int indexOfReadEquipment = armorList.indexOf(readFlag, readArmor);
							if(indexOfReadEquipment!=-1) {
								if(!includedArmorList.add(readFlag,armorList.get(readFlag,indexOfReadEquipment))) {
									PrintMessage.warning("自動捨棄重複防具-" + readArmor);
								}
							}
							else {
								PrintMessage.warning("找不到防具-" + readArmor);
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
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
