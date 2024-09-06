package com.project.vault.services.storage;

import com.project.vault.entities.VaultSpace;

public interface SpaceServiceInterface {

	VaultSpace saveNewSpace(String name, Long userId);

	Boolean isHimTheSpaceOwner(Long spaceId, Long userId);

	VaultSpace getSpaceById(Long id);

}
