package com.devsuperior.dscommerce.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.devsuperior.dscommerce.entities.User;
import com.devsuperior.dscommerce.repositories.UserRepository;
import com.devsuperior.dscommerce.services.exceptions.ForbiddenException;

@Service
public class AuthService {

	private final UserRepository userRepository;

	public AuthService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User authenticated() {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName(); // email
			return userRepository.findByEmail(username).orElseThrow(() -> new ForbiddenException("User not found"));
		} catch (Exception e) {
			throw new ForbiddenException("Invalid user");
		}
	}

	public void validateSelfOrAdmin(Long userId) {
		User me = authenticated();
		if (me.hasRole("ROLE_ADMIN")) {
			return;
		}
		if (!me.getId().equals(userId)) {
			throw new ForbiddenException("Access denied");
		}
	}
}
