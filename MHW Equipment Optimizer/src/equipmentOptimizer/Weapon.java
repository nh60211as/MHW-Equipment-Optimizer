package equipmentOptimizer;

public class Weapon extends Equipment implements Cloneable {
	private int rawAttack;
	private final int sharpness;
	private double affinity;

	private final ElementalDamageList elementalDamageList;

	Weapon(final String input, final SkillHashMap skillHashMap) {
		// 冰炎劍維爾瑪閃焰;180,0,0.10;冰,240,否,爆,240,否;0;0,0,1,0;加速再生,1
		String[] stringBlock = input.split(";", -1);

		// 冰炎劍維爾瑪閃焰
		name = stringBlock[0];

		// 180,0,0.10
		String[] attackBlock = stringBlock[1].split(",");
		rawAttack = Integer.parseInt(attackBlock[0]);
		sharpness = Integer.parseInt(attackBlock[1]);
		affinity = Double.parseDouble(attackBlock[2]);

		// 冰,240,否,爆,240,否
		String[] elementalDamageBlock = stringBlock[2].split(",");

		elementalDamageList = new ElementalDamageList();
		for (int i = 0; i <= elementalDamageBlock.length - 1; i += 3) {
			elementalDamageList.add(elementalDamageBlock[i], elementalDamageBlock[i + 1], elementalDamageBlock[i + 2]);
		}

		// 0
		defense = Integer.parseInt(stringBlock[3]);

		// 0,0,1,0
		String[] decorBlock = stringBlock[4].split(",");
		setDecor(decorBlock);

		// 加速再生,1
		skills = new ItemSkillList(stringBlock[5], skillHashMap);
	}

	Weapon() {
		name = "(未指定武器)";

		rawAttack = 0;
		sharpness = 0;
		affinity = 0.00;

		elementalDamageList = new ElementalDamageList();

		defense = 0;

		setDecorToDefault();

		skills = new ItemSkillList();
	}

	void setBoost(int attackBoost, int affinityBoost, int defenseBoost, int decorationSlotBoost, int leechBoost) {
		if (attackBoost == 0 && affinityBoost == 0 && defenseBoost == 0 && decorationSlotBoost == 0 && leechBoost == 0)
			return;

		name += "(";
		name += (attackBoost >= 1) ? "攻擊強化" + attackBoost + "," : "";
		name += (affinityBoost >= 1) ? "會心率強化" + affinityBoost + "," : "";
		name += (defenseBoost >= 1) ? "防禦強化" + defenseBoost + "," : "";
		name += (decorationSlotBoost >= 1) ? "鑲嵌槽強化" + decorationSlotBoost + "," : "";
		name += (leechBoost >= 1) ? "吸血強化" + leechBoost + "," : "";
		name = name.substring(0, name.length() - 1);
		name += ")";

		rawAttack += 5 * attackBoost;

		switch (affinityBoost) {
			case 0:
				break;
			case 1:
				affinity += 0.1;
				break;
			case 2:
				affinity += 0.15;
				break;
			case 3:
				affinity += 0.2;
				break;
			default:
				break;
		}

		defense += 10 * defenseBoost;

		switch (decorationSlotBoost) {
			case 0:
				break;
			case 1:
				decor1 += 1;
				break;
			case 2:
				decor2 += 1;
				break;
			case 3:
				decor3 += 1;
				break;
			case 4:
				decor4 += 1;
				break;
			default:
				break;
		}
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
