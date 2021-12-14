package com.ivan4usa.fp.services;

import com.ivan4usa.fp.entities.CustomUserDetails;
import com.ivan4usa.fp.entities.User;
import com.ivan4usa.fp.payload.request.RegisterRequest;
import com.ivan4usa.fp.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-local.properties")
class UserServiceTest {

    @Autowired
    private UserService service;

    @MockBean
    private UserRepository repository;

    @Test
    void createUser() {
        User user = new User(4L, "test@mail.com", "password", null);
        when(repository.save(any(User.class))).thenReturn(user);

        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@email.com");
        request.setPassword("password");
        User returnedUser = service.createUser(request);
        Assertions.assertEquals(returnedUser, user);
    }

    @Test
    void userExistsByEmail() {
        String email = "test@mail.com";
        User user = new User(2L, "any", "any", null);
        when(repository.findUserByEmail(email)).thenReturn(Optional.of(user));

        boolean returnedValue = service.userExistsByEmail(email);
        Assertions.assertTrue(returnedValue);
    }

    @Test
    void getUserById() {
        User user = new User(4L, "test@mail.com", "password", null);

        Authentication authentication = mock(Authentication.class);
        CustomUserDetails details = mock(CustomUserDetails.class);
        details.setUser(user);
        when(authentication.getPrincipal()).thenReturn(details);
        when(details.getId()).thenReturn(user.getId());
        when(details.getUser()).thenReturn(user);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Optional<User> returnedUser = service.getUserById(4L);
        Assertions.assertNotNull(Optional.of(returnedUser).get(), "User was not found");
        Assertions.assertSame(returnedUser.get(), user, "The user returned was not the same as the mock");
    }

    @Test
    void updateUserPassword() {
        BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
        String password = "password";
        String email = "test@mail.com";
        int rows = 1;
        when(repository.updateUserPassword(anyString(), eq(email))).thenReturn(rows);
        int returnedRows = service.updateUserPassword(password, email);
        Assertions.assertEquals(returnedRows, 1);
    }

    @Test
    void getUserId() {

        User user = new User(3L, "test@mail.com", "password", null);

        Authentication authentication = mock(Authentication.class);
        CustomUserDetails details = mock(CustomUserDetails.class);
        details.setUser(user);
        when(authentication.getPrincipal()).thenReturn(details);
        when(details.getId()).thenReturn(user.getId());
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Long returnedId = service.getUserId();
        Assertions.assertEquals(returnedId, 3L);
    }

    @Test
    void updateUser() {
    User user = new User(2L, "test@mail.com", "password", null);

    when(repository.updateUserData(user.getName(), user.getEmail(), user.getId())).thenReturn(1);
    String returnedToken = service.updateUser(user);
    Assertions.assertNotNull(returnedToken);
    }
}