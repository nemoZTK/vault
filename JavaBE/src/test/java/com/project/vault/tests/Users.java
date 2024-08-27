package com.project.vault.tests;

//public class Users {
//
//}
//
//interface UserRepo extends ListCrudRepository<VaultUsersTest, Long> {
//
//}
//
//@Controller
//@ResponseBody
//class UsersControllerTest {
//
//	private final UserRepo userRepo;
//
//	UsersControllerTest(UserRepo userRepo) {
//		this.userRepo = userRepo;
//	}
//
//	@GetMapping("/all")
//	List<VaultUsersTest> getAllUsers() {
//		List<VaultUsersTest> allUsers = this.userRepo.findAll();
//		System.out.println("finded {" + allUsers.toArray().toString() + "}");
//		return allUsers;
//	}
//
//	@PostMapping("/create")
//	void create(@RequestBody VaultUsersTest user) {
//		userRepo.save(user);
//		System.out.println("saved{" + user + "}");
//	}
//
//}
//
//@Table(name = "vault_users")
//record VaultUsersTest(@Id Long id, String username, String email, String password, String path) {
//
//}

//@Table(name = "topic_space")
//record TopicSpace(@Id Long id, String name, String description, String path) {
//}