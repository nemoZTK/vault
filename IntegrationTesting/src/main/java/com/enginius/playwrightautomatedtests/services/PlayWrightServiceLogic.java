package com.enginius.playwrightautomatedtests.services;

import java.io.IOException;

import org.springframework.stereotype.Component;

@Component
public class PlayWrightServiceLogic {

	// ************************************************************************************************************//
	public void startCodegen(String url) {
		if (url == null) {
			url = "google.com";
		}
		String npxPath = "C:\\Program Files\\nodejs\\npx.cmd";
		ProcessBuilder processBuilder = new ProcessBuilder(npxPath, "playwright", "codegen", url,
				" --viewport-size=415,968 ");
		processBuilder.redirectErrorStream(true);
		try {
			Process process = processBuilder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
