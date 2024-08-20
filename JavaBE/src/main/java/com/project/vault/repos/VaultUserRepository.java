package com.project.vault.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.vault.models.VaultUser;

@Repository
public interface VaultUserRepository extends JpaRepository<VaultUser, Long> {
	String findPathById(Long id);

	VaultUser findByUsername(String username);

}
