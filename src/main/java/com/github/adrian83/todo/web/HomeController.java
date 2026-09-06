package com.github.adrian83.todo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.adrian83.todo.security.UserPrincipal;
import com.github.adrian83.todo.web.util.ModelUtil;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    public static final String HOME_PATH = "/";
    private static final String HOME_VIEW = "home";

    @GetMapping(HOME_PATH)
    public String home(
            Model model, 
            @RequestParam(value = "message", required = false) String message,
            @RequestParam(value = "error", required = false) String error,
            @AuthenticationPrincipal UserPrincipal principal
        ) {

        ModelUtil.enrichWithMessageAndError(model, message, error);

        if (principal != null) {
            model.addAttribute("userPrincipal", principal);
        }
        return HOME_VIEW;
    }
}