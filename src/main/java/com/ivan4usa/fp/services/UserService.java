package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entities.CustomUserDetails;
import com.ivan4usa.fp.entities.Role;
import com.ivan4usa.fp.entities.User;
import com.ivan4usa.fp.exceptions.UserExistsException;
import com.ivan4usa.fp.payload.request.RegisterRequest;
import com.ivan4usa.fp.repositories.RoleRepository;
import com.ivan4usa.fp.repositories.UserRepository;
import com.ivan4usa.fp.security.JWTTokenProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * The service that is called from the corresponding controller and controls actions on the User
 */
@Service
public class UserService {

    /**
     * Instance of log Manager
     */
    private final Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Instance of UserRepository
     */
    private final UserRepository userRepository;

    /**
     * Instance of RoleRepository
     */
    private final RoleRepository roleRepository;

    /**
     * Instance of BCryptPasswordEncoder
     */
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private JWTTokenProvider jwtTokenProvider;

    /**
     * Constructor for the class
     * @param userRepository user repository
     * @param roleRepository role repository
     * @param bCryptPasswordEncoder bCryptPassword encoder
     */
    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                       JWTTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * Create new user method
     * @param request with user info
     * @return new user
     */
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
            Role role;
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

    /**
     * Method that checks if user exists by email
     * @param email of checking user
     * @return true or false
     */
    public boolean userExistsByEmail(String email) {
        return this.userRepository.findUserByEmail(email).isPresent();
    }

    /**
     * Get user by id method
     * @param id of user
     * @return Optional object with found user or empty
     */
    public Optional<User> getUserById(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUser = (CustomUserDetails) authentication.getPrincipal();
        if (Objects.equals(customUser.getId(), id)) {
            return Optional.ofNullable(customUser.getUser());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Update user password method
     * @param password new password
     * @param email current email of user
     * @return number of updated rows
     */
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
     * Method updates users data - name or/and email by id and returns new generated token for updated user or null
     * @param user with updated data
     * @return token if success or null if failure
     */
    public String updateUser(User user) {
        try {
            String newToken = this.jwtTokenProvider.generateToken(user);
            int rows = userRepository.updateUserData(user.getName(), user.getEmail(), user.getId());
            if (rows == 1) {
                return newToken;
            }
        } catch (Exception e) {
            logger.error("Failed to update user. " + e.getMessage());
        }
        return null;
    }
}
