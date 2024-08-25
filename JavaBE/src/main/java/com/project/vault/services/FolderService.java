package com.project.vault.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.vault.repos.FolderRepository;
import com.project.vault.repos.SectionRepository;

@Service
public class FolderService {
	@Autowired
	FolderRepository folderRepo;

	@Autowired
	SectionRepository sectionRepo;

}
