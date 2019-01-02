package equipmentOptimizer;

public class Armor extends Equipment{
	int fireDef;
	int waterDef;
	int thunderDef;
	int iceDef;
	int dragonDef;
	
	String setBonus;
	
	
	public Armor(String input) {
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

		skillList = new EquipmentSkillList();
		for(int i=1;i<=skillBlock.length-1;i+=2)
			skillList.add(skillBlock[i], Integer.parseInt(skillBlock[i+1]));

		isReplaceable = true;
	}
	
	public void setIsReplaceable(SkillList includedSkill, SetBonusList setBonusList) {
		for(Skill includedSkillNow:includedSkill) {
			int indexOfSkill = skillList.indexOf(includedSkillNow.skillName);
			if(indexOfSkill!=-1) {
				int skillLevel = skillList.getSkillLevel(includedSkillNow.skillName);
				switch(includedSkillNow.levelOfDecor) {
				case 1:
					combinedDecor1 += skillLevel;
					break;
				case 2:
					combinedDecor2 += skillLevel;
					break;
				case 3:
					combinedDecor3 += skillLevel;
					break;
				default:
					break;
				}
				if(!includedSkillNow.isReplaceable)
					isReplaceable = false;
			}
		}
		if(setBonusList.contains(setBonus) && !setBonus.contentEquals("(無)"))
			isReplaceable = false;

		totalCombinedDecor = combinedDecor3+combinedDecor2+combinedDecor1;
	}
	
	public int isBetter(Armor anotherArmor) {
		int score[] = {0,0}; // 實際裝飾珠, 總和裝飾珠
		
		int compareValue = isBetterWhenSameDecro(this,anotherArmor);
		if(this.totalDecor>anotherArmor.totalDecor) { // {0,0,3} , {1,0,0}
			if(compareValue==-1) // {0,0,3} , {1,0,0}
				score[0] = 0;
			else // {0,2,0} , {0,1,0}
				score[0] = 1;
		}
		else if(this.totalDecor==anotherArmor.totalDecor) // {0,0,3} , {1,1,1}
			score[0] = compareValue;
		else if(this.totalDecor<anotherArmor.totalDecor) { // {1,0,0} , {0,0,3}
			if(compareValue==1) // {1,0,0} , {0,0,3}
				score[0] = 0;
			else // {0,1,0} , {0,2,0}
				score[0] = -1;
		}

		compareValue = isBetterWhenSameCombinedDecro(this,anotherArmor);
		if(this.totalCombinedDecor>anotherArmor.totalCombinedDecor) { // {0,0,3} , {1,0,0}
			if(compareValue==-1) // {0,0,3} , {1,0,0}
				score[1] = 0;
			else // {0,2,0} , {0,1,0}
				score[1] = 1;
		}
		else if(this.totalCombinedDecor==anotherArmor.totalCombinedDecor)
			score[1] = compareValue;
		else if(this.totalCombinedDecor<anotherArmor.totalCombinedDecor) { // {1,0,0} , {0,0,3}
			if(compareValue==1) // {1,0,0} , {0,0,3}
				score[1] = 0;
			else // {0,1,0} , {0,2,0}
				score[1] = -1;
		}
		//System.out.println(score[0] + " " + score[1]);

		int sum = 0;
		for(int i=0;i<=score.length-1;i++) {
			sum+=score[i];
		}
		
		//System.out.println((sum==score.length) + " " + (this.defense>=anotherEquipment.defense));
		//System.out.println((sum==-score.length) + " " + (this.defense<=anotherEquipment.defense));
		
		if((sum==score.length) && (this.defense>=anotherArmor.defense))
			return 1;
		else if((sum==-score.length) && (this.defense<=anotherArmor.defense))
			return -1;
		else
			return 0;
	}

	private static int isBetterWhenSameDecro(Armor e1, Armor e2) {
		int e1Score = e1.decor3*100+e1.decor2*10+e1.decor1;
		int e2Score = e2.decor3*100+e2.decor2*10+e2.decor1;

		if(e1Score>e2Score)
			return 1;
		else if(e1Score==e2Score)
			return 0;
		else
			return -1;
	}

	private static int isBetterWhenSameCombinedDecro(Armor e1, Armor e2) {
		int e1Score = e1.combinedDecor3*100+e1.combinedDecor2*10+e1.combinedDecor1;
		int e2Score = e2.combinedDecor3*100+e2.combinedDecor2*10+e2.combinedDecor1;

		if(e1Score>e2Score)
			return 1;
		else if(e1Score==e2Score)
			return 0;
		else
			return -1;
	}
}
