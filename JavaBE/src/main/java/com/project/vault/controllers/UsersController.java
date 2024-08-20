package com.project.vault.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private AuthenticationService authServ;

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

	@PostMapping("/create")
	NewUserResponse create(@RequestBody VaultUser user) {
		return authServ.create(user);
	}
}
