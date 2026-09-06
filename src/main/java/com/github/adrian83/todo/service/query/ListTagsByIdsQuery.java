package com.github.adrian83.todo.service.query;

import java.util.List;

import com.github.adrian83.todo.domain.User;

public class ListTagsByIdsQuery {

    private final User user;
    private final List<Long> ids;

    public ListTagsByIdsQuery(User user, List<Long> ids) {
        this.user = user;
        this.ids = ids;
    }

    public User getUser() {
        return user;
    }

    public List<Long> getIds() {
        return ids;
    }
}
