package equipmentOptimizer;

import java.util.ArrayList;

public class JewelList extends ArrayList<Jewel> {
	public JewelList(JewelList includedJewel) {
		this.addAll(includedJewel);
	}

	public JewelList() {

	}
}
