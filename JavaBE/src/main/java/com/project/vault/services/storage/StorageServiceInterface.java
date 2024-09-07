package com.project.vault.services.storage;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

public interface StorageServiceInterface {

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
