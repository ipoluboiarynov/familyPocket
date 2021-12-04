package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entities.User;
import com.ivan4usa.fp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * The service that is called from the corresponding controller and controls actions on the CustomUserDetails
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + " not found."));
        return new CustomUserDetails(user);
    }

    public UserDetails loadUserById(Long id) {
        User user =  userRepository.findUserById(id)
                .orElse(null);
        assert user != null;
        return new CustomUserDetails(user);
    }
}
