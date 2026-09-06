package com.github.adrian83.todo.web.util;

import org.springframework.ui.Model;


public final class ModelUtil {

    private ModelUtil() {}


    public static void enrichWithMessageAndError(Model model, String message, String error) {
        model.addAttribute("message", InfoMessage.resolveMessage(message));
        model.addAttribute("error", ErrorMessage.resolveMessage(error));
    }

}