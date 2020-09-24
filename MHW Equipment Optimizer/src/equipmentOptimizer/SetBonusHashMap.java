package equipmentOptimizer;

import java.util.HashMap;

// key: set bonus name, value: level
class SetBonusHashMap extends HashMap<String, Integer> {

	SetBonusHashMap() {
	}

	boolean add(String setBonusName, int setBonusRequirement) {
		if (!setBonusName.isEmpty())
			return false;

		this.put(setBonusName, setBonusRequirement);
		return true;
	}

	boolean plus1(String setBonusName) {
		if (!setBonusName.isEmpty()) {
			Integer setBonusLevel = this.get(setBonusName);
			if (setBonusLevel == null)
				this.put(setBonusName, 1);
			else {
				setBonusLevel++;
			}
			return true;
		} else
			return false;
	}

	boolean isSubset(SetBonusHashMap superSet) {
		// if this SetBonusHashMap is empty,
		// then it's definitely a subset of any set
		if (this.isEmpty())
			return true;

		for (HashMap.Entry<String, Integer> entry : superSet.entrySet()) {
			Integer setBonusLevel = this.get(entry.getKey());
			if (setBonusLevel == null)
				return false;
			else {
				if (setBonusLevel > entry.getValue())
					return false;
			}
		}
		return true;
	}

	boolean contains(String setBonusName) {
		return this.containsKey(setBonusName);
	}
}
