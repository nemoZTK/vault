package com.project.vault.services;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.project.vault.models.Folder;
import com.project.vault.models.Space;
import com.project.vault.models.VaultUser;
import com.project.vault.repos.FileRepository;
import com.project.vault.repos.FolderRepository;
import com.project.vault.repos.SectionRepository;
import com.project.vault.repos.SpaceRepository;

import lombok.Data;

@Data
@Service
public class StorageService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Value("${folder.main.path}")
	private String basePath;

	@Autowired
	FolderRepository folderRepo;
	@Autowired
	SpaceRepository spaceRepo;
	@Autowired
	SectionRepository sectionRepo;
	@Autowired
	FileRepository fileRepo;

	public Boolean createNewSpace(String userMainPath, String spacePath) {
		String fullPath = basePath + userMainPath + spacePath;
		logger.info("Creating new space folder in " + fullPath);
		return (createFolder(fullPath)) ? true : false;
	}

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

	/**
	 * 
	 * 
	 * @param name
	 * @param user
	 * @return a id or null if error
	 */
	public Long holdNewSpaceRequest(String name, VaultUser user) {
		Boolean isDone = false;
		Space space = new Space();
		space.setCreatedAt(LocalDateTime.now());
		space.setName(name);
		space.setPath("/" + space.getName());

		space.setVaultUser(user);

		isDone = createNewSpace(user.getPath(), space.getPath());
		if (isDone)
			try {
				spaceRepo.save(space);
				return space.getId();
			} catch (Exception e) {
				e.printStackTrace();
			}
		return null;

	}

	/**
	 * 
	 * @param user
	 * @param spaceId
	 * @param parentId
	 * @param name
	 * @return a id or null if not found
	 */
	public Long holdNewFolderRequest(VaultUser user, Long spaceId, Long parentId, String name) {
		Folder folder = new Folder();
		folder.setCreatedAt(LocalDateTime.now());
		folder.setName(name);
		folder.setVaultUser(user);
		String completePath = user.getPath();
		Space space;
		try {
			space = spaceRepo.findById(spaceId).get();
		} catch (Exception e) {
			logger.error("you've passed a invalid space id. creation failed");
			e.printStackTrace();
			return null;

		}
		folder.setSpace(space);
		completePath += space.getPath();
		folder.setPath("/" + name);
		if (parentId != null) {
			Folder parentFolder = null;
			try {
				parentFolder = folderRepo.findById(parentId).get();
			} catch (Exception e) {
				logger.error("you've passed a invalid parent id. creation failed");
				e.printStackTrace();
				return null;
			}
			folder.setParentFolder(parentFolder);
			completePath += getFullPathById(parentId);
			completePath += folder.getPath();
		} else {
			folder.setParentFolder(null);
			completePath += folder.getPath();
		}
		try {
			createFolder(basePath + completePath);
			logger.info("---saving-folder-in-db");
			folderRepo.save(folder);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return folder.getId();
	}

	public String getFullPathById(Long folderId) {
		Optional<Folder> folderOptional = folderRepo.findById(folderId);

		if (folderOptional.isEmpty()) {
			return null;
		}

		Folder folder = folderOptional.get();
		StringBuilder fullPath = new StringBuilder(folder.getPath());

		while (folder.getParentFolder() != null) {
			folder = folder.getParentFolder();
			fullPath.insert(0, folder.getPath());
		}

		return fullPath.toString();
	}
}
