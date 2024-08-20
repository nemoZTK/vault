package com.project.vault.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// scelta del nome un pò infelice ma che vuoi farci haha
@Repository
public interface RepoRepository extends JpaRepository<com.project.vault.models.VaultSpace, Long> {

}
