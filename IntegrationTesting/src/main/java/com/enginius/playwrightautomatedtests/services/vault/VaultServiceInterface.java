package com.enginius.playwrightautomatedtests.services.vault;

import com.microsoft.playwright.Page;

public interface VaultServiceInterface {

	Boolean doLogin(String username, String password, Page page);

	Boolean doRegister(Page page);

	Boolean doLogout(Page page);

	Boolean doNewSpace(String spaceName, Page page);

	Boolean doNewFolder(String spaceName, String folderName, Page page);

	Boolean doUploadPhoto(String spaceName, String folderName, Page page);

	Boolean doVisualizePhoto(String spaceName, String folderName, String photoName, Page page);

	Boolean doDownloadPhoto(String spaceName, String folderName, String photoName, Page page);
}
