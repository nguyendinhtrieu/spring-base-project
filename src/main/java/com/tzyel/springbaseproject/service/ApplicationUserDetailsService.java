package com.tzyel.springbaseproject.service;

import com.tzyel.springbaseproject.config.authentication.AppUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class ApplicationUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> roles = new ArrayList<>();
        if ("ADMIN".equalsIgnoreCase(username)) {
            roles = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        if ("USER".equalsIgnoreCase(username)) {
            roles = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return new AppUserDetails(username, "password", roles);
    }
}
