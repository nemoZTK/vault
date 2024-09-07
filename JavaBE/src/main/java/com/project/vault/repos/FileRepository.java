package com.project.vault.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.vault.entities.VaultFile;

@Repository
public interface FileRepository extends JpaRepository<VaultFile, Long> {

	List<VaultFile> findBySpaceIdAndParentFolderIsNull(Long spaceId);

	List<VaultFile> findByParentFolderId(Long parentId);

	@Query("SELECT COUNT(vf) > 0 FROM VaultFile vf WHERE vf.id = :fileId AND vf.vaultUser.id = :userId")
	boolean existsByIdAndUserId(@Param("fileId") Long fileId, @Param("userId") Long userId);
}
