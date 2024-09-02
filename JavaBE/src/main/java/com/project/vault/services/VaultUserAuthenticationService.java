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

import com.project.vault.entities.VaultUser;
import com.project.vault.repos.VaultUserRepository;
import com.project.vault.services.storage.FileManagerService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class VaultUserAuthenticationService {

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
	FileManagerService fileManagerServ;

	private BCryptPasswordEncoder encoder;

	public Boolean existById(Long id) {
		return userRepo.existsById(id);
	}

	public VaultUser getUserById(Long id) {
		VaultUser user;
		if (userRepo.findById(id).get() != null) {
			user = userRepo.findById(id).get();
			return user;
		}
		return null;
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

	public VaultUser create(VaultUser user) {
		Boolean isDone = false;
		VaultUser savedUser = new VaultUser();
		savedUser.setUsername(user.getUsername());
		savedUser.setEmail(user.getEmail());
		encoder = new BCryptPasswordEncoder(rounds);
		savedUser.setCreatedAt(LocalDateTime.now());
		String pwd = user.getPassword();
		savedUser.setPassword(encoder.encode(user.getPassword()));
		isDone = fileManagerServ.createFolder("/" + user.getUsername());
		if (isDone) {
			try {
				userRepo.save(savedUser);
				return savedUser;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public Boolean doAuthorizationCheck(HttpServletRequest req, Long userId) {
		if (existById(userId)) {
			if (!getTokenAndValidateRequestByUsername(getUsernameById(userId), req)) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

//	public JSONObject create(VaultUser user) {
//		JSONObject response = new JSONObject();
//		encoder = new BCryptPasswordEncoder(rounds);
//		user.setCreatedAt(LocalDateTime.now());
//		String pwd = user.getPassword();
//		user.setPassword(encoder.encode(user.getPassword()));
//		try {
//			folderServ.createUserMainFolder(user.getName());
//			VaultUser savedUser = userRepo.save(user);
//			logger.info("New user '{}' saved in DB", user.getUsername());
//			return response.put("id", savedUser.getId()).put("token", verify(user.getUsername(), pwd));
//		} catch (Exception e) {
//			logger.error("Failed to register user '{}'", user.getUsername(), e);
//			return response.put("result", "error during db saving");
//		}
//	}

	public String verify(String username, String password) {
		Authentication authentication = authMan
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		if (authentication.isAuthenticated()) {
			return JWTServ.generateToken(username);
		} else {
			return "fail";
		}
	}

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

	public List<VaultUser> getAllUsers() {
		return userRepo.findAll();
	}
}
