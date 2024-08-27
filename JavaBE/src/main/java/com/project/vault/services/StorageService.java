package com.project.vault.services;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.vault.models.dto.FolderPathDTO;
import com.project.vault.models.entities.VaultFile;
import com.project.vault.models.entities.VaultFolder;
import com.project.vault.models.entities.VaultSpace;
import com.project.vault.models.entities.VaultUser;
import com.project.vault.repos.FileRepository;
import com.project.vault.repos.FolderRepository;
import com.project.vault.repos.SpaceRepository;

import lombok.Data;

@Data
@Service
public class StorageService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Value("${folder.main.path}")
	private String basePath;
//	@Autowired
//	AuthenticationService authServ;
	@Autowired
	FolderRepository folderRepo;
	@Autowired
	SpaceRepository spaceRepo;
//	@Autowired
//	SectionRepository sectionRepo;
	@Autowired
	FileRepository fileRepo;

	// -------------------------------------------------------------------------------------------------------------------

	private Boolean createFolder(String completePath) {
		File folder = new File(completePath);
		logger.trace("Creating folder at: " + folder.getPath());
		if (!folder.exists()) {
			Boolean isCreated = folder.mkdirs();
			if (isCreated) {
				logger.info("Folder created successfully at: " + folder.getPath());
				return true;
			} else {
				logger.error("Failed to create folder at: " + folder.getPath());
				return false;
			}
		} else {
			logger.warn("Folder already exists at: " + folder.getPath());
			return true;
		}
	}

	public Boolean createUserMainFolder(String userMainPath) {
		String fullPath = basePath + userMainPath;
		logger.info("Creating user main folder in " + fullPath);
		return (createFolder(fullPath)) ? true : false;

	}

	public Boolean createNewSpace(String userMainPath, String spacePath) {
		String fullPath = basePath + userMainPath + spacePath;
		logger.info("Creating new space folder in " + fullPath);
		return (createFolder(fullPath)) ? true : false;
	}

	public String getFullPathById(Long folderId) {
		if (!folderRepo.existsById(folderId)) {
			return null;
		}
		StringBuilder fullPath = new StringBuilder();

		Long currentId = folderId;
		while (currentId != null) {
			FolderPathDTO folderDto = folderRepo.findPathAndParentById(currentId);
			if (folderDto == null) {
				break;
			}
			fullPath.insert(0, folderDto.getPath());
			currentId = folderDto.getParentId();
		}

		return fullPath.toString();
	}
	// ---------------------------------------SPACE----------------------------------------------------------------------------

	/**
	 * 
	 * @param name (new space name)
	 * @param user (VaultUser)
	 * @return JSONObject (response)
	 */
	public JSONObject holdNewSpaceRequest(String name, VaultUser user) {
		JSONObject response = new JSONObject();
		Boolean isDone = false;
		VaultSpace space = new VaultSpace();
		space.setCreatedAt(LocalDateTime.now());
		space.setName(name);
		space.setPath("/" + space.getName());
		space.setVaultUser(user);
		isDone = createNewSpace(user.getPath(), space.getPath());
		if (isDone) {
			try {
				spaceRepo.save(space);
				return response.put("result", "done").put("id", space.getId()).put("name", name);
			} catch (Exception e) {
				e.printStackTrace();
				// TODO:delete folder
				return response.put("result", "save in db failed for space request " + name);

			}
		}
		return response.put("result", "local folder creation failed for space request " + name);
	}

	public VaultSpace getSpaceById(Long id) throws Exception {
		VaultSpace space;
		try {
			space = spaceRepo.findById(id).get();
		} catch (Exception e) {
			logger.error("[" + id + "] not found you've passed a invalid space id.");
			e.printStackTrace();
			throw new Exception("space id not found " + id);
		}
		return space;
	}

	// --------------------------------FOLDER-----------------------------------------------------------------------------------

	public VaultFolder getFolderById(Long id) throws Exception {
		VaultFolder parentFolder = null;
		try {
			parentFolder = folderRepo.findById(id).get();
		} catch (Exception e) {
			logger.error("you've passed a invalid parent id -" + id);
			throw new Exception("parent folder id not found" + id);
		}
		return parentFolder;
	}

	public JSONObject holdNewFolderRequest(VaultUser user, Long spaceId, Long parentId, String name) {
		JSONObject response = new JSONObject();
		VaultSpace space;
		try {
			space = getSpaceById(spaceId);
		} catch (Exception e) {
			e.printStackTrace();
			return response.put("result", "invalid spaceId");
		}
		VaultFolder folder = new VaultFolder();
		folder.setCreatedAt(LocalDateTime.now());
		folder.setVaultUser(user);
		folder.setSpace(space);
		folder.setName(name);
		folder.setPath("/" + name);
		String completePath = user.getPath() + space.getPath();
		if (parentId != null) {

			try {
				VaultFolder parentFolder = getFolderById(parentId);
				folder.setParentFolder(parentFolder);
			} catch (Exception e) {
				return response.put("result", "invalid parentId");
			}

			String parentPath = getFullPathById(parentId);
			if (parentPath != null) {
				completePath += parentPath;
				completePath += folder.getPath();
			} else {
				return response.put("result", "error in parent folder recreation");
			}
		} else {
			folder.setParentFolder(null);
			completePath += folder.getPath();
		}
		Boolean isDone = createFolder(basePath + completePath);
		if (isDone) {
			try {
				logger.info("---saving-folder-in-db");
				folderRepo.save(folder);
				return response.put("id", folder.getId()).put("name", folder.getName());
			} catch (Exception e) {
				e.printStackTrace();
				// TODO:delete folder
				return response.put("result", "error during folder db saving");
			}
		} else {

			return response.put("result", "error during folder local saving");
		}
	}

	// ------------------------------------UPLOAD-------------------------------------------------------------------------------

//	public String getFullPathAndAssign

	public JSONObject holdUploadRequest(VaultUser user, Long spaceId, Long parentId, MultipartFile file) {
		JSONObject response = new JSONObject();
		try {
			boolean isZipFile = file.getOriginalFilename().endsWith(".zip");

			if (isZipFile) {
				logger.info("-------" + file.getOriginalFilename() + "is a zip file");
				return handleZipUpload(file, user, spaceId, parentId);
			} else {
				logger.info("-------" + file.getOriginalFilename() + "is single file");
				return handleSingleFileUpload(file, user, spaceId, parentId);
			}

		} catch (Exception e) {
			logger.error("error in upload");

			e.printStackTrace();
			return response.put("result", "error in upload");

		}
	}

	private JSONObject handleZipUpload(MultipartFile file, VaultUser user, Long spaceId, Long parentId) {
		JSONObject response = null;
		return response.put("result", "");

	}

	private JSONObject handleSingleFileUpload(MultipartFile file, VaultUser user, Long spaceId, Long parentId) {
		JSONObject response = new JSONObject();
		VaultFile vaultFile = new VaultFile();
		vaultFile.setCreatedAt(LocalDateTime.now());
		vaultFile.setVaultUser(user);
		String completePath = basePath + user.getPath();
		try {
			VaultSpace space = spaceRepo.findById(spaceId).get();
			vaultFile.setSpace(space);
			completePath += space.getPath();
		} catch (Exception spaceError) {
			return response.put("result", "error- space not found");
		}
		vaultFile.setName(file.getOriginalFilename());
		vaultFile.setPath("/" + vaultFile.getName());
		vaultFile.setSize(file.getSize());
		vaultFile.setType(file.getContentType());

		if (parentId != null) {
			String parentPath = getFullPathById(parentId);
			if (parentPath != null) {
				completePath += parentPath;
			} else {
				return response.put("result", "error in parent folder recreation");
			}
		} else {

			vaultFile.setFolder(null);
		}
		Path absolutePath = Paths.get(completePath, vaultFile.getName());
		try {
			file.transferTo(absolutePath);
		} catch (Exception localSaveError) {
			localSaveError.printStackTrace();
			return response.put("result", "saving failed");
		}
		try {
			fileRepo.save(vaultFile);
			return response.put("result", "done").put("name", vaultFile.getName()).put("id", vaultFile.getId());
		} catch (Exception dbSaveError) {
			return response.put("result", "error during folder db saving");
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
