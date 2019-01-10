package equipmentOptimizer;

import java.util.function.Consumer;

public class PrintMessage {
	static void print(Consumer consumer, String message) {
		consumer.accept(message);
	}

	static void warning(Consumer consumer, String message) {
		consumer.accept("警告：" + message);
	}

//	Consumer consumer = PrintMessage::printNames;
//	private static void printNames(String name) {
//		System.out.println(name);
//	}
}
