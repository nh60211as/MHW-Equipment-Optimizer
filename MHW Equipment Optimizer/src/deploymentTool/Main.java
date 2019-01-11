package deploymentTool;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;

public class Main {
	public static void main(String[] args) throws IOException {
		final int MAJORVERSION = Integer.parseInt(args[0]);
		final int MINORVERSION = Integer.parseInt(args[1]);
		final int FIXVERSION = Integer.parseInt(args[2]);

		File currentFile = new File(new File(".").getAbsolutePath()).getParentFile().getParentFile();
		String currentPath = currentFile.getAbsolutePath();
		System.out.println("目前目錄：" + currentPath);

		String deployPath = currentFile.getParentFile().getAbsolutePath()
				+ String.format("/MHW-Equipment-Optimizer_%d.%d.%d", MAJORVERSION, MINORVERSION, FIXVERSION);
		System.out.println("配置目錄：" + deployPath);

		File deployFile = new File(deployPath);
		// 創建新目錄
		deployFile.mkdirs();

		List<String> fileList = new LinkedList<>();
		fileList.add("LICENSE");
		fileList.add("README.md");
		fileList.add("MHW Equipment Optimizer/MHW-Equipment-Optimizer.jar");
		fileList.add("MHW Equipment Optimizer/MHW-Equipment-Optimizer-GUI.jar");
		fileList.add("MHW Equipment Optimizer/範例.txt");
		for (String fileName : fileList) {
			File srcFile = new File(currentPath + "/" + fileName);
			File dstFile = new File(deployFile + "/" + srcFile.getName());
			System.out.println("來源：" + srcFile);
			System.out.println("目標：" + dstFile);
			Files.copy(srcFile.toPath(), dstFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
	}
}
