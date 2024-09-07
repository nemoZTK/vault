package com.enginius.playwrightautomatedtests.tests.experiments;

import java.util.Scanner;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.enginius.playwrightautomatedtests.services.PlayWrightServiceLogic;

@SpringBootTest
class PlaywrightApplicationTests {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	PlayWrightServiceLogic playService;

	@Test
	void contextLoads() {
		logger.debug("\n\n\n\n\n\n\t\t\t\t>INIZIO<");
		Scanner scanner = new Scanner(System.in);
		logger.debug("\n\n\n\n\n\n\nscrivi e invia per chiudere------------>");
		String sss = scanner.nextLine();

	}

	// *****************************************AVVIA*CODEGEN******************************************************************//
	// @Test
	void startCodegen() {
		String url = "https://app-test.eclassic.io";
		logger.info("_______________CODEGEN CALL- started_______________");
		playService.startCodegen(url);
		logger.info("fine");
	}
}
