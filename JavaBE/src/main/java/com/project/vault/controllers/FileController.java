package com.project.vault.controllers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.vault.services.VaultUserAuthenticationService;
import com.project.vault.services.storage.FileService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/storage")
public class FileController {

	@Autowired
	private VaultUserAuthenticationService authServ;

	@Autowired
	FileService fileServ;
	// --------------------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------------------

	@PutMapping("/rename/file")
	public ResponseEntity<?> renameFile(@RequestBody RenameFileRequest renameFile, HttpServletRequest req) {
		JSONObject response = new JSONObject();
		if (authServ.doAuthorizationCheck(req, renameFile.userId)) {
			response = fileServ.holdRenameFileRequest(renameFile.userId, renameFile.fileId, renameFile.newName);
			if (response != null) {
				return ResponseEntity.ok(response.toString());
			}
			return ResponseEntity.badRequest().body(response.put("result", "bad result").toString());
		}
		return ResponseEntity.badRequest().body(response.put("result", "permission denied").toString());
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------------------

	record RenameFileRequest(Long userId, String newName, Long fileId) {

	}

}
