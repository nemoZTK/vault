package com.project.vault.services.storage;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import com.project.vault.entities.VaultFile;
import com.project.vault.entities.VaultFolder;
import com.project.vault.entities.VaultSpace;

public interface StorageServiceInterface {

	// -----------folders

	/**
	 * return true if the user is also the folder owner
	 * 
	 * @param folderId
	 * @param userId
	 * @return true if is the folder owner
	 */
	Boolean isHimTheFolderOwner(Long folderId, Long userId);

	/**
	 * save a new folder with the given in local and in db, then return a feedback
	 * that say if is done and what is going wrong
	 * 
	 * @param userId
	 * @param spaceId
	 * @param parentId
	 * @param name
	 * @return
	 */
	JSONObject holdNewFolderRequest(Long userId, Long spaceId, Long parentId, String name);

	Boolean isHimTheFolderOwner(Long folderId, Long userId);

	// ---------spaces
	VaultSpace saveNewSpace(String name, Long userId);

	JSONObject holdNewSpaceRequest(String name, Long userId);

	Boolean isHimTheSpaceOwner(Long spaceId, Long userId);

	VaultSpace getSpaceById(Long id);

	// ----files
	VaultFile saveNewFile(MultipartFile file, Long userId, Long spaceId, Long parentId);
	/**
	 * save a new space with the given in local and in db, then return a feedback
	 * that say if is done and what is going wrong
	 * 
	 * @param name
	 * @param userId
	 * @return
	 */
	JSONObject holdNewSpaceRequest(String name, Long userId);

	/**
	 * return true if the user is also the space owner
	 * 
	 * @param spaceId
	 * @param userId
	 * @return
	 */
	Boolean isHimTheSpaceOwner(Long spaceId, Long userId);

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

	/// ------------uploads

	/**
	 * 
	 * @param userId
	 * @param spaceId
	 * @param parentId
	 * @param file
	 * @return
	 */
	JSONObject holdUploadRequest(Long userId, Long spaceId, Long parentId, MultipartFile file);

	/**
	 * 
	 * @param file
	 * @param userId
	 * @param spaceId
	 * @param parentId
	 * @return
	 */
	JSONObject handleZipUpload(MultipartFile file, Long userId, Long spaceId, Long parentId);

}
