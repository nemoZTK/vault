package com.project.vault.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.vault.entities.VaultFile;

@Repository
public interface FileRepository extends JpaRepository<VaultFile, Long> {
}
