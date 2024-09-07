package com.enginius.playwrightautomatedtests.services.vault;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

@Service
public class VaultService implements VaultServiceInterface {

	@Autowired
	VaultCheckComponent checkComponent;

	@Override
	public Boolean doLogin(String username, String password, Page page) {
		if (!checkComponent.checkNavbar(page)) {
			return false;
		}
		page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();

		if (!checkComponent.checkLoginAndRegisterForm(page)) {
			return false;
		}
		page.getByPlaceholder("Username").click();
		page.getByPlaceholder("Username").fill(username);
		page.getByPlaceholder("Username").press("Tab");
		page.getByPlaceholder("Password").fill(password);
		page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm")).click();
		page.waitForTimeout(4000);
//		page.navigate("http://localhost:8686/");
		if (!checkComponent.checkNavbarLogged(username, page)) {
			return false;
		}

//		page.getByRole(AriaRole.IMG, new Page.GetByRoleOptions().setName("Spaces")).isVisible();
//		page.getByRole(AriaRole.IMG, new Page.GetByRoleOptions().setName("Spaces")).click();
//		page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("S P A C E S")).isVisible();
		return true;
	}

	public Boolean isAlreadyLogged(String username, Page page) {
		if (!checkComponent.checkNavbarLogged(username, page)) {
			return false;
		} else
			return true;
	}

	@Override
	public Boolean doRegister(Page page) {

		return null;
	}

	@Override
	public Boolean doLogout(Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean doNewSpace(String spaceName, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean doNewFolder(String spaceName, String folderName, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean doUploadPhoto(String spaceName, String folderName, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean doVisualizePhoto(String spaceName, String folderName, String photoName, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean doDownloadPhoto(String spaceName, String folderName, String photoName, Page page) {
		// TODO Auto-generated method stub
		return null;
	}

}
