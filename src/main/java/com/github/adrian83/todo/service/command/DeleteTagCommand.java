package com.github.adrian83.todo.service.command;

import com.github.adrian83.todo.domain.User;

public class DeleteTagCommand {

    private final User user;
    private final Long id;

    public DeleteTagCommand(User user, Long id) {
        this.user = user;
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }
}
