package com.enginius.playwrightautomatedtests.tests.experiments;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import com.enginius.playwrightautomatedtests.util.handler.ResponseHandler;
import com.microsoft.playwright.Response;

public class HandlerTest {

	@Test
	void doTest() {
		ResponseHandler rh = new ResponseHandler("test");
		Consumer<Response> handler1 = rh.getHandler();
		System.out.println("h1: " + handler1);
		Consumer<Response> handler2 = rh.getHandler();
		System.out.println("h2: " + handler2);
		Consumer<Response> handler3 = rh.getHandler();
		System.out.println("h3: " + handler3);

		assert handler1 == handler2 && handler2 == handler3;
	}
// QUESTE RIGHE MESSE IN UN TEST SALVANO I COOKIE DI SESSIONE PER RIUSARLI IN UN SUCCESSIVO TEST
//	Path storageStatePath = Paths.get("src\\test\\resources\\storageState.json");
//	stdContext.storageState(new BrowserContext.StorageStateOptions().setPath(storageStatePath));
//	logger.info("Session state saved to: " + storageStatePath.toString());

}
