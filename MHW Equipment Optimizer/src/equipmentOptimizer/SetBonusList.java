package equipmentOptimizer;

import java.util.ArrayList;
import java.util.List;

class SetBonusList {
	private List<String> _setBonusName;
	private List<Integer> _setBonusRequirement;

	SetBonusList() {
		_setBonusName = new ArrayList<>();
		_setBonusRequirement = new ArrayList<>();
	}

	void add(String setBonusName, int setBonusRequirement) {
		_setBonusName.add(setBonusName);
		_setBonusRequirement.add(setBonusRequirement);
	}

	void plus1(String setBonusName) {
		if (!setBonusName.isEmpty()) {
			int indexOfSetBonus = indexOf(setBonusName);
			if (indexOfSetBonus == -1) {
				_setBonusName.add(setBonusName);
				_setBonusRequirement.add(1);
			} else
				_setBonusRequirement.set(indexOfSetBonus, _setBonusRequirement.get(indexOfSetBonus) + 1);
		}
	}

	private int indexOf(String setBonusName) {
		return _setBonusName.indexOf(setBonusName);
	}

	boolean contains(String setBonusName) {
		return _setBonusName.contains(setBonusName);
	}

	private int getSetBonusRequirement(String setBonusName) {
		int indexOfSetBonus = indexOf(setBonusName);
		return (indexOfSetBonus != -1) ? _setBonusRequirement.get(indexOfSetBonus) : 0;
	}

	int size() {
		return _setBonusName.size();
	}

	boolean checkSetBonus(SetBonusList checkedSetBonusList) {
		for (int i = 0; i <= _setBonusName.size() - 1; i++) {
			String currentSetBonusName = this._setBonusName.get(i);
			int currentSetBonusRequirement = this._setBonusRequirement.get(i);

			if (!checkedSetBonusList.contains(currentSetBonusName)) {
				return false;
			} else {
				if (checkedSetBonusList.getSetBonusRequirement(currentSetBonusName) < currentSetBonusRequirement) {
					return false;
				}
			}
		}
		return true;
	}
}
