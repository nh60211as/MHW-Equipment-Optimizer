package equipmentOptimizer;

abstract class Equipment extends Item {
	int defense;

	int decor4;
	int decor3;
	int decor2;
	int decor1;
	int totalDecor;
	int maxDecorLevel;

	int combinedDecor4;
	int combinedDecor3;
	int combinedDecor2;
	int combinedDecor1;
	int totalCombinedDecor;

	Equipment() {
	}

	void setDecor(String[] decorBlock) {
		decor4 = Integer.parseInt(decorBlock[0]);
		decor3 = Integer.parseInt(decorBlock[1]);
		decor2 = Integer.parseInt(decorBlock[2]);
		decor1 = Integer.parseInt(decorBlock[3]);
		totalDecor = decor4 + decor3 + decor2 + decor1;

		combinedDecor4 = decor4;
		combinedDecor3 = decor3;
		combinedDecor2 = decor2;
		combinedDecor1 = decor1;
		totalCombinedDecor = combinedDecor4 + combinedDecor3 + combinedDecor2 + combinedDecor1;
	}

	void setDecorToDefault() {
		decor4 = 0;
		decor3 = 0;
		decor2 = 0;
		decor1 = 0;
		totalDecor = decor4 + decor3 + decor2 + decor1;

		combinedDecor4 = decor4;
		combinedDecor3 = decor3;
		combinedDecor2 = decor2;
		combinedDecor1 = decor1;
		totalCombinedDecor = combinedDecor4 + combinedDecor3 + combinedDecor2 + combinedDecor1;
	}
}
