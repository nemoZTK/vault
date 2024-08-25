package com.project.vault.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.vault.models.Space;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Long> {

}
