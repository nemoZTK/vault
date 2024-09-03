package com.project.vault.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.vault.entities.VaultFolder;
import com.project.vault.models.NameAndParentDTO;

@Repository
public interface FolderRepository extends JpaRepository<VaultFolder, Long> {

	@Query("SELECT new com.project.vault.models.NameAndParentDTO(f.name, f.parentFolder.id) FROM VaultFolder f WHERE f.id = :id")
	NameAndParentDTO findNameAndParentById(Long id);

	@Query("SELECT COUNT(f) > 0 FROM VaultFolder f WHERE f.id = :folderId AND f.vaultUser.id = :userId")
	boolean existsByIdAndUserId(@Param("folderId") Long folderId, @Param("userId") Long userId);

	List<VaultFolder> findBySpaceIdAndParentFolderIsNull(Long spaceId);

	List<VaultFolder> findByParentFolderId(Long parentId);
}
