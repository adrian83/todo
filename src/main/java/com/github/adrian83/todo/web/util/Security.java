package com.github.adrian83.todo.web.util;

import com.github.adrian83.todo.web.exception.UnauthenticatedUserException;



public final class Security {

    private Security() {
    }

    public static void assertPrincipalNotNull(Object principal) {
        if (principal == null) {
            throw new UnauthenticatedUserException();
        }
    }
}