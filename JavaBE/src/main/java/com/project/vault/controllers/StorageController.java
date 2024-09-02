package com.project.vault.controllers;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.vault.entities.VaultSpace;
import com.project.vault.services.VaultUserAuthenticationService;
import com.project.vault.services.storage.StorageService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/storage")
public class StorageController {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	StorageService storageServ;
	@Autowired
	private VaultUserAuthenticationService authServ;

	@PostMapping("/newspace")
	String createNewSpace(@RequestBody NewSpaceRequest bodyReq, HttpServletRequest req) {
		Long userId = bodyReq.userId();
		JSONObject response = new JSONObject();
		if (authServ.doAuthorizationCheck(req, userId)) {
			response = storageServ.holdNewSpaceRequest(bodyReq.spaceName(), userId);
			return response.toString();
		}
		return response.put("result", "bad credentials").toString();

	}

	@PostMapping("/newfolder")
	public String newFolder(@RequestBody NewFolderRequest bodyReq, HttpServletRequest req) {
		Long userId = bodyReq.userId();
		JSONObject response = new JSONObject();
		if (authServ.doAuthorizationCheck(req, userId)) {
			response = storageServ.holdNewFolderRequest(userId, bodyReq.spaceId(), bodyReq.parentId(), bodyReq.name());

			return response.toString();
		}
		return response.put("result", "bad credentials").toString();
	}

	@PostMapping("/upload")
	public String upload(@RequestParam(name = "userId", required = true) Long userId,
			@RequestParam(name = "spaceId", required = true) Long spaceId,
			@RequestParam(name = "parentId", required = false) Long parentId,
			@RequestParam(name = "file", required = true) MultipartFile file, HttpServletRequest req) {

		JSONObject response = new JSONObject();
		if (authServ.doAuthorizationCheck(req, userId)) {
			if (file.isEmpty()) {
				logger.error("Il file è vuoto.");
				response.put("result", "it seems an empty file");
				return response.toString();
			}
			response = storageServ.holdUploadRequest(userId, spaceId, parentId, file);
			return response.toString();
		} else {
			return response.put("result", "bad credentials").toString();
		}
	}

	@PostMapping("/download")
	public ResponseEntity<?> download(@RequestParam(name = "userId", required = true) Long userId,
			@RequestParam(name = "spaceId", required = true) Long spaceId,
			@RequestParam(name = "folderId", required = false) Long folderId,
			@RequestParam(name = "fileId", required = false) Long fileId, HttpServletRequest req) {

		if ((folderId == null && fileId == null) || (folderId != null && fileId != null)) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("you must give a fileId OR a folderId");
		}
		if (authServ.doAuthorizationCheck(req, userId)) {
			Resource resource = storageServ.holdDownloadRequest(userId, spaceId, folderId, fileId);
			if (resource != null && resource.exists()) {
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION,
								"attachment; filename=\"" + resource.getFilename() + "\"")
						.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
			}
		}
		return null;

	}

	@GetMapping("/spaces/all/{userId}")
	public ResponseEntity<?> getAllSpacesByUserId(@PathVariable(name = "userId", required = true) Long userId,
			HttpServletRequest req) {
		JSONObject response = new JSONObject();
		if (authServ.doAuthorizationCheck(req, userId)) {
			List<VaultSpace> spaceList = storageServ.getSpaceListByUserId(userId);
			JSONArray spaceArray = new JSONArray();
			for (VaultSpace space : spaceList) {
				spaceArray.put(new JSONObject(space));
			}

			response.put("spaces", spaceArray);
			return ResponseEntity.ok(response.toString());
		}
		return ResponseEntity.badRequest().body(response.put("result", "permission denied").toString());
	}

	@GetMapping("/spaces")
	public ResponseEntity<?> getSpacesContentById(@RequestParam(name = "userId", required = true) Long userId,
			@RequestParam(name = "spaceId", required = true) Long spaceId, HttpServletRequest req) {
		JSONObject response = new JSONObject();
		if (authServ.doAuthorizationCheck(req, userId) && storageServ.isHimTheSpaceOwner(spaceId, userId)) {
			response = storageServ.getFileAndFolderWithParentNullBySpaceId(spaceId);
			if (response != null) {
				return ResponseEntity.ok(response.toString());
			}
			return ResponseEntity.badRequest().body(response.put("result", "bad result").toString());
		}
		return ResponseEntity.badRequest().body(response.put("result", "permission denied").toString());
	}

	record NewFolderRequest(Long userId, Long spaceId, Long parentId, String name) {
	}

	record NewSpaceRequest(Long userId, String spaceName) {
	}
}