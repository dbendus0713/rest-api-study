package com.myboot.restapi.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

import com.myboot.restapi.accounts.AccountService;
import com.myboot.restapi.common.AppProperties;

@SuppressWarnings("deprecation")
@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
	PasswordEncoder passwordEncoder;
	AccountService accountService;
	AppProperties appProperties;
	
	public AuthServerConfig(PasswordEncoder passwordEncoder, AccountService accountService,
			AppProperties appProperties) {
		super();
		this.passwordEncoder = passwordEncoder;
		this.accountService = accountService;
		this.appProperties = appProperties;
	}

// OAuth2 읶증 서버 보안(Password) 정보를 설정
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.passwordEncoder(passwordEncoder);
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory().withClient(appProperties.getClientId())
						.secret(this.passwordEncoder.encode(appProperties.getClientSecret()))
						.authorizedGrantTypes("password", "refresh_token")
						.scopes("read", "write")
						.accessTokenValiditySeconds(10 * 60)
						.refreshTokenValiditySeconds(6 * 10 * 60);
	}
}