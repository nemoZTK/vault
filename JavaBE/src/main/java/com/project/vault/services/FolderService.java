package com.project.vault.services;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.Data;

@Data
@Service
public class FolderService {
	@Value("${folder.main.path}")
	private String basePath;

	public Boolean createRepository(String path, String name) {
		String fullPath = path + "/" + name;
		boolean isAllDone = false;
		try {
			createFolder(fullPath);
			createFolder(fullPath + "/documents");
			createFolder(fullPath + "/codebase");
			isAllDone = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isAllDone;

	}

	public Boolean createFolder(String path) {
		File folder = new File(path);
		System.out.println("Creating folder at: " + folder.getPath());
		if (!folder.exists()) {
			boolean isCreated = folder.mkdirs();
			if (isCreated) {
				System.out.println("Folder created successfully at: " + folder.getPath());
				return true;
			} else {
				System.out.println("Failed to create folder at: " + folder.getPath());
				return false;
			}
		} else {
			System.out.println("Folder already exists at: " + folder.getPath());
			return false;
		}
	}

}
