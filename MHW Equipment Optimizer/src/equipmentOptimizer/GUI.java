package equipmentOptimizer;

import javax.swing.*;

public class GUI {
	private JPanel mainPanel;
	private JButton chooseFileButton;
	private JButton startMatchingButton;
	private JLabel eventLabel;
	private JTextArea resultTextArea;

	private EquipmentOptimizer equipmentOptimizer;
	private String fileName;
	private JFileChooser fileChooser;

	private GUI() {
		startMatchingButton.setEnabled(false);
		resultTextArea.setEditable(false);

		equipmentOptimizer = new EquipmentOptimizer(resultTextArea);

		fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new java.io.File("."));
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		chooseFileButton.addActionListener(e -> {
			startMatchingButton.setEnabled(false);
			if (fileChooser.showOpenDialog(mainPanel) == JFileChooser.APPROVE_OPTION) {
				fileName = fileChooser.getSelectedFile().getAbsolutePath();
				eventLabel.setText("目前選擇檔案：" + fileName);

				try {
					startMatchingButton.setEnabled(true);
					equipmentOptimizer.readRequirement(fileName);
					equipmentOptimizer.generateIncludedEquipmentList();
				} catch (Exception e1) {
					startMatchingButton.setEnabled(false);
					eventLabel.setText("檔案格式錯誤");
				}
			}
		});
		startMatchingButton.addActionListener(e -> {
			try {
				equipmentOptimizer.findAndPrintMatchingEquipmentList();
			} catch (Exception e1) {
				startMatchingButton.setEnabled(false);
				eventLabel.setText("未知錯誤");
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
