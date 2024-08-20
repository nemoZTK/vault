package com.project.vault.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.vault.models.VaultUser;
import com.project.vault.repos.VaultUserRepository;

@Service
public class AuthenticationService {
	public record NewUserResponse(Boolean done, Long id) {
	}

	@Autowired
	private VaultUserRepository userRepo;

	@Autowired
	private FolderService folderService;

	@Value("${bcrypt.rounds}")
	private Integer rounds;

	private BCryptPasswordEncoder encoder; // rounds

	public List<VaultUser> getAllUsers() {
		return this.userRepo.findAll();
	}

	public NewUserResponse create(VaultUser user) {
		encoder = new BCryptPasswordEncoder(rounds);
		user.setCreatedAt(LocalDateTime.now());
		user.setPath(folderService.getBasePath() + "/" + user.getUsername());
		user.setPassword(encoder.encode(user.getPassword()));
		try {
			VaultUser savedUser = userRepo.save(user);
			folderService.createFolder(savedUser.getPath());
			return new NewUserResponse(true, savedUser.getId());
		} catch (Exception e) {
			e.printStackTrace();
			return new NewUserResponse(false, null);
		}
	}

	VaultUser getUserById(Long id) {
		if (userRepo.findById(id).isPresent()) {
			return userRepo.findById(id).get();
		}
		System.err.println("non trovato user per id -> " + id);
		return null;
	}
}
