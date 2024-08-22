package com.project.vault.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.vault.models.UserPrincipal;
import com.project.vault.models.VaultUser;
import com.project.vault.repos.VaultUserRepository;

@Service
public class MyDetailService implements UserDetailsService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private VaultUserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		VaultUser user = userRepo.findByUsername(username);

		if (user != null) {
			logger.info("User '{}' found in the database", username);
			return new UserPrincipal(user);
		} else {
			logger.error("Username '{}' not found in the database", username);
			throw new UsernameNotFoundException("Username '" + username + "' not found in the database");
		}
	}
}