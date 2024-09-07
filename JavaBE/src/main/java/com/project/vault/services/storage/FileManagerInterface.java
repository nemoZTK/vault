package com.project.vault.services.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileManagerInterface {

	/**
	 * 
	 * @param knownPath
	 * @return
	 */
	Boolean createFolder(String knownPath);

	/**
	 * 
	 * @param knownPath
	 * @param file
	 * @return
	 */
	Boolean saveFile(String knownPath, MultipartFile file);

	/**
	 * 
	 * @param knownPath
	 * @return
	 */
	Resource getFile(String knownPath);
}