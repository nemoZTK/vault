package com.enginius.playwrightautomatedtests.services.vault;

import java.util.List;

import com.microsoft.playwright.Page;

public interface VaultServiceInterface {

	Boolean doLogin(String username, String password, Page page, String url);

	Boolean doRegister(Page page);

	Boolean doLogout(Page page, String username);

	Boolean doNewSpace(String spaceName, Page page);

	Boolean doNewFolder(String folderName, Page page);

	Boolean doDeletePhoto(String photoName, Page page);

	Boolean doDeleteFolder(String folderName, Page page);

	Boolean doRenameFolder(String folderName, String newName, Page page);

	Boolean doRenamePhoto(String photoName, String newName, Page page);

	Boolean doUploadPhoto(String spaceName, String folderName, Page page);

	Boolean doVisualizePhoto(String spaceName, String folderName, String photoName, Page page);

	Boolean doDownloadPhoto(String spaceName, String folderName, String photoName, Page page);

	Boolean doCreateFoldersLoop(List<String> names, Page page);

	Boolean doNavigateFolderSpace(List<String> names, Page page);
}
