package equipmentOptimizer;

import java.util.Map;
import java.util.HashMap;

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
	String setBonus;
	Map<String,Integer> skill;

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
		
		// (無),收刀術,1,麻痺屬性強化,1
		String[] skillBlock = stringBlock[4].split(",");
		// (無)
		setBonus = skillBlock[0];
		
		skill = new HashMap<String,Integer>();
		for(int i=1;i<=skillBlock.length-1;i+=2)
			this.skill.put(skillBlock[i], Integer.parseInt(skillBlock[i+1]));
	}
}
