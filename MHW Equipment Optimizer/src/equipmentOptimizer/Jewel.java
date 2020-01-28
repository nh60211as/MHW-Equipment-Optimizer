package equipmentOptimizer;

public class Jewel extends Item {
	// Jewel attributes
	int slotLevel; // level of the decoration slot
	int maxRequired; // the minimum jewels needed to max out the skill
	int owned;


	// Used to optimize equipment
	//int required;
	//boolean isReplaceable;

	public Jewel(final String input, final int slotLevel, final SkillHashMap skillHashMap) {
		// 跳躍迴避珠;迴避距離UP,1,迴避性能,1;5,1
		String[] stringBlock = input.split(";");
		// 跳躍迴避珠
		name = stringBlock[0];
		// 迴避距離UP,1,迴避性能,1
		//System.out.println(stringBlock[1]);
		skills = new ItemSkillList(stringBlock[1], skillHashMap);
		// 5,1
		String[] requirementBlock = stringBlock[2].split(",");
		// 5
		maxRequired = Integer.parseInt(requirementBlock[0]);
		// 1
		owned = Math.min(maxRequired, Integer.parseInt(requirementBlock[1]));

		this.slotLevel = slotLevel;

		validSkills = new ItemSkillList();
	}
}
