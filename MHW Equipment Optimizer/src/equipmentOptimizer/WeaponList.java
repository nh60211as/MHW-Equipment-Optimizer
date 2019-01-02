package equipmentOptimizer;

import java.util.ArrayList;
import java.util.List;

public class WeaponList extends ArrayList<List<Weapon>>{
	public static final int GREATSWORD = 0;
	public static final int LONGSWORD = 1;
	public static final int SWORDNSHIELD = 2;
	public static final int DUALBLADES = 3;
	public static final int HAMMER = 4;
	public static final int HUNTINGHORN = 5;
	public static final int LANCE = 6;
	public static final int GUNLANCE = 7;
	public static final int SWITCHAXE = 8;
	public static final int CHARGEBLADE = 9;
	public static final int INSECTGLAIVE = 10;
	public static final int LIGHTBOWGUN = 11;
	public static final int HEAVYBOWGUN = 12;
	public static final int BOW = 13;

	public WeaponList() {
		for(int i=GREATSWORD;i<=BOW;i++)
			this.add(new ArrayList<Weapon>());
	}

	public boolean add(int weaponType, Weapon addedWeapon) {
		List<Weapon> weaponTypeNow = this.get(weaponType);
		for(Weapon weaponNow:weaponTypeNow) {
			if(weaponNow.equipmentName.contentEquals(addedWeapon.equipmentName)) {
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
		for(List<Weapon> weaponTypeNow:this) {
			for(Weapon weaponNow:weaponTypeNow) {
				if(weaponName.contentEquals(weaponNow.equipmentName)) {
					return weaponNow;
				}
			}
		}
		return null;
	}

	public int indexOf(int bodyPart, String equipmentName) {
		for(int i=0;i<=this.get(bodyPart).size()-1;i++) {
			if(this.get(bodyPart).get(i).equipmentName.contentEquals(equipmentName)) {
				return i;
			}
		}
		return -1;
	}

	public int[] indexOf(String equipmentName) {
		int[] index = {-1,-1};
		for(int weaponType=0;weaponType<=this.size()-1;weaponType++) {
			for(int weaponNow=0;weaponNow<=this.get(weaponType).size()-1;weaponNow++) {
				if(this.get(weaponType).get(weaponNow).equipmentName.contentEquals(equipmentName)) {
					index[0] = weaponType;
					index[1] = weaponNow;
					return index;
				}
			}
		}
		return index;
	}

	public boolean isEmptyWeaponList() {
		for(List<Weapon> weaponTypeNow:this) {
			if(weaponTypeNow.size()!=0) {
				return false;
			}
		}
		return true;
	}
}
