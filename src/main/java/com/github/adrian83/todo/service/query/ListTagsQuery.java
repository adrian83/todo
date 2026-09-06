package com.github.adrian83.todo.service.query;

import com.github.adrian83.todo.domain.User;

public class ListTagsQuery {

    private final User user;

    public ListTagsQuery(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
