package com.enginius.playwrightautomatedtests.tests.experiments;

import java.util.Map;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;

public class UnusedMethods {
	private String bearer = "non_valido";

	private String getBearerToken(BrowserContext browser, String link) {
		String linkPost = link + "/web-api/topic?offset=0&limit=20&categoryId=1";
		Page resPage = browser.newPage();

		resPage.onRequest(httpRequest -> {
			System.out.println(httpRequest.url());

			String responseUrl = httpRequest.url();
			if (responseUrl.contains("/web-api/topic")) {
				bearer = null;
				byte[] resBody = httpRequest.response().body();
				String bho = new String(resBody);
				System.out.println("\n\ndovrei esser un body json in teoria " + bho);
				Map<String, String> headers = httpRequest.headers();
				if (headers.containsKey("authorization")) {
					bearer = headers.get("authorization");
				}

				if (bearer != null && bearer.startsWith("Bearer ")) {
					bearer = bearer.substring(7);
					System.out.println("Bearer Token: " + bearer);
				}
			}
		});
		Response s = resPage.navigate(linkPost);
		System.out.println(s.request().headerValue("authorization"));
		System.out.println(s.headerValue("authorization"));
		return bearer;
	}
}
/*
page.onRequest(httpRequest -> {
	System.out.println("REQ -----> " + httpRequest.url());
});
*/

/*
page.onResponse(httpResponse -> {
	if (httpResponse.url().contains("/web-api")) {
		System.out.println("RES <----- " + httpResponse.url());
		try {
			System.out.println(new String(httpResponse.body()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
});
*/
// v2
/*
  	
private String getBearerToken(BrowserContext browser) {

	Page resPage = browser.newPage();

	resPage.onRequest(httpRequest -> {
		System.out.println(httpRequest.url());

		String responseUrl = httpRequest.url();
		logger.debug(responseUrl);
		if (responseUrl.contains("/web-api/topic")) {

			bearer = null;
			byte[] resBody = httpRequest.response().body();
			String bho = new String(resBody);
			Map<String, String> headers = httpRequest.headers();
			if (headers.containsKey("authorization")) {
				bearer = headers.get("authorization");
			}

			if (bearer != null && bearer.startsWith("Bearer ")) {
				bearer = bearer.substring(7);
				logger.debug("Bearer Token: " + bearer);
			}
		}
	});
	return bearer;}
*/
