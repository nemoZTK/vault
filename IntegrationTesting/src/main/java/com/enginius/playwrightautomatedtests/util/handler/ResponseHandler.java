package com.enginius.playwrightautomatedtests.util.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;

public class ResponseHandler {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	final Map<String, String> tempResponses = new HashMap<String, String>();
	private final String baseUrlToSave;
	private final Consumer<Response> handler;
	boolean isRedirect = false;

	public ResponseHandler(String baseUrlToSave) {
		this.baseUrlToSave = baseUrlToSave;
		this.handler = (httpResponse) -> {
			String requestMethod = httpResponse.request().method();
			String url = httpResponse.url();
			String responseBody = null;
			String requestBody = null;
			requestBody = httpResponse.request().postData();
			if (url.contains(baseUrlToSave) && httpResponse.status() != 500) {
				try {
					try {
						responseBody = new String(httpResponse.body());
					} catch (Exception e) {
						logger.info("body not obtenible for " + url + " [" + requestMethod + " " + httpResponse.status()
								+ " ]");
					}
				} finally {
					if (requestBody != null && requestBody.isBlank() && requestBody.isEmpty()
							&& requestBody.length() > 4) {
						logger.info("Br " + httpResponse.status() + ": " + requestBody);
					}
					if (responseBody != null && responseBody.length() > 4) {
						logger.debug("b [" + requestMethod + " " + httpResponse.status() + "] <----- " + url
								+ "  body----->" + responseBody);
						tempResponses.put(url, httpResponse.text());
					}
				}
			}
			if (httpResponse.status() == 500) {

				responseBody = httpResponse.text();
				if (requestBody != null && requestBody.length() > 2) {
					logger.error("Body richiesta 500 : " + requestBody + " ");
				}
				logger.error("  [" + requestMethod + "] 500-ERRORE chiamata----->" + url + " non riuscita");
				if (responseBody != null && !responseBody.isEmpty() && !responseBody.isBlank()
						&& httpResponse.text().length() > 5) {

					logger.error("Body della risposta " + httpResponse.status() + ": " + responseBody);
				}
//				assert false;
			} else if (url.contains(baseUrlToSave)) {
				if (httpResponse == null || httpResponse.text().isEmpty() || httpResponse.text().isBlank()
						|| httpResponse.text().length() < 5) {
					logger.debug("RISPOSTA VUOTA PER---> [" + requestMethod + "] " + url);
				}
			}
		};

	}

	public Consumer<Response> getHandler() {
		return handler;
	}

	public String getResponse(List<String> mustContain, List<String> mustNotContain, Page page) {

		boolean bodyFound = false;
		while (!bodyFound) {
			Optional<String> bodyString;
			// cerco nella mappa tempResponses la chiamata ai topic così ne prendo uno
			if (mustNotContain == null || mustNotContain.isEmpty()) {
				bodyString = tempResponses.entrySet().stream()
						.filter(couple -> mustContain.stream().allMatch(couple.getKey()::contains))
						.map(Map.Entry::getValue).findFirst();
			} else {
				bodyString = tempResponses.entrySet().stream()
						.filter(couple -> mustContain.stream().allMatch(couple.getKey()::contains)
								&& mustNotContain.stream().noneMatch(couple.getKey()::contains))
						.map(Map.Entry::getValue).findFirst();
			}
			if (bodyString.isPresent()) {
				bodyFound = true;
				logger.info(" ECCOLO----->" + bodyString.get());
				// PER FAVORE NON CAMBIARLO, SE NON TI PIACE ELIMINALO E BASTA
				logger.info(">>>>>>>>>>RESPONSE OTTENUTA PER " + mustContain + " <<<<<<<<<<");
				return bodyString.get();

			} else {

				page.waitForTimeout(1000);
			}
			// boh.. non ha ancora finito di fare la chiamata ai topic
			logger.error("non trovo body per " + mustContain);
		}

		return null;
	}

}
