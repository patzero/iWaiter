package com.insta.iwaiter.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileLoader {
	
	public List<String> fileNames = new ArrayList<String>();
	
	public FileLoader() {
		try {
			Files.walk(Paths.get(SimParameters.brainDirectory)).forEach(filePath -> {
			    if (Files.isRegularFile(filePath)) {
			        String[] fileName = filePath.getFileName().toString().split("\\.");
			        fileNames.add(fileName[0]);
			    }
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String> getFileNames() {
		return fileNames;
	}

	public void setFileNames(List<String> fileNames) {
		this.fileNames = fileNames;
	}
	
	

}
