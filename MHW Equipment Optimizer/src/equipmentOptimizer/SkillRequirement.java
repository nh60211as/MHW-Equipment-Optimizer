package equipmentOptimizer;

public class SkillRequirement{
	String skillName;
	int required;
	int owned;
	boolean isReplaceable;
	int levelOfDecor;
	
	public SkillRequirement(String[] stringBlock){
		SkillRequirement_main(stringBlock);
	}
	
	public SkillRequirement(String input){
		String[] stringBlock = input.split(",");
		SkillRequirement_main(stringBlock);
	}
	
	public SkillRequirement(String[] stringBlock, int levelOfDecor){
		this.skillName = stringBlock[0];
		this.required = Integer.parseInt(stringBlock[1]);
		this.owned = Integer.parseInt(stringBlock[2]);
		isReplaceable = (this.required-this.owned)<=0;
		this.levelOfDecor = levelOfDecor;
	}
	
	private void SkillRequirement_main(String[] stringBlock) {
		this.skillName = stringBlock[0];
		this.required = Integer.parseInt(stringBlock[1]);
		this.owned = Integer.parseInt(stringBlock[2]);
		isReplaceable = (this.required-this.owned)<=0;
		this.levelOfDecor = Integer.parseInt(stringBlock[3]);
	}
	
	public void setRequired(int required) {
		this.required = required;
		isReplaceable = (this.required-this.owned)<=0;
	}
}
