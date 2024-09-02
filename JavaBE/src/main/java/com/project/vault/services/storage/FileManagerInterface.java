package com.project.vault.services.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileManagerInterface {
	Boolean createFolder(String knownPath);

	Boolean saveFile(String knownPath, MultipartFile file);

	Resource getFile(String knownPath);
}