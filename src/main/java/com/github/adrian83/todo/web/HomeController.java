package com.github.adrian83.todo.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.adrian83.todo.security.UserPrincipal;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model, @RequestParam(value = "message", required = false) String message,
                       @AuthenticationPrincipal UserPrincipal principal) {
        if (message != null && !message.isBlank()) {
            model.addAttribute("message", message);
        } else if (!model.containsAttribute("message")) {
            model.addAttribute("message", "Moja pierwsza aplikacja notatek 🚀");
        }
        if (principal != null) {
            model.addAttribute("userPrincipal", principal);
        }
        return "home";
    }
}