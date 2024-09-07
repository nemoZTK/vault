package com.enginius.playwrightautomatedtests.services.vault;

import com.microsoft.playwright.Page;

public interface VaultCheckComponentInterface {

	Boolean checkNavbar(Page page);

	Boolean checkLoginAndRegisterForm(Page page);

	Boolean checkNavbarLogged(String username, Page page);
}
