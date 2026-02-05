package com.github.adrian83.todo.web.exception;

public class UnauthenticatedUserException extends RuntimeException {

    public UnauthenticatedUserException() {
        super("User is not authenticated");
    }
}