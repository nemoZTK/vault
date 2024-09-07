package com.enginius.playwrightautomatedtests.tests.experiments;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ResponseParserTests {

	@Test
	public void testResponseParsing() {
		String input = "b  [GET 200] <----- https://google.com  body ----->{\"number\":0,\"size\":20,\"noe\":20,\"content\":[{\"id\":33369,\"name\":\"\"}]}";

		// Test extractCallNumber
		assertEquals(200, ResponseParser.extractCallNumber(input));

		// Test extractHttpMethod
		assertEquals("GET", ResponseParser.extractHttpMethod(input));

		// Test extractArrows
		String[] expectedArrows = { "<-----", "----->" };
		assertArrayEquals(expectedArrows, ResponseParser.extractArrows(input));
	}

	public class ResponseParser {
		public static String extractHttpMethod(String input) {
			// Pattern to match the HTTP method (GET, POST, etc.)
			Pattern pattern = Pattern.compile("\\[([A-Z]+)\\s\\d+\\]");
			Matcher matcher = pattern.matcher(input);

			if (matcher.find()) {
				return matcher.group(1);
			} else {
				throw new IllegalArgumentException("No HTTP method found in the input string");
			}
		}

		public static int extractCallNumber(String input) {
			// Pattern to match the number inside curly braces
			Pattern pattern = Pattern.compile("\\[GET\\s(\\d+)\\]");
			Matcher matcher = pattern.matcher(input);

			if (matcher.find()) {
				return Integer.parseInt(matcher.group(1));
			} else {
				throw new IllegalArgumentException("No call number found in the input string");
			}
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
