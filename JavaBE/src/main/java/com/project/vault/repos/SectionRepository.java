package com.project.vault.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.vault.models.Section;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
}
