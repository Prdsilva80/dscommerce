package com.devsuperior.dscommerce.config.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.devsuperior.dscommerce.repositories.UserDetailsProjection;

public class CustomUserAuthorities {

	private CustomUserAuthorities() {
	}

	public static Collection<? extends GrantedAuthority> getAuthorities(List<UserDetailsProjection> list) {
		return list.stream().map(x -> new SimpleGrantedAuthority(x.getAuthority())).toList();
	}
}
