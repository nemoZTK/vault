package com.project.vault.services.storage;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.vault.entities.VaultFile;
import com.project.vault.entities.VaultFolder;

@Service
public class StorageService implements StorageServiceInterface {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	FolderService folderServ;
	@Autowired
	FileManagerService fileManagerServ;

	@Autowired
	SpaceService spaceServ;
	@Autowired
	FileService fileServ;

	// -------------------------------------------------------------------------------------------------------------------

	public JSONObject getFolderContentById(Long folderId) {
		List<VaultFile> files = fileServ.getCleanFileByParentFolderId(folderId);
		List<VaultFolder> folders = folderServ.getCleanFoldersByFolderId(folderId);
		JSONObject response = new JSONObject();
		JSONArray fileArray = new JSONArray();
		if (files != null) {
			for (VaultFile file : files) {
				fileArray.put(new JSONObject(file));
			}
			response.put("files", fileArray);
		}
		JSONArray folderArray = new JSONArray();
		if (folders != null) {
			for (VaultFolder folder : folders) {
				folderArray.put(new JSONObject(folder));
			}
			response.put("folders", folderArray);
		}
		return response;
	}

	public JSONObject getFileAndFolderWithParentNullBySpaceId(Long spaceId) {
		List<VaultFile> files = fileServ.getCleanFileWithNullParentBySpaceId(spaceId);
		List<VaultFolder> folders = folderServ.getFolderWithNullParentBySpaceId(spaceId);
		JSONObject response = new JSONObject();
		JSONArray fileArray = new JSONArray();
		if (files != null) {
			for (VaultFile file : files) {
				fileArray.put(new JSONObject(file));
			}
			response.put("files", fileArray);
		}
		JSONArray folderArray = new JSONArray();
		if (folders != null) {
			for (VaultFolder folder : folders) {
				folderArray.put(new JSONObject(folder));
			}
			response.put("folders", folderArray);
		}
		return response;
	}

	// ------------------------------------UPLOAD-------------------------------------------------------------------------------

//	public String getFullPathAndAssign
	public JSONObject holdUploadRequest(Long userId, Long spaceId, Long parentId, MultipartFile file) {

		JSONObject response = new JSONObject();
		try {
			boolean isZipFile = file.getOriginalFilename().endsWith(".zip");

			if (isZipFile) {
				logger.info("-------" + file.getOriginalFilename() + "is a zip file");
				return handleZipUpload(file, userId, spaceId, parentId);
			} else {
				logger.info("-------" + file.getOriginalFilename() + "is single file");
				return fileServ.handleSingleFileUpload(file, userId, spaceId, parentId);
			}

		} catch (Exception e) {
			logger.error("error in upload");

			e.printStackTrace();
			return response.put("result", "error in upload");

		}
	}

	public JSONObject handleZipUpload(MultipartFile file, Long userId, Long spaceId, Long parentId) {
		JSONObject response = null;
		return response.put("result", "");

	}

	// -------------------------------------------------------------------------------------------------

	public Resource holdDownloadRequest(Long userId, Long spaceId, Long folderId, Long fileId) {
		Resource resource;
		if (fileId != null) {
			resource = fileServ.holdSingleDownloadRequest(userId, spaceId, fileId);
			if (resource != null) {
				return resource;
			} else {
				return null;
			}

		} else {
			// is a folder download request
		}

		return null;
	}

	public JSONObject holdFolderDeleteRequest(Long folderId, Long userId) {
		JSONObject response = new JSONObject();
		if (!folderServ.existsById(folderId)) {
			return response.put("result", "invalid folderId");
		}
		if (!folderServ.isHimTheFolderOwner(folderId, userId)) {
			return response.put("result", "you are not the folder owner.");
		}

		boolean isDone = deleteFolderRecursively(folderId);
		if (isDone) {
			return response.put("result", "folder deleted successfully");
		} else {
			return response.put("result", "error during folder deletion");
		}
	}

	private boolean deleteFolderRecursively(Long folderId) {
		List<VaultFile> files = fileServ.getFileByParentFolderId(folderId);
		List<VaultFolder> folders = folderServ.getFoldersByFolderId(folderId);
		// Delete all files in the folder
		for (VaultFile file : files) {
			boolean fileDeleted = deleteFile(file.getId());
			if (!fileDeleted) {
				return false; // Abort if any file deletion fails
			}
		}

		// Delete all subfolders
		for (VaultFolder folder : folders) {
			boolean folderDeleted = deleteFolderRecursively(folder.getId());
			if (!folderDeleted) {
				return false; // Abort if any subfolder deletion fails
			}
		}

		// Finally, delete the folder itself
		VaultFolder folder = folderServ.getFolderById(folderId);
		String knownPath = "/" + folder.getVaultUser().getUsername() + "/" + folder.getSpace().getName();
		if (folder.getParentFolder() != null) {
			String folderPath = folderServ.getFullPathById(folderId);
			knownPath += folderPath;
		} else {
			knownPath += "/" + folder.getName();
		}

		if (fileManagerServ.deleteFolder(knownPath)) {
			folderServ.folderRepo.deleteById(folderId); // Delete folder from DB
			return true;
		} else {
			return false;
		}
	}

	public JSONObject holdFileDeleteRequest(List<Long> fileIds, Long userId) {
		JSONObject response = new JSONObject();
		for (Long fileId : fileIds) {
			VaultFile file = fileServ.getFileById(fileId);
			if (file == null) {
				continue; // Skip if file not found
			}
			if (!fileServ.isHimTheFileOwner(fileId, userId)) {
				return response.put("result", "you are not the file owner for fileId: " + fileId);
			}

			boolean isDone = deleteFile(fileId);
			if (!isDone) {
				return response.put("result", "error during deletion of fileId: " + fileId);
			}
		}
		return response.put("result", "files deleted successfully");
	}

	private boolean deleteFile(Long fileId) {
		VaultFile file = fileServ.getFileById(fileId);
		if (file == null) {
			return false; // File not found
		}
		String knownPath = "/" + file.getVaultUser().getUsername() + "/" + file.getSpace().getName();
		logger.info("actual known path--->" + knownPath);
		if (file.getParentFolder() != null) {
			String folderPath = folderServ.getFullPathById(file.getParentFolder().getId());
			knownPath += folderPath + "/" + file.getName();
			logger.info("actual known path--->" + knownPath);
		} else {
			knownPath += "/" + file.getName();
			logger.info("actual known path--->" + knownPath);
		}

		if (fileManagerServ.deleteFile(knownPath)) {
			fileServ.fileRepo.deleteById(fileId); // Delete file from DB
			return true;
		} else {
			return false;
		}
	}

	// TODO:delete file
//	public String getFullPathById(Long folderId) {
//		Optional<Folder> folderOptional = folderRepo.findById(folderId);
//
//		if (folderOptional.isEmpty()) {
//			return null;
//		}
//
//		Folder folder = folderOptional.get();
//		StringBuilder fullPath = new StringBuilder(folder.getPath());
//
//		while (folder.getParentFolder() != null) {
//			folder = folder.getParentFolder();
//			fullPath.insert(0, folder.getPath());
//		}
//
//		return fullPath.toString();
//	}

}
