package com.devsuperior.dscommerce.config.security;

import java.util.Map;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

public class CustomPasswordAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 1L;

	private final Authentication clientPrincipal;
	private final Map<String, Object> additionalParameters;

	public CustomPasswordAuthenticationToken(Authentication clientPrincipal, Map<String, Object> additionalParameters) {
		super(java.util.List.of());
		this.clientPrincipal = clientPrincipal;
		this.additionalParameters = additionalParameters;
		setAuthenticated(false);
	}

	@Override
	public Object getCredentials() {
		return "";
	}

	@Override
	public Object getPrincipal() {
		return clientPrincipal;
	}

	public AuthorizationGrantType getGrantType() {
		return new AuthorizationGrantType("password");
	}

	public Map<String, Object> getAdditionalParameters() {
		return additionalParameters;
	}
}
