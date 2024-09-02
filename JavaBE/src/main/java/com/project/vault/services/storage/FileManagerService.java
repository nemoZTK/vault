package com.project.vault.services.storage;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileManagerService implements FileManagerInterface {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${folder.main.path}")
	private String basePath;

	@Override
	public Boolean createFolder(String knownPath) {
		if (knownPath == null) {
			logger.error("null path");
			return false;
		}
		knownPath = basePath + knownPath;
		try {
			Path folderPath = Paths.get(knownPath).normalize();
			logger.trace("Creating folder at: " + folderPath.toString());
			if (Files.exists(folderPath)) {
				logger.warn("Folder already exists at: " + folderPath.toString());
				return true;
			}
			Files.createDirectories(folderPath);
			logger.info("Folder created successfully at: " + folderPath.toString());
			return true;
		} catch (IOException e) {
			logger.error("Failed to create folder at: " + knownPath, e);
			return false;
		}

	}

	@Override
	public Boolean saveFile(String knownPath, MultipartFile file) {
		knownPath = basePath + knownPath;
		Path absolutePath = Paths.get(knownPath).normalize();
		try {
			file.transferTo(absolutePath);
		} catch (Exception localSaveError) {
			localSaveError.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public Resource getFile(String knownPath) {
		knownPath = basePath + knownPath;
		try {
			URI fileUri = Paths.get(knownPath).normalize().toUri();
			Resource resource = new UrlResource(fileUri);
			if (resource.exists() && resource.isReadable()) {
				return resource;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
