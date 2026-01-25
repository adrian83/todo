package com.github.adrian83.todo.service.exception;

public class TagNotFoundException extends RuntimeException {

    private final Long tagId;

    public TagNotFoundException(Long tagId) {
        super("Tag not found: " + tagId);
        this.tagId = tagId;
    }

    public Long getTagId() {
        return tagId;
    }
}
