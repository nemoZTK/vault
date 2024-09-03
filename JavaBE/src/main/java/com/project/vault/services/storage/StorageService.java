package com.project.vault.services.storage;

import java.time.LocalDateTime;
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
import com.project.vault.entities.VaultSpace;
import com.project.vault.entities.VaultUser;
import com.project.vault.models.NameAndParentDTO;
import com.project.vault.repos.FileRepository;
import com.project.vault.repos.FolderRepository;
import com.project.vault.repos.SpaceRepository;
import com.project.vault.services.VaultUserAuthenticationService;

@Service
public class StorageService implements StorageServiceInterface {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	FolderRepository folderRepo;
	@Autowired
	FileRepository fileRepo;
	@Autowired
	FileManagerService fileManagerServ;
	@Autowired
	SpaceRepository spaceRepo;
	@Autowired
	VaultUserAuthenticationService userAuthServ;

	// -------------------------------------------------------------------------------------------------------------------

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

//------------------------------------SPACE--------------------------------------------------------------------------------
	public VaultSpace saveNewSpace(String name, Long userId) {
		JSONObject response = new JSONObject();
		Boolean isDone = false;
		VaultSpace space = new VaultSpace();
		VaultUser user = null;
		space.setCreatedAt(LocalDateTime.now());
		space.setName(name);

		user = userAuthServ.getUserById(userId);
		if (user == null) {
			// no user
			return null;
		}
		String knownPath = "/" + user.getUsername() + "/" + space.getName();
		isDone = fileManagerServ.createFolder(knownPath);
		if (isDone) {
			try {
				space.setVaultUser(user);
				spaceRepo.save(space);
				return space;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public JSONObject holdNewSpaceRequest(String name, Long userId) {
		JSONObject response = new JSONObject();
		if (!userAuthServ.existById(userId)) {
			return response.put("result", "user not found " + userId);
		}
		VaultSpace space = saveNewSpace(name, userId);
		if (space == null) {
			return response.put("result", "local folder creation failed for space request " + name);
		} else {
			return response.put("result", "done").put("id", space.getId()).put("name", name);
		}
	}

	public Boolean isHimTheSpaceOwner(Long spaceId, Long userId) {
		return spaceRepo.existsByIdAndUserId(spaceId, userId);
	}

	public VaultSpace getSpaceById(Long id) {
		if (id != null && spaceRepo.existsById(id)) {
			return spaceRepo.findById(id).get();

		}
		logger.error("[" + id + "] not found you've passed a invalid space id.");
		return null;
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

	public List<VaultFolder> getFoldersByFolderId(Long folderId) {
		if (folderRepo.existsById(folderId)) {
			List<VaultFolder> folders = folderRepo.findByParentFolderId(folderId);
			for (VaultFolder folder : folders) {
				folder.setSpace(null);
				folder.setVaultUser(null);
				folder.setSection(null);
			}

			return folders;
		}
		return null;
	}

	public List<VaultFile> getFileByParentFolderId(Long folderId) {
		if (folderRepo.existsById(folderId)) {
			List<VaultFile> files = fileRepo.findByParentFolderId(folderId);
			for (VaultFile file : files) {
				file.setSpace(null);
				file.setVaultUser(null);
				file.setSection(null);
			}
			return files;
		}
		return null;
	}

	public JSONObject getFolderContentById(Long folderId) {
		List<VaultFile> files = getFileByParentFolderId(folderId);
		List<VaultFolder> folders = getFoldersByFolderId(folderId);
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

	public List<VaultFolder> getFolderWithNullParentBySpaceId(Long spaceId) {
		if (spaceRepo.existsById(spaceId)) {
			List<VaultFolder> folders = folderRepo.findBySpaceIdAndParentFolderIsNull(spaceId);
			for (VaultFolder folder : folders) {
				folder.setSpace(null);
				folder.setVaultUser(null);
				folder.setSection(null);
			}

			return folders;
		}
		return null;
	}

	public List<VaultFile> getFileWithNullParentBySpaceId(Long spaceId) {
		if (spaceRepo.existsById(spaceId)) {
			List<VaultFile> files = fileRepo.findBySpaceIdAndParentFolderIsNull(spaceId);
			for (VaultFile file : files) {
				file.setSpace(null);
				file.setVaultUser(null);
				file.setSection(null);
			}
			return files;
		}
		return null;
	}

	public JSONObject getFileAndFolderWithParentNullBySpaceId(Long spaceId) {
		List<VaultFile> files = getFileWithNullParentBySpaceId(spaceId);
		List<VaultFolder> folders = getFolderWithNullParentBySpaceId(spaceId);
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

	// -------------------------------------------------------------------------------------------------

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
		space = getSpaceById(spaceId);
		if (space == null) {
			// no space
			return null;
		}
		if (!isHimTheSpaceOwner(spaceId, userId)) {
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

	// -------------------------------------------------------------------------------------------------

	public JSONObject holdNewFolderRequest(Long userId, Long spaceId, Long parentId, String name) {
		JSONObject response = new JSONObject();
		Boolean isDone = false;
		if (!userAuthServ.existById(userId)) {
			return response.put("result", "no user found");
		}
		if (!spaceRepo.existsById(spaceId)) {
			return response.put("result", "invalid spaceId");
		}
		if (!isHimTheSpaceOwner(spaceId, userId)) {
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

	public VaultFile getFileById(Long id) {
		if (id != null && fileRepo.existsById(id)) {
			return fileRepo.findById(id).get();
		}
		logger.error("[" + id + "] not found you've passed a invalid file id.");
		return null;

	}

	public List<VaultSpace> getSpaceListByUserId(Long userId) {
		List<VaultSpace> spaces = spaceRepo.findByVaultUserId(userId);
		for (VaultSpace space : spaces) {
			space.setVaultUser(null);
		}
		return spaces;

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
				return handleSingleFileUpload(file, userId, spaceId, parentId);
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

	public JSONObject handleSingleFileUpload(MultipartFile file, Long userId, Long spaceId, Long parentId) {
		JSONObject response = new JSONObject();

		VaultSpace space = getSpaceById(spaceId);

		if (space == null) {
			return response.put("result", "invalid spaceId");
		}
		if (!isHimTheSpaceOwner(spaceId, userId)) {
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
		VaultSpace space = getSpaceById(spaceId);
		Boolean isDone = false;

		if (space == null || !isHimTheSpaceOwner(spaceId, userId)) {
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
			parentPath = getFullPathById(parentId);
			if (parentPath != null) {
				vaultFile.setParentFolder(getFolderById(parentId));
				completePath += parentPath;
				completePath += "/" + vaultFile.getName();
			} else {
				return null;
			}
		}
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

	// -------------------------------------------------------------------------------------------------

	public Resource holdDownloadRequest(Long userId, Long spaceId, Long folderId, Long fileId) {
		Resource resource;
		if (fileId != null) {
			resource = holdSingleDownloadRequest(userId, spaceId, fileId);
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

	private Resource holdSingleDownloadRequest(Long userId, Long spaceId, Long fileId) {
		VaultFile fileInfo;
		String path;
		Resource resource;
		if (userAuthServ.existById(userId)) {

			if (isHimTheSpaceOwner(spaceId, userId)) {

				path = "/" + userAuthServ.getUsernameById(userId) + "/" + spaceRepo.findNameById(spaceId);
				fileInfo = getFileById(fileId);
				if (fileInfo.getParentFolder() == null) {

					path += "/" + fileInfo.getName();
					resource = fileManagerServ.getFile(path);
				} else {
					path += getFullPathById(fileInfo.getParentFolder().getId());
					path += "/" + fileInfo.getName();
					resource = fileManagerServ.getFile(path);
				}
				return resource;
			}

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
