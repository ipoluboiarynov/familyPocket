package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entity.Role;
import com.ivan4usa.fp.entity.User;
import com.ivan4usa.fp.exception.UserExistsException;
import com.ivan4usa.fp.payload.request.RegisterRequest;
import com.ivan4usa.fp.repository.RoleRepository;
import com.ivan4usa.fp.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    private final Logger logger = LogManager.getLogger(this.getClass());

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    public User createUser(RegisterRequest request) {
        // Check if user exists
        if (this.userRepository.findUserByEmail(request.getEmail()).isPresent()) {
            throw new UserExistsException("User already exists.");
        }
        try {
            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
            Role role = new Role();
            if (roleRepository.findRoleByName("USER").isPresent()) {
                role = roleRepository.findRoleByName("USER").get();
            } else {
                role = roleRepository.saveAndFlush(new Role("USER"));
            }
            Set<Role> roles = new HashSet<>();
            roles.add(role);
            user.setRoles(roles);
            logger.info("Save new User: " + request.getEmail());
            return userRepository.save(user);
        } catch (Exception e) {
            logger.error("Failed to save new user. " + e.getMessage());
        }

        return null;
    }

    public boolean userExistsByEmail(String email) {
        return this.userRepository.findUserByEmail(email).isPresent();
    }

    public User getUserById(Long id) {
        if (userRepository.findUserById(id).isPresent()) {
            return userRepository.findUserById(id).get();
        }
        return null;
    }

    public int updateUserPassword(String password, String email) {
        return userRepository.updateUserPassword(bCryptPasswordEncoder.encode(password), email);
    }
}
