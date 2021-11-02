package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entities.Role;
import com.ivan4usa.fp.entities.User;
import com.ivan4usa.fp.exceptions.UserExistsException;
import com.ivan4usa.fp.payload.request.RegisterRequest;
import com.ivan4usa.fp.repositories.RoleRepository;
import com.ivan4usa.fp.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
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

    public Optional<User> getUserById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUser = (CustomUserDetails) authentication.getPrincipal();
        if (customUser.getId() == id) {
            return Optional.ofNullable(customUser.getUser());
        } else return Optional.empty();
    }

    public int updateUserPassword(String password, String email) {
        return userRepository.updateUserPassword(bCryptPasswordEncoder.encode(password), email);
    }

    /**
     * Method returns user id from SecurityContextHolder context
     * @return user id (Long type)
     */
    public Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUser = (CustomUserDetails) authentication.getPrincipal();
        return customUser.getId();
    }

    /**
     * Method updates users data - name or/and email by id
     *
     * @param user with updated data
     * @return 1 if success or 0 if failure
     */
    public int updateUser(User user) {
        try {
            return userRepository.updateUserData(user.getName(), user.getEmail(), user.getId());
        } catch (Exception e) {
            logger.error("Failed to update user. " + e.getMessage());
        }
        return 0;
    }
}
