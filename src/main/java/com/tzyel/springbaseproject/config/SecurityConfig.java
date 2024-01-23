package com.tzyel.springbaseproject.config;

import com.tzyel.springbaseproject.config.filter.JwtAuthenticationFilter;
import com.tzyel.springbaseproject.constant.ViewHtmlConst;
import com.tzyel.springbaseproject.service.ApplicationUserDetailsService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ApplicationUserDetailsService userDetailsService;
    private final AuthenticationEntryPoint authEntryPoint;
    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          ApplicationUserDetailsService userDetailsService,
                          @Qualifier("customAuthenticationEntryPoint") AuthenticationEntryPoint authEntryPoint,
                          AuthenticationSuccessHandler authenticationSuccessHandler) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth
                                .requestMatchers(
                                        "/example/**",
                                        "/health",
                                        "/auth/login"
                                ).permitAll()
                                .requestMatchers(ViewHtmlConst.ANT_MATCHERS_RESOURCES).permitAll()
                                .anyRequest().authenticated()
//                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/api/user/**").hasRole("USER")
                )
                .formLogin(formLogin -> {
                    formLogin.loginPage("/login");
                    formLogin.defaultSuccessUrl("/");
                    formLogin.failureUrl("/login?error=true");
                    formLogin.permitAll();
                    formLogin.successHandler(authenticationSuccessHandler);
                })
                .logout(logout -> {
                    logout.logoutSuccessUrl("/login?logout");
                    logout.invalidateHttpSession(true);
                    logout.permitAll();
                })
                .sessionManagement(session -> {
                    session.maximumSessions(-1);
                    session.sessionConcurrency(sessionConcurrency ->
                            sessionConcurrency.sessionRegistry(new SessionRegistryImpl()));
                })
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        var authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
