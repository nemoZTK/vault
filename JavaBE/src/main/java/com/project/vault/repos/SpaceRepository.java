package com.project.vault.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.vault.entities.VaultSpace;

@Repository
public interface SpaceRepository extends JpaRepository<VaultSpace, Long> {
	@Query("SELECT s.name FROM VaultSpace s WHERE s.id = :id")
	public String findNameById(Long id);

	@Query("SELECT EXISTS (SELECT 1 FROM VaultSpace s WHERE s.id = :spaceId AND s.vaultUser.id = :userId)")
	boolean existsByIdAndUserId(@Param("spaceId") Long spaceId, @Param("userId") Long userId);

}
