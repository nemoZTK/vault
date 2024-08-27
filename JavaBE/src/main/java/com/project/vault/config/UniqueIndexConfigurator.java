package com.project.vault.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UniqueIndexConfigurator implements InitializingBean {

	private final JdbcTemplate jdbcTemplate;

	public UniqueIndexConfigurator(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		jdbcTemplate.execute("CREATE UNIQUE INDEX IF NOT EXISTS unique_folder_name"
				+ " ON vault_folders(name, parent_folder_id) NULLS NOT DISTINCT;");
		jdbcTemplate.execute("CREATE UNIQUE INDEX IF NOT EXISTS unique_file_name "
				+ "ON vault_files(name, vault_folder_id) NULLS NOT DISTINCT;");
		jdbcTemplate.execute(
				"CREATE UNIQUE INDEX IF NOT EXISTS unique_user_space_name " + "ON vault_spaces(vault_user_id, name);");

		jdbcTemplate.execute(
				"CREATE UNIQUE INDEX IF NOT EXISTS unique_section_name " + "ON vault_sections(vault_user_id, name);");
	}
}