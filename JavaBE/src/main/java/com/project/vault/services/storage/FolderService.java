package com.project.vault.services.storage;

import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.vault.entities.VaultFolder;
import com.project.vault.entities.VaultSpace;
import com.project.vault.entities.VaultUser;
import com.project.vault.models.NameAndParentDTO;
import com.project.vault.repos.FolderRepository;
import com.project.vault.services.VaultUserAuthenticationService;

@Service
public class FolderService implements FolderServiceInterface {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	FolderRepository folderRepo;
	@Autowired
	VaultUserAuthenticationService userAuthServ;
	@Autowired
	FileManagerService fileManagerServ;

	@Autowired
	SpaceService spaceServ;

	public String getFullPathById(Long folderId) {
		if (!folderRepo.existsById(folderId))
			return null;
		StringBuilder fullPath = new StringBuilder();
		Long currentId = folderId;
		NameAndParentDTO folderDTO;

		while (currentId != null) {
			folderDTO = folderRepo.findNameAndParentById(currentId);
			if (folderDTO == null) {
				break;
			}
			fullPath.insert(0, "/" + folderDTO.getName());
			currentId = folderDTO.getParentId();
		}

		return fullPath.toString();
	}

	// --------------------------------FOLDER-----------------------------------------------------------------------------------

	public VaultFolder getFolderById(Long id) {
		if (id != null && folderRepo.existsById(id)) {
			return folderRepo.findById(id).get();
		}
		logger.error("[" + id + "] not found you've passed a invalid folder id.");
		return null;
	}

	// -------------------------------------------------------------------------------------------------
	public VaultFolder cleanVaultFolder(VaultFolder folder) {
		folder.setSpace(null);
		folder.setVaultUser(null);
		folder.setSection(null);
		folder.setParentFolder(null);
		return folder;
	}

	public List<VaultFolder> getCleanFoldersByFolderId(Long folderId) {
		List<VaultFolder> folders = getFoldersByFolderId(folderId);
		if (folders != null) {
			for (VaultFolder folder : folders) {
				cleanVaultFolder(folder);
			}

			return folders;
		}

		return null;
	}

	public List<VaultFolder> getFoldersByFolderId(Long folderId) {
		if (folderRepo.existsById(folderId)) {
			List<VaultFolder> folders = folderRepo.findByParentFolderId(folderId);
			return folders;
		}
		return null;
	}

	public List<VaultFolder> getFolderWithNullParentBySpaceId(Long spaceId) {
		if (spaceServ.existsById(spaceId)) {
			List<VaultFolder> folders = folderRepo.findBySpaceIdAndParentFolderIsNull(spaceId);
			for (VaultFolder folder : folders) {
				cleanVaultFolder(folder);
			}

			return folders;
		}
		return null;
	}

	public VaultFolder saveNewFolder(Long userId, Long spaceId, Long parentId, String name) {
		VaultFolder folder = new VaultFolder();
		Boolean isDone = false;
		String completePath = null;
		String parentPath = null;
		VaultUser user = null;
		VaultSpace space = null;
		VaultFolder parentFolder = null;

		user = userAuthServ.getUserById(userId);
		if (user == null) {
			// no user
			return null;
		}
		space = spaceServ.getSpaceById(spaceId);
		if (space == null) {
			// no space
			return null;
		}
		if (!spaceServ.isHimTheSpaceOwner(spaceId, userId)) {
			// permiss denied
			return null;
		}
		folder.setCreatedAt(LocalDateTime.now());
		folder.setVaultUser(user);
		folder.setSpace(space);
		folder.setName(name);
		completePath = "/" + user.getUsername() + "/" + space.getName();
		if (parentId != null) {
			parentFolder = getFolderById(parentId);
			if (parentFolder != null && isHimTheFolderOwner(parentId, userId)) {
				folder.setParentFolder(parentFolder);
				parentPath = getFullPathById(parentId);
				if (parentPath != null) {
					completePath += parentPath;
					completePath += "/" + folder.getName();
				}
			} else {
				// error in folder path recreation
				return null;
			}
		} else {
			folder.setParentFolder(null);
			completePath += "/" + folder.getName();
		}

		isDone = fileManagerServ.createFolder(completePath);
		if (isDone) {
			try {
				logger.info("---saving-folder-in-db");
				folderRepo.save(folder);
				return folder;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	public Boolean existsById(Long id) {
		return folderRepo.existsById(id);
	}

	public JSONObject holdNewFolderRequest(Long userId, Long spaceId, Long parentId, String name) {
		JSONObject response = new JSONObject();
		Boolean isDone = false;
		if (!userAuthServ.existById(userId)) {
			return response.put("result", "no user found");
		}
		if (!spaceServ.existsById(spaceId)) {
			return response.put("result", "invalid spaceId");
		}
		if (!spaceServ.isHimTheSpaceOwner(spaceId, userId)) {
			return response.put("result", "you are not the space owner.");
		}
		if (parentId != null) {
			if (!folderRepo.existsById(parentId)) {
				return response.put("result", "invalid parentId");
			}
			if (!isHimTheFolderOwner(parentId, userId)) {
				return response.put("result", "you are not the folder owner.");
			}
		}
		VaultFolder folder = saveNewFolder(userId, spaceId, parentId, name);
		if (folder == null) {
			return response.put("result", "error during folder saving");
		} else {
			return response.put("id", folder.getId()).put("name", folder.getName());
		}
	}

	// -------------------------------------------------------------------------------------------------
	public Boolean isHimTheFolderOwner(Long folderId, Long userId) {
		return folderRepo.existsByIdAndUserId(folderId, userId);
	}

	public JSONObject holdRenameFolderRequest(Long userId, Long folderId, String newName) {
		if (folderId == null || !folderRepo.existsById(folderId) || !isHimTheFolderOwner(folderId, userId)) {
			return null;
		}
		JSONObject vaultFileObj = new JSONObject();
		VaultFolder vaultFolder = folderRepo.findById(folderId).get();
		renameFolder(vaultFolder, newName);
		if (vaultFolder != null) {
			vaultFileObj = new JSONObject(vaultFolder);
			return vaultFileObj;
		}
		return null;
	}

	private VaultFolder renameFolder(VaultFolder vaultFolder, String newName) {
		String knownPath = "/" + vaultFolder.getVaultUser().getUsername() + "/" + vaultFolder.getSpace().getName();
		String foldersPath = null;
		if (vaultFolder.getParentFolder() != null) {
			foldersPath = getFullPathById(vaultFolder.getParentFolder().getId());
		}
		if (foldersPath != null) {
			knownPath += foldersPath;
		}
		knownPath += "/" + vaultFolder.getName();

		try {
			fileManagerServ.renameFolder(knownPath, newName);
			vaultFolder.setName(newName);
			folderRepo.save(vaultFolder);
			cleanVaultFolder(vaultFolder);
			return vaultFolder;
		} catch (Exception e) {
			return null;
		}

	}
}
