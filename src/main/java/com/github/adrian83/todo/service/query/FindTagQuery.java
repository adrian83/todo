package com.github.adrian83.todo.service.query;

import com.github.adrian83.todo.domain.User;

public class FindTagQuery {

    private final User user;
    private final Long id;

    public FindTagQuery(User user, Long id) {
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
