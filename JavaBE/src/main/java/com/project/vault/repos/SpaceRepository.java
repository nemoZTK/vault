package com.project.vault.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.vault.models.entities.VaultSpace;

@Repository
public interface SpaceRepository extends JpaRepository<VaultSpace, Long> {
	@Query("SELECT s.path FROM VaultSpace s WHERE s.id = :id")
	public String findPathById(Long id);
}
