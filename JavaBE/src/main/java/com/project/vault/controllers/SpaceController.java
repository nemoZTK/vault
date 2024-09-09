package com.project.vault.controllers;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.vault.entities.VaultSpace;
import com.project.vault.services.VaultUserAuthenticationService;
import com.project.vault.services.storage.SpaceService;
import com.project.vault.services.storage.StorageService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/storage")
public class SpaceController {

	@Autowired
	SpaceService spaceServ;
	@Autowired
	private VaultUserAuthenticationService authServ;
	@Autowired
	StorageService storageServ;
	// --------------------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------------------

	@PostMapping("/newspace")
	ResponseEntity<?> createNewSpace(@RequestBody NewSpaceRequest bodyReq, HttpServletRequest req) {
		Long userId = bodyReq.userId();
		JSONObject response = new JSONObject();
		if (authServ.doAuthorizationCheck(req, userId)) {
			response = spaceServ.holdNewSpaceRequest(bodyReq.spaceName(), userId);
			return ResponseEntity.ok(response.toString());
		}
		return ResponseEntity.badRequest().body(response.put("result", "permission denied").toString());

	}
	// --------------------------------------------------------------------------------------------------------------------------------------------------

	@GetMapping("/spaces/all/{userId}")
	public ResponseEntity<?> getAllSpacesByUserId(@PathVariable(name = "userId", required = true) Long userId,
			HttpServletRequest req) {
		JSONObject response = new JSONObject();
		if (authServ.doAuthorizationCheck(req, userId)) {
			List<VaultSpace> spaceList = spaceServ.getSpaceListByUserId(userId);
			JSONArray spaceArray = new JSONArray();
			for (VaultSpace space : spaceList) {
				spaceArray.put(new JSONObject(space));
			}

			response.put("spaces", spaceArray);
			return ResponseEntity.ok(response.toString());
		}
		return ResponseEntity.badRequest().body(response.put("result", "permission denied").toString());
	}
	// --------------------------------------------------------------------------------------------------------------------------------------------------

	@GetMapping("/spaces")
	public ResponseEntity<?> getSpacesContentById(@RequestParam(name = "userId", required = true) Long userId,
			@RequestParam(name = "spaceId", required = true) Long spaceId, HttpServletRequest req) {
		JSONObject response = new JSONObject();
		if (authServ.doAuthorizationCheck(req, userId) && spaceServ.isHimTheSpaceOwner(spaceId, userId)) {
			response = storageServ.getFileAndFolderWithParentNullBySpaceId(spaceId);
			if (response != null) {
				return ResponseEntity.ok(response.toString());
			}
			return ResponseEntity.badRequest().body(response.put("result", "bad result").toString());
		}
		return ResponseEntity.badRequest().body(response.put("result", "permission denied").toString());
	}

	// --------------------------------------------------------------------------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------------------------------------------------------------------------

	record NewSpaceRequest(Long userId, String spaceName) {
	}
}
