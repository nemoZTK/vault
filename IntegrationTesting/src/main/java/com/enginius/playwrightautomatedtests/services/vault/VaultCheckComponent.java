package com.enginius.playwrightautomatedtests.services.vault;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

@Component
public class VaultCheckComponent implements VaultCheckComponentInterface {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Boolean checkNavbar(Page page) {
		List<Boolean> visibilityChecks = Arrays.asList(
				page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Vault")).isVisible(),
				page.locator("button:has-text('Login')").isVisible(),
				page.locator("button:has-text('Sign-up')").isVisible());
		return visibilityChecks.stream().allMatch(Boolean::booleanValue);
	}

	@Override
	public Boolean checkLoginAndRegisterForm(Page page) {
		List<Boolean> registerFormChecks;
		List<Boolean> loginChecks = Arrays.asList(page.getByPlaceholder("Username").isVisible(),
				page.getByPlaceholder("Password").isVisible(),
				page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm")).isVisible(),
				page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Cancel")).isVisible(),
				page.locator("button:has-text('Sign-up')").isVisible());

		boolean isAllVisible = loginChecks.stream().allMatch(Boolean::booleanValue);

		if (isAllVisible) {
			page.locator("button:has-text('Sign-up')").click();

			registerFormChecks = Arrays
					.asList(page.getByPlaceholder("Username").isVisible(), page.getByPlaceholder("Email").isVisible(),
							page.getByPlaceholder("Password", new Page.GetByPlaceholderOptions().setExact(true))
									.isVisible(),
							page.getByPlaceholder("Confirm Password").isVisible(),
							page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm")).isVisible(),
							page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Camcel")).isVisible(),
							page.getByRole(AriaRole.PARAGRAPH)
									.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Login"))
									.isVisible());

			isAllVisible = registerFormChecks.stream().allMatch(Boolean::booleanValue);

			if (isAllVisible) {
				page.getByRole(AriaRole.PARAGRAPH)
						.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Login")).click();
			}
		}

		return isAllVisible;
	}

	@Override
	public Boolean checkNavbarLogged(String username, Page page) {

		Locator welcomeText = page.locator(".welcome-container .welcome-text");
		Locator usernameText = welcomeText.locator("span:last-child");

		List<Boolean> visibilityChecks = Arrays.asList(page.getByText(username).isVisible(), welcomeText.isVisible(),
				page.getByText("Welcome").isVisible(), page.locator("button.logout-button").isVisible());

		boolean isUsernameCorrect = usernameText.isVisible() && usernameText.textContent().trim().equals(username);
		boolean allChecksPass = visibilityChecks.stream().allMatch(Boolean::booleanValue);

		return allChecksPass && isUsernameCorrect;
	}

	public Boolean checkSpaceName(String spaceName, Page page) {

		String actualH1Text = page.locator("#space-name").textContent();
		if (actualH1Text.equals(spaceName)) {

			logger.info("ho trovato " + spaceName + " in " + actualH1Text);
			return true;
		}
		logger.error("NON ho trovato " + spaceName + " in " + actualH1Text);
		return false;
	}

	public Boolean checkFolderName(String folderName, Page page) {

		String actualH2Text = page.locator("#actual-folder").textContent();

		if (actualH2Text.equals(folderName)) {

			logger.info("ho trovato " + folderName + " in " + actualH2Text);
			return true;
		}
		logger.error("NON ho trovato " + folderName + " in " + actualH2Text);
		return false;
	}
}
