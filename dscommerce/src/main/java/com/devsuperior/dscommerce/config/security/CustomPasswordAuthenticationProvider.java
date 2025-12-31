package com.devsuperior.dscommerce.config.security;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import com.devsuperior.dscommerce.repositories.UserDetailsProjection;
import com.devsuperior.dscommerce.repositories.UserRepository;

public class CustomPasswordAuthenticationProvider implements AuthenticationProvider {

	private final OAuth2AuthorizationService authorizationService;
	private final OAuth2TokenGenerator<?> tokenGenerator;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public CustomPasswordAuthenticationProvider(OAuth2AuthorizationService authorizationService,
			OAuth2TokenGenerator<?> tokenGenerator, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.authorizationService = authorizationService;
		this.tokenGenerator = tokenGenerator;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Authentication authenticate(Authentication authentication) {

		CustomPasswordAuthenticationToken passwordGrant = (CustomPasswordAuthenticationToken) authentication;

		OAuth2ClientAuthenticationToken clientPrincipal = (OAuth2ClientAuthenticationToken) passwordGrant
				.getPrincipal();
		if (!clientPrincipal.isAuthenticated()) {
			throw new OAuth2AuthenticationException(new OAuth2Error("invalid_client"));
		}

		Map<String, Object> params = passwordGrant.getAdditionalParameters();
		String username = (String) params.get("username");
		String password = (String) params.get("password");

		List<UserDetailsProjection> result = userRepository.searchUserAndRolesByEmail(username);
		if (result.isEmpty()) {
			throw new OAuth2AuthenticationException(new OAuth2Error("invalid_grant", "Bad credentials", null));
		}

		String dbPassword = result.get(0).getPassword();
		if (!passwordEncoder.matches(password, dbPassword)) {
			throw new OAuth2AuthenticationException(new OAuth2Error("invalid_grant", "Bad credentials", null));
		}

		var authorities = CustomUserAuthorities.getAuthorities(result);

		Authentication userPrincipal = new UsernamePasswordAuthenticationToken(username, null, authorities);
		Set<String> authorizedScopes = clientPrincipal.getRegisteredClient().getScopes();

		OAuth2TokenContext tokenContext = DefaultOAuth2TokenContext.builder()
				.authorizationServerContext(AuthorizationServerContextHolder.getContext())
				.registeredClient(clientPrincipal.getRegisteredClient()).principal(userPrincipal)
				.authorizationGrantType(passwordGrant.getGrantType()).authorizationGrant(passwordGrant)
				.authorizedScopes(authorizedScopes).tokenType(OAuth2TokenType.ACCESS_TOKEN).build();

		var generatedAccessToken = tokenGenerator.generate(tokenContext);
		if (generatedAccessToken == null) {
			throw new OAuth2AuthenticationException(new OAuth2Error("server_error", "Token generator failed", null));
		}

		OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
				generatedAccessToken.getTokenValue(), generatedAccessToken.getIssuedAt(),
				generatedAccessToken.getExpiresAt(), authorizedScopes);

		OAuth2Authorization authorization = OAuth2Authorization
				.withRegisteredClient(clientPrincipal.getRegisteredClient()).principalName(username)
				.authorizationGrantType(passwordGrant.getGrantType()).authorizedScopes(authorizedScopes)
				.token(accessToken).attribute(Principal.class.getName(), userPrincipal).build();

		authorizationService.save(authorization);

		return new OAuth2AccessTokenAuthenticationToken(clientPrincipal.getRegisteredClient(), clientPrincipal,
				accessToken, null);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return CustomPasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
