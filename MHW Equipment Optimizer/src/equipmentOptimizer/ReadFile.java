package equipmentOptimizer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;

public class ReadFile {
	private static final int DEFAULT_READ_FLAG = -3;
	private static final int SET_BONUS_READ_FLAG = -2;
	private static final int SKILL_REQUIREMENT_READ_FLAG = -1;


	private static int changeReadFlag(String currentLine){
		if(currentLine.equals("系列需求：")) return SET_BONUS_READ_FLAG;
		else if(currentLine.equals("需求：")) return SKILL_REQUIREMENT_READ_FLAG;
		else if(currentLine.equals("武器：")) return 0;
		else if(currentLine.equals("頭：")) return 1;
		else if(currentLine.equals("身：")) return 2;
		else if(currentLine.equals("腕：")) return 3;
		else if(currentLine.equals("腰：")) return 4;
		else if(currentLine.equals("腳：")) return 5;
		else if(currentLine.equals("護石：")) return 6;

		return DEFAULT_READ_FLAG;
	}
	
	public static DecorationList readDecorationFile(String equipmentFileDirectory, String decorationFileName) {
		DecorationList decorationList = new DecorationList();
		
		Reader reader = null;
		BufferedReader br = null;
		try {
			reader = new InputStreamReader(new FileInputStream(equipmentFileDirectory+decorationFileName),"UTF-8");
			br = new BufferedReader(reader);

			//開始閱讀檔案
			int levelOfDecor = 0;
			String currentLine = "";

			while ((currentLine = br.readLine()) != null) {
				if(currentLine.length()==0)
					continue;
				if(currentLine.substring(0, 1).contentEquals("#"))
					continue;

				String[] stringBlock = currentLine.split(",");
				if(stringBlock.length==2) {
					if(stringBlock[0].contentEquals("鑲嵌槽等級"))
						levelOfDecor = Integer.parseInt(stringBlock[1]);
				}
				else if(stringBlock.length==3) {
					decorationList.add(new SkillRequirement(stringBlock, levelOfDecor));
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
	
	public static EquipmentList readEquipmentFile(String equipmentFileDirectory, String[] equipmentFileName) {
		EquipmentList equipmentList = new EquipmentList();
		
		for(String stringNow:equipmentFileName) {
			Reader reader = null;
			BufferedReader br = null;
			try {
				reader = new InputStreamReader(new FileInputStream(equipmentFileDirectory+stringNow),"UTF-8");
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

					currentFlag = changeReadFlag(currentLine); // currentFlag 只能為-2或是0~6
					//System.out.println(currentLine);
					if(currentFlag!=DEFAULT_READ_FLAG)
						readFlag = currentFlag;
					else
						equipmentList.get(readFlag).add(new Equipment(currentLine));
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
		
		return equipmentList;
	}

	public static void readRequirmentFile(String fileName,
			DecorationList decorationList,EquipmentList equipmentList,
			SetBonusList setBouns,DecorationList includedSkill,EquipmentList includedEquipmentList) {
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
				//System.out.println(currentLine);
				if(currentFlag!=DEFAULT_READ_FLAG)
					readFlag = currentFlag;
				else{
					String[] stringBlock = currentLine.split(",");
					if(readFlag==SET_BONUS_READ_FLAG)
						setBouns.add(stringBlock[0], Integer.parseInt(stringBlock[1]));
					else if(readFlag==SKILL_REQUIREMENT_READ_FLAG){
						boolean found = false;
						for(SkillRequirement decorNow:decorationList) {
							String skillNow = stringBlock[0];
							int skillLevelNow = Integer.parseInt(stringBlock[1]);
							if(skillNow.contentEquals(decorNow.skillName) && (skillLevelNow>=1)) {
								decorNow.setRequired(skillLevelNow);
								includedSkill.add(decorNow);
								found = true;
								break;
							}
						}

						if(!found)
							System.out.println("警告：找不到技能-" + stringBlock[0]);
					}
					else{
						// stringBlock = {礦石鎧甲α,礦石鎧甲β}
						for(int stringNow=0;stringNow<=stringBlock.length-1;stringNow++) {
							boolean found = false;
							List<Equipment> currentEquipmentList = equipmentList.get(readFlag);
							for(Equipment equipmentNow:currentEquipmentList)
								if(stringBlock[stringNow].contentEquals(equipmentNow.equipmentName)) {
									includedEquipmentList.get(readFlag).add(equipmentNow);
									found = true;
									break;
								}
							if(!found)
								System.out.println("警告：找不到裝備-" + stringBlock[0]);
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
