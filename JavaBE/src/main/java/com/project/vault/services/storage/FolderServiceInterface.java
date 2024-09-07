package com.project.vault.services.storage;

import org.json.JSONObject;

public interface FolderServiceInterface {

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

}
