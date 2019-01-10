package equipmentOptimizer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI {
	static EquipmentOptimizer equipmentOptimizer;
	private JPanel mainPanel;
	private JTextArea resultTextArea;
	private JButton testStartButton;

	private GUI() {
		testStartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String fileName = "大劍.txt";
				try {
					equipmentOptimizer.readAndFindMatchingEquipmentList(fileName);
				} catch (CloneNotSupportedException cse) {
					System.out.println(cse.getMessage());
				}
			}
		});
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("GUI");
		frame.setContentPane(new GUI().mainPanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

		equipmentOptimizer = new EquipmentOptimizer();
	}
}
