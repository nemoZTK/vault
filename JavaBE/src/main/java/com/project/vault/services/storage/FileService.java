package com.project.vault.services.storage;

import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.vault.entities.VaultFile;
import com.project.vault.entities.VaultSpace;
import com.project.vault.entities.VaultUser;
import com.project.vault.repos.FileRepository;
import com.project.vault.services.VaultUserAuthenticationService;

@Service
public class FileService implements FileServiceInterface {

	@Autowired
	FileRepository fileRepo;
	@Autowired
	SpaceService spaceServ;
	@Autowired
	VaultUserAuthenticationService userAuthServ;
	@Autowired
	FileManagerService fileManagerServ;
	@Autowired
	FolderService folderServ;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public List<VaultFile> getCleanFileByParentFolderId(Long folderId) {
		List<VaultFile> files = getFileByParentFolderId(folderId);
		if (files != null) {
			for (VaultFile file : files) {
				cleanVaultFile(file);
			}
			return files;
		}
		return null;
	}

	public List<VaultFile> getFileByParentFolderId(Long folderId) {
		if (folderServ.existsById(folderId)) {
			List<VaultFile> files = fileRepo.findByParentFolderId(folderId);
			return files;
		}
		return null;
	}

	public VaultFile getFileById(Long id) {
		if (id != null && fileRepo.existsById(id)) {
			return fileRepo.findById(id).get();
		}
		logger.error("[" + id + "] not found you've passed a invalid file id.");
		return null;

	}

	public List<VaultFile> getCleanFileWithNullParentBySpaceId(Long spaceId) {
		if (spaceServ.existsById(spaceId)) {
			List<VaultFile> files = fileRepo.findBySpaceIdAndParentFolderIsNull(spaceId);
			for (VaultFile file : files) {
				cleanVaultFile(file);
			}
			return files;
		}
		return null;
	}

	Resource holdSingleDownloadRequest(Long userId, Long spaceId, Long fileId) {
		VaultFile fileInfo;
		String path;
		Resource resource;
		if (userAuthServ.existById(userId)) {

			if (spaceServ.isHimTheSpaceOwner(spaceId, userId)) {

				path = "/" + userAuthServ.getUsernameById(userId) + "/" + spaceServ.findNameById(spaceId);
				fileInfo = getFileById(fileId);
				if (fileInfo.getParentFolder() == null) {

					path += "/" + fileInfo.getName();
					resource = fileManagerServ.getFile(path);
				} else {
					path += folderServ.getFullPathById(fileInfo.getParentFolder().getId());
					path += "/" + fileInfo.getName();
					resource = fileManagerServ.getFile(path);
				}
				return resource;
			}

		}
		return null;

	}

	public Boolean isHimTheFileOwner(Long fileId, Long userId) {
		return fileRepo.existsByIdAndUserId(fileId, userId);
	}

	public JSONObject holdRenameFileRequest(Long userId, Long fileId, String newName) {
		if (fileId == null || !fileRepo.existsById(fileId) || !isHimTheFileOwner(fileId, userId)) {
			return null;
		}
		JSONObject vaultFileObj = new JSONObject();
		VaultFile vaultFile = fileRepo.findById(fileId).get();
		renameFile(vaultFile, newName);
		if (vaultFile != null) {
			vaultFileObj = new JSONObject(vaultFile);
			return vaultFileObj;
		}
		return null;
	}

	public VaultFile renameFile(VaultFile vaultFile, String newName) {
		String knownPath = "/" + vaultFile.getVaultUser().getUsername() + "/" + vaultFile.getSpace().getName();
		String foldersPath = null;
		if (vaultFile.getParentFolder() != null) {
			foldersPath = folderServ.getFullPathById(vaultFile.getParentFolder().getId());
		}
		if (foldersPath != null) {
			knownPath += foldersPath;
		}
		knownPath += "/" + vaultFile.getName();

		try {
			fileManagerServ.renameFile(knownPath, newName);
			vaultFile.setName(newName);
			fileRepo.save(vaultFile);
			cleanVaultFile(vaultFile);
			return vaultFile;
		} catch (Exception e) {
			return null;
		}

	}

	public VaultFile cleanVaultFile(VaultFile vaultFile) {
		vaultFile.setParentFolder(null);
		vaultFile.setVaultUser(null);
		vaultFile.setVaultUser(null);
		vaultFile.setSection(null);
		vaultFile.setSpace(null);
		return vaultFile;
	}

	public JSONObject handleSingleFileUpload(MultipartFile file, Long userId, Long spaceId, Long parentId) {
		JSONObject response = new JSONObject();

		VaultSpace space = spaceServ.getSpaceById(spaceId);

		if (space == null) {
			return response.put("result", "invalid spaceId");
		}
		if (!spaceServ.isHimTheSpaceOwner(spaceId, userId)) {
			return response.put("result", "you are not the space owner.");
		}
		VaultFile savedFile = saveNewFile(file, userId, spaceId, parentId);
		if (savedFile != null) {
			return response.put("result", "done").put("name", savedFile.getName()).put("id", savedFile.getId());
		}
		return response.put("result", "error during local db saving");

	}

	public VaultFile saveNewFile(MultipartFile file, Long userId, Long spaceId, Long parentId) {

		VaultFile vaultFile = new VaultFile();
		VaultUser user = userAuthServ.getUserById(userId);
		String completePath = null;
		String parentPath = null;
		VaultSpace space = spaceServ.getSpaceById(spaceId);
		Boolean isDone = false;

		if (space == null || !spaceServ.isHimTheSpaceOwner(spaceId, userId)) {
			return null;
		}
		vaultFile.setName(file.getOriginalFilename());

		vaultFile.setCreatedAt(LocalDateTime.now());
		vaultFile.setVaultUser(user);
		vaultFile.setSpace(space);
		vaultFile.setSize(file.getSize());
		vaultFile.setType(file.getContentType());

		completePath = "/" + user.getUsername() + "/" + space.getName();
		if (parentId == null) {
			vaultFile.setParentFolder(null);
			completePath += "/" + vaultFile.getName();
		} else {
			parentPath = folderServ.getFullPathById(parentId);
			if (parentPath != null) {
				vaultFile.setParentFolder(folderServ.getFolderById(parentId));
				completePath += parentPath;

				completePath += "/" + vaultFile.getName();

			} else {
				return null;
			}
		}
		logger.info("before the method path is---->" + completePath);
		isDone = fileManagerServ.saveFile(completePath, file);
		if (isDone) {
			try {
				fileRepo.save(vaultFile);
				return vaultFile;
			} catch (Exception dbSaveError) {
				dbSaveError.printStackTrace();
			}
		}
		return null;
	}
}
