package com.ivan4usa.fp.entities;

import com.ivan4usa.fp.entities.User;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Setter
public class CustomUserDetails implements UserDetails {

    /**
     * Instance of User
     */
    private User user;

    /**
     * List of authorities of user
     */
    private List<GrantedAuthority> authorities;

    /**
     * Constructor for the class
     * @param user object
     */
    public CustomUserDetails(User user) {
        this.user = user;
        this.authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Getter for user
     * @return user
     */
    public User getUser() {
        return user;
    }

    /**
     * Getter for user id
     * @return user id
     */
    public Long getId() {
        return user.getId();
    }

    /**
     * Getter for username
     * @return username
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Getter for authorities
     * @return null
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    /**
     * Getter for password
     * @return password
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Checks if account is not expired
     * @return true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Checks if account is not locked
     * @return true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Checks if credentials is not expired
     * @return true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Checks if is enabled
     * @return true
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
