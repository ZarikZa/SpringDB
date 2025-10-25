package com.example.project2.service;

import com.example.project2.model.Credentials;
import com.example.project2.model.User;
import com.example.project2.repository.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Credentials credentials = credentialsRepository.findByUsernameAndUserActive(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));

        if (credentials.isLocked()) {
            throw new UsernameNotFoundException("Аккаунт заблокирован");
        }

        return new CustomUserDetails(credentials);
    }

    public static class CustomUserDetails implements UserDetails {
        private final Credentials credentials;
        private final User user;

        public CustomUserDetails(Credentials credentials) {
            this.credentials = credentials;
            this.user = credentials.getUser();
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        }

        @Override
        public String getPassword() {
            return credentials.getPassword();
        }

        @Override
        public String getUsername() {
            return credentials.getUsername();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return !credentials.isLocked();
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return user.isActive() && !user.isDeleted();
        }

        public User getUser() {
            return user;
        }

        public Credentials getCredentials() {
            return credentials;
        }
    }
}