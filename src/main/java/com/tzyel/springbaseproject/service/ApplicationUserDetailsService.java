package com.tzyel.springbaseproject.service;

import com.tzyel.springbaseproject.config.authentication.AppUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> roles = new ArrayList<>();
        if ("ADMIN".equalsIgnoreCase(username)) {
            roles = AuthorityUtils.createAuthorityList("ROLE_ADMIN");
        }
        if ("USER".equalsIgnoreCase(username)) {
            roles = AuthorityUtils.createAuthorityList("ROLE_USER");
        }
        String passwordEncoded = "$2a$10$jXN83qi9EG8qPsw8AiJ7TOTWl/q/d5HW8B/hjhQF5Gqz97z4OLyii"; // encode of "password"
        return new AppUserDetails(username, passwordEncoded, roles);
    }
}
