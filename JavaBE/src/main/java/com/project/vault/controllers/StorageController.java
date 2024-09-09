package com.project.vault.controllers;

import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.vault.services.VaultUserAuthenticationService;
import com.project.vault.services.storage.SpaceService;
import com.project.vault.services.storage.StorageService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/storage")
public class StorageController {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	StorageService storageServ;
	@Autowired
	SpaceService spaceServ;
	@Autowired
	private VaultUserAuthenticationService authServ;

	// --------------------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------------------

	@PostMapping("/upload")
	public ResponseEntity<?> upload(@RequestParam(name = "userId", required = true) Long userId,
			@RequestParam(name = "spaceId", required = true) Long spaceId,
			@RequestParam(name = "parentId", required = false) Long parentId,
			@RequestParam(name = "file", required = true) MultipartFile file, HttpServletRequest req) {

		JSONObject response = new JSONObject();
		if (authServ.doAuthorizationCheck(req, userId)) {
			if (file.isEmpty()) {
				logger.error("il file è vuoto.");
				response.put("result", "it seems an empty file");
				return ResponseEntity.badRequest().body(response.toString());
			}
			response = storageServ.holdUploadRequest(userId, spaceId, parentId, file);
			return ResponseEntity.ok(response.toString());
		} else {
			return ResponseEntity.badRequest().body(response.put("result", "permission denied").toString());
		}
	}
	// --------------------------------------------------------------------------------------------------------------------------------------------------

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
	// --------------------------------------------------------------------------------------------------------------------------------------------------

	@GetMapping("/image")
	public ResponseEntity<?> getImage(@RequestParam(name = "userId", required = true) Long userId,
			@RequestParam(name = "spaceId", required = true) Long spaceId,
			@RequestParam(name = "folderId", required = false) Long folderId,
			@RequestParam(name = "fileId", required = false) Long fileId,
			@RequestParam(name = "download", required = false, defaultValue = "false") boolean download,
			HttpServletRequest req) {

		if ((folderId == null && fileId == null) || (folderId != null && fileId != null)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You must provide a fileId OR a folderId");
		}

		if (authServ.doAuthorizationCheck(req, userId)) {
			Resource resource = storageServ.holdDownloadRequest(userId, spaceId, folderId, fileId);
			if (resource != null && resource.exists()) {
				MediaType mediaType = determineMediaType(resource.getFilename());

				ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok().contentType(mediaType);
				responseBuilder.header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition");

				if (download) {
					responseBuilder.header(HttpHeaders.CONTENT_DISPOSITION,
							"attachment; filename=\"" + resource.getFilename() + "\"");

				} else {
					responseBuilder.header(HttpHeaders.CONTENT_DISPOSITION,
							"inline; filename=\"" + resource.getFilename() + "\"");
				}

				return responseBuilder.body(resource);
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource not found");
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------

	@DeleteMapping("/delete/file")
	public ResponseEntity<?> deleteFile(@RequestParam List<Long> fileIds, @RequestParam Long userId,
			HttpServletRequest req) {
		JSONObject response = new JSONObject();
		if (authServ.doAuthorizationCheck(req, userId)) {
			response = storageServ.holdFileDeleteRequest(fileIds, userId);
			return ResponseEntity.ok(response.toString());
		} else {
			return ResponseEntity.badRequest().body(response.put("result", "permission denied").toString());
		}
	}

	@DeleteMapping("/delete/folder")
	public ResponseEntity<?> deleteFolder(@RequestParam Long folderId, @RequestParam Long userId,
			HttpServletRequest req) {
		JSONObject response = new JSONObject();
		if (authServ.doAuthorizationCheck(req, userId)) {
			response = storageServ.holdFolderDeleteRequest(folderId, userId);
			return ResponseEntity.ok(response.toString());
		} else {
			return ResponseEntity.badRequest().body(response.put("result", "permission denied").toString());
		}
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------------------

	private MediaType determineMediaType(String filename) {
		if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
			return MediaType.IMAGE_JPEG;
		} else if (filename.endsWith(".png")) {
			return MediaType.IMAGE_PNG;
		} else if (filename.endsWith(".gif")) {
			return MediaType.IMAGE_GIF;
		}
		return MediaType.APPLICATION_OCTET_STREAM; // Default
	}

}