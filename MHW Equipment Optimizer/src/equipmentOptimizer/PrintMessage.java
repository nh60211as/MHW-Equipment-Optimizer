package equipmentOptimizer;

import javax.swing.*;
import java.awt.*;

class PrintMessage {
	static void print(JTextArea textArea, String message) {
		updateTextArea(textArea, message);
		System.out.print(message);
	}

	static void warning(JTextArea textArea, String message) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("警告：");
		stringBuilder.append(message);
		stringBuilder.append("\n");

		updateTextArea(textArea, stringBuilder.toString());
		System.out.print(stringBuilder.toString());
	}

	private static void updateTextArea(JTextArea textArea, String message) {
		SwingUtilities.invokeLater(() -> textArea.append(message));
	}

	static void updateEventLabel(JLabel label, String message) {
		SwingUtilities.invokeLater(() -> label.setText(message));
		System.out.print(message);
	}

	static void updateEventLabelError(JLabel label, String message) {
		SwingUtilities.invokeLater(() -> label.setForeground(Color.RED));
		SwingUtilities.invokeLater(() -> label.setText(message));
		System.out.print(message);
	}
}
