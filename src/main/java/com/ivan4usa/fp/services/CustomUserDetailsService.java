package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entity.User;
import com.ivan4usa.fp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findUserByEmail(username)
                .orElseThrow(() -> new RuntimeException("error"));
        return build(user);
    }

    public User loadUserById(Long id) {
        return userRepository.findUserById(id)
                .orElse(null);
    }

    public static User build(User user) {

        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());

        return new User(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}