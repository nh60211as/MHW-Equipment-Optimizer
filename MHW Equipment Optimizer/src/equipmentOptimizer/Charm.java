package equipmentOptimizer;

public class Charm extends Equipment {
	final int[] elementalResistance;
	final String setBonus;

	Charm(String input) {
		// 採集鐵人護石;0;+0,+0,+0,+0,+0;0,0,0;(無),採集達人,1,剝取鐵人,1
		String[] stringBlock = input.split(";");

		// 採集鐵人護石
		equipmentName = stringBlock[0];

		// 0
		defense = Integer.parseInt(stringBlock[1]);

		// +0,+0,+0,+0,+0
		String[] elementalDefBlock = stringBlock[2].split(",");
		elementalResistance = new int[5];
		for (int i = 0; i <= elementalResistance.length - 1; i++)
			elementalResistance[i] = Integer.parseInt(elementalDefBlock[i]);

		// 0,0,0
		String[] decorBlock = stringBlock[3].split(",");
		decor3 = Integer.parseInt(decorBlock[0]);
		decor2 = Integer.parseInt(decorBlock[1]);
		decor1 = Integer.parseInt(decorBlock[2]);
		totalDecor = decor3 + decor2 + decor1;

		combinedDecor3 = decor3;
		combinedDecor2 = decor2;
		combinedDecor1 = decor1;
		totalCombinedDecor = combinedDecor3 + combinedDecor2 + combinedDecor1;

		// (無),採集達人,1,剝取鐵人,1
		String[] skillBlock = stringBlock[4].split(",");
		// (無)
		if (skillBlock[0].contentEquals("(無)"))
			setBonus = "";
		else
			setBonus = skillBlock[0];

		skillList = new EquipmentSkillList();
		for (int i = 1; i <= skillBlock.length - 1; i += 2)
			skillList.add(skillBlock[i], Integer.parseInt(skillBlock[i + 1]));

		isReplaceable = true;
	}
}
