package com.tzyel.springbaseproject.config.filter;

import com.tzyel.springbaseproject.service.ApplicationUserDetailsService;
import com.tzyel.springbaseproject.service.JwtService;
import com.tzyel.springbaseproject.utils.ApplicationUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final ApplicationUserDetailsService applicationUserDetailsService;

    @SuppressWarnings("NullableProblems")
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (ApplicationUtil.isApiRequest(request)) {
            // Clear context for authentication from session
            SecurityContextHolder.clearContext();

            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String token = null;
            String userName = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                userName = jwtService.extractUsernameFromToken(token);
            }
            if (userName != null & SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = applicationUserDetailsService.loadUserByUsername(userName);
                if (jwtService.validateToken(token, userDetails)) {
                    var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
