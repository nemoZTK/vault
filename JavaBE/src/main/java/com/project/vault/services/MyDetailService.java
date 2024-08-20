package com.project.vault.services;

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

	@Autowired
	private VaultUserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		VaultUser user = userRepo.findByUsername(username);
		if (user != null) {
			return new UserPrincipal(user);
		}
		System.out.println(username + " username not found in db ");
		throw new UsernameNotFoundException(username + " username not found in db ");

	}
}
