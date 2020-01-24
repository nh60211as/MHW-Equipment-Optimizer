package equipmentOptimizer;

import java.util.ArrayList;
import java.util.List;

class WeaponList extends ArrayList<List<Weapon>> {
	static final int GREATSWORD = 0;
	static final int LONGSWORD = 1;
	static final int SWORDNSHIELD = 2;
	static final int DUALBLADES = 3;
	static final int HAMMER = 4;
	static final int HUNTINGHORN = 5;
	static final int LANCE = 6;
	static final int GUNLANCE = 7;
	static final int SWITCHAXE = 8;
	static final int CHARGEBLADE = 9;
	static final int INSECTGLAIVE = 10;
	static final int LIGHTBOWGUN = 11;
	static final int HEAVYBOWGUN = 12;
	static final int BOW = 13;

	WeaponList() {
		for (int i = GREATSWORD; i <= BOW; i++)
			this.add(new ArrayList<>());
	}

	public boolean add(int weaponType, Weapon addedWeapon) {
		List<Weapon> weaponTypeNow = this.get(weaponType);
		for (Weapon weaponNow : weaponTypeNow) {
			if (weaponNow.name.contentEquals(addedWeapon.name)) {
				return false;
			}
		}
		weaponTypeNow.add(addedWeapon);
		return true;
	}

	public Equipment get(int bodyPart, int index) {
		return this.get(bodyPart).get(index);
	}

	public Weapon get(String weaponName) {
		for (List<Weapon> weaponTypeNow : this) {
			for (Weapon weaponNow : weaponTypeNow) {
				if (weaponName.contentEquals(weaponNow.name)) {
					return weaponNow;
				}
			}
		}
		return null;
	}

	public int indexOf(int bodyPart, String equipmentName) {
		for (int i = 0; i <= this.get(bodyPart).size() - 1; i++) {
			if (this.get(bodyPart).get(i).name.contentEquals(equipmentName)) {
				return i;
			}
		}
		return -1;
	}

	int[] indexOf(String equipmentName) {
		int[] index = {-1, -1};
		for (int weaponType = 0; weaponType <= this.size() - 1; weaponType++) {
			for (int weaponNow = 0; weaponNow <= this.get(weaponType).size() - 1; weaponNow++) {
				if (this.get(weaponType).get(weaponNow).name.contentEquals(equipmentName)) {
					index[0] = weaponType;
					index[1] = weaponNow;
					return index;
				}
			}
		}
		return index;
	}

	boolean isEmptyWeaponList() {
		for (List<Weapon> weaponTypeNow : this) {
			if (weaponTypeNow.size() != 0) {
				return false;
			}
		}
		return true;
	}

	int totalSize() {
		int size = 0;
		for (List<Weapon> weaponTypeNow : this) {
			size += weaponTypeNow.size();
		}
		return size;
	}
}
