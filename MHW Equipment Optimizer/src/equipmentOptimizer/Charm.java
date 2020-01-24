package equipmentOptimizer;

public class Charm extends Equipment {
	final int[] elementalResistance;
	final String setBonus;

	Charm(final String input, final SkillHashMap skillHashMap) {
		// 採集鐵人護石;0;+0,+0,+0,+0,+0;0,0,0,0;(無);採集達人,1,剝取鐵人,1
		String[] stringBlock = input.split(";");

		// 採集鐵人護石
		name = stringBlock[0];

		// 0
		defense = Integer.parseInt(stringBlock[1]);

		// +0,+0,+0,+0,+0
		String[] elementalDefBlock = stringBlock[2].split(",");
		elementalResistance = new int[5];
		for (int i = 0; i <= elementalResistance.length - 1; i++)
			elementalResistance[i] = Integer.parseInt(elementalDefBlock[i]);

		// 0,0,0,0
		setDecorToDefault();

		// (無);採集達人,1,剝取鐵人,1
		String[] skillBlock = stringBlock[4].split(",");
		// (無)
		if (skillBlock[0].contentEquals("(無)"))
			setBonus = "";
		else
			setBonus = skillBlock[0];
		// 採集達人,1,剝取鐵人,1
		skills = new ItemSkillList(stringBlock[5], skillHashMap);
	}
}
