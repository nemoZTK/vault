package com.project.vault.tests;

import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

public class Security {

	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails admin = User.withDefaultPasswordEncoder().username("mimmo").password("vault").roles("USER").build();
		UserDetails user1 = User.withDefaultPasswordEncoder().username("user").password("vault").roles("USER").build();
		return new InMemoryUserDetailsManager(admin, user1);
	}
}
