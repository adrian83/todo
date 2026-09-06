package com.github.adrian83.todo.service.command;

import com.github.adrian83.todo.domain.User;

public class UpdateTagCommand {

    private final User user;
    private final Long id;
    private final String name;

    public UpdateTagCommand(User user, Long id, String name) {
        this.user = user;
        this.id = id;
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
