package com.devsuperior.dscommerce.repositories;

public interface UserDetailsProjection {
	String getUsername();

	String getPassword();

	Long getRoleId();

	String getAuthority();
}
