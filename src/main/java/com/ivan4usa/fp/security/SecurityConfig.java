package com.ivan4usa.fp.security;

import com.ivan4usa.fp.exception.ExceptionHandlerFilter;
import com.ivan4usa.fp.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
//@EnableGlobalMethodSecurity(
//        securedEnabled = true,
//        jsr250Enabled = true,
//        proxyTargetClass = true
//)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${jwt.auth_urls}")
    private String auth_urls;

    private final CustomUserDetailsService customUserDetailsService;

    /**
     * All arguments constructor
     */
    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Inject JWTAuthenticationFilter to this class
     * @return new JWTAuthenticationFilter object
     */
    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    };

    /**
     * Inject ExceptionHandlerFilter to this class
     * @return new ExceptionHandlerFilter object
     */
    @Bean
    public ExceptionHandlerFilter exceptionHandlerFilter() {
        return new ExceptionHandlerFilter();
    };

    /**
     * Authentication manager for checkin login ang password of user in database
     * @return authentication manager object
     * @throws Exception any exception
     */
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    protected AuthenticationManager  authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * Method for encoding the password
     * @return BCryptPasswordEncoder object
     */
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Override method for checking if user existing in database throw the customUserDetailsService service
     * @param auth AuthenticationManagerBuilder object
     * @throws Exception any exception while doing this method
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    /**
     * Configurstion for http requests
     * @param http HttpSecurity object
     * @throws Exception any exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // disable csrf attacks defender
        http.cors().and().csrf().disable();

        // The session is not stored on the server, and every request is sent with a token
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // start jwtAuthenticationFilter filer before UsernamePasswordAuthenticationFilter filter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // start exceptionHandlerFilter filer before JWTAuthenticationFilter filter
        http.addFilterBefore(exceptionHandlerFilter(), JWTAuthenticationFilter.class);

        // disable form while app uses form not of spring technology
        http.formLogin().disable();

        // turn off basic browser validation form
        http.httpBasic().disable();

        // for https use
        http.requiresChannel().anyRequest().requiresSecure();
    }
}