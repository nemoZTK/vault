package com.project.vault.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.vault.models.VaultUser;
import com.project.vault.services.AuthenticationService;
import com.project.vault.services.AuthenticationService.NewUserResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/users")
public class UsersController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private AuthenticationService authServ;

	@PostMapping("/create")
	NewUserResponse create(@RequestBody VaultUser user) {
		logger.info("new user created--->" + user.getUsername());
		return authServ.create(user);

	}

	@PostMapping("/login")
	public String login(@RequestBody VaultUser user) {
		return authServ.verify(user);

	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (token != null) {
			return ResponseEntity.ok("Logged out successfully");
		} else {
			return ResponseEntity.status(400).body("No token provided");
		}
	}

	@GetMapping("/all")
	List<VaultUser> getAllUsers() {
		List<VaultUser> allUsers = authServ.getAllUsers();
		System.out.println("finded {" + allUsers.toArray().toString() + "}");
		return allUsers;
	}

	@GetMapping("/id")
	String hello(HttpServletRequest req) {
		return "session id=" + req.getSession().getId();
	}

	@GetMapping("/csrf-token")
	public CsrfToken getCsrfToken(HttpServletRequest req) {
		return (CsrfToken) req.getAttribute("_csrf");
	}

}
