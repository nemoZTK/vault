package com.enginius.playwrightautomatedtests.services.vault;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

@Component
public class VaultCheckComponent implements VaultCheckComponentInterface {

	@Override
	public Boolean checkNavbar(Page page) {
		List<Boolean> visibilityChecks = Arrays.asList(
				page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Vault")).isVisible(),
				page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).isVisible(),
				page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign-up")).isVisible());
		return visibilityChecks.stream().allMatch(Boolean::booleanValue);
	}

	@Override
	public Boolean checkLoginAndRegisterForm(Page page) {
		List<Boolean> registerFormChecks;
		List<Boolean> loginChecks = Arrays.asList(page.getByPlaceholder("Username").isVisible(),
				page.getByPlaceholder("Password").isVisible(),
				page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm")).isVisible(),
				page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Cancel")).isVisible(),
				page.getByRole(AriaRole.PARAGRAPH)
						.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Sign-up")).isVisible());

		boolean isAllVisible = loginChecks.stream().allMatch(Boolean::booleanValue);

		if (isAllVisible) {
			page.getByRole(AriaRole.PARAGRAPH)
					.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Sign-up")).click();

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

		List<Boolean> visibilityChecks = Arrays.asList(page.getByText(username).isVisible(),
				page.getByText("Welcome").isVisible(),
				page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Logout")).isVisible());

		return visibilityChecks.stream().allMatch(Boolean::booleanValue);
	}

}
