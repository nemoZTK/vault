package com.enginius.playwrightautomatedtests.tests.examples;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;

public class PlaywrightTest {
	static Playwright playwright;
	static Browser stdBrowser;
	static BrowserContext stdContext;
	static Page page;

	@BeforeAll
	static void init() {
		System.out.println("Initializing Playwright and browser...");
		playwright = Playwright.create();
		page = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false)).newContext().newPage();
	}

	@AfterAll
	static void closeBrowser() {
		System.out.println("Closing browser...");
		stdContext.close();
		stdBrowser.close();
		playwright.close();
	}

	@Test
	public void testWikipediaSearch() {
		page.navigate("https://www.wikipedia.org/");
		page.getByLabel("Search Wikipedia").click();
		page.getByLabel("Search Wikipedia").fill("wikipedia");
		page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Wikipedia Free online")).click();
		page.waitForTimeout(4000);
		String pageTitle = page.title();
		System.out.println("Pagina attuale: " + pageTitle);
	}
}
