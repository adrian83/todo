package com.github.adrian83.todo.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.github.adrian83.todo.domain.RefreshToken;
import com.github.adrian83.todo.domain.User;
import com.github.adrian83.todo.repository.UserRepository;
import com.github.adrian83.todo.service.exception.InvalidPasswordException;
import com.github.adrian83.todo.service.exception.UserAlreadyExistsException;
import com.github.adrian83.todo.service.exception.UserNotFoundException;
import com.github.adrian83.todo.service.response.TokenResponse;
import com.github.adrian83.todo.web.request.NewUserRequest;

@Service
public class UserService {

    private static final String FIELD_UNIQUE_USERNAME = "username";
    private static final String FIELD_UNIQUE_EMAIL = "email";

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public UserService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public User register(NewUserRequest form) {
        String passwordHash = hashPassword(form.getPassword());
        User user = new User(form.getUsername(), form.getEmail(), passwordHash);
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            String message = determineDuplicateField(e);
            throw new UserAlreadyExistsException(message);
        }
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        String providedHash = hashPassword(password);
        if (!user.getPasswordHash().equals(providedHash)) {
            throw new InvalidPasswordException("Invalid password");
        }
        
        return user;
    }

    public TokenResponse generateTokens(User user) {
        String accessToken = tokenService.generateAccessToken(user);
        RefreshToken refreshToken = tokenService.generateRefreshToken(user);
        return new TokenResponse(accessToken, refreshToken.getToken());
    }

    private String determineDuplicateField(DataIntegrityViolationException e) {
        String message = e.getMessage();
        if (message != null && message.contains(FIELD_UNIQUE_USERNAME)) {
            return FIELD_UNIQUE_USERNAME;
        }
        if (message != null && message.contains(FIELD_UNIQUE_EMAIL)) {
            return FIELD_UNIQUE_EMAIL;
        }
        return "user";
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

}
