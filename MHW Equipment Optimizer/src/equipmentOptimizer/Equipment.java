package equipmentOptimizer;

abstract class Equipment {
	String equipmentName;
	int defense;

	int decor3;
	int decor2;
	int decor1;
	int totalDecor;
	int maxDecorLevel;

	int combinedDecor3;
	int combinedDecor2;
	int combinedDecor1;
	int totalCombinedDecor;

	EquipmentSkillList skillList;

	boolean isReplaceable;

	Equipment() {
	}
}
