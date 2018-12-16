package equipmentOptimizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class EquipmentOptimizer {

	// 需求技能的資料
	static String setName;
	static int setSize;
	static List<SkillRequirement> skillRequirement;

	// 所有裝備的資料
	static ArrayList<ArrayList<Equipment>> equipmentList;

	// 包含裝備的資料
	static ArrayList<ArrayList<Equipment>> includedEquipmentList;

	public static void main(String[] args) {

		equipmentList = new ArrayList<ArrayList<Equipment> >();
		for(int i=1;i<=7;i++)
			equipmentList.add(new ArrayList<Equipment>());

		setName = ""; //setName在readRequirmentFile(requirementFileName);執行後應該為"(無)"或是系列技能名稱
		setSize = Integer.MAX_VALUE;
		skillRequirement = new ArrayList<SkillRequirement>();

		includedEquipmentList = new ArrayList<ArrayList<Equipment> >();
		for(int i=1;i<=7;i++)
			includedEquipmentList.add(new ArrayList<Equipment>());

		String equipmentFileDirectory = "裝備檔案/";
		String[] equipmentFileName = {"_武器.txt", "_頭.txt", "_身.txt", "_腕.txt", "_腰.txt", "_腳.txt", "_護石.txt"};
		for(String stringNow:equipmentFileName)
			readEquipmentFile(equipmentFileDirectory+stringNow);

		String requirementFileName = args[0];
		readRequirmentFile(requirementFileName);

		for(int bodyPartNow=0;bodyPartNow<=includedEquipmentList.size()-1;bodyPartNow++) {
			if(includedEquipmentList.get(bodyPartNow).size()==0) {
				if(bodyPartNow==0) { //若裝備為武器
					System.out.println("未指定武器");
					return;
				}
				else if(bodyPartNow==6) { //若裝備為護石
					for(Equipment equipmentNow:equipmentList.get(bodyPartNow))
						for(SkillRequirement skillNow:skillRequirement)
							if(equipmentNow.skill.containsKey(skillNow.skillName)) {
								if(!includedEquipmentList.get(bodyPartNow).contains(equipmentNow))
									includedEquipmentList.get(bodyPartNow).add(equipmentNow);
							}
				}
				else { //若為頭、身、腕、腰、腳
					for(Equipment equipmentNow:equipmentList.get(bodyPartNow)) {
						equipmentNow.setIsReplaceable(skillRequirement,setName);
						if(!equipmentNow.isReplaceable())
							includedEquipmentList.get(bodyPartNow).add(equipmentNow);
						else {
							for(Equipment includedEquipmentNow:includedEquipmentList.get(bodyPartNow)) {
								if(includedEquipmentNow.isReplaceable()) {
									int isEquipmentNowBetter = equipmentNow.isBetter(includedEquipmentNow);
									switch(isEquipmentNowBetter) {
									case 1:
										break;
									case 0:
										includedEquipmentList.get(bodyPartNow).add(equipmentNow);
										break;
									case -1:
										includedEquipmentList.get(bodyPartNow).remove(includedEquipmentNow);
										includedEquipmentList.get(bodyPartNow).add(equipmentNow);
										break;
									default:
										break;
									}
								}

							}
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

		//開始配對裝備
		for(Equipment e1:includedEquipmentList.get(0))
			for(Equipment e2:includedEquipmentList.get(1))
				for(Equipment e3:includedEquipmentList.get(2))
					for(Equipment e4:includedEquipmentList.get(3))
						for(Equipment e5:includedEquipmentList.get(4))
							for(Equipment e6:includedEquipmentList.get(5))
								for(Equipment e7:includedEquipmentList.get(6)){
									List<Equipment> currentEquipment = new ArrayList<Equipment>();
									//e1=0;e2=1;e3=3;e4=1;e5=0;e6=0;e7=0;
									currentEquipment.add(0, e1);
									currentEquipment.add(1, e2);
									currentEquipment.add(2, e3);
									currentEquipment.add(3, e4);
									currentEquipment.add(4, e5);
									currentEquipment.add(5, e6);
									currentEquipment.add(6, e7);

									int defense = 0;
									int elementalDef[] = new int[5];
									int isSetSize = 0;
									int numberOfHole[] = new int[4];
									int skillHave[] = new int[skillRequirement.size()];
									int skillNeed[] = new int[skillRequirement.size()];
									for(int i=0;i<=skillNeed.length-1;i++)
										skillNeed[i] = skillRequirement.get(i).required;

									for(int i=0;i<=currentEquipment.size()-1;i++){
										defense += currentEquipment.get(i).defense;
										elementalDef[0] += currentEquipment.get(i).fireDef;
										elementalDef[1] += currentEquipment.get(i).waterDef;
										elementalDef[2] += currentEquipment.get(i).thunderDef;
										elementalDef[3] += currentEquipment.get(i).iceDef;
										elementalDef[4] += currentEquipment.get(i).dragonDef;

										isSetSize += (currentEquipment.get(i).setBonus.contentEquals(setName)) ? 1: 0;
										numberOfHole[3] += currentEquipment.get(i).decor3;
										numberOfHole[2] += currentEquipment.get(i).decor2;
										numberOfHole[1] += currentEquipment.get(i).decor1;

										Map<String,Integer> skill = currentEquipment.get(i).skill;
										for(int j=0;j<=skillRequirement.size()-1;j++){
											String currentSkillName = skillRequirement.get(j).skillName;
											if(skill.containsKey(currentSkillName))
												skillHave[j] += skill.get(currentSkillName);
										}
									}

									boolean success = true;
									//檢查是否為套裝
									if(isSetSize<setSize)
										success = false;

									for(int i=0;i<=skillNeed.length-1;i++){
										int gemNeed = skillNeed[i]-skillHave[i];
										if(gemNeed>=1)
											if(gemNeed>skillRequirement.get(i).owned) {
												success = false;
												break;
											}
									}

									int numberOfHoleNeed[] = new int[4];
									for(int i=0;i<=skillNeed.length-1;i++){
										int temp = skillNeed[i]-skillHave[i];
										if(temp>=1)
											numberOfHoleNeed[skillRequirement.get(i).levelOfDecor] += temp;
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
									for(int i=0;i<=currentEquipment.size()-1;i++)
										System.out.print(currentEquipment.get(i).equipmentName + ",");
									System.out.println();
									System.out.print("防禦力： " + defense + ", ");
									System.out.print("屬性抗性：  ");
									for(int i=0;i<=elementalDef.length-1;i++) {
										if(elementalDef[i]>=0)
											System.out.print("+" + elementalDef[i] + ",");
										else
											System.out.print(elementalDef[i] + ",");
									}
									System.out.println(" 剩餘裝飾品數： " + numberOfHole[0]);
								}

	}

	private static void readEquipmentFile(String fileName) {
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);

			//開始閱讀檔案
			int readFlag = -2;
			int currentFlag = -2;
			String currentLine = "";

			while ((currentLine = br.readLine()) != null) {
				if(currentLine.length()==0)
					continue;
				if(currentLine.substring(0, 1).contentEquals("#"))
					continue;

				currentFlag = changeReadFlag(currentLine); // currentFlag 只能為-2或是0~6
				//System.out.println(currentLine);
				if(currentFlag!=-2)
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
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static void readRequirmentFile(String fileName) {
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);

			//開始閱讀檔案
			int readFlag = -2;
			int currentFlag = -2;
			String currentLine = "";

			while ((currentLine = br.readLine()) != null) {
				if(currentLine.length()==0)
					continue;
				if(currentLine.substring(0, 1).contentEquals("#"))
					continue;

				currentFlag = changeReadFlag(currentLine);
				//System.out.println(currentLine);
				if(currentFlag!=-2)
					readFlag = currentFlag;
				else{
					String[] stringBlock = currentLine.split(",");
					if(readFlag==-1) {
						if(stringBlock.length==2) {
							setName = stringBlock[0];
							setSize = Integer.parseInt(stringBlock[1]);
						}
						else
							skillRequirement.add(new SkillRequirement(stringBlock));
					}
					else{
						for(int equipmentNow=0;equipmentNow<=stringBlock.length-1;equipmentNow++) {
							List<Equipment> currentEquipmentList = equipmentList.get(readFlag);
							//System.out.println(currentList.size() + " " + currentList);
							for(int i=0;i<=currentEquipmentList.size()-1;i++)
								if(stringBlock[equipmentNow].contentEquals(currentEquipmentList.get(i).equipmentName)) {
									includedEquipmentList.get(readFlag).add(currentEquipmentList.get(i));
									break;
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
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static int changeReadFlag(String currentLine){
		if(currentLine.equals("需求：")) return -1;
		else if(currentLine.equals("武器：")) return 0;
		else if(currentLine.equals("頭：")) return 1;
		else if(currentLine.equals("身：")) return 2;
		else if(currentLine.equals("腕：")) return 3;
		else if(currentLine.equals("腰：")) return 4;
		else if(currentLine.equals("腳：")) return 5;
		else if(currentLine.equals("護石：")) return 6;

		return -2;
	}
}
