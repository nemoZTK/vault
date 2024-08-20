package com.project.vault.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.project.vault.services.MyDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Value("${bcrypt.rounds}")
	private Integer rounds;
	@Autowired
	private MyDetailService userDetailsService;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(customizer -> customizer.disable());
		http.authorizeHttpRequests(
				reqest -> reqest.requestMatchers("/api/users/create").permitAll().anyRequest().authenticated());
		http.httpBasic(Customizer.withDefaults());// Permette di autenticarti con la HTTP Basic tipo con postman

		// http.formLogin(Customizer.withDefaults()); // risponde con un form
		return http.build();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
		daoProvider.setPasswordEncoder(new BCryptPasswordEncoder(rounds));
		daoProvider.setUserDetailsService(userDetailsService);
		return daoProvider;
	}
}
