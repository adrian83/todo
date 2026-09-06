package com.github.adrian83.todo.web.util;

public enum ErrorMessage {
    
  
    USER_ALREADY_EXISTS("User with given username already exists"),
    NOTE_NOT_FOUND("Note not found"),
    TAG_NOT_FOUND("Tag not found"),
    INVALID_CREDENTIALS("Invalid username or password");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String addErrorToUrl() {
        return "error=" + this.name().toLowerCase();
    }

    public static String resolveMessage(String name){
        if(name == null || name.isBlank()) {
            return null;
        }
        
        var nameUpper = name.toUpperCase();
        for (ErrorMessage msg : ErrorMessage.values()) {
            if (msg.name().equals(nameUpper)) {
                return msg.message; 
            }
        }
        return null;
    }
}