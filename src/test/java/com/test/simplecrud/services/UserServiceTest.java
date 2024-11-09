package com.test.simplecrud.services;

import com.test.simplecrud.constants.ValidatorMessages;
import com.test.simplecrud.dtos.requests.AuthDTO;
import com.test.simplecrud.dtos.requests.SignupDTO;
import com.test.simplecrud.dtos.responses.TokenDTO;
import com.test.simplecrud.entities.User;
import com.test.simplecrud.enums.RoleName;
import com.test.simplecrud.repositories.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepository repository;

    @Mock
    private RoleService roleService;

    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Mock
    private SignupDTO signupDTO;

    private Validator validator;

    @BeforeEach
    void setUp() {
        signupDTO = new SignupDTO("Test User", "test@example.com", "password", "123456789");
        UserService realUserService = new UserService(authenticationManager, tokenService, repository, roleService);
        userService = Mockito.spy(realUserService);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testSignup_Success() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setPassword("password");
        user.setPhone("123456789");

        when(repository.save(Mockito.any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(user.getId()); // Simulate setting an ID after saving
            return savedUser;
        });
        when(repository.existsByEmail("test@example.com")).thenReturn(Boolean.valueOf(false));

        UUID userId = userService.signup(signupDTO);

        Mockito.verify(repository).existsByEmail("test@example.com");
        Mockito.verify(repository).save(userArgumentCaptor.capture());

        Mockito.verify(roleService).createRole(userArgumentCaptor.getValue(), RoleName.ROLE_ADMINISTRATOR);


        User capturedUser = userArgumentCaptor.getValue();
        assertEquals("Test User", capturedUser.getName());
        assertEquals("test@example.com", capturedUser.getEmail());
        assertEquals(userId, user.getId());
    }

    @Test
    void testValidSignupDTO() {
        SignupDTO dto = new SignupDTO("Test User", "test@example.com", "password", "123456789");
        Set<ConstraintViolation<SignupDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidSignupDTO_NameBlank() {
        SignupDTO dto = new SignupDTO("", "test@example.com", "password", "123456789");
        Set<ConstraintViolation<SignupDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals(ValidatorMessages.NAME_CANNOT_BE_EMPTY, violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidSignupDTO_EmailBlank() {
        SignupDTO dto = new SignupDTO("Test User", "", "password", "123456789");
        Set<ConstraintViolation<SignupDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals(ValidatorMessages.MAIL_CANNOT_BE_EMPTY, violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidSignupDTO_PasswordBlank() {
        SignupDTO dto = new SignupDTO("Test User", "test@example.com", "", "123456789");
        Set<ConstraintViolation<SignupDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals(ValidatorMessages.PASSWORD_CANNOT_BE_EMPTY, violations.iterator().next().getMessage());
    }

    @Test
    void testInvalidSignupDTO_PhoneBlank() {
        SignupDTO dto = new SignupDTO("Test User", "test@example.com", "password", "");
        Set<ConstraintViolation<SignupDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
        assertEquals(ValidatorMessages.PHONE_CANNOT_BE_EMPTY, violations.iterator().next().getMessage());
    }

    @Test
    void test_successful_authentication_with_valid_credentials() {
        AuthDTO authDTO = new AuthDTO("validUser", "validPassword");
        User user = new User();
        user.setEmail("validUser");
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(authDTO.username(), authDTO.password());

        AuthenticationManager authManager = mock(AuthenticationManager.class);
        TokenService tokenSvc = mock(TokenService.class);
        UserService userSvc = new UserService(authManager, tokenSvc, mock(UserRepository.class), mock(RoleService.class));

        when(authManager.authenticate(token)).thenReturn(new UsernamePasswordAuthenticationToken(user, null));
        when(tokenSvc.generateToken(user)).thenReturn("validToken");

        TokenDTO result = userSvc.login(authDTO);

        assertEquals("validToken", result.token());
    }

    @Test
    void test_authentication_failure_with_invalid_credentials() {
        AuthDTO authDTO = new AuthDTO("invalidUser", "invalidPassword");
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(authDTO.username(), authDTO.password());

        AuthenticationManager authManager = mock(AuthenticationManager.class);
        TokenService tokenSvc = mock(TokenService.class);
        UserService userSvc = new UserService(authManager, tokenSvc, mock(UserRepository.class), mock(RoleService.class));

        when(authManager.authenticate(token)).thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () -> userSvc.login(authDTO));
    }
}