package com.devsuperior.dscommerce.config.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

public class CustomPasswordAuthenticationConverter implements AuthenticationConverter {

	@Override
	public Authentication convert(HttpServletRequest request) {

		String grantType = request.getParameter("grant_type");
		if (!"password".equals(grantType)) {
			return null;
		}

		Authentication clientPrincipal = (Authentication) request.getUserPrincipal();
		if (clientPrincipal == null) {
			throw new OAuth2AuthenticationException(new OAuth2Error("invalid_client"));
		}

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
			throw new OAuth2AuthenticationException(
					new OAuth2Error("invalid_request", "username/password required", null));
		}

		Map<String, Object> additionalParameters = new HashMap<>();
		additionalParameters.put("username", username);
		additionalParameters.put("password", password);

		return new CustomPasswordAuthenticationToken(clientPrincipal, additionalParameters);
	}
}
