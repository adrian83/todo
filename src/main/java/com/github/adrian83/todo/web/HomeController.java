package com.github.adrian83.todo.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model, @org.springframework.web.bind.annotation.RequestParam(value = "message", required = false) String message) {
        if (message != null && !message.isBlank()) {
            model.addAttribute("message", message);
        } else if (!model.containsAttribute("message")) {
            model.addAttribute("message", "Moja pierwsza aplikacja notatek 🚀");
        }
        return "home";
    }
}