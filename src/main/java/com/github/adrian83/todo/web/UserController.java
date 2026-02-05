package com.github.adrian83.todo.web;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ServerWebExchange;

import com.github.adrian83.todo.domain.User;
import com.github.adrian83.todo.service.UserService;
import com.github.adrian83.todo.service.exception.InvalidPasswordException;
import com.github.adrian83.todo.service.exception.UserAlreadyExistsException;
import com.github.adrian83.todo.service.exception.UserNotFoundException;
import com.github.adrian83.todo.service.response.TokenResponse;
import com.github.adrian83.todo.web.request.LoginForm;
import com.github.adrian83.todo.web.request.NewUserRequest;

import jakarta.validation.Valid;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final String COOKIE_ACCESS_TOKEN = "accessToken";
    private static final String COOKIE_REFRESH_TOKEN = "refreshToken";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String createForm(Model model) {
        model.addAttribute("newUserRequest", new NewUserRequest());
        return "user_form";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("newUserRequest") NewUserRequest form,
                           BindingResult bindingResult,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("hasErrors", true);
            return "user_form";
        }

        userService.register(form);
        return "redirect:/?message=User+registered+successfully";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm") LoginForm form,
                        BindingResult bindingResult,
                        Model model,
                        ServerWebExchange exchange) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("hasErrors", true);
            return "login";
        }

        try {
            User user = userService.login(form.getUsername(), form.getPassword());
            TokenResponse tokens = userService.generateTokens(user);
            
            addTokenCookies(exchange, tokens);
            
            return "redirect:/";
        } catch (UserNotFoundException | InvalidPasswordException e) {
            return "redirect:/login?error=Invalid+username+or+password";
        }
    }

    @GetMapping("/logout")
    public String logout(ServerWebExchange exchange) {
        clearTokenCookies(exchange);
        return "redirect:/";
    }

    private void addTokenCookies(ServerWebExchange exchange, TokenResponse tokens) {
        HttpHeaders headers = exchange.getResponse().getHeaders();
        
        ResponseCookie accessTokenCookie = ResponseCookie.from(COOKIE_ACCESS_TOKEN, tokens.getAccessToken())
                .httpOnly(true)
                .secure(false) // Set to true in production with HTTPS
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();
        headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());

        ResponseCookie refreshTokenCookie = ResponseCookie.from(COOKIE_REFRESH_TOKEN, tokens.getRefreshToken())
                .httpOnly(true)
                .secure(false) // Set to true in production with HTTPS
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    private void clearTokenCookies(ServerWebExchange exchange) {
        HttpHeaders headers = exchange.getResponse().getHeaders();
        
        ResponseCookie accessTokenCookie = ResponseCookie.from(COOKIE_ACCESS_TOKEN, "")
                .path("/")
                .maxAge(Duration.ZERO)
                .build();
        headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());

        ResponseCookie refreshTokenCookie = ResponseCookie.from(COOKIE_REFRESH_TOKEN, "")
                .path("/")
                .maxAge(Duration.ZERO)
                .build();
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public String handleExists(UserAlreadyExistsException ex) {
        return "redirect:/register?error=" + ex.getMessage();
    }
}
