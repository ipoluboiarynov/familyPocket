package com.ivan4usa.fp.security;

import com.ivan4usa.fp.entity.User;
import com.ivan4usa.fp.exception.JwtNotValidException;
import com.ivan4usa.fp.services.CustomUserDetails;
import com.ivan4usa.fp.services.CustomUserDetailsService;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * The class that is responsible for all the authentication / authorization functionality intercepts all requests
 * (login, logout, getting a list of tasks, editing, etc.) - everything that gets into the backend
 */
@Component
@Getter
@Setter
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final Logger logger = LogManager.getLogger(this.getClass());

    // List of public urls without jwt needed
    @Value("#{'${jwt.auth_urls}'.split(',')}")
    private List<String> public_urls;

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JWTCookieProvider jwtCookieProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        boolean isRequestToPublicApi = public_urls.stream().anyMatch(s-> httpServletRequest.getRequestURI().toLowerCase().equals(s));

        if (!isRequestToPublicApi && SecurityContextHolder.getContext().getAuthentication() == null) {
            String jwt = jwtCookieProvider.getCookieAccessToken(httpServletRequest);

            if (jwt != null) {
                if (jwtTokenProvider.validateToken(jwt)) {
                    User user = jwtTokenProvider.getUserFromToken(jwt);
                    CustomUserDetails userDetails = new CustomUserDetails(user);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } else throw new JwtNotValidException("jwt validate exception");

            } else throw new AuthenticationCredentialsNotFoundException("token not found");
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
