package equipmentOptimizer;

import java.util.ArrayList;

public class SkillList extends ArrayList<Skill>{

	public SkillList() {
	}
	
	public int indexOf(String skillName) {
		for(int i=0;i<=this.size()-1;i++) {
			if(this.get(i).skillName.contentEquals(skillName)) {
				return i;
			}
		}
		return -1;
	}
	
}
