package equipmentOptimizer;

import javax.swing.*;

public class GUI {
	private static final int MAJORVERSION = 2;
	private static final int MINORVERSION = 0;
	private static final int FIXVERSION = 1;

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
		eventLabel.setText("MHW Equipment Optimizer");

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
					equipmentOptimizer = new EquipmentOptimizer(resultTextArea, eventLabel);
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
			resultTextArea.setText("");
			try {
				Thread thread = new Thread(() -> equipmentOptimizer.findAndPrintMatchingEquipmentList());
				thread.start();
			} catch (Exception e1) {
				startMatchingButton.setEnabled(false);
				eventLabel.setText("未知錯誤");
			}
		});
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame(String.format("MHW Equipment Optimizer Version %d.%d.%d by nh60211as", MAJORVERSION, MINORVERSION, FIXVERSION));
			frame.setContentPane(new GUI().mainPanel);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.pack();
			frame.setVisible(true);
		});
	}
}
