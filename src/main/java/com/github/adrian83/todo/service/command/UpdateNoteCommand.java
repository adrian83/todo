package com.github.adrian83.todo.service.command;

import java.util.List;

import com.github.adrian83.todo.domain.User;

public record UpdateNoteCommand(User user, Long id, String title, String content, List<Long> tagIds) {
}