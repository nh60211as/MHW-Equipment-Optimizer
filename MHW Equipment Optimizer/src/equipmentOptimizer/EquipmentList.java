package equipmentOptimizer;

import java.util.ArrayList;

public class EquipmentList extends ArrayList<ArrayList<Equipment>>{
	public static final int WEAPON = 0;
	public static final int HEAD = 1;
	public static final int BODY = 2;
	public static final int HAND = 3;
	public static final int BELT = 4;
	public static final int FEET = 5;
	public static final int CHARM = 6;

	public EquipmentList() {
		for(int i=1;i<=7;i++)
			this.add(new ArrayList<Equipment>());
	}

	public boolean add(int bodyPart, Equipment equipment) {
		for(Equipment equipmentNow:this.get(bodyPart)) {
			if(equipmentNow.equipmentName.contentEquals(equipment.equipmentName)) {
				return false;
			}
		}
		this.get(bodyPart).add(equipment);
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
