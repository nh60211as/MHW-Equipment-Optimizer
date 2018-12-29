package equipmentOptimizer;

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
	int maxDecorLevel;
	int totalDecor;

	String setBonus;
	SkillList skillList;

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

		skillList = new SkillList();
		for(int i=1;i<=skillBlock.length-1;i+=2)
			skillList.add(skillBlock[i], Integer.parseInt(skillBlock[i+1]));

		isReplaceable = true;
	}

	public void setIsReplaceable(DecorationList skillRequirement, SetBonusList setBonusList) {
		for(Decoration skillNow:skillRequirement) {
			int indexOfSkill = skillList.indexOf(skillNow.skillName);
			if(indexOfSkill!=-1) {
				int skillLevel = skillList.getSkillLevel(skillNow.skillName);
				switch(skillNow.levelOfDecor) {
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
				if(!skillNow.isReplaceable)
					isReplaceable = false;
			}
		}
		if(setBonusList.contains(setBonus) && !setBonus.contentEquals("(無)"))
			isReplaceable = false;

		totalCombinedDecor = combinedDecor3+combinedDecor2+combinedDecor1;
	}

	public boolean isReplaceable() {
		return this.isReplaceable;
	}

	public int isBetter(Equipment anotherEquipment) {
		int score[] = {0,0}; // 實際裝飾珠, 總和裝飾珠
		
		int compareValue = isBetterWhenSameDecro(this,anotherEquipment);
		if(this.totalDecor>anotherEquipment.totalDecor) { // {0,0,3} , {1,0,0}
			if(compareValue==-1) // {0,0,3} , {1,0,0}
				score[0] = 0;
			else // {0,2,0} , {0,1,0}
				score[0] = 1;
		}
		else if(this.totalDecor==anotherEquipment.totalDecor) // {0,0,3} , {1,1,1}
			score[0] = compareValue;
		else if(this.totalDecor<anotherEquipment.totalDecor) { // {1,0,0} , {0,0,3}
			if(compareValue==1) // {1,0,0} , {0,0,3}
				score[0] = 0;
			else // {0,1,0} , {0,2,0}
				score[0] = -1;
		}

		compareValue = isBetterWhenSameCombinedDecro(this,anotherEquipment);
		if(this.totalCombinedDecor>anotherEquipment.totalCombinedDecor) { // {0,0,3} , {1,0,0}
			if(compareValue==-1) // {0,0,3} , {1,0,0}
				score[1] = 0;
			else // {0,2,0} , {0,1,0}
				score[1] = 1;
		}
		else if(this.totalCombinedDecor==anotherEquipment.totalCombinedDecor)
			score[1] = compareValue;
		else if(this.totalCombinedDecor<anotherEquipment.totalCombinedDecor) { // {1,0,0} , {0,0,3}
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
		
		if((sum==score.length) && (this.defense>=anotherEquipment.defense))
			return 1;
		else if((sum==-score.length) && (this.defense<=anotherEquipment.defense))
			return -1;
		else
			return 0;
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

	private static int isBetterWhenSameCombinedDecro(Equipment e1, Equipment e2) {
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
