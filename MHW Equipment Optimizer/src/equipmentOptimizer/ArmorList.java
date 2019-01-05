package equipmentOptimizer;

import java.util.ArrayList;
import java.util.List;

public class ArmorList extends ArrayList<List<Armor>> {
	static final int HEAD = 0;
	static final int BODY = 1;
	static final int HANDS = 2;
	static final int BELT = 3;
	static final int FEET = 4;
	static final int CHARM = 5;

	ArmorList() {
		for (int i = HEAD; i <= CHARM; i++)
			this.add(new ArrayList<>());
	}

	public boolean add(int bodyPart, Armor addedArmor) {
		List<Armor> bodyPartNow = this.get(bodyPart);
		for (Armor armorNow : bodyPartNow) {
			if (armorNow.equipmentName.contentEquals(addedArmor.equipmentName)) {
				return false;
			}
		}
		bodyPartNow.add(addedArmor);
		return true;
	}

	public Armor get(int bodyPart, int index) {
		return this.get(bodyPart).get(index);
	}

	int indexOf(int bodyPart, String equipmentName) {
		for (int i = 0; i <= this.get(bodyPart).size() - 1; i++) {
			if (this.get(bodyPart).get(i).equipmentName.contentEquals(equipmentName)) {
				return i;
			}
		}
		return -1;
	}

	public void printBodyPart(int bodyPart) {
		for (Equipment eq : this.get(bodyPart))
			System.out.print(eq.equipmentName + ",");
		System.out.println();
	}

	int iterationSize() {
		int size = 1;
		for (List<Armor> bodyPartNow : this) {
			size *= bodyPartNow.size();
		}
		return size;
	}
}
