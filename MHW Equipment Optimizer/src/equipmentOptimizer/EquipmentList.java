package equipmentOptimizer;

import java.util.ArrayList;
import java.util.List;

public class EquipmentList extends ArrayList<List<Equipment>>{
	public static final int WEAPON = 0;
	public static final int HEAD = 1;
	public static final int BODY = 2;
	public static final int HAND = 3;
	public static final int BELT = 4;
	public static final int FEET = 5;
	public static final int CHARM = 6;

	List<List<Equipment>> equipmentList;

	public EquipmentList() {
		equipmentList = new ArrayList<List<Equipment>>();
		for(int i=0;i<=6;i++)
			equipmentList.add(new ArrayList<Equipment>());
	}

	public boolean add(int bodyPart, Equipment equipment) {
		List<Equipment> bodyPartNow = this.get(bodyPart);
		for(Equipment weaponNow:bodyPartNow) {
			if(weaponNow.equipmentName.contentEquals(equipment.equipmentName)) {
				return false;
			}
		}
		bodyPartNow.add((Weapon)equipment);
		return true;
	}

	public Equipment get(int bodyPart, int index) {
		return this.get(bodyPart).get(index);
	}

	public int indexOf(int bodyPart, String equipmentName) {
		for(int i=0;i<=this.get(bodyPart).size()-1;i++) {
			if(this.get(bodyPart).get(i).equipmentName.contentEquals(equipmentName)) {
				return i;
			}
		}
		return -1;
	}

	public void printBodyPart(int bodyPart) {
		for(Equipment eq:this.get(bodyPart))
			System.out.print(eq.equipmentName + ",");
		System.out.println();
	}
}
