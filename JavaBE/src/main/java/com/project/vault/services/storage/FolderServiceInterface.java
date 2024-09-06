package com.project.vault.services.storage;

import com.project.vault.entities.VaultFolder;

public interface FolderServiceInterface {

	String getFullPathById(Long folderId);

	VaultFolder getFolderById(Long id);

	Boolean isHimTheFolderOwner(Long folderId, Long userId);

}
