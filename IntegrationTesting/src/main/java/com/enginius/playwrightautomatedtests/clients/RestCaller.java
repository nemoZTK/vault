package com.enginius.playwrightautomatedtests.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestCaller extends RestTemplate {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private <T> T call(String url, HttpMethod method, ParameterizedTypeReference<T> returnTypeReference, String token,
			Object body) {
		logger.debug("calling " + method + " " + url);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Bearer " + token);

		// request entity is created with request body and headers
		HttpEntity<Object> requestEntity = new HttpEntity<>(body, headers);
		try {

			/* converts TypeReference to ParameterizedTypeReference
			ParameterizedTypeReference<T> pt = new ParameterizedTypeReference<T>() {
			    @Override
			    public Type getType() {
			        return returnTypeReference.getType();
			    }
			};*/

			ResponseEntity<T> responseEntity = exchange(url, method, requestEntity, returnTypeReference);

			if (responseEntity.getStatusCode() == HttpStatus.OK) {
				return responseEntity.getBody();

				// return (T) responseEntity.toString();
			} else {
				throw new RuntimeException("problems to " + method + " call [" + url + "] - " + responseEntity);
			}
		} catch (Throwable eee) {
			throw new RuntimeException("got exception on " + method + " calling [" + url + "]", eee);

		}

	}

	public <T> T doGet(String url, ParameterizedTypeReference<T> returnTypeReference, String token) {
		return call(url, HttpMethod.GET, returnTypeReference, token, null);

	}

	public <T> T doPost(String url, ParameterizedTypeReference<T> returnTypeReference, String token, Object body) {
		return call(url, HttpMethod.POST, returnTypeReference, token, body);
	}

	public <T> T doPut(String url, ParameterizedTypeReference<T> returnTypeReference, String token, Object body) {
		return call(url, HttpMethod.PUT, returnTypeReference, token, body);
	}
}
