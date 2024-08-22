package com.project.vault.services;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.vault.models.VaultUser;
import com.project.vault.repos.VaultUserRepository;

@Service
public class AuthenticationService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${bcrypt.rounds}")
	private Integer rounds;

	@Autowired
	private JWTtokenService JWTServ;

	@Autowired
	private VaultUserRepository userRepo;

	@Autowired
	AuthenticationManager authMan;

	private BCryptPasswordEncoder encoder;

	public List<VaultUser> getAllUsers() {
		return this.userRepo.findAll();
	}

	public record NewUserResponse(Boolean done, Long id) {
	}

	public NewUserResponse create(VaultUser user) {
		encoder = new BCryptPasswordEncoder(rounds);
		user.setCreatedAt(LocalDateTime.now());
		user.setPassword(encoder.encode(user.getPassword()));
		try {
			VaultUser savedUser = userRepo.save(user);
			logger.info("New user '{}' saved in DB", user.getUsername());
			return new NewUserResponse(true, savedUser.getId());
		} catch (Exception e) {
			logger.error("Failed to register user '{}'", user.getUsername(), e);
			return new NewUserResponse(false, null);
		}
	}

	public String verify(VaultUser user) {
		Authentication authentication = authMan
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		if (authentication.isAuthenticated()) {
			return JWTServ.generateToken(user.getUsername());
		} else {
			return "fail";
		}
	}

}
