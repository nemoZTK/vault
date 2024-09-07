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
	SpaceService spaceServ;
	@Autowired
	FileService fileServ;

	// -------------------------------------------------------------------------------------------------------------------

	public JSONObject getFolderContentById(Long folderId) {
		List<VaultFile> files = fileServ.getFileByParentFolderId(folderId);
		List<VaultFolder> folders = folderServ.getFoldersByFolderId(folderId);
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
		List<VaultFile> files = fileServ.getFileWithNullParentBySpaceId(spaceId);
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
