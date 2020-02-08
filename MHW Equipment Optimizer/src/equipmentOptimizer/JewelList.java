package equipmentOptimizer;

import java.util.ArrayList;

public class JewelList extends ArrayList<Jewel> {
	public JewelList(JewelList includedJewel) {
		this.addAll(includedJewel);
	}

	public JewelList() {

	}

	public JewelList getJewelsContainsValidSkill(int skillIndex) {
		JewelList ans = new JewelList();

		for (Jewel currentJewel : this) {
			if (currentJewel.validSkills.containsSkillIndex(skillIndex))
				ans.add(currentJewel);
		}

		return ans;
	}

	public JewelList removeContained(IncludedJewelTable includedJewelTable) {
		JewelList ans = new JewelList(this);

		for (Jewel currentJewel : ans) {
			if (includedJewelTable.contains(currentJewel))
				ans.remove(currentJewel);
		}

		return ans;
	}
}
