package com.project.vault.models;

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
@Table(name = "files")
public class File {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "vault_user_id", nullable = false)
	private VaultUser vaultUser;

	@ManyToOne
	@JoinColumn(name = "space_id", nullable = false)
	private Space space;

	@ManyToOne
	@JoinColumn(name = "section_id", nullable = true)
	private Section section;

	@ManyToOne
	@JoinColumn(name = "folder_id", nullable = true)
	private Folder folder;

	@Column(nullable = false)
	private String name;
	private String type;
	private String extension;
	private String language;
	private Long size;
	private String path;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	// Getters and Setters
}
