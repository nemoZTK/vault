package com.project.vault.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.vault.entities.VaultUser;

@Repository
public interface VaultUserRepository extends JpaRepository<VaultUser, Long> {

	Optional<VaultUser> findByUsername(String username);

	@Query("SELECT u.id FROM VaultUser u WHERE u.username = :username")
	Long findIdByUsername(String username);

	@Query("SELECT u.username FROM VaultUser u WHERE u.id = :id")
	String findUsernameById(Long id);
}
