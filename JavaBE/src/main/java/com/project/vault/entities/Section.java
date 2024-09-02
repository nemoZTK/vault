package com.project.vault.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vault_sections")
public class Section {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "vault_user_id", nullable = false)
	private VaultUser vaultUser;

	@ManyToMany
	@JoinTable(name = "section_space", joinColumns = @JoinColumn(name = "vault_section_id"), inverseJoinColumns = @JoinColumn(name = "vault_space_id"))
	private List<VaultSpace> spaces;

	@Column(nullable = false)
	private String name;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
