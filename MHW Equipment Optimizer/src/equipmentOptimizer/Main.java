package equipmentOptimizer;

class Main {

	public static void main(String[] args) throws CloneNotSupportedException {
		//System.out.println("讀取檔案中...");
		EquipmentOptimizer equipmentOptimizer = new EquipmentOptimizer(null, null);
		//System.out.println("讀取完成");

		equipmentOptimizer.readRequirement(args[0]);
		equipmentOptimizer.generateIncludedEquipmentList();
		equipmentOptimizer.findAndPrintMatchingEquipmentList();
	}

}
