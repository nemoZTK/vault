package com.enginius.playwrightautomatedtests.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConsoleBeautifying {

	// -------------------------------------------------------------------COSTANTI-VARIE--------------------------------------------------------------------------
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * create a spacer with given charater and length, for standard measures use
	 * 
	 * -1 for default measure(120) -2 for little measures(60)
	 * 
	 * @param c
	 * @param length
	 * @return
	 */
	public String createCustomSpacer(Character c, int length) {
		switch (length) {
			case -1:
				length = 120;
				break;
			case -2:
				length = 60;
				break;
			default:
				break;
		}
		String spacer = "";
		String builder = "";
		for (int i = 0; i < length; i++) {
			builder += c;
		}
		spacer = "\n" + builder + "\n\t\t\t";
		return spacer;
	}

	public String printResult(boolean isTestPassed, String testName) {

		if (isTestPassed) {

			logger.info("\n\n\t\t\t" + testName + "\n" + PASSATO);
			return "\n" + PASSATO;
		} else {
			logger.error("\n\n\t\t\t" + testName + "\n" + FALLITO);
			return "\n" + FALLITO;
		}

	}

	final String PASSATO = """
			\n
			######## ########  ######  ########    ########  #### ##     ##  ######   ######  #### ########  #######
			   ##    ##       ##    ##    ##       ##     ##  ##  ##     ## ##    ## ##    ##  ##     ##    ##     ##
			   ##    ##       ##          ##       ##     ##  ##  ##     ## ##       ##        ##     ##    ##     ##
			   ##    ######    ######     ##       ########   ##  ##     ##  ######  ##        ##     ##    ##     ##
			   ##    ##             ##    ##       ##   ##    ##  ##     ##       ## ##        ##     ##    ##     ##
			   ##    ##       ##    ##    ##       ##    ##   ##  ##     ## ##    ## ##    ##  ##     ##    ##     ##
			   ##    ########  ######     ##       ##     ## ####  #######   ######   ######  ####    ##     #######
			""";

	final String FALLITO = """
			\n
			######## ########  ######  ########    ########    ###    ##       ##       #### #### ########  #######
			   ##    ##       ##    ##    ##       ##         ## ##   ##       ##        ##   ##     ##    ##     ##
			   ##    ##       ##          ##       ##        ##   ##  ##       ##        ##   ##     ##    ##     ##
			   ##    ######    ######     ##       ######   ##     ## ##       ##        ##   ##     ##    ##     ##
			   ##    ##             ##    ##       ##       ######### ##       ##        ##   ##     ##    ##     ##
			   ##    ##       ##    ##    ##       ##       ##     ## ##       ##        ##   ##     ##    ##     ##
			   ##    ########  ######     ##       ##       ##     ## ######## ######## #### ####    ##     #######
			""";

}
