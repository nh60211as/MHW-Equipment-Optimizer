package equipmentOptimizer;

public class Main {

	public static void main(String[] args) throws CloneNotSupportedException {
		//System.out.println("讀取檔案中...");
		EquipmentOptimizer equipmentOptimizer = new EquipmentOptimizer();
		//System.out.println("讀取完成");

		equipmentOptimizer.readAndFindMatchingEquipmentList(args[0]);
	}

}
