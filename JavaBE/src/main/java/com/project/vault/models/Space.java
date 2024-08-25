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
@Table(name = "vault_space")
public class Space {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "vault_user_id", nullable = false)
	private VaultUser vaultUser;

	@Column(nullable = false)
	private String name;
	private String description;
	private String type;

	private Long size;
	private String path;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	// Relazioni implicite:
	// @OneToMany(mappedBy = "space")
	// private List<File> files;

	// @OneToMany(mappedBy = "space")
	// private List<Folder> folders;

	// @OneToMany(mappedBy = "space")
	// private List<Section> sections;
}
