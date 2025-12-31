package com.devsuperior.dscommerce.config.security;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;

@Configuration
public class AuthorizationServerConfig {

	@Value("${jwt.public.key}")
	private Resource publicKeyResource;

	@Value("${jwt.private.key}")
	private Resource privateKeyResource;

	@Bean
	SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		http.formLogin(Customizer.withDefaults());
		return http.build();
	}

	@Bean
	AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	JWKSet jwkSet() throws Exception {
		RSAPublicKey publicKey = readPublicKey();
		RSAPrivateKey privateKey = readPrivateKey();

		RSAKey rsaKey = new RSAKey.Builder(publicKey).privateKey(privateKey).keyID("dscommerce-key").build();

		return new JWKSet(rsaKey);
	}

	private RSAPublicKey readPublicKey() throws Exception {
		String key = new String(publicKeyResource.getInputStream().readAllBytes())
				.replaceAll("-----BEGIN PUBLIC KEY-----", "").replaceAll("-----END PUBLIC KEY-----", "")
				.replaceAll("\\s", "");
		byte[] decoded = Base64.getDecoder().decode(key);
		X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
		return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
	}

	private RSAPrivateKey readPrivateKey() throws Exception {
		String key = new String(privateKeyResource.getInputStream().readAllBytes())
				.replaceAll("-----BEGIN PRIVATE KEY-----", "").replaceAll("-----END PRIVATE KEY-----", "")
				.replaceAll("\\s", "");
		byte[] decoded = Base64.getDecoder().decode(key);
		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
		return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(spec);
	}
}
