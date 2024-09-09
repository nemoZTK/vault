package com.enginius.playwrightautomatedtests.tests;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.enginius.playwrightautomatedtests.services.vault.VaultService;
import com.enginius.playwrightautomatedtests.util.ConsoleBeautifying;
import com.enginius.playwrightautomatedtests.util.handler.ResponseHandler;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;

@SpringBootTest
@TestPropertySource("classpath:test-vault-project.properties")
public class VaultProjectTest {
	@Autowired
	VaultService vaultServ;
	@Autowired
	ConsoleBeautifying beautyCService;

	// -----------------------------------------------------------------------------------
	@Value("${playwright.hideBrowser}")
	boolean hideBrowser;

	@Value("${vault.testUsername}")
	String username;

	@Value("${vault.testPassword}")
	String password;
	@Value("${vault.spaceName}")
	String spaceName;
	@Value("${vault.folderName}")
	String folderName;
	@Value("${vault.baselink}")
	String baseLink;

	@Value("${vault.be.link}")
	String beLink;
	// -----------------------------------------------------------------------------------
	boolean isTestPassed = false;
	static Playwright playwright;
	static Browser stdBrowser;
	static BrowserType.LaunchOptions options;
	BrowserContext stdContext;
	boolean isStdContextOpen = false;
	Page page;
	ResponseHandler responseHandler;
	// -----------------------------------------------------------------------------------

	private static final Logger logger = LoggerFactory.getLogger(VaultProjectTest.class);

	@BeforeAll
	static void launchPlaywright() {
		logger.debug(":::::::::: beforeAll ::::::::::");
		playwright = Playwright.create();
		logger.debug("playwright created");
		options = new BrowserType.LaunchOptions();
		logger.debug(":::::::::: options launched ::::::::::");
	}

	@AfterAll
	static void closePlaywright() {
		playwright.close();
		logger.debug(":::::::::: playwright closed ::::::::::");
	}

	@BeforeEach
	void createContextAndPage() {
		logger.info(":::::::::: before each ::::::::::");
		logger.info("value " + hideBrowser);
		options.setHeadless(hideBrowser);
		String mode = (hideBrowser) ? "headless" : "show browser";
		logger.info("Browser set in " + mode + " mode");
		stdBrowser = playwright.firefox().launch(options);

		stdContext = stdBrowser.newContext();
		logger.info("-std browser STARTED with -->" + stdContext.browser().browserType().name());
		isStdContextOpen = true;
		stdContext.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(true));
		logger.info("tracer for stdBrowsar created");
		page = stdContext.newPage();
		responseHandler = new ResponseHandler("/api/");

		page.onResponse(responseHandler.getHandler());
		logger.info(":::::::::: hook handler attached ::::::::::");
	}

	@AfterEach
	void closeContext() {
		logger.info(":::::::::: after each ::::::::::");
		Path path = Paths.get("src\\test\\resources\\vaultTrace.zip");
		if (isStdContextOpen) {
			stdContext.tracing().stop(new Tracing.StopOptions().setPath(path));
			stdContext.close();
			logger.info(":::::::::: context closed ::::::::::");
		} else {
			logger.info(":::::::::: context was already closed ::::::::::");
		}
	}
	// -----------------------------------------------------------------------------------

	@Test
	public void doLoginAndCheckThenLogout() {
		try {
			logger.info("TEST < doLoginAndCheckThenLogout > STARTED");
			isTestPassed = vaultServ.doLogin(username, password, page, baseLink);
			isTestPassed = (vaultServ.isAlreadyLogged(username, page)) ? vaultServ.doLogout(page, username) : false;
			beautyCService.printResult(isTestPassed, "DO LOGIN AND CHECK FORMS");

		} catch (Exception e) {
			isTestPassed = false;
			beautyCService.printResult(isTestPassed, "DO LOGIN AND CHECK FORMS");
			e.printStackTrace();
		}
		logger.info("TEST > doLoginAndCheckThenLogout < ENDED");
	}

	@Test
	public void doLoginAndCreateAnewSpace() {
		try {
			logger.info("TEST < doLoginAndCreateAnewSpace > STARTED");
			isTestPassed = vaultServ.doLogin(username, password, page, baseLink);
			String spaceName = "T-" + vaultServ.generateRandomString(5);
			isTestPassed = (vaultServ.isAlreadyLogged(username, page)) ? vaultServ.doNewSpace(spaceName, page) : false;
			isTestPassed = vaultServ.enterInSpace(spaceName, page);
			beautyCService.printResult(isTestPassed, "DO LOGIN AND CHECK FORMS");

		} catch (Exception e) {
			isTestPassed = false;
			beautyCService.printResult(isTestPassed, "DO LOGIN AND CHECK FORMS");
			e.printStackTrace();
		}
		logger.info("TEST > doLoginAndCreateAnewSpace < ENDED");
	}

	@Test
	public void doLoginEnterSpaceAndCreateNewFolder() {
		try {
			logger.info("TEST < doLoginAndCreateAnewSpace > STARTED");

			isTestPassed = vaultServ.doLogin(username, password, page, baseLink);
			String folderName = "T-" + vaultServ.generateRandomString(5);
			isTestPassed = (vaultServ.isAlreadyLogged(username, page)) ? vaultServ.enterInSpace(spaceName, page)
					: false;
			isTestPassed = (vaultServ.doNewFolder(folderName, page)) ? vaultServ.doEnterInFolder(folderName, page)
					: false;
			beautyCService.printResult(isTestPassed, "DO LOGIN AND CHECK FORMS");
		} catch (Exception e) {
			isTestPassed = false;
			beautyCService.printResult(isTestPassed, "DO LOGIN AND CHECK FORMS");
			e.printStackTrace();
		}
		logger.info("TEST > doLoginAndCreateAnewSpace < ENDED");
	}

	@Test
	public void doLoginEnterSpaceAndRenameFolder() {
		try {
			logger.info("TEST < doLoginEnterSpaceAndRenameFolder > STARTED");

			isTestPassed = vaultServ.doLogin(username, password, page, baseLink);
			isTestPassed = (vaultServ.isAlreadyLogged(username, page)) ? vaultServ.enterInSpace(spaceName, page)
					: false;
			isTestPassed = (vaultServ.doEnterInFolder(folderName, page))
					? vaultServ.doRenameFolder(folderName, folderName, page)
					: false;
			beautyCService.printResult(isTestPassed, "DO LOGIN AND CHECK FORMS");
		} catch (Exception e) {
			isTestPassed = false;
			beautyCService.printResult(isTestPassed, "DO LOGIN AND CHECK FORMS");
			e.printStackTrace();
		}

		logger.info("TEST > doLoginEnterSpaceAndRenameFolder < ENDED");

	}

}
