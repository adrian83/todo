package com.github.adrian83.todo.service.command;

import com.github.adrian83.todo.domain.User;

public class CreateTagCommand {

    private final User user;
    private final String name;

    public CreateTagCommand(User user, String name) {
        this.user = user;
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return name;
    }
}
