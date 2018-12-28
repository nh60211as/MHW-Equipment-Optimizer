package equipmentOptimizer;

import java.util.ArrayList;
import java.util.List;

public class SetBonusList {
	private List<String> _setBonusName;
	private List<Integer> _setBonusRequirement;
	
	public SetBonusList(){
		_setBonusName = new ArrayList<String>();
		_setBonusRequirement = new ArrayList<Integer>();
	}
	
	public void add(String setBonusName, int setBonusRequirement) {
		_setBonusName.add(setBonusName);
		_setBonusRequirement.add(setBonusRequirement);
	}
	
	public void plus1(String setBonusName) {
		int indexOfSetBonus = indexOf(setBonusName);
		if(indexOfSetBonus==-1) {
			_setBonusName.add(setBonusName);
			_setBonusRequirement.add(1);
		}
		else
			_setBonusRequirement.set(indexOfSetBonus, _setBonusRequirement.get(indexOfSetBonus)+1);
	}
	
	// return the first index of the instance. return -1 if not found
	public int indexOf(String setBonusName) {
		return _setBonusName.indexOf(setBonusName);
	}
	
	public boolean contains(String setBonusName) {
		return _setBonusName.contains(setBonusName);
	}
	
	public int getsetBonusRequirement(String setBonusName) {
		int indexOfSetBonus = indexOf(setBonusName);
		return (indexOfSetBonus!=-1) ? _setBonusRequirement.get(indexOfSetBonus) : 0;
	}
	
	public int size() {
		return _setBonusName.size();
	}
	
	public boolean checkSetBonus(SetBonusList checkedSetBonusList) {
		for(int i=0;i<=_setBonusName.size()-1;i++) {
			String currentSetBonusName = this._setBonusName.get(i);
			int currentSetBonusRequirement = this._setBonusRequirement.get(i);
			
			if(!checkedSetBonusList.contains(currentSetBonusName)) {
				return false;
			}
			else {
				if(checkedSetBonusList.getsetBonusRequirement(currentSetBonusName)<currentSetBonusRequirement) {
					return false;
				}
			}
		}
		return true;
	}
}
