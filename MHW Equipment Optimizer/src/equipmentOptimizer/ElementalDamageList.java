package equipmentOptimizer;

import java.util.ArrayList;
import java.util.List;

class ElementalDamageList {
	private final List<String> _elementalType;
	private final List<Integer> _elementalValue;
	private final List<Boolean> _needFreeElem;

	ElementalDamageList() {
		_elementalType = new ArrayList<>();
		_elementalValue = new ArrayList<>();
		_needFreeElem = new ArrayList<>();
	}

	public void add(String elementalType, String elementalValue, String needFreeElemString) {
		boolean needFreeElem;
		switch (needFreeElemString) {
			case "是":
				needFreeElem = true;
				break;
			case "否":
				needFreeElem = false;
				break;
			default:
				needFreeElem = false;
				break;
		}
		add(elementalType, Integer.parseInt(elementalValue), needFreeElem);
	}

	private void add(String elementalType, int elementalValue, boolean needFreeElem) {
		_elementalType.add(elementalType);
		_elementalValue.add(elementalValue);
		_needFreeElem.add(needFreeElem);
	}
}
