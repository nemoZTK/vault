package com.project.vault.services.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileManagerInterface {

	/**
	 * crate a folder with the given path, added to the base path
	 * 
	 * @param knownPath (es. path/to/folderName)
	 * @return true or false based on result
	 */
	Boolean createFolder(String knownPath);

	/**
	 * save the given file in the given path, added to the base path
	 * 
	 * @param knownPath (es. path/to/folderName)
	 * @param file
	 * @return true or false based on result
	 */
	Boolean saveFile(String knownPath, MultipartFile file);

	/**
	 * get a file from the given path, added to the base path
	 * 
	 * @param knownPath (es. path/to/folderName)
	 * @return the resource asked
	 */
	Resource getFile(String knownPath);

	/**
	 * rename the file founded at the given path ( path+file name) added to the base
	 * path
	 * 
	 * @param knownPath   (es. path/to/fileName)
	 * @param newFileName
	 * @return true or false based on result
	 */
	Boolean renameFile(String knownPath, String newFileName);

	/**
	 * delete a folder and all the content inside founded at the given path,added to
	 * the base path
	 * 
	 * @param knownPath (es. path/to/folderName)
	 * @return true or false based on result
	 */
	Boolean deleteFolder(String knownPath);

	/**
	 * delete a file founded at given the path,added to the base path
	 * 
	 * @param knownPath(es. path/to/fileName)
	 * @return true or false based on result
	 */
	Boolean deleteFile(String knownPath);

	/**
	 * rename a folder founded at the given path, added to the base path
	 * 
	 * @param knownPath     (es. path/to/folderName)
	 * @param newFolderName
	 * @return true or false based on result
	 */
	Boolean renameFolder(String knownPath, String newFolderName);

}
