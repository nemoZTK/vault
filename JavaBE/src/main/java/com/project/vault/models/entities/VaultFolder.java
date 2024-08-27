package com.project.vault.models.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vault_folders")
public class VaultFolder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "vault_space_id", nullable = false)
	private VaultSpace space;

	@ManyToOne
	@JoinColumn(name = "vault_user_id", nullable = false)
	private VaultUser vaultUser;

	@ManyToOne
	@JoinColumn(name = "parent_folder_id", nullable = true)
	private VaultFolder parentFolder;

	@ManyToOne
	@JoinColumn(name = "vault_section_id", nullable = true)
	private Section section;

	@Column(nullable = false)
	private String name;
	private Long size;
	private String path;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
