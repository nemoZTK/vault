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

		try {
			Path folderPath = Paths.get(basePath + knownPath).normalize();
			logger.trace("Creating folder at: " + folderPath.toString());
			if (Files.exists(folderPath)) {
				logger.warn("Folder already exists at: " + folderPath.toString());
				return true;
			}
			Files.createDirectory(folderPath);
			logger.info("Folder created successfully at: " + folderPath.toString());
			return true;
		} catch (IOException e) {
			logger.error("Failed to create folder at: " + knownPath, e);
			return false;
		}

	}

	public Boolean renameFolder(String knownPath, String newFolderName) {
		Path sourcePath = Paths.get(basePath + knownPath).normalize();
		Path targetPath = sourcePath.resolveSibling(newFolderName).normalize();

		try {
			if (Files.exists(sourcePath) && Files.isDirectory(sourcePath)) {
				Files.move(sourcePath, targetPath);
				logger.info("Folder renamed successfully from: " + sourcePath + " to: " + targetPath);
				return true;
			} else {
				logger.error("Folder does not exist or is not a directory at path: " + sourcePath);
				return false;
			}
		} catch (IOException e) {
			logger.error("Failed to rename folder from: " + sourcePath + " to: " + targetPath, e);
			return false;
		}
	}

	@Override
	public Boolean saveFile(String knownPath, MultipartFile file) {
		logger.info("file will be saved in " + basePath + knownPath);
		Path absolutePath = Paths.get(basePath + knownPath).normalize();

		logger.info("final path is " + absolutePath.toString());

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

		try {

			URI fileUri = Paths.get(basePath + knownPath).normalize().toUri();
			Resource resource = new UrlResource(fileUri);
			if (resource.exists() && resource.isReadable()) {
				return resource;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Boolean renameFile(String knownOldPath, String newFileName) {
		Path sourcePath = Paths.get(basePath + knownOldPath).normalize();

		Path targetPath = sourcePath.resolveSibling(newFileName).normalize();
		String g;

		try {
			if (Files.exists(sourcePath)) {
				Files.move(sourcePath, targetPath);
				logger.info("File renamed successfully from: " + sourcePath + " to: " + targetPath);
				return true;
			} else {
				logger.error("File does not exist at path: " + sourcePath);
				return false;
			}
		} catch (IOException e) {
			logger.error("Failed to rename file from: " + sourcePath + " to: " + targetPath, e);
			return false;
		}
	}

	public Boolean deleteFile(String knownPath) {
		if (knownPath == null) {
			logger.error("null path");
			return false;
		}

		Path filePath = Paths.get(basePath + knownPath).normalize();

		logger.trace("Deleting file at: " + filePath.toString());

		try {
			if (Files.exists(filePath)) {
				Files.delete(filePath);
				logger.info("File deleted successfully at: " + filePath.toString());
				return true;
			} else {
				logger.warn("File does not exist at path: " + filePath.toString());
				return false;
			}
		} catch (IOException e) {
			logger.error("Failed to delete file at: " + filePath.toString(), e);
			return false;
		}
	}
}
