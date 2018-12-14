package equipmentOptimizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class EquipmentOptimizer {

	static String setName;
	static int setSize;
	static List<SkillRequirment> requirments;
	static List<ArrayList<Equipment>> equipmentList;

	public static void main(String[] args) {
		setName = "";
		setSize = Integer.MAX_VALUE;
		requirments = new ArrayList<SkillRequirment>();
		equipmentList = new ArrayList<ArrayList<Equipment> >();
		for(int i=1;i<=7;i++)
			equipmentList.add(new ArrayList<Equipment>());

		String requirmentFileName = args[0];
		readFile(requirmentFileName);

		//		String[] equipmentFileName = {"_武器.txt", "_頭.txt", "_身.txt", "_腕.txt", "_腰.txt", "_腳.txt", "_護石.txt"};
		//		for(int i=0;i<=equipmentFileName.length-1;i++)
		//			readFile(equipmentFileName[i]);

		//		System.out.println(requirments.size());
		//		for(int i=0;i<=equipmentLists.size()-1;i++)
		//			System.out.println(equipmentLists.get(i).size());

		//開始配對裝備
		for(int e1=0;e1<=equipmentList.get(0).size()-1;e1++)
			for(int e2=0;e2<=equipmentList.get(1).size()-1;e2++)
				for(int e3=0;e3<=equipmentList.get(2).size()-1;e3++)
					for(int e4=0;e4<=equipmentList.get(3).size()-1;e4++)
						for(int e5=0;e5<=equipmentList.get(4).size()-1;e5++)
							for(int e6=0;e6<=equipmentList.get(5).size()-1;e6++)
								for(int e7=0;e7<=equipmentList.get(6).size()-1;e7++){
									List<Equipment> currentEquipment = new ArrayList<Equipment>();
									//e1=0;e2=1;e3=3;e4=1;e5=0;e6=0;e7=0;
									currentEquipment.add(0, equipmentList.get(0).get(e1));
									currentEquipment.add(1, equipmentList.get(1).get(e2));
									currentEquipment.add(2, equipmentList.get(2).get(e3));
									currentEquipment.add(3, equipmentList.get(3).get(e4));
									currentEquipment.add(4, equipmentList.get(4).get(e5));
									currentEquipment.add(5, equipmentList.get(5).get(e6));
									currentEquipment.add(6, equipmentList.get(6).get(e7));

									int defense = 0;
									int elementalDef[] = new int[5];
									int isSetSize = 0;
									int numberOfHole[] = new int[4];
									int skillHave[] = new int[requirments.size()];
									int skillNeed[] = new int[requirments.size()];
									for(int i=0;i<=skillNeed.length-1;i++)
										skillNeed[i] = requirments.get(i).required;

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
										for(int j=0;j<=requirments.size()-1;j++){
											String currentSkillName = requirments.get(j).skillName;
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
											if(gemNeed>requirments.get(i).owned) {
												success = false;
												break;
											}
									}

									int numberOfHoleNeed[] = new int[4];
									for(int i=0;i<=skillNeed.length-1;i++){
										int temp = skillNeed[i]-skillHave[i];
										if(temp>=1)
											numberOfHoleNeed[requirments.get(i).levelOfDecor] += temp;
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

	private static void readFile(String fileName) {
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
				//System.out.println("currentFlag " + currentFlag);
				//System.out.println(currentLine);
				if(currentFlag!=-2)
					readFlag = currentFlag;
				else{
					//System.out.println("readFlag " + readFlag);
					if(readFlag==-1) {
						String[] stringBlock = currentLine.split(",");
						if(stringBlock.length==2) {
							setName = stringBlock[0];
							setSize = Integer.parseInt(stringBlock[1]);
						}
						else
						requirments.add(new SkillRequirment(stringBlock));
					}
					else{
						ArrayList<Equipment> currentList = equipmentList.get(readFlag);
						//System.out.println(currentList.size() + " " + currentList);
						currentList.add(new Equipment(currentLine));
						equipmentList.set(readFlag, currentList);
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
