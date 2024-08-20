package com.project.vault.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.vault.models.VaultSpace;
import com.project.vault.services.FolderService;

@RestController
@RequestMapping("/api/repository")
public class RepoController {

	@Autowired
	private FolderService folderService;

	@PostMapping("/new")
	Boolean createNewRepo(@RequestBody NewRepoRequest req) {

		VaultSpace repo = new VaultSpace();
		repo.setCreatedAt(LocalDateTime.now());
		repo.setName(req.repoName());

		return folderService.createRepository("", req.repoName());
	}

}

@JsonFormat
record NewRepoRequest(Long userId, String repoName) {
}
