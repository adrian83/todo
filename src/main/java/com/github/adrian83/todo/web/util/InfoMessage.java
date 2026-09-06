package com.github.adrian83.todo.web.util;

public enum InfoMessage {
    
    USER_REGISTERED_SUCCESSFULLY("User registered successfully"),
    NOTE_CREATED_SUCCESSFULLY("Note created successfully"),
    NOTE_UPDATED_SUCCESSFULLY("Note updated successfully"),
    NOTE_DELETED_SUCCESSFULLY("Note deleted successfully"),
    TAG_CREATED_SUCCESSFULLY("Tag created successfully"),
    TAG_UPDATED_SUCCESSFULLY("Tag updated successfully"),
    TAG_DELETED_SUCCESSFULLY("Tag deleted successfully");

    private final String message;

    InfoMessage(String message) {
        this.message = message;
    }

    public String addMessageToUrl() {
        return "message=" + this.name().toLowerCase();
    }

    public static String resolveMessage(String name){
        if(name == null || name.isBlank()) {
            return null;
        }
        
        var nameUpper = name.toUpperCase();
        for (InfoMessage msg : InfoMessage.values()) {
            if (msg.name().equals(nameUpper)) {
                return msg.message; 
            }
        }
        return null;
    }
}