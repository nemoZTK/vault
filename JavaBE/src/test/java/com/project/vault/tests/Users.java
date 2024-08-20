package com.project.vault.tests;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.persistence.Table;

public class Users {

}

interface UserRepo extends ListCrudRepository<VaultUsers, Long> {

}

@Controller
@ResponseBody
class UsersController {

	private final UserRepo userRepo;

	UsersController(UserRepo userRepo) {
		this.userRepo = userRepo;
	}

	@GetMapping("/all")
	List<VaultUsers> getAllUsers() {
		List<VaultUsers> allUsers = this.userRepo.findAll();
		System.out.println("finded {" + allUsers.toArray().toString() + "}");
		return allUsers;
	}

	@PostMapping("/create")
	void create(@RequestBody VaultUsers user) {
		userRepo.save(user);
		System.out.println("saved{" + user + "}");
	}

}

@Table(name = "vault_users")
record VaultUsers(@Id Long id, String username, String email, String password, String path) {

}

//@Table(name = "topic_space")
//record TopicSpace(@Id Long id, String name, String description, String path) {
//}