package equipmentOptimizer;

public class SkillRequirment {
	String skillName;
	int required;
	int owned;
	int levelOfDecor;
	
	public SkillRequirment(String skillName, int required, int have, int levelOfHole){
		this.skillName = skillName;
		this.required = required;
		this.owned = have;
		this.levelOfDecor = levelOfHole;
	}
	
	public SkillRequirment(String[] stringBlock){
		this.skillName = stringBlock[0];
		this.required = Integer.parseInt(stringBlock[1]);
		this.owned = Integer.parseInt(stringBlock[2]);
		this.levelOfDecor = Integer.parseInt(stringBlock[3]);
	}
	
	public SkillRequirment(String input){
		String[] stringBlock = input.split(",");
		
		this.skillName = stringBlock[0];
		this.required = Integer.parseInt(stringBlock[1]);
		this.owned = Integer.parseInt(stringBlock[2]);
		this.levelOfDecor = Integer.parseInt(stringBlock[3]);
	}
}
