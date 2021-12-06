package com.ivan4usa.fp.security;

import com.ivan4usa.fp.exceptions.ExceptionHandlerFilter;
import com.ivan4usa.fp.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Url Address of frontend
     */
    @Value("${client.url}")
    private String clientUrl;

    /**
     * Instance of CustomUserDetailsService
     */
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Instance of JWTAuthenticationFilter
     */
    private JWTAuthenticationFilter authenticationFilter;

    /**
     * Instance of ExceptionHandlerFilter
     */
    private ExceptionHandlerFilter exceptionHandlerFilter;

    /**
     * Setter for exceptionHandlerFilter
     * @param exceptionHandlerFilter of type ExceptionHandlerFilter
     */
    @Autowired
    public void setExceptionHandlerFilter(ExceptionHandlerFilter exceptionHandlerFilter) {
        this.exceptionHandlerFilter = exceptionHandlerFilter;
    }

    /**
     * Setter for customUserDetailsService
     * @param customUserDetailsService of type CustomUserDetailsService
     */
    @Autowired
    public void setCustomUserDetailsService(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Setter for authenticationFilter
     * @param authenticationFilter of type JWTAuthenticationFilter
     */
    @Autowired
    public void setAuthenticationFilter(JWTAuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    /**
     * Method sets global CORS rules
     * @return WebMvcConfigurer object
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.
                        addMapping("/**"). // for all URL
                        allowedOrigins(clientUrl). // from which addresses to allow requests (can be specified separated by commas)
                        allowCredentials(true). // allow cookies to be sent for cross-site request
                        allowedHeaders("*"). // allow all headers - without this setting, it may not work in some browsers
                        allowedMethods("*"); // all methods are allowed (GET, POST, etc.) - CORS won't work without this setting!
            }
        };
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
     * Authentication manager for checkin login ang password of user in database
     * @return authentication manager object
     * @throws Exception any exception
     */
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
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
     * Method to disable the call to the AuthTokenFilter filter for the container servlet
     * (so that the filter is not called 2 times, but only once from the Spring container)
     * https://stackoverflow.com/questions/39314176/filter-invoke-twice-when-register-as-spring-bean
     * @param filter - JWT Authentication Filter parameter
     * @return registration
     */
    @Bean
    public FilterRegistrationBean registration(JWTAuthenticationFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }

    /**
     * Configurstion for http requests
     * @param http HttpSecurity object
     * @throws Exception any exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // disable csrf attacks defender
        http.csrf().disable();

        // disable form while app uses form not of spring technology
        http.formLogin().disable();

        // turn off basic browser validation form
        http.httpBasic().disable();

        // The session is not stored on the server, and every request is sent with a token
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // start jwtAuthenticationFilter filer before UsernamePasswordAuthenticationFilter filter
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // start exceptionHandlerFilter filer before JWTAuthenticationFilter filter
        http.addFilterBefore(exceptionHandlerFilter, JWTAuthenticationFilter.class);

        // for https use
        http.requiresChannel().anyRequest().requiresSecure();
    }
}
