package com.project.vault.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.vault.services.VaultUserAuthenticationService;
import com.project.vault.services.storage.FolderService;
import com.project.vault.services.storage.StorageService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/storage")
public class FolderController {

	@Autowired
	private VaultUserAuthenticationService authServ;
	@Autowired
	StorageService storageServ;
	@Autowired
	FolderService folderServ;

	// --------------------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------------------

	@PostMapping("/newfolder")
	public ResponseEntity<?> newFolder(@RequestBody NewFolderRequest bodyReq, HttpServletRequest req) {
		Long userId = bodyReq.userId();
		JSONObject response = new JSONObject();
		if (authServ.doAuthorizationCheck(req, userId)) {
			response = folderServ.holdNewFolderRequest(userId, bodyReq.spaceId(), bodyReq.parentId(), bodyReq.name());

			return ResponseEntity.ok(response.toString());
		}
		return ResponseEntity.badRequest().body(response.put("result", "permission denied").toString());
	}
	// --------------------------------------------------------------------------------------------------------------------------------------------------

	@GetMapping("/folder")
	public ResponseEntity<?> getFoldersContentById(@RequestParam(name = "userId", required = true) Long userId,
			@RequestParam(name = "folderId", required = true) Long folderId, HttpServletRequest req) {
		JSONObject response = new JSONObject();
		if (authServ.doAuthorizationCheck(req, userId) && folderServ.isHimTheFolderOwner(folderId, userId)) {
			response = storageServ.getFolderContentById(folderId);
			if (response != null) {
				return ResponseEntity.ok(response.toString());
			}
			return ResponseEntity.badRequest().body(response.put("result", "bad result").toString());
		}
		return ResponseEntity.badRequest().body(response.put("result", "permission denied").toString());
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------

	@PutMapping("/rename/folder")
	public ResponseEntity<?> renameFolder(@RequestBody RenameFolderRequest folderReq, HttpServletRequest req) {
		JSONObject response = new JSONObject();
		if (authServ.doAuthorizationCheck(req, folderReq.userId)) {
			response = folderServ.holdRenameFolderRequest(folderReq.userId, folderReq.folderId, folderReq.newName);
			if (response != null) {
				return ResponseEntity.ok(response.toString());
			}
			return ResponseEntity.badRequest().body(response.put("result", "bad result").toString());
		}
		return ResponseEntity.badRequest().body(response.put("result", "permission denied").toString());
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------------------

	record RenameFolderRequest(Long userId, String newName, Long folderId) {

	}

	record NewFolderRequest(Long userId, Long spaceId, Long parentId, String name) {
	}
}
