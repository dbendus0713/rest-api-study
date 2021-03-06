package com.myboot.restapi.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import com.myboot.restapi.accounts.AccountService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	AccountService accountService;
	PasswordEncoder passwordEncoder;
	TokenStore tokenStore;
	AuthenticationManager authenticationManager;
	

	public SecurityConfig(AccountService accountService, PasswordEncoder passwordEncoder, TokenStore tokenStore,
			AuthenticationManager authenticationManager) {
		this.accountService = accountService;
		this.passwordEncoder = passwordEncoder;
		this.tokenStore = tokenStore;
		this.authenticationManager = authenticationManager;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.anonymous().and().formLogin().and().authorizeRequests().mvcMatchers(HttpMethod.GET, "/api/**").permitAll()
				.anyRequest().authenticated();
	}

	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(accountService).passwordEncoder(passwordEncoder);
	}
}
