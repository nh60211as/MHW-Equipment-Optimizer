package equipmentOptimizer;

public class SkillRequirement{
	String skillName;
	int max;
	int owned;
	int levelOfDecor;
	
	int required;
	boolean isReplaceable;
	
	public SkillRequirement(String[] stringBlock){
		SkillRequirement_main(stringBlock);
	}
	
	public SkillRequirement(String input){
		String[] stringBlock = input.split(",");
		SkillRequirement_main(stringBlock);
	}
	
	public SkillRequirement(String[] stringBlock, int levelOfDecor){
		this.skillName = stringBlock[0];
		this.max = Integer.parseInt(stringBlock[1]);
		this.owned = Integer.parseInt(stringBlock[2]);
		this.levelOfDecor = levelOfDecor;
		
		this.required = 0;
		isReplaceable = true;
	}
	
	private void SkillRequirement_main(String[] stringBlock) {
		this.skillName = stringBlock[0];
		this.max = Integer.parseInt(stringBlock[1]);
		this.owned = Integer.parseInt(stringBlock[2]);
		this.levelOfDecor = Integer.parseInt(stringBlock[3]);
		
		this.required = 0;
		isReplaceable = true;
	}
	
	public void setRequired(int required) {
		this.required = required;
		isReplaceable = (this.required-this.owned)<=0;
	}
}
