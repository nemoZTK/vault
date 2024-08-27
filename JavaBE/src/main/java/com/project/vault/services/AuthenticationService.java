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

import com.project.vault.models.entities.VaultUser;
import com.project.vault.repos.VaultUserRepository;

import jakarta.servlet.http.HttpServletRequest;

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

	@Autowired
	StorageService folderServ;

	private BCryptPasswordEncoder encoder;

	public List<VaultUser> getAllUsers() {
		return userRepo.findAll();
	}

	public Boolean existById(Long id) {
		return userRepo.existsById(id);
	}

	public VaultUser getUserById(Long id) throws Exception {
		VaultUser user;
		if (userRepo.findById(id).get() != null) {
			user = userRepo.findById(id).get();
			return user;
		}
		throw new Exception("user id not found " + id);

	}

	public String getUsernameById(Long id) {
		return userRepo.findUsernameById(id);
	}

	public Long getUserIdByUsername(String username) {
		Long userId;
		if (userRepo.findIdByUsername(username) != null) {
			userId = userRepo.findIdByUsername(username);
			return userId;
		}
		return null;

	}

	public record NewUserResponse(Boolean done, Long id) {
	}

	public NewUserResponse create(VaultUser user) {
		encoder = new BCryptPasswordEncoder(rounds);
		user.setCreatedAt(LocalDateTime.now());
		user.setPassword(encoder.encode(user.getPassword()));
		user.setPath("/" + user.getUsername());
		try {
			folderServ.createUserMainFolder(user.getPath());
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

//	public String getPathById(Long id) {
//		return "/" + userRepo.findUsernameById(id);
//	}

	public Boolean getTokenAndValidateRequestByUsername(String username, HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		if (JWTServ.validateToken(token, username)) {
			return true;
		}
		return false;
	}

}
