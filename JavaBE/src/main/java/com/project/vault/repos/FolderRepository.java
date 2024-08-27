package com.project.vault.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.vault.models.dto.FolderPathDTO;
import com.project.vault.models.entities.VaultFolder;

@Repository
public interface FolderRepository extends JpaRepository<VaultFolder, Long> {

	@Query("SELECT new com.project.vault.models.dto.FolderPathDTO(f.path, f.parentFolder.id) FROM VaultFolder f WHERE f.id = :id")
	FolderPathDTO findPathAndParentById(Long id);

}
