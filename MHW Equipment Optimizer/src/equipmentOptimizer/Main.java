package equipmentOptimizer;

import javax.swing.*;

class Main {

	public static void main(String[] args) throws CloneNotSupportedException {
		//System.out.println("讀取檔案中...");
		EquipmentOptimizer equipmentOptimizer = new EquipmentOptimizer(new JTextArea(), new JLabel());
		//System.out.println("讀取完成");

		equipmentOptimizer.readRequirement(args[0]);
		equipmentOptimizer.findAndPrintMatchingEquipmentList();
	}

}
