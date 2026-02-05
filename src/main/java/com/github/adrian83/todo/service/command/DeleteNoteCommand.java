package com.github.adrian83.todo.service.command;

import com.github.adrian83.todo.domain.User;

public record DeleteNoteCommand(User user, Long noteId) {
}