package com.enginius.playwrightautomatedtests.util.logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomResponseLoggerUtil {

	// Inner class for parsing responses and extracting information
	public static class ResponseParser {
		public static String extractHttpMethod(String input) {
			// Pattern to match the HTTP method (GET, POST, etc.) with optional digits
			Pattern pattern = Pattern.compile("\\[([A-Z]+)(?:\\s\\d+)?\\]");
			Matcher matcher = pattern.matcher(input);

			if (matcher.find()) {
				return matcher.group(1);
			} else {
				return input;
			}
		}

		public static int extractCallNumber(String input) {
			// Rimuovi le sequenze di escape ANSI dal messaggio
			String plainText = input.replaceAll("\u001B\\[[;\\d]*m", "");

			Pattern pattern = Pattern.compile("\\[(GET|POST|PUT|DELETE|HEAD|TRACE|PATCH)(?:\\s(\\d+))?\\]");
			Matcher matcher = pattern.matcher(plainText);

			if (matcher.find()) {
				String numberStr = matcher.group(2);
				if (numberStr != null) {
					return Integer.parseInt(numberStr); // il secondo gruppo catturato è il numero
				}
			}
			return -1;
		}

		public static String[] extractArrows(String input) {
			// Pattern to match arrows "<-----"
			Pattern pattern = Pattern.compile("<?-{5}>?");

			Matcher matcher = pattern.matcher(input);

			List<String> arrowsList = new ArrayList<>();
			while (matcher.find()) {
				arrowsList.add(matcher.group());
			}

			String[] arrowsArray = arrowsList.toArray(new String[0]);
			return arrowsArray;
		}
	}
}
