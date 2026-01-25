package com.github.adrian83.todo.service.command;

import com.github.adrian83.todo.domain.User;

public record CreateNoteCommand(User user, String title, String content, java.util.Set<Long> tagIds) {
}

