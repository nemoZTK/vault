package com.project.vault.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.vault.models.VaultUser;

@Repository
public interface VaultUserRepository extends JpaRepository<VaultUser, Long> {
	String findPathById(Long id);

	Optional<VaultUser> findByUsername(String username);

	@Query("SELECT u.id FROM VaultUser u WHERE u.username = :username")
	Long findIdByUsername(String username);

}
