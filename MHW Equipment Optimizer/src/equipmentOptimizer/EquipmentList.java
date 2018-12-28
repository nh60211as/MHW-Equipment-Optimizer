package equipmentOptimizer;

import java.util.ArrayList;

public class EquipmentList extends ArrayList<ArrayList<Equipment>>{

	public EquipmentList() {
		for(int i=1;i<=7;i++)
			this.add(new ArrayList<Equipment>());
	}
}
