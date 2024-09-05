package com.project.vault.controllers;

import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.vault.entities.VaultUser;
import com.project.vault.services.VaultUserAuthenticationService;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://localhost:8686")
@RestController
@RequestMapping("/api/users")
public class UsersController {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private VaultUserAuthenticationService authServ;

	@PostMapping("/create")
	ResponseEntity<?> create(@RequestBody VaultUser user) {
		logger.info("new user creation request--->" + user.getUsername());
		VaultUser savedUser = authServ.create(user);
		JSONObject response = new JSONObject();
		if (savedUser != null) {
			response.put("username", savedUser.getUsername()).put("id", savedUser.getId());
			response.put("token", authServ.verify(savedUser.getUsername(), user.getPassword()));
			return ResponseEntity.ok(response.toString());
		} else {

			response.put("result", "error");
			return ResponseEntity.badRequest().body(response.toString());
		}
	}

	public record newLoginRequest(String username, String password) {

	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody newLoginRequest req) {
		Long requestUserId;

		requestUserId = authServ.getUserIdByUsername(req.username);
		if (requestUserId != null) {
			return ResponseEntity.ok(requestUserId + "|" + authServ.verify(req.username, req.password));
		}
		return ResponseEntity.badRequest().body("wrong username or password");
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (token != null) {
			return ResponseEntity.ok("Logged out successfully");
		} else {
			return ResponseEntity.badRequest().body("No token provided");
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
