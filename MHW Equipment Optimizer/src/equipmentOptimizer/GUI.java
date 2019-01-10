package equipmentOptimizer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
	private EquipmentOptimizer equipmentOptimizer;
	private JPanel mainPanel;
	private JTextArea resultTextArea;
	private JButton testStartButton;

	private GUI() {
		equipmentOptimizer = new EquipmentOptimizer(resultTextArea::append);
		resultTextArea.setFocusable(false);

		testStartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String fileName = "大劍.txt";
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
