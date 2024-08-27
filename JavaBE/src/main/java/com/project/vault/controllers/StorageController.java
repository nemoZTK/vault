package com.project.vault.controllers;

import java.util.concurrent.Callable;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.vault.models.entities.VaultUser;
import com.project.vault.services.AuthenticationService;
import com.project.vault.services.StorageService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/storage")
public class StorageController {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	StorageService storageServ;
	@Autowired
	private AuthenticationService authServ;

	private String executeWithAuthorization(HttpServletRequest req, Long userId, Callable<String> code) {
		JSONObject response = new JSONObject();
		if (authServ.existById(userId)) {
			if (!authServ.getTokenAndValidateRequestByUsername(authServ.getUsernameById(userId), req)) {
				response.put("result", "bad credentials");
				return response.toString();
			}
			try {
				return code.call();
			} catch (Exception e) {
				e.printStackTrace();
				response.put("result", "generic unchecked error");
				return response.toString();

			}
		} else {
			response.put("result", "user id not found");
			return response.toString();
		}
	}

	@PostMapping("/newspace")
	String createNewSpace(@RequestBody NewSpaceRequest bodyReq, HttpServletRequest req) {
		Long userId = bodyReq.userId();
		return executeWithAuthorization(req, userId, () -> {
			VaultUser user = authServ.getUserById(userId);
			JSONObject response = new JSONObject();
			response = storageServ.holdNewSpaceRequest(bodyReq.spaceName(), user);
			return response.toString();
		});
//		Long newSpaceId = null;
//		newSpaceId = 
	}

//	@PostMapping
//	String createJsonResponse(@RequestBody NewFolderRequest Bodyreq, HttpServletRequest req) {
//		JSONObject response = new JSONObject();
//		response.put("result", "done");
//
//		return response.toString();
//	}

	@PostMapping("/newfolder")
	public String newFolder(@RequestBody NewFolderRequest bodyReq, HttpServletRequest req) {
		Long userId = bodyReq.userId();
		return executeWithAuthorization(req, userId, () -> {
			JSONObject response = new JSONObject();
			VaultUser user = authServ.getUserById(userId);
			response = storageServ.holdNewFolderRequest(user, bodyReq.spaceId(), bodyReq.parentId(), bodyReq.name());

			return response.toString();
		});

	}

	@PostMapping("/upload")
	public String upload(
	    @RequestParam("userId") Long userId,
	    @RequestParam("spaceId") Long spaceId,
	    @RequestParam(value = "parentId", required = false) Long parentId,
	    @RequestParam("file") MultipartFile file,
	    HttpServletRequest req) {

	    return executeWithAuthorization(req, userId, () -> {
	        JSONObject response = new JSONObject();
	        VaultUser user = authServ.getUserById(userId);
	        if (file.isEmpty()) {
	            logger.error("Il file è vuoto.");
	            response.put("result", "it seems an empty file");
	            return response.toString();
	        }
	        response = storageServ.holdUploadRequest(user, spaceId, parentId, file);
	        return response.toString();
	    });}
}

record NewFolderRequest(Long userId, Long spaceId, Long parentId, String name) {
}

record NewSpaceRequest(Long userId, String spaceName) {
}
