package com.enginius.playwrightautomatedtests.services.vault;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

@Service
public class VaultService implements VaultServiceInterface {

	@Autowired
	VaultCheckComponent checkComponent;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public Boolean doMegatest(String username, String password, String photoPath, Page page) {
		try {

			page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Rename")).click();
			page.getByPlaceholder("Name...").click();
			page.getByPlaceholder("Name...").fill("rinomino cartella");
			page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm")).click();
			page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Delete")).click();
			page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Delete")).click();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public Boolean doLogin(String username, String password, Page page, String url) {
		Boolean isDone = false;
		try {
			page.navigate(url);
			isDone = checkComponent.checkNavbar(page);
			page.locator("button:has-text('Login')").click();
			page.getByPlaceholder("Username").click();
			page.getByPlaceholder("Username").fill(username);
			page.getByPlaceholder("Username").press("Tab");
			page.getByPlaceholder("Password").fill(password);
			page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm")).click();
			page.waitForTimeout(4000);
			isDone = true;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return isDone;
	}

	public Boolean isAlreadyLogged(String username, Page page) {
		if (!checkComponent.checkNavbarLogged(username, page)) {
			return false;
		} else
			logger.info(username + " IS LOGGED IN");
		return true;
	}

	public Boolean enterInSpace(String spaceName, Page page) {
		try {
			page.getByRole(AriaRole.IMG, new Page.GetByRoleOptions().setName("Spaces")).click();
			page.locator("[data-space-name='" + spaceName + "']").click();
			page.waitForTimeout(1000);
			return checkComponent.checkSpaceName(spaceName, page);

		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}

	}

	@Override
	public Boolean doRegister(Page page) {
		try {

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}
	}

	@Override
	public Boolean doLogout(Page page, String username) {
		try {
			if (isAlreadyLogged(username, page)) {
				page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Logout")).click();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Boolean doNewSpace(String spaceName, Page page) {
		try {

			page.getByRole(AriaRole.IMG, new Page.GetByRoleOptions().setName("Spaces")).click();
			page.locator("button.grey-button:has-text('Add')").click();
			page.locator("input[placeholder='Name...']").fill(spaceName);
			page.locator("button:has-text('Confirm')").click();
			logger.info("NUOVO SPAZIO CREATO " + spaceName);
			return true;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return false;
	}

	@Override
	public Boolean doUploadPhoto(String spaceName, String folderName, Page page) {
		try {

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}
	}

	@Override
	public Boolean doVisualizePhoto(String spaceName, String folderName, String photoName, Page page) {
		try {

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}
	}

	@Override
	public Boolean doDownloadPhoto(String spaceName, String folderName, String photoName, Page page) {
		try {

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}
	}

	@Override
	public Boolean doNewFolder(String folderName, Page page) {
		try {
			page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add")).click();
			page.getByPlaceholder("Name...").click();
			page.getByPlaceholder("Name...").fill(folderName);
			page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm")).click();
			logger.info("DO NEW FOLDER DONE " + folderName);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}
	}

	public Boolean doEnterInFolder(String folderName, Page page) {
		try {
			logger.info("cerco " + folderName);
			page.locator("li.folder-item:has-text('" + folderName + "')").click();
			return checkComponent.checkFolderName(folderName, page);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Boolean doDeletePhoto(String photoName, Page page) {
		try {

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}
	}

	@Override
	public Boolean doDeleteFolder(String folderName, Page page) {
		try {

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}
	}

	@Override
	public Boolean doRenameFolder(String folderName, String newName, Page page) {
		try {

			page.locator("li[data-folder-name='" + folderName + "'] .rename-button").click();
			page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Rename")).click();
			page.getByPlaceholder("Name...").click();
			page.getByPlaceholder("Name...").fill(newName);
			page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm")).click();
			page.locator("[data-folder-name='" + newName + "']").isVisible();
			logger.info("FOLDER --" + folderName + "-- RENAMED IN --" + newName);
			page.locator("li[data-folder-name='" + newName + "'] .rename-button").click();
			page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Rename")).click();
			page.getByPlaceholder("Name...").click();
			page.getByPlaceholder("Name...").fill(folderName);
			page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm")).click();
			logger.info("FOLDER --" + newName + "-- RENAMED IN --" + folderName);
			return page.locator("[data-folder-name='" + folderName + "']").isVisible();

		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}
	}

	@Override
	public Boolean doRenamePhoto(String photoName, String newName, Page page) {
		try {

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}
	}

	@Override
	public Boolean doCreateFoldersLoop(List<String> names, Page page) {

		try {

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}
	}

	@Override
	public Boolean doNavigateFolderSpace(List<String> names, Page page) {
		try {

			// Verifica che il testo in <h2> sia quello atteso (se presente)
			String expectedH2Text = "Nome della sottocartella"; // Inserisci il testo che ti aspetti nell'h2
			if (page.locator("h2").isVisible()) {
				String actualH2Text = page.locator("h2").textContent();
				assert actualH2Text.equals(expectedH2Text) : "Test fallito: il testo di <h2> non corrisponde";
			} else {
				System.out.println("<h2> non è visibile.");
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;

		}
	}

	public String generateRandomString(int length) {
		String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder randomString = new StringBuilder();
		Random random = new Random();

		for (int i = 0; i < length; i++) {
			int randomIndex = random.nextInt(characters.length());
			randomString.append(characters.charAt(randomIndex));
		}

		return randomString.toString();
	}

}
