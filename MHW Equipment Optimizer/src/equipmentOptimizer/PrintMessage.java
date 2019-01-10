package equipmentOptimizer;

import javax.swing.*;

public class PrintMessage {
	static void print(JTextArea textArea, String message) {
		textArea.append(message);
		System.out.print(message);
	}

	static void warning(JTextArea textArea, String message) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("警告：");
		stringBuilder.append(message);
		stringBuilder.append("\n");
		textArea.append(stringBuilder.toString());
		System.out.print(stringBuilder.toString());
	}

}
