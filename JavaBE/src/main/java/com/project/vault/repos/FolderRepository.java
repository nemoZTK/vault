package com.project.vault.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.vault.models.Folder;

@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {
}
