package com.project.vault.services.storage;

import org.json.JSONObject;

import com.project.vault.entities.VaultSpace;

public interface SpaceServiceInterface {
	/**
	 * return true if the user is also the space owner
	 * 
	 * @param spaceId
	 * @param userId
	 * @return
	 */
	Boolean isHimTheSpaceOwner(Long spaceId, Long userId);

	// ---------spaces
	VaultSpace saveNewSpace(String name, Long userId);

	/**
	 * save a new space with the given in local and in db, then return a feedback
	 * that say if is done and what is going wrong
	 * 
	 * @param name
	 * @param userId
	 * @return
	 */
	JSONObject holdNewSpaceRequest(String name, Long userId);

}
