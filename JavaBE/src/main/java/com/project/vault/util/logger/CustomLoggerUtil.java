package com.project.vault.util.logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.project.vault.util.logger.CustomResponseLoggerUtil.ResponseParser;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;

public class CustomLoggerUtil extends ForegroundCompositeConverterBase<ILoggingEvent> {

	@Override
	protected String getForegroundColorCode(ILoggingEvent event) {
		Level level = event.getLevel();
		switch (level.toInt()) {
			case Level.ERROR_INT: // rosso
				return "31";
			case Level.WARN_INT: // giallo
				return "33";
			case Level.INFO_INT: // verde
				return "32";
			case Level.DEBUG_INT: // ciano
				return "36";
			case Level.TRACE_INT: // blu scuro
				return "34";
			default:
				return "0"; // Colore default
		}
	}

	// Metodo che parsa il messaggio e colora parti specifiche
	public String colorizeMessage(String message) {

		if (!message.startsWith(" ECCOLO")) {
			String parsedMessage = message;
			message = colorizeHttpMethod(message);
			message = colorizeStatusCode(message);
		}
		// Puoi aggiungere altre regole di colorazione qui
		return message;
	}

	// Metodo per colorare i metodi HTTP
	private String colorizeHttpMethod(String message) {
		String httpMethod;
		try {
			httpMethod = ResponseParser.extractHttpMethod(message);
		} catch (Exception e) {
			e.printStackTrace();
			return message; // Se c'è un'eccezione, restituisci il messaggio originale
		}

		// Assegniamo un colore diverso per ogni metodo HTTP
		String colorCode;
		switch (httpMethod.toUpperCase()) {
			case "GET":
				colorCode = "32"; // verde
				break;
			case "POST":
				colorCode = "36"; // ciano
				break;
			case "PUT":
				colorCode = "33"; // giallo
				break;
			case "DELETE":
				colorCode = "31"; // rosso
				break;
			case "OPTIONS":
				colorCode = "35"; // magenta
				break;
			case "HEAD":
				colorCode = "37"; // bianco
				break;
			case "TRACE":
				colorCode = "34"; // blu scuro
				break;
			case "PATCH":
				colorCode = "90"; // grigio
				break;
			default:
				colorCode = "0"; // Colore default
		}

		// Colora il metodo HTTP nel messaggio
		Pattern pattern = Pattern.compile("\\b" + httpMethod + "\\b", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(message);
		if (matcher.find()) {
			message = matcher.replaceAll("\u001b[" + colorCode + "m" + httpMethod + "\u001b[0m");
		}

		return message;
	}

	// Metodo per colorare i codici di stato HTTP
	private String colorizeStatusCode(String message) {
		int statusCode;
		try {
			statusCode = ResponseParser.extractCallNumber(message);
		} catch (Exception e) {
			e.printStackTrace();
			return message; // Se c'è un'eccezione, restituisci il messaggio originale
		}

		// Assegniamo un colore diverso per ogni codice di stato HTTP
		String colorCode;
		switch (statusCode) {
			case 200: // OK
				colorCode = "32"; // verde
				break;
			case 201: // Created
				colorCode = "36"; // ciano
				break;
			case 204: // No Content
				colorCode = "33"; // giallo
				break;
			case 400: // Bad Request
				colorCode = "31"; // rosso
				break;
			case 401: // Unauthorized
				colorCode = "35"; // magenta
				break;
			case 403: // Forbidden
				colorCode = "31"; // rosso
				break;
			case 404: // Not Found
				colorCode = "33"; // giallo
				break;
			case 500: // Internal Server Error
				colorCode = "31"; // rosso
				break;
			case 502: // Bad Gateway
				colorCode = "31"; // rosso
				break;
			case 503: // Service Unavailable
				colorCode = "33"; // giallo
				break;
			case 504: // Gateway Timeout
				colorCode = "33"; // giallo
				break;
			default:
				colorCode = "0"; // Colore default
		}
		// Colora il codice di stato nel messaggio
		String statusCodeStr = Integer.toString(statusCode);
		Pattern pattern = Pattern.compile("\\b" + statusCodeStr + "\\b");
		Matcher matcher = pattern.matcher(message);
		if (matcher.find()) {
			message = matcher.replaceAll("\u001b[" + colorCode + "m" + statusCodeStr + "\u001b[0m");
		}

		return message;
	}
}
