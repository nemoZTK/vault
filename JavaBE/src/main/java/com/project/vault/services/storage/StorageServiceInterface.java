package com.project.vault.services.storage;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.project.vault.entities.VaultFile;
import com.project.vault.entities.VaultFolder;
import com.project.vault.entities.VaultSpace;

public interface StorageServiceInterface {

	// -----------folders
	String getFullPathById(Long folderId);

	VaultFolder getFolderById(Long id);

	JSONObject holdNewFolderRequest(Long userId, Long spaceId, Long parentId, String name);

	Boolean isHimTheFolderOwner(Long folderId, Long userId);

	// ---------spaces
	VaultSpace saveNewSpace(String name, Long userId);

	JSONObject holdNewSpaceRequest(String name, Long userId);

	Boolean isHimTheSpaceOwner(Long spaceId, Long userId);

	VaultSpace getSpaceById(Long id);

	// ----files
	VaultFile saveNewFile(MultipartFile file, Long userId, Long spaceId, Long parentId);

	JSONObject handleSingleFileUpload(MultipartFile file, Long userId, Long spaceId, Long parentId);

	/// ------------uploads
	JSONObject holdUploadRequest(Long userId, Long spaceId, Long parentId, MultipartFile file);

	JSONObject handleZipUpload(MultipartFile file, Long userId, Long spaceId, Long parentId);

}
