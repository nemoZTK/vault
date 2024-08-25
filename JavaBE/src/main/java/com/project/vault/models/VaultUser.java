package com.project.vault.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vault_users")
public class VaultUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	private String role;
	private String email;
	private String password;
	private String path;
	private LocalDateTime createdAt;
	// Relazioni implicite:
	// @OneToMany(mappedBy = "vaultUser")
	// private List<Space> spaces;

	// @OneToMany(mappedBy = "vaultUser")
	// private List<Folder> folders;

	// @OneToMany(mappedBy = "vaultUser")
	// private List<File> files;

	// @OneToMany(mappedBy = "vaultUser")
	// private List<Section> sections;
}
