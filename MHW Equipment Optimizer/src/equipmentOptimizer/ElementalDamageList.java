package equipmentOptimizer;

import java.util.ArrayList;
import java.util.List;

public class ElementalDamageList {
	private List<String> _elementalType;
	private List<Integer> _elementalValue;
	private List<Boolean> _needFreeElem;
	
	public ElementalDamageList(){
		_elementalType = new ArrayList<String>();
		_elementalValue = new ArrayList<Integer>();
		_needFreeElem = new ArrayList<Boolean>();
	}
	
	public void add(String elementalType, String elementalValue, String needFreeElemString) {
		boolean needFreeElem = false;
		if(needFreeElemString.contentEquals("是"))
			needFreeElem = true;
		else if(needFreeElemString.contentEquals("否"))
			needFreeElem = false;
		
		add(elementalType,Integer.parseInt(elementalValue),needFreeElem);
	}
	
	public void add(String elementalType, int elementalValue, boolean needFreeElem) {
		_elementalType.add(elementalType);
		_elementalValue.add(elementalValue);
		_needFreeElem.add(needFreeElem);
	}
}
