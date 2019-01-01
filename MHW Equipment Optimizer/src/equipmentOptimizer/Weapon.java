package equipmentOptimizer;

public class Weapon extends Equipment{
	int rawAttack;
	int sharpness;
	double affinity;
	
	ElementalDamageList elementalDamageList;
	
	public Weapon(String input) {
		// 冰炎劍維爾瑪閃焰;180,0,0.10;冰,240,否,爆,240,否;0;0,1,0;加速再生,1
		String[] stringBlock = input.split(";");

		// 冰炎劍維爾瑪閃焰
		equipmentName = stringBlock[0];

		// 180,0,0.10
		String[] attackBlock = stringBlock[1].split(",");
		rawAttack = Integer.parseInt(attackBlock[0]);
		sharpness = Integer.parseInt(attackBlock[1]);
		affinity = Double.parseDouble(attackBlock[2]);

		// 冰,240,否,爆,240,否
		String[] elementalDamageBlock = stringBlock[2].split(",");
		
		elementalDamageList = new ElementalDamageList();
		for(int i=0;i<=elementalDamageBlock.length-1;i+=3) {
			elementalDamageList.add(elementalDamageBlock[i+0], elementalDamageBlock[i+1], elementalDamageBlock[i+2]);
		}

		// 0
		defense = Integer.parseInt(stringBlock[3]);
		
		// 0,1,0
		String[] decorBlock = stringBlock[4].split(",");
		decor3 = Integer.parseInt(decorBlock[0]);
		decor2 = Integer.parseInt(decorBlock[1]);
		decor1 = Integer.parseInt(decorBlock[2]);
		totalDecor = decor3+decor2+decor1;
		
		// 加速再生,1
		String[] skillBlock = stringBlock[5].split(",");

		skillList = new SkillList();
		for(int i=0;i<=skillBlock.length-1;i+=2)
			skillList.add(skillBlock[i+0], Integer.parseInt(skillBlock[i+1]));

		isReplaceable = true;
	}
}
