package com.project.vault.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.vault.repos.SpaceRepository;

@Service
public class SpaceService {
	@Autowired
	SpaceRepository spaceRepo;

	@Autowired
	StorageService storageServ;

}
