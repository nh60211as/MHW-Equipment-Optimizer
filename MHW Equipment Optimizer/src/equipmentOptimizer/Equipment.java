package equipmentOptimizer;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class Equipment {
	String equipmentName;
	int defense;
	int fireDef;
	int waterDef;
	int thunderDef;
	int iceDef;
	int dragonDef;
	int decor3;
	int decor2;
	int decor1;
	int totalDecor;
	String setBonus;
	Map<String,Integer> skill;

	private boolean isReplaceable;
	private int combinedDecor3;
	private int combinedDecor2;
	private int combinedDecor1;
	private int totalCombinedDecor;

	public Equipment(String input){
		// 巨蜂頭盔α;76;-2,+1,+1,+1,+2;0,0,0;(無),收刀術,1,麻痺屬性強化,1
		String[] stringBlock = input.split(";");

		// 巨蜂頭盔α
		equipmentName = stringBlock[0];

		// 76
		defense = Integer.parseInt(stringBlock[1]);

		// -2,+1,+1,+1,+2
		String[] elementalDefBlock = stringBlock[2].split(",");
		fireDef    = Integer.parseInt(elementalDefBlock[0]);
		waterDef   = Integer.parseInt(elementalDefBlock[1]);
		thunderDef = Integer.parseInt(elementalDefBlock[2]);
		iceDef     = Integer.parseInt(elementalDefBlock[3]);
		dragonDef  = Integer.parseInt(elementalDefBlock[4]);

		// 0,0,0
		String[] decorBlock = stringBlock[3].split(",");
		decor3 = Integer.parseInt(decorBlock[0]);
		decor2 = Integer.parseInt(decorBlock[1]);
		decor1 = Integer.parseInt(decorBlock[2]);
		totalDecor = decor3+decor2+decor1;

		combinedDecor3 = decor3;
		combinedDecor2 = decor2;
		combinedDecor1 = decor1;

		// (無),收刀術,1,麻痺屬性強化,1
		String[] skillBlock = stringBlock[4].split(",");
		// (無)
		setBonus = skillBlock[0];

		skill = new HashMap<String,Integer>();
		for(int i=1;i<=skillBlock.length-1;i+=2)
			this.skill.put(skillBlock[i], Integer.parseInt(skillBlock[i+1]));

		isReplaceable = true;
	}

	public void setIsReplaceable(List<SkillRequirement> skillRequirement, String setName) {
		for(int skillNow=0;skillNow<=skillRequirement.size()-1;skillNow++)
			if(skill.containsKey(skillRequirement.get(skillNow).skillName)) {
				switch(skillRequirement.get(skillNow).levelOfDecor) {
				case 1:
					combinedDecor1 += skill.get(skillRequirement.get(skillNow).skillName);
					break;
				case 2:
					combinedDecor2 += skill.get(skillRequirement.get(skillNow).skillName);
					break;
				case 3:
					combinedDecor3 += skill.get(skillRequirement.get(skillNow).skillName);
					break;
				default:
					break;
				}

				if(!skillRequirement.get(skillNow).isReplaceable)
					isReplaceable = false;
			}
		if(setBonus.contentEquals(setName) && !setBonus.contentEquals("(無)"))
			isReplaceable = false;
		totalCombinedDecor = combinedDecor3+combinedDecor2+combinedDecor1;
	}

	public boolean isReplaceable() {
		return this.isReplaceable;
	}

	public int isBetter(Equipment anotherEquipment) {
		int score[] = {0,0,0}; // defense, 實際裝飾珠, 總和裝飾珠

		if(this.defense>anotherEquipment.defense)
			score[0] = 1;
		else if(this.defense==anotherEquipment.defense)
			score[0] = 0;
		else if(this.defense<anotherEquipment.defense)
			score[0] = -1;

		if(this.totalDecor==anotherEquipment.totalDecor)
			score[1] = isBetterWhenSameDecro(this,anotherEquipment);

		if(this.totalCombinedDecor==anotherEquipment.totalCombinedDecor)
			score[2] = isBetterWhenSameDecro(this,anotherEquipment);

		int thisIsBetterFlag = score[0];
		for(int i=1;i<=score.length-1;i++) {
			if(thisIsBetterFlag==1)
				if(score[i]==-1)
					return 0;
			else if(thisIsBetterFlag==0)
				thisIsBetterFlag = score[i];
			else if(thisIsBetterFlag==-1)
				if(score[i]==1)
					return 0;
		}

		return thisIsBetterFlag;
	}

	private static int isBetterWhenSameDecro(Equipment e1, Equipment e2) {
		int e1Score = e1.decor3*100+e1.decor2*10+e1.decor1;
		int e2Score = e2.decor3*100+e2.decor2*10+e2.decor1;

		if(e1Score>e2Score)
			return 1;
		else if(e1Score==e2Score)
			return 0;
		else
			return -1;
	}
}
