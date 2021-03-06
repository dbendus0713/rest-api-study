package com.myboot.restapi.accounts;

import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {
	AccountRepository accountRepository;
	PasswordEncoder passwordEncoder;

	public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
		this.accountRepository = accountRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public Account saveAccount(Account account) {
		account.setPassword(this.passwordEncoder.encode(account.getPassword()));
		return this.accountRepository.save(account);
	}

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException(username));
		return new User(account.getEmail(), account.getPassword(), authorities(account.getRoles()));
	}

	private Collection<? extends GrantedAuthority> authorities(Set<AccountRole> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(toSet());
	}
}