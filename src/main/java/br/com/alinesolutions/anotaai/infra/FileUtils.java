package br.com.alinesolutions.anotaai.infra;

import java.io.File;


public class FileUtils {
	
	private static final FileUtils instance;
	private String rootDir;

	static {
		instance = new FileUtils();
	}
	
	private FileUtils() {
		super();
	}
	
	public static FileUtils getInstance() {
		return instance;
	}
	
	public File createFile(String fileNameStr) {
		StringBuilder fileName = new StringBuilder(fileNameStr);
		File file = new File(fileName.toString());
		if (!file.exists()) {
			file.mkdirs();			
		}
		return file;
	}

	public String getRootDir() {
		return rootDir;
	}

	public void setRootDir(String rootDir) {
		this.rootDir = rootDir;
	}
	
}
