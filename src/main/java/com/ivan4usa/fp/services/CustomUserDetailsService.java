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

    /**
     * UserRepository Instance
     */
    private final UserRepository userRepository;

    /**
     * Constructor for the class
     * @param userRepository instance
     */
    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Load user by email to the UserDetails object method
     * @param email of user
     * @return UserDetails object
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email + " not found."));
        return new CustomUserDetails(user);
    }

    /**
     * Load user by user id to theUserDetails object method
     * @param id of user
     * @return UserDetails object
     */
    public UserDetails loadUserById(Long id) {
        User user =  userRepository.findUserById(id)
                .orElse(null);
        assert user != null;
        return new CustomUserDetails(user);
    }
}
