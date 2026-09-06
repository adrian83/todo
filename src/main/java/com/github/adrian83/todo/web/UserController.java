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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ServerWebExchange;

import com.github.adrian83.todo.domain.User;
import com.github.adrian83.todo.service.UserService;
import com.github.adrian83.todo.service.exception.InvalidPasswordException;
import com.github.adrian83.todo.service.exception.UserAlreadyExistsException;
import com.github.adrian83.todo.service.exception.UserNotFoundException;
import com.github.adrian83.todo.service.response.TokenResponse;
import com.github.adrian83.todo.web.request.LoginForm;
import com.github.adrian83.todo.web.request.NewUserRequest;
import com.github.adrian83.todo.web.util.ErrorMessage;
import com.github.adrian83.todo.web.util.InfoMessage;
import com.github.adrian83.todo.web.util.ModelUtil;
import com.github.adrian83.todo.web.util.RedirectBuilder;

import jakarta.validation.Valid;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private static final String REGISTER_PATH = "/register";
    private static final String LOGIN_PATH = "/login";
    private static final String LOGOUT_PATH = "/logout";

    private static final String REGISTER_VIEW = "user_form";
    private static final String LOGIN_VIEW = "login";

    private static final String COOKIE_ACCESS_TOKEN = "accessToken";
    private static final String COOKIE_REFRESH_TOKEN = "refreshToken";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(REGISTER_PATH)
    public String createForm(
            @RequestParam(required = false) String message,
            @RequestParam(required = false) String error,
            Model model
    ) {
        model.addAttribute("newUserRequest", new NewUserRequest());
        
        ModelUtil.enrichWithMessageAndError(model, message, error);
        return REGISTER_VIEW;
    }

    @PostMapping(REGISTER_PATH)
    public String register(
            @Valid @ModelAttribute("newUserRequest") NewUserRequest form,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("hasErrors", true);
            return REGISTER_VIEW;
        }

        userService.register(form);
        return new RedirectBuilder("/")
                .addInfoMessage(InfoMessage.USER_REGISTERED_SUCCESSFULLY)
                .build();
    }

    @GetMapping(LOGIN_PATH)
    public String loginForm(
            @RequestParam(required = false) String message,
            @RequestParam(required = false) String error,
            Model model) {
        model.addAttribute("loginForm", new LoginForm());
        
        ModelUtil.enrichWithMessageAndError(model, message, error);
        return LOGIN_VIEW;
    }

    @PostMapping(LOGIN_PATH)
    public String login(@Valid @ModelAttribute("loginForm") LoginForm form,
            BindingResult bindingResult,
            Model model,
            ServerWebExchange exchange) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("hasErrors", true);
            return LOGIN_VIEW;
        }

        User user = userService.login(form.getUsername(), form.getPassword());
        TokenResponse tokens = userService.generateTokens(user);

        addTokenCookies(exchange, tokens);

        return new RedirectBuilder("/")
                .build();

    }

    @GetMapping(LOGOUT_PATH)
    public String logout(ServerWebExchange exchange) {
        clearTokenCookies(exchange);
        return new RedirectBuilder("/")
                .build();
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
        return new RedirectBuilder(REGISTER_PATH)
                .addErrorMessage(ErrorMessage.USER_ALREADY_EXISTS)
                .build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleExists(UserNotFoundException ex) {
        return new RedirectBuilder(LOGIN_PATH)
                .addErrorMessage(ErrorMessage.INVALID_CREDENTIALS)
                .build();
    }

}
