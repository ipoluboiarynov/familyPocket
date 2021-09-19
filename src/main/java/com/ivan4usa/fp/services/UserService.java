package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entity.User;
import com.ivan4usa.fp.enums.Roles;
import com.ivan4usa.fp.payload.request.RegisterRequest;
import com.ivan4usa.fp.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final Logger logger = LogManager.getLogger(this.getClass());

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User createUser(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.getRoles().add(Roles.USER);

        try {
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
}
