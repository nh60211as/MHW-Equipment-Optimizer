package equipmentOptimizer;

import javax.swing.*;

public class GUI {
	private EquipmentOptimizer equipmentOptimizer;
	private JFileChooser fileChooser;
	private JPanel mainPanel;
	private JTextArea resultTextArea;
	private JButton chooseFileButton;

	private GUI() {
		resultTextArea.setEditable(false);
		equipmentOptimizer = new EquipmentOptimizer(resultTextArea);

		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new java.io.File("."));
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);


		chooseFileButton.addActionListener(e -> {
			String fileName;
			if (fileChooser.showOpenDialog(mainPanel) == JFileChooser.APPROVE_OPTION) {
				fileName = fileChooser.getSelectedFile().getAbsolutePath();

				try {
					equipmentOptimizer.readRequirement(fileName);
					equipmentOptimizer.generateIncludedEquipmentList();
					equipmentOptimizer.findAndPrintMatchingEquipmentList();
				} catch (CloneNotSupportedException cse) {
					System.out.println(cse.getMessage());
				}
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
