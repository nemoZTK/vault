package com.project.vault.services.storage;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.project.vault.entities.VaultFile;

public interface FileServiceInterface {
	// ----files
	VaultFile saveNewFile(MultipartFile file, Long userId, Long spaceId, Long parentId);

	// ----files

	/**
	 * 
	 * @param file
	 * @param userId
	 * @param spaceId
	 * @param parentId
	 * @return
	 */
	JSONObject handleSingleFileUpload(MultipartFile file, Long userId, Long spaceId, Long parentId);
}
