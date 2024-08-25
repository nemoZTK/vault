package com.project.vault.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.vault.models.VaultUser;
import com.project.vault.services.AuthenticationService;
import com.project.vault.services.StorageService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/storage")
public class StorageController {
	@Autowired
	StorageService storageServ;
	@Autowired
	private AuthenticationService authServ;

	@PostMapping("/newspace")
	Long createNewSpace(@RequestBody NewSpaceRequest bodyReq, HttpServletRequest req) throws Exception {
		VaultUser user = authServ.getUserById(bodyReq.userId());
		Long newSpaceId = null;
		if (authServ.getTokenAndValidateRequestByUsername(user.getUsername(), req))
			newSpaceId = storageServ.holdNewSpaceRequest(bodyReq.spaceName(), user);
		return newSpaceId;
	}

	@PostMapping("/newfolder")
	public Long newFolder(@RequestBody NewFolderRequest folderReq, HttpServletRequest req) throws Exception {
		VaultUser user = authServ.getUserById(folderReq.userId());
		Long newFolderId = null;
		if (authServ.getTokenAndValidateRequestByUsername(user.getUsername(), req))
			newFolderId = storageServ.holdNewFolderRequest(user, folderReq.spaceId(), folderReq.parentId(),
					folderReq.name());

		return newFolderId;
	}

}

record NewFolderRequest(Long userId, Long spaceId, Long parentId, String name) {
}

@JsonFormat
record NewSpaceRequest(Long userId, String spaceName) {
}
