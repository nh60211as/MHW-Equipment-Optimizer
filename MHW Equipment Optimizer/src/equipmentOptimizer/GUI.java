package equipmentOptimizer;

import javax.swing.*;

public class GUI {
	private EquipmentOptimizer equipmentOptimizer;
	private JPanel mainPanel;
	private JTextArea resultTextArea;
	private JButton testStartButton;

	private GUI() {
		resultTextArea.setEditable(false);
		equipmentOptimizer = new EquipmentOptimizer(resultTextArea);

		testStartButton.addActionListener(e -> {

			String fileName = "雙劍_麻痺.txt";
			try {
				equipmentOptimizer.readRequirement(fileName);
				equipmentOptimizer.generateIncludedEquipmentList();
				equipmentOptimizer.findAndPrintMatchingEquipmentList();
			} catch (CloneNotSupportedException cse) {
				System.out.println(cse.getMessage());
			}
		});
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("MHW Equipment Optimizer by nh60211as");
		frame.setContentPane(new GUI().mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
