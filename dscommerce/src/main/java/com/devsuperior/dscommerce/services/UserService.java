package com.devsuperior.dscommerce.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscommerce.dto.UserDTO;
import com.devsuperior.dscommerce.entities.User;

@Service
public class UserService {

	private final AuthService authService;

	public UserService(AuthService authService) {
		this.authService = authService;
	}

	@Transactional(readOnly = true)
	public UserDTO getMe() {
		User entity = authService.authenticated();
		return new UserDTO(entity);
	}
}
